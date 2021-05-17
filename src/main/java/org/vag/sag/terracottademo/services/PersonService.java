package org.vag.sag.terracottademo.services;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URL;

@Service
public class PersonService {
    Logger logger = LoggerFactory.getLogger(PersonService.class);

    private CacheManager manager;
    private Cache personCache;

    @PostConstruct
    public void init(){
        URL url = getClass().getResource("/ehcache.xml");
        manager = CacheManager.newInstance(url);
        logger.info("Cache Manager Initialized");

        personCache = manager.getCache("personCache");

        logger.info("personCache Initialized");
    }
}
