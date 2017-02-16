package com.leaf.uquiz.file.service.impl;


import com.leaf.uquiz.file.domain.Dimension;
import com.leaf.uquiz.file.domain.ImageType;

import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

public class AwtImageTransform implements ImageTransform {

    protected BufferedImage srcBImage;
    protected BufferedImage destBImage;
    protected int imgWidth;
    protected int imgHeight;
    protected boolean modified = false;
    protected String formatName;
    protected float quality = 1;

    public AwtImageTransform() {

    }

    public AwtImageTransform(float quality) {
        this.quality = quality;
    }

    @Override
    public void load(String srcPath) throws IOException {
        load(new FileInputStream(srcPath));
    }

    @Override
    public void save(String destPath) throws IOException {
        if (destBImage == null) {
            destBImage = srcBImage;
        }
        write(new FileOutputStream(new File(destPath)));
    }

    @Override
    public void load(InputStream input) throws IOException {
        convertToBufferedImage(input);
    }

    @Override
    public void save(OutputStream out) throws IOException {
        if (destBImage == null) {
            destBImage = srcBImage;
        }
        write(out);
    }

    private void write(OutputStream out) throws IOException {
        if (quality < 1 && getImageType() == ImageType.JPG) {
            ImageWriter iw = ImageIO.getImageWritersByFormatName(formatName).next();
            iw.setOutput(new MemoryCacheImageOutputStream(out));
            ImageWriteParam iwp = iw.getDefaultWriteParam();
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionQuality(quality);
            iw.write(null, new IIOImage(destBImage, null, null), iwp);
            iw.dispose();
        } else {
            ImageIO.write(destBImage, formatName, out);
        }
    }

    public boolean isModified() {
        return modified;
    }

    @Override
    public Dimension getSize() throws IOException {
        return new Dimension(imgWidth, imgHeight);
    }

    @Override
    public void resize(int width, int height) throws IOException {
        zoomImage(width, height);
    }

    @Override
    public void rotate(double rotate) throws IOException {
        if (rotate < 1e-3) {
            return;
        }
        double radian = rotate * Math.PI / 180;  //get radian
        double w = Math.abs(imgHeight * Math.sin(radian)) + Math.abs(imgWidth * Math.cos(radian));  //get rotated height
        double h = Math.abs(imgHeight * Math.cos(radian)) + Math.abs(imgWidth * Math.sin(radian));  //get rotated width
        rotateImage((int) w, (int) h, radian, 1);
    }

    @Override
    public void resizeWithMax(Integer maxWidth, Integer maxHeight) throws IOException {
        Double ratio = null;
        if (maxWidth != null && maxWidth > 0 && imgWidth > maxWidth) {
            ratio = (double) maxWidth / imgWidth;
        }
        if (maxHeight != null && maxHeight > 0 && imgHeight > maxHeight) {
            double yRatio = (double) maxHeight / imgHeight;
            if (ratio == null || yRatio < ratio) {
                ratio = yRatio;
            }
        }
        if (ratio != null) {
            zoomImage((int) (imgWidth * ratio), (int) (imgHeight * ratio));
        }
    }

    @Override
    public void rotateWithMax(double rotate, Integer maxWidth, Integer maxHeight) throws IOException {
        if (rotate < 1) {
            return;
        }
        double radian = rotate * Math.PI / 180;  //get radian
        double w = Math.abs(imgHeight * Math.sin(radian)) + Math.abs(imgWidth * Math.cos(radian));  //get rotated height
        double h = Math.abs(imgHeight * Math.cos(radian)) + Math.abs(imgWidth * Math.sin(radian));  //get rotated width
        double ratio = 1;
        if (maxWidth != null && maxWidth > 0) {
            ratio = (double) maxWidth / w;
        }
        if (maxHeight != null && maxHeight > 0) {
            double yRatio = (double) maxHeight / h;
            if (ratio == 1 || yRatio < ratio) {
                ratio = yRatio;
            }
        }
        rotateImage((int) Math.abs(w), (int) Math.abs(h), radian, ratio);
    }

