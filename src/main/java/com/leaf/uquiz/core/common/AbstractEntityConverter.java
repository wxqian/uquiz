package com.leaf.uquiz.core.common;

import com.leaf.uquiz.core.utils.Classes;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/8/22.
 */
@DependsOn("repositories")
public abstract class AbstractEntityConverter<S, ID extends Serializable, T> extends AbstractConverter<S, T> implements InitializingBean {

    @Autowired
    private Repositories repositories;

    private JpaRepository<S, ID> repo;

    public AbstractEntityConverter() {
        Class<?> clazz = Classes.getGenericParameter0(getClass());
    }

    public List<T> convertByIds(Collection<ID> ids) {
        return convert(repo.findAll(ids));
    }

//    public Map<ID, T> convertToMap(Collection<ID> ids) {
//        Map<ID, S> sources = repo.mget(ids);
//        if (org.springframework.util.CollectionUtils.isEmpty(sources)) {
//            return Collections.emptyMap();
//        } else {
//            Map<ID, T> result = Maps.newHashMap();
//            for (Map.Entry<ID, S> entry : sources.entrySet()) {
//                result.put(entry.getKey(), convert(entry.getValue()));
//            }
//            return result;
//        }
//    }

    @Override
    @SuppressWarnings("unchecked")
    public void afterPropertiesSet() throws Exception {
        repo = (JpaRepository<S, ID>) repositories.getRepositoryFor(Classes.getGenericParameter0(getClass()));
    }
}
