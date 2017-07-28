package com.compomics.secretesite.controllers;

import com.compomics.secretesite.controllers.services.ProteinService;
import com.compomics.secretesite.domain.Protein;
import com.compomics.secretesite.domain.dataTransferObjects.DomainDTO;
import com.compomics.secretesite.domain.dataTransferObjects.FragmentDTO;
import com.compomics.secretesite.domain.dataTransferObjects.ProteinDTO;
import com.google.gson.Gson;
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
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

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

        List<ProteinDTO> proteinDTOS = new ArrayList<>();
        List<DomainDTO> domainDTOS = new ArrayList<>();
        List<FragmentDTO> fragmentDTOS = new ArrayList<>();
        ProteinDTO proteinDTO = new ProteinDTO();

        proteinDTO.setProteinAccession(uniprotAccession);
        proteinDTO.setProteinName(findProteinName(uniprotAccession));
        protein.getDomainsContainedInProtein().forEach(d->{
            domainDTOS.add(new DomainDTO(d.getDomain().getDomainAccession(),findDomainName(d.getDomain().getDomainAccession()), d.getDomainStart(), d.getDomainEnd()));
        });
        proteinDTO.setDomainDTOs(domainDTOS);

        protein.getParentTranscripts().forEach(t -> {

            fragmentDTOS.add(new FragmentDTO(t.getParentTranscript().getEnsembleTranscriptAccession(), t.getTranscriptStart(), t.getTranscriptEnd(),
                    t.getParentTranscript().getFoundIn().stream().map(f->f.getTranscriptstructure().getPdbId()).collect(Collectors.joining (",")),
                    t.getParentTranscript().getFoundIn().stream().map(f->f.getTranscriptstructure().getTranscriptStructureId().toString()).collect(Collectors.joining (","))
                    , t.getParentTranscript().getSecretionStatus()));
        });
        proteinDTO.setMainFragmentDTOs(fragmentDTOS);

        proteinDTOS.add(proteinDTO);

        attr.put("proteinDTOS",proteinDTOS);
        attr.put("proteinDTOSJson", new Gson().toJson(proteinDTOS));
        model.addAllAttributes(attr);
        return "index";
    }

    private String findDomainName(String domainAccession){
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

    private String findProteinName(String proteinAccession){
        String url = "http://www.uniprot.org/uniprot/" + proteinAccession + ".xml";
        String proteinName = "";

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            URL u = new URL(url);
            Document doc = dBuilder.parse(u.openStream());
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("recommendedName");
            Node nNode = nList.item(0);
            Element eElement = (Element) nNode;
            proteinName =  eElement.getElementsByTagName("fullName").item(0).getTextContent();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  proteinName;
    }


}
