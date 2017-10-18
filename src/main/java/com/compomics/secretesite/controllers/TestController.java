package com.compomics.secretesite.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by demet on 9/25/2017.
 */
@Controller
public class TestController {

    @RequestMapping("/test")
    String test(){
        return "test";
    }
}
