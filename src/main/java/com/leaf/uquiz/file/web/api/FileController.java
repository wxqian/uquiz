package com.leaf.uquiz.file.web.api;

import com.google.common.collect.Lists;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.leaf.uquiz.core.utils.Codecs;
import com.leaf.uquiz.core.utils.IOUtils;
import com.leaf.uquiz.core.utils.RequestUtils;
import com.leaf.uquiz.core.utils.ResponseUtils;
import com.leaf.uquiz.file.convert.FileConverter;
import com.leaf.uquiz.file.domain.File;
import com.leaf.uquiz.file.domain.Space;
import com.leaf.uquiz.file.domain.SpaceType;
import com.leaf.uquiz.file.domain.Zoom;
import com.leaf.uquiz.file.service.FileService;
import com.leaf.uquiz.file.service.Storage;
import com.leaf.uquiz.file.service.VFile;
import com.leaf.uquiz.file.service.impl.AwtImageTransform;
import com.leaf.uquiz.file.service.impl.ImageTransform;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/8/14.
 */
@Controller
@RequestMapping("/api/file")
public class FileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @Autowired
    private FileConverter fileConverter;

    @Autowired
    @Qualifier("thumbStorage")
    private Storage thumbStorage;

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping("/upload")
    public Object upload(MultipartRequest mrequest, String space, String owner, String name) throws IOException {
        Space s = fileService.getSpace(space);
        List<File> files = Lists.newArrayList();
        for (List<MultipartFile> uplodList : mrequest.getMultiFileMap().values()) {
            for (MultipartFile upload : uplodList) {
                if (StringUtils.isEmpty(upload.getOriginalFilename()) || upload.getSize() < 1) {
                    continue;
                }
                File file = new File();
                file.setSpaceId(s.getId());
                file.setName(upload.getOriginalFilename());
                if (StringUtils.isNotBlank(owner)) {
                    file.setOwner(owner);
                }
                file.setUserId(0L);
                fileService.saveInputStreamFile(file, upload.getInputStream());
                files.add(file);
            }
        }
        if (files.size() == 1) {
            return fileConverter.convert(files.get(0));
        } else {
            return fileConverter.convert(files);
        }
    }


    @RequestMapping({"/z{zoom}/{id}", "/{id}"})
    public void get(@PathVariable("id") long id, @PathVariable @RequestParam(name = "zoom", required = false, defaultValue = "1") int zoom, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File f = fileService.getFile(id);
        if (f == null) {
            ResponseUtils.notFound(response, "File " + id + " not found");
            return;
        }
        try {
            Space space = fileService.getSpace(f.getSpaceId());
            VFile original = fileService.getVFile(f);
            VFile decorate = null;
            boolean inline = false;
            if (space.getType() == SpaceType.IMAGE) {
                if (zoom == File.ZOOM_SMALL || zoom == File.ZOOM_BIG && !StringUtils.equals(f.getExt(), "gif")) {
                    List<Zoom> zooms = space.getZooms();
                    if (zoom <= zooms.size()) {
                        Zoom z = zooms.get(zoom - 1);
                        decorate = decorateImageVFile(original, z.getWidth(), z.getHeight(), z.getCropHeight());
                    }
                }
                inline = true;
            }
            sendFile(decorate == null ? original : decorate, space, f.getName(), inline, request, response);
        } catch (Exception e) {
            ResponseUtils.notFound(response, "File " + id + " not found");
        }
    }


    private void sendFile(VFile vFile, Space space, String fileName, boolean inline, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!vFile.exist()) {
            ResponseUtils.notFound(response, "");
            return;
        }
        if (!checkModified(vFile, request, response)) {
            ResponseUtils.notModified(response, null);
            return;
        }

        int cacheSeconds = space.getCacheSeconds();
        if (cacheSeconds > 0) {
            setDateHeader(response, "Expires", System.currentTimeMillis() + cacheSeconds * 1000L);
            response.setHeader("Cache-Control", "max-age=" + cacheSeconds);
        }
        String xsendfilePath = vFile.getXsendfilePath();
        if (vFile.getXsendfilePath() != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("XsendfilePath:" + xsendfilePath);
            }
            response.setHeader("X-Accel-Redirect", xsendfilePath);
            int limitRate = space.getLimitRate();
            if (limitRate > 0) {
                response.setHeader("X-Accel-Limit-Rate", String.valueOf(limitRate));
            }
            return;
        }
        ResponseUtils.renderBinary(response, vFile.getInputStream(), fileName, inline);
    }

    private VFile decorateImageVFile(VFile vf, int maxWidth, int maxHeight, int cropHeight) {
        VFile tvf = thumbStorage.getVFile(vf.getKey() + "-" + maxWidth + "-" + maxHeight);
        if (!tvf.exist() || tvf.lastModified() < vf.lastModified()) {// 如果不存在或者比原始文件老,就需要重新创建缩略文件
            ImageTransform itf = getImageTransform(0.7f);
            try {
                itf.load(vf.getInputStream());
                itf.resizeAndcrop(maxWidth, maxHeight, cropHeight);
                if (itf.isModified()) {
                    itf.save(tvf.getOutputStream());
                } else {
                    tvf.copyFrom(vf.getNativeFile());
                }
            } catch (IOException e) {
                //ignore
            }
        }
        return tvf;
    }

    private ImageTransform getImageTransform(float quality) {
        return new AwtImageTransform(quality);
    }

    static void setDateHeader(HttpServletResponse response, String name, long ts) {
        response.setHeader(name, RequestUtils.getHttpDateFormatter().format(new Date(ts)));
    }

    static String getHeader(HttpServletRequest request, String name) {
        return request.getHeader(name);
    }

    static long getDateHeader(HttpServletRequest request, String name) {
        String s = getHeader(request, name);
        if (s != null) {
            try {
                return RequestUtils.getHttpDateFormatter().parse(s).getTime();
            } catch (ParseException ignored) {
            }
        }
        return 0;
    }

    static boolean checkModified(VFile vf, HttpServletRequest request, HttpServletResponse response) {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            Long modified = vf.lastModified();
            if (modified == 0l) return true;
            String etag = Codecs.encode(modified);
            String ifNoneMatch = request.getHeader("if-none-match");
            if (ifNoneMatch != null && etag.equals(ifNoneMatch)) {
                return false;
            }
            long ifModifiedSince = getDateHeader(request, "if-modified-since");
            if (ifModifiedSince > 0L) {
                long modDate = (modified / 1000L) * 1000L;
                if (modDate <= ifModifiedSince) {
                    return false;
                }
            }
            setDateHeader(response, "Last-Modified", modified);
            response.setHeader("Etag", etag);
        }
        return true;
    }

    @RequestMapping("/qr")
    public void qr(String code, int size, String name, String text, boolean useIcon, HttpServletRequest request, HttpServletResponse response) {
        String etag = Codecs.hash(request.getRequestURI());
        if (!RequestUtils.isModified(etag, 0)) {
            ResponseUtils.notModified(response, null);
            return;
        }

        response.setContentType("image/png");
        QRCode qrCode = (QRCode) QRCode.from(code).withHint(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M).withHint(EncodeHintType.MARGIN, 1).to(ImageType.PNG);
        if (size > 0) {
            qrCode.withSize(size, size);
        }
        RequestUtils.cacheFor(response, etag, "300d", System.currentTimeMillis());

        if (name != null) {
            RequestUtils.attachment(request, response, name + ".png");
        }
        byte[] imgBytes = qrCode.stream().toByteArray();
        try {
            Image qr = ImageIO.read(new ByteArrayInputStream(imgBytes));
            int qw = qr.getWidth(null);

            boolean addText = StringUtils.isNotBlank(text);
            int qh = addText ? qw + 20 : qw;

            BufferedImage bi = new BufferedImage(qw, qh, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bi.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, qw, qh);
            g.drawImage(qr, 0, 0, null);

            if (useIcon) {
                Image icon = ImageIO.read(IOUtils.load(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/global/images/icon.png"));
                int iw = qr.getWidth(null);
                int maxIconWidth = qw / 8;
                if (maxIconWidth < iw) {
                    iw = maxIconWidth;
                    icon = icon.getScaledInstance(iw, iw, Image.SCALE_SMOOTH);
                }
                g.drawImage(icon, (qw - iw) / 2, (qw - iw) / 2, iw, iw, null);
            }

            if (addText) {
                g.setColor(Color.BLACK);
                g.drawString(text, 10, qh - 10);
            }

            g.dispose();
            ByteArrayOutputStream bao = new ByteArrayOutputStream();

            ImageIO.write(bi, "png", bao);
            ResponseUtils.renderBinary(response, new ByteArrayInputStream(bao.toByteArray()), Codecs.hash(code) + ".png", true);
        } catch (IOException e) {
        }
    }

}
