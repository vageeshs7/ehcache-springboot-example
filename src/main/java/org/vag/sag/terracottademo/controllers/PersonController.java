package org.vag.sag.terracottademo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vag.sag.terracottademo.dom.Person;
import org.vag.sag.terracottademo.exceptions.PersonNotFoundException;
import org.vag.sag.terracottademo.services.PersonService;

import java.util.List;

@RestController
public class PersonController {

    Logger logger = LoggerFactory.getLogger(PersonService.class);

    @Autowired
    private PersonService personService;

    @RequestMapping(method = RequestMethod.GET,value = "/persons/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getPerson(@PathVariable String id){
        Person person = null;
        try {
             person = personService.getPerson(id);
        } catch (PersonNotFoundException e) {
            logger.info(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Person>(person, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/persons",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addNewPerson(@RequestBody Person person){
        try {
            person = personService.createPerson(person);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Person>(person, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT,value = "/persons",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updatePerson(@RequestBody Person person){
        try {
            person = personService.updatePerson(person);
        } catch (PersonNotFoundException e) {
            logger.info(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Person>(person, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE,value = "/persons/{id}",produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deletePerson(@PathVariable String id){
        Boolean response = null;
        try {
            response = personService.deletePerson(id);
        } catch (PersonNotFoundException e) {
            logger.info(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<String>("Person with ID=" + id + " deleted", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/persons",produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getAllPersons(){
        List<String> keys = personService.getAllPersons();
        String response = "";
        for(String key : keys)
            response += "Key with ID=" + key + " stored\n";

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }
}
