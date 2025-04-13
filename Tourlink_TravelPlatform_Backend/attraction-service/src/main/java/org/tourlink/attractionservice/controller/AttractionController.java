package org.tourlink.attractionservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/attraction")
public class AttractionController {
    @GetMapping(value = "/test")
    public String test (){
        return "this is attraction-service";
    }
}
