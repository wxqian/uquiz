package com.leaf.uquiz.core.common;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/8/18.
 */
public abstract class AbstractConverter<S, T> implements Converter<S, T> {

    @Override
    public T convert(S source) {
        if (source == null) {
            return null;
        }
        return internalConvert(source);
    }

    protected abstract T internalConvert(S source);

    public List<T> convert(Collection<S> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return Collections.emptyList();
        }
        List<T> result = Lists.newArrayList();
        for (S source : sources) {
            result.add(convert(source));
        }
        return result;
    }

    public Page<T> convert(Page<S> source, Pageable pageable) {
        List<S> slist = source.getContent();
        List<T> tlist = new ArrayList<>();
        if (!CollectionUtils.isEmpty(slist)) {
            tlist = convert(slist);
        }
        return new PageImpl<T>(tlist, pageable, source.getTotalElements());
    }
}
