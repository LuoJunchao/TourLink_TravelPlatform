package org.tourlink.dataplatformservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/data-platform")
public class DataPlatformController {
    @GetMapping(value = "/test")
    public String test (){
        return "this is data-platform-service";
    }
}