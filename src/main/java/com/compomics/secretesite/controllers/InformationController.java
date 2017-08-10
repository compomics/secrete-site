package com.compomics.secretesite.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by demet on 8/7/2017.
 */
@Controller
public class InformationController {

    @RequestMapping("/information")
    String information (){
        return "information";
    }
}
