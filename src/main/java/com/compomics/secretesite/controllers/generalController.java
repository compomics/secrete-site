package com.compomics.secretesite.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by davy on 4/9/2017. name to be revised
 */
@RestController
public class generalController {

    @RequestMapping("/")
    public String index(){
        return "test2";
    }


}
