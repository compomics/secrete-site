package com.compomics.secretesite.controllers;

import com.compomics.secretesite.controllers.services.ProteinService;
import com.compomics.secretesite.domain.Protein;
import com.compomics.secretesite.domain.dataTransferObjects.ProteinDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by davy on 4/12/2017.
 */
@Controller
public class IndexController {

    private final ProteinService proteinService;

    public IndexController(ProteinService proteinService) {
        this.proteinService = proteinService;
    }

    @RequestMapping("/index")
    String index(@RequestParam(value="id",defaultValue = "") String uniprotAccession, Model model){
        Map<String,Object> attr = new HashMap<>();
        Protein protein = proteinService.getProteinByAccession(uniprotAccession.toUpperCase());
        protein.getDomainsContainedInProtein().forEach(proteinDomain -> {
            proteinDomain.getDomain().setDomainName(findDomainName(proteinDomain.getDomain().getDomainAccession()));
        });
        List<Protein> proteins = new ArrayList<>();
        proteins.add(protein);
        attr.put("proteins",proteins);
        List<ProteinDTO> proteinDTOS = new ArrayList<>();
        protein.getDomainsContainedInProtein().forEach(d->{
            proteinDTOS.add(new ProteinDTO(protein.getProteinAccession(), d.getDomainStart(), d.getDomainEnd(), d.getDomain().getDomainAccession()));
        });
        attr.put("proteinDTOS",proteinDTOS);
        model.addAllAttributes(attr);
        return "index";
    }

    private static String findDomainName(String domainAccession){
        String url = "http://pfam.xfam.org/family/" + domainAccession + "?output=xml";
        String domainName = "";

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            URL u = new URL(url);
            Document doc = dBuilder.parse(u.openStream());
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("entry");
            Node nNode = nList.item(0);
            Element eElement = (Element) nNode;
            domainName =  eElement.getElementsByTagName("description").item(0).getTextContent();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return domainName;
    }

}
