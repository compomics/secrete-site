package com.compomics.secretesite.controllers;

import com.compomics.secretesite.controllers.services.DomainService;
import com.compomics.secretesite.controllers.services.ProteinService;
import com.compomics.secretesite.domain.Domain;
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
    private final DomainService domainService;

    public IndexController(ProteinService proteinService, DomainService domainService) {
        this.proteinService = proteinService;
        this.domainService = domainService;
    }

    @RequestMapping("/index")
    String index(@RequestParam(value="protein",defaultValue = "") String uniprotAccession,
                 @RequestParam(value="domain",defaultValue = "") String domainAccession, Model model){
        Map<String,Object> attr = new HashMap<>();
        List<ProteinDTO> proteinDTOS = new ArrayList<>();

        if(!uniprotAccession.equals("") && domainAccession.equals("")){
            Protein protein = proteinService.getProteinByAccession(uniprotAccession.toUpperCase());
            if(protein != null){
                proteinDTOS.add(createProteinDTO(protein));
            }
        }else if(uniprotAccession.equals("") && !domainAccession.equals("")){
            List<String> proteinAccessions = new ArrayList<>();
            Domain domain =domainService.getDomainByAccession(domainAccession);
            domain.getProteinDomains().forEach(pd -> {
                if(!proteinAccessions.contains(pd.getProtein().getProteinAccession())){
                    Protein protein = pd.getProtein();
                    proteinDTOS.add(createProteinDTO(protein));
                    proteinAccessions.add(pd.getProtein().getProteinAccession());
                }
            });
        }
        attr.put("proteinDTOS",proteinDTOS);
        attr.put("proteinDTOSJson", new Gson().toJson(proteinDTOS));

        model.addAllAttributes(attr);
        return "index";
    }

    public String findDomainName(String domainAccession){
        String url = "http://pfam.xfam.org/family/" + domainAccession + "?output=xml";
        String domainName = "";

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
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

    public List<String> findProteinNameAndLabel(String proteinAccession){
        String url = "http://www.uniprot.org/uniprot/" + proteinAccession + ".xml";
        List<String> proteinNameLabel= new ArrayList<>();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            URL u = new URL(url);
            Document doc = dBuilder.parse(u.openStream());
            doc.getDocumentElement().normalize();
            NodeList nList1 = doc.getElementsByTagName("entry");
            Node nNode1 = nList1.item(0);
            Element eElement1 = (Element) nNode1;
            proteinNameLabel.add(eElement1.getElementsByTagName("name").item(0).getTextContent());

            NodeList nList2 = doc.getElementsByTagName("recommendedName");
            Node nNode2 = nList2.item(0);
            Element eElement2 = (Element) nNode2;
            proteinNameLabel.add(eElement2.getElementsByTagName("fullName").item(0).getTextContent());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  proteinNameLabel;
    }

    private ProteinDTO createProteinDTO(Protein protein){
        ProteinDTO proteinDTO = new ProteinDTO();

        List<DomainDTO> domainDTOS = new ArrayList<>();
        List<FragmentDTO> fragmentDTOS = new ArrayList<>();

        proteinDTO.setProteinAccession(protein.getProteinAccession());
        proteinDTO.setProteinName(findProteinNameAndLabel(protein.getProteinAccession()).get(1));
        proteinDTO.setProteinLabel(findProteinNameAndLabel(protein.getProteinAccession()).get(0));
        protein.getDomainsContainedInProtein().forEach(d->{
            domainDTOS.add(new DomainDTO(d.getDomain().getDomainAccession(),findDomainName(d.getDomain().getDomainAccession()), d.getDomainStart(), d.getDomainEnd()));
        });
        proteinDTO.setDomainDTOs(domainDTOS);

        protein.getParentTranscripts().forEach(t -> {


            String species = t.getParentTranscript().getTranscriptsExpressableInSpecies().stream().map(s -> s.getSpecies().getSpeciesName()).collect(Collectors.joining(","));
            fragmentDTOS.add(new FragmentDTO(t.getParentTranscript().getTranscriptId(),t.getParentTranscript().getEnsembleTranscriptAccession(), t.getTranscriptStart(), t.getTranscriptEnd(),
                    t.getParentTranscript().getFoundIn().stream().map(f->f.getTranscriptstructure().getPdbId()).collect(Collectors.joining (",")),
                    t.getParentTranscript().getFoundIn().stream().map(f->f.getTranscriptstructure().getTranscriptStructureId().toString()).collect(Collectors.joining (","))
                    , t.getParentTranscript().getSecretionStatus(), species));
        });
        proteinDTO.setMainFragmentDTOs(fragmentDTOS);

        return proteinDTO;
    }
}
