package com.compomics.secretesite.controllers;

import com.compomics.secretesite.controllers.services.DomainService;
import com.compomics.secretesite.controllers.services.ProteinService;
import com.compomics.secretesite.domain.Domain;
import com.compomics.secretesite.domain.dataTransferObjects.ProteinDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by demet on 5/8/2017.
 */
@Controller
public class SearchController {

    private final ProteinService proteinService;
    private final DomainService domainService;

    public SearchController(ProteinService proteinService, DomainService domainService) {
        this.proteinService = proteinService;
        this.domainService = domainService;
    }

    @RequestMapping("/search")
    String search(){

        return "search";

    }

    @RequestMapping("/detailedSearch")
    String detailedSearch(@RequestParam(value="protein",defaultValue = "") String uniprotName,
                       @RequestParam(value="domain",defaultValue = "") String domainName, Model model){

        Map<String,Object> attr = new HashMap<>();

        if(uniprotName != null && domainName != null){
            if(!uniprotName.equals("") && domainName.equals("")){

                Map<String, ProteinDTO> proteins = new HashMap<>();
                proteinService.getProteinByName(uniprotName).forEach(protein -> {
                    if(protein.getSwissProtName() == null || protein.getSwissProtName().equals("")){
                        if(!proteins.containsKey(protein.getTrEmblAccession())){
                            proteins.put(protein.getTrEmblAccession(),new ProteinDTO(protein.getTrEmblAccession(), protein.getTrEmblName()));
                        }
                    }else{
                        if(!proteins.containsKey(protein.getSwissProtAccession())){
                            proteins.put(protein.getSwissProtAccession(), new ProteinDTO(protein.getSwissProtAccession(), protein.getSwissProtName()));
                        }
                    }
                });

                attr.put("proteins",proteins.values());


            }else if(uniprotName.equals("") && !domainName.equals("")) {
                Map<String, Domain> domains = new HashMap<>();
                domainService.getByDomainName(domainName).forEach(domain -> {
                    if(!domains.containsKey(domain.getDomainAccession())){
                        domains.put(domain.getDomainAccession(), domain);
                    }
                });

                attr.put("domains",domains.values());

            }
        }

        model.addAllAttributes(attr);

        return "detailedSearch";

    }

}
