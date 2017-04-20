package com.compomics.secretesite.controllers;

import com.compomics.secretesite.model.repositories.GeneRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by davy on 4/13/2017.
 */
@Controller
public class GeneViewController {

    private final GeneRepository geneRepository;

    public GeneViewController(GeneRepository geneRepository) {
        this.geneRepository = geneRepository;
    }

    @RequestMapping("/geneview")
    String geneView(@RequestParam(value="id",defaultValue = "") String geneAccession, Model model){

        Map<String,Object> attr = new HashMap<>();
        if (geneAccession.isEmpty()){
            model.addAttribute("genes",geneRepository.findAll());
            return "genelist";
       }

        attr.put("gene",geneRepository.findByGeneAccession(geneAccession));
        model.addAllAttributes(attr);
        return "geneview";
    }

}
