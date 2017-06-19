package com.compomics.secretesite.controllers;

import com.compomics.secretesite.domain.repositories.ProteinRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by davy on 4/12/2017.
 */
@Controller
public class IndexController {

    private final ProteinRepository proteinRepository;

    public IndexController(ProteinRepository proteinRepository) {
        this.proteinRepository = proteinRepository;
    }
    @RequestMapping("/index")
    String index(@RequestParam(value="id",defaultValue = "") String uniprotAccession, Model model){
        Map<String,Object> attr = new HashMap<>();
        attr.put("protein",proteinRepository.findByProteinAccession(uniprotAccession.toUpperCase()));
        model.addAllAttributes(attr);
        return "index";
    }
}
