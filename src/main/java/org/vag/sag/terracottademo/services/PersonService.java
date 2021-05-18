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

    public Person createPerson(Person person){
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
        if(element != null)
            return (Person)element.getObjectValue();
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
