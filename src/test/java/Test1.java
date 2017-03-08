import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/3/7
 */

public class Test1 {

    @Test
    public void testSubString() {
        String a = "Content-disposition: attachment; filename=\"MEDIA_ID.jpg\"";
        String b = StringUtils.substring(a,a.indexOf("\"")+1,a.lastIndexOf("\""));
        System.out.println(b);
    }
}
