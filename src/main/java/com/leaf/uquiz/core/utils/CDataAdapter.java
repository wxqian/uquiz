package com.leaf.uquiz.core.utils;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2014/12/5
 */
public class CDataAdapter extends XmlAdapter<String, String> {
    @Override
    public String marshal(String str) throws Exception {
        return "<![CDATA[" + str + "]]>";
    }

    @Override
    public String unmarshal(String str) throws Exception {
        return str;
    }
}
