package org.vag.sag.terracottademo.services;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vag.sag.terracottademo.dom.Person;
import org.vag.sag.terracottademo.exceptions.PersonNotFoundException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.URL;
import java.util.List;

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

    @PreDestroy
    public void disconnect(){
        if(!personCache.isTerracottaClustered()){
            logger.info("Cache flushing");
            personCache.flush();

            logger.info("Cache manager shutdown");
            manager.shutdown();
        }
    }

    public Person createPerson(Person person) throws Exception {
        Element oldEle = personCache.get(person.getId());
        if(oldEle != null){
            throw new Exception("Element with ID= " + person.getId() + " already exists in the cache");
        }
        Element element = new Element(person.getId(), person);
        personCache.put(element);

        return person;
    }

    public Person updatePerson(Person person) throws PersonNotFoundException {

        Element element = personCache.get(person.getId());
        if(element != null) {
            element = new Element(person.getId(), person);
            personCache.replace(element);
        }
        else
            throw new PersonNotFoundException("Person with ID=" + person.getId() + " is not found in the cache");

        return person;
    }

    public Person getPerson(String id) throws PersonNotFoundException {
        Element element = personCache.get(id);
        if(element != null) {
            Object object = element.getObjectValue();
            if(object instanceof Person) {
                return (Person)object;
            }else{
                logger.error("Cache object with ID=" + id + " is not of type Person");
                throw new PersonNotFoundException("Person with ID=" + id + " is not found in the cache");
            }
        }
        else
            throw new PersonNotFoundException("Person with ID=" + id + " is not found in the cache");
    }

    public boolean deletePerson(String id) throws PersonNotFoundException {
        Element element = personCache.get(id);
        if(element != null) {
            personCache.remove(id);
            return true;
        }
        else
            throw new PersonNotFoundException("Person with ID=" + id + " is not found in the cache");
    }

    public List<String> getAllPersons()  {
        List keys = personCache.getKeysNoDuplicateCheck();
        return keys;
    }
}
