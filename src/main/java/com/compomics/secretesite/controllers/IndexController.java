package com.compomics.secretesite.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by davy on 4/12/2017.
 */
@Controller
public class IndexController {

    @RequestMapping("/index")
    String index(){
        return "index";
    }
}
