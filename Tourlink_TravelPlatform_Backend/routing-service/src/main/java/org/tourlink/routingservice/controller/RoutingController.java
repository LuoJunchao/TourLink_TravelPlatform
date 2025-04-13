package org.tourlink.routingservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/routing")
public class RoutingController {
    @GetMapping(value = "/test")
    public String test (){
        return "this is routing-service";
    }
}