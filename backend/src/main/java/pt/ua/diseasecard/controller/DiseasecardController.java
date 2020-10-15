package pt.ua.diseasecard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ua.diseasecard.configuration.DiseasecardProperties;

@RestController
public class DiseasecardController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot! ";
    }
}
