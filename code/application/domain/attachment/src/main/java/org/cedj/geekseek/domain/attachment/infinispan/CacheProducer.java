package org.cedj.geekseek.domain.attachment.infinispan;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

import org.infinispan.Cache;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

public class CacheProducer {

    @Produces @ApplicationScoped
    public EmbeddedCacheManager  create() {
        GlobalConfiguration config = new GlobalConfigurationBuilder()
            .globalJmxStatistics().cacheManagerName("geekseek").build();

        return new DefaultCacheManager(config);
    }

    @Produces @ApplicationScoped
    public Cache<Object, Object> create(EmbeddedCacheManager manager) {
        return manager.getCache();
    }

    public void destory(@Disposes EmbeddedCacheManager manager) {
        manager.stop();
    }

    public void destory(@Disposes Cache<Object, Object> cache) {
        cache.stop();
    }
}
