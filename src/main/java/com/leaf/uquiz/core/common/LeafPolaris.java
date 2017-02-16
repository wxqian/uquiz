package com.leaf.uquiz.core.common;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/10/27
 */
public class LeafPolaris implements IdentifierGenerator {

    public static final String Type = "com.leaf.uquiz.core.common.LeafPolaris";

    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        return IdGen.get().nextId();
    }
}
