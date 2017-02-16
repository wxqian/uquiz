package com.leaf.uquiz.core.utils;

import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/8/17.
 */
public class ListUtils {

    public interface KeyFinder<T, K> {
        K findKey(T target);
    }

    public interface Decide<T> {
        boolean judge(T target);
    }

    public interface Transform<K, T> {
        T transform(K target);
    }

    public interface Filter<K> {
        boolean filter(K target);
    }

    public static <K, T> Map<K, T> toMap(Collection<T> targets, KeyFinder<T, K> keyFinder) {
        if (targets == null) {
            return Collections.emptyMap();
        }
        Map<K, T> result = Maps.newHashMap();
        for (T target : targets) {
            result.put(keyFinder.findKey(target), target);
        }
        return result;
    }

    public static <T> T findOne(Collection<T> targets, Decide decide) {
        if (CollectionUtils.isEmpty(targets)) {
            return null;
        }

        for (T target : targets) {
            if (decide.judge(target)) {
                return target;
            }
        }

        return null;
    }

    public static <T> List<T> findMore(Collection<T> targets, Decide decide) {
        List<T> result = new ArrayList<>();

        if (CollectionUtils.isEmpty(targets)) {
            return result;
        }

        for (T target : targets) {
            if (decide.judge(target)) {
                result.add(target);
            }
        }

        return result;
    }

    public static <K, T> List<T> transform(Collection<K> targets, Transform<K, T> transform) {
        if (CollectionUtils.isEmpty(targets)) {
            return Collections.emptyList();
        }

        List<T> result = new ArrayList<>(targets.size());
        for (K k : targets) {
            result.add(transform.transform(k));
        }

        return result;
    }

    public static <K> List<K> filter(Collection<K> targets, Filter<K> filter) {
        if (CollectionUtils.isEmpty(targets)) {
            return Collections.EMPTY_LIST;
        }
        List<K> result = new ArrayList<>(targets.size());
        for (K k : targets) {
            if (filter.filter(k)) {
                result.add(k);
            }
        }
        return result;
    }
}
