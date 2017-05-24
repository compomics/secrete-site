package com.compomics.secretesite.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by demet on 5/8/2017.
 */
@Controller
public class SearchController {

    @RequestMapping("/search")
    String protein(){
        return "search";
    }
}