    @Override
    public void crop(int left, int top, Integer width, Integer height) throws IOException {
        if (width == null || width == 0) {
            width = imgWidth - left;
        }
        if (height == null || height == 0) {
            height = imgHeight - top;
        }
        if (width > 0 && height > 0 && width <= imgWidth && height <= imgHeight) {
            cropImage(left, top, width, height);
        }
    }

    @Override
    public void resizeAndcrop(int maxWidth, int maxHeight, int cropHeight) throws IOException {
        Double ratio = null;
        if (maxWidth > 0 && imgWidth > maxWidth) {
            ratio = (double) maxWidth / imgWidth;
        }
        if (maxHeight > 0 && imgHeight > maxHeight) {
            double yRatio = (double) maxHeight / imgHeight;
            if (ratio == null || yRatio < ratio) {
                ratio = yRatio;
            }
        }
        int width = imgWidth;
        int height = imgHeight;
        if (ratio != null) {
            width = (int) (imgWidth * ratio);
            height = (int) (imgHeight * ratio);
            if (width == 0) {
                width = 1;
            }
            if (height == 0) {
                height = 1;
            }
        }
        boolean needCrop = cropHeight > 0 && height > cropHeight;
        if (ratio == null && !needCrop) {
            return;
        }
        destBImage = new BufferedImage(width, needCrop ? cropHeight : height, srcBImage.getType());
        destBImage.getGraphics().drawImage(ratio != null ? srcBImage.getScaledInstance(width, height, Image.SCALE_SMOOTH) : srcBImage, 0, 0, null);
        modified = true;
    }

    private void zoomImage(int width, int height) {
        if (width == 0) {
            width = 1;
        }
        if (height == 0) {
            height = 1;
        }
        destBImage = new BufferedImage(width, height, srcBImage.getType());
        destBImage.getGraphics().drawImage(srcBImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        modified = true;
    }

    private void rotateImage(int w, int h, double radian, double ratio) {
        AffineTransform transform = new AffineTransform();
        transform.setToScale(ratio, ratio);
        transform.rotate(radian, w / 2, h / 2);
        transform.translate(w / 2 - imgWidth / 2, h / 2 - imgHeight / 2);
        AffineTransformOp ato = new AffineTransformOp(transform, null);
        w *= ratio;
        h *= ratio;
        destBImage = new BufferedImage(w, h, srcBImage.getType());
        if (getImageType() == ImageType.JPG) {
            Graphics gs = destBImage.getGraphics();
            gs.setColor(Color.white); //set canvas background to white
            gs.fillRect(0, 0, w, h);
        }
        ato.filter(srcBImage, destBImage);
        modified = true;
    }


    private void cropImage(int left, int top, int width, int height) {
        destBImage = new BufferedImage(width, height, srcBImage.getType());
        destBImage.getGraphics().drawImage(srcBImage, -left, -top, imgWidth, imgHeight, null);
        modified = true;
    }

    private void convertToBufferedImage(InputStream inputStream) throws IOException {
        if (inputStream != null) {
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(inputStream);
            initFormatName(imageInputStream);
            if (formatName != null) {
                this.srcBImage = ImageIO.read(imageInputStream);
                this.imgWidth = srcBImage.getWidth();
                this.imgHeight = srcBImage.getHeight();
            }
        }
    }

    private void initFormatName(ImageInputStream imageInputStream) throws IOException {
        Iterator<ImageReader> ir = ImageIO.getImageReaders(imageInputStream);
        if (ir.hasNext()) {
            this.formatName = ir.next().getFormatName();
        }
    }

    @Override
    public InputStream getTransformed() throws IOException {
        if (destBImage == null) {
            destBImage = srcBImage;
        }
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
        ImageIO.write(destBImage, formatName, imOut);
        return new ByteArrayInputStream(bs.toByteArray());
    }

    @Override
    public ImageType getImageType() {
        return ImageType.fromExt(formatName);
    }
}
