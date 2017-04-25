package com.compomics.secretesite.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by demet on 4/19/2017.
 */
@Controller
public class ProteinController {

    @RequestMapping("/3dProtein")
    String protein(){
        return "3dProtein";
    }
}
