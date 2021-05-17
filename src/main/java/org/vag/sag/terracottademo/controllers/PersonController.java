package org.vag.sag.terracottademo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.vag.sag.terracottademo.services.PersonService;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @RequestMapping(method = RequestMethod.GET,value = "/person",produces = MediaType.APPLICATION_JSON_VALUE)
    public String getPerson(){
        return "Hello";
    }
}
