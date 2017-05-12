package com.compomics.secretesite.controllers;

import com.compomics.secretesite.domain.repositories.GeneRepository;
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

    private static final String ENSEMBLEGENE = "ENSG";

    private final GeneRepository geneRepository;

    public GeneViewController(GeneRepository geneRepository) {
        this.geneRepository = geneRepository;
    }

    @RequestMapping("/geneview")
    public String geneView(@RequestParam(value="id",defaultValue = "") String geneAccession, Model model){

        Map<String,Object> attr = new HashMap<>();
        if (geneAccession.isEmpty()){
            model.addAttribute("genes",geneRepository.findAll());
            return "genelist";
        }
        if(geneAccession.length() == 15 && geneAccession.toUpperCase().startsWith(ENSEMBLEGENE)){
            attr.put("gene",geneRepository.findByGeneAccession(geneAccession.toUpperCase()));
        }else{
            attr.put("gene", geneRepository.findByGeneName(geneAccession));
            if(attr.get("gene") == null || attr.get("gene").equals("null")){
                attr.put("gene", geneRepository.findByGeneName(geneAccession.toUpperCase()));
            }
            if(attr.get("gene") == null || attr.get("gene").equals("null")){
                attr.put("gene", geneRepository.findByGeneName(geneAccession.toLowerCase()));
            }
        }
        model.addAllAttributes(attr);
        return "geneview";
    }
}
