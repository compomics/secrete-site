package com.compomics.secretesite.controllers;

import com.compomics.secretesite.controllers.services.DomainService;
import com.compomics.secretesite.controllers.services.ProteinService;
import com.compomics.secretesite.controllers.services.TranscriptService;
import com.compomics.secretesite.domain.*;
import com.compomics.secretesite.domain.dataTransferObjects.DomainDTO;
import com.compomics.secretesite.domain.dataTransferObjects.FragmentDTO;
import com.compomics.secretesite.domain.dataTransferObjects.ProteinDTO;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by davy on 4/12/2017.
 */
@Controller
public class IndexController {

    private final ProteinService proteinService;
    private final DomainService domainService;
    private final TranscriptService transcriptService;

    public IndexController(ProteinService proteinService, DomainService domainService, TranscriptService transcriptService) {
        this.proteinService = proteinService;
        this.domainService = domainService;
        this.transcriptService = transcriptService;
    }

    @RequestMapping("/index")
    String index(@RequestParam(value="protein",defaultValue = "") String uniprotAccession,
                 @RequestParam(value="domain",defaultValue = "") String domainAccession,
                 @RequestParam(value="type",defaultValue = "") String type,Model model){
        Map<String,Object> attr = new HashMap<>();
        List<ProteinDTO> proteinDTOS = new ArrayList<>();
        Set<Protein> proteins = new HashSet<>();
        if(!uniprotAccession.equals("") && domainAccession.equals("")){
            proteins = proteinService.getProteinByAccession(uniprotAccession.toUpperCase());

        }else if(uniprotAccession.equals("") && !domainAccession.equals("")){
            Domain domain =domainService.getDomainByAccession(domainAccession);

            for(ProteinDomain pd : domain.getProteinDomains())  {
                proteins.addAll(proteinService.getProteinByEnsemblAccession(pd.getProteinStableId()));
            }
        }

        if(!proteins.isEmpty()){
            proteinDTOS.addAll(createProteinDTO(proteins, type));
        }

        attr.put("proteinDTOS",proteinDTOS);
        attr.put("proteinDTOSJson", new Gson().toJson(proteinDTOS));

        model.addAllAttributes(attr);
        return "index";
    }





    private List<ProteinDTO> createProteinDTO(Set<Protein> proteins, String type){
        List<ProteinDTO> proteinDTOs = new ArrayList<>();

        Map<String , List<Protein>> proteinMap = new HashMap<>();

        for(Protein p : proteins){
            if(p.getSwissProtAccession() != null && !p.getSwissProtAccession().equals("")){
                if(!proteinMap.containsKey(p.getSwissProtAccession())){
                    proteinMap.put(p.getSwissProtAccession(), new ArrayList<>(Arrays.asList(p)));
                }else{
                    proteinMap.get(p.getSwissProtAccession()).add(p);
                }
            }else if(p.getTrEmblAccession() != null && !p.getTrEmblAccession().equals("")){
                if(!proteinMap.containsKey(p.getTrEmblAccession())){
                    proteinMap.put(p.getTrEmblAccession(), new ArrayList<>(Arrays.asList(p)));
                }else{
                    proteinMap.get(p.getTrEmblAccession()).add(p);
                }
            }
        }

        proteinMap.forEach((k,v) -> {

            ProteinDTO proteinDTO = new ProteinDTO();
            proteinDTO.setProteinAccession(k);
            proteinDTO.setProteinName((v.get(0).getSwissProtName() != null) ? v.get(0).getSwissProtName() : v.get(0).getTrEmblName() );
            proteinDTO.setProteinLabel((v.get(0).getSwissProtLabel() != null) ? v.get(0).getSwissProtLabel() : v.get(0).getTrEmblLabel());
            Set<TranscriptProtein> transcriptProteins = new HashSet<>();
            Map<Integer, List<FragmentDTO>> transcriptClusterMap = new HashMap<>();
            Set<DomainDTO> domainDTOS = new HashSet<>();
            v.forEach(protein -> {
                domainService.getProteinDomainsbyProteinStableId(protein.getProteinEnsemblAccession()).forEach(pd ->{
                    DomainDTO newDomainDTO = new DomainDTO(pd.getDomain().getDomainAccession(), pd.getDomain().getDomainName(), pd.getDomainStart(), pd.getDomainEnd());
                    boolean isDomainNew = true;
                    for(DomainDTO domainDTO : domainDTOS){
                        if(domainDTO.equals(newDomainDTO)){ isDomainNew = false;}
                    }
                    if(isDomainNew){domainDTOS.add(newDomainDTO);}

                });


                transcriptService.findTranscriptProteinsbyProteinStableId(protein.getProteinEnsemblAccession()).forEach(tp ->{
                    transcriptProteins.add(tp);
                });
            });
            proteinDTO.getDomainDTOs().addAll(domainDTOS);
            Collections.sort(proteinDTO.getDomainDTOs(), (o1, o2) -> new Integer(o1.getDomainStart()).compareTo(o2.getDomainStart()));
            proteinDTO.setDomainDTOs(proteinDTO.getDomainDTOs());


            for (TranscriptProtein tp : transcriptProteins) {
                String species = tp.getParentTranscript().getTranscriptsExpressableInSpecies().stream().map(s -> s.getSpecies().getSpeciesName()).collect(Collectors.joining(","));


                List<TranscriptCluster> transcriptClusters = transcriptService.findTranscriptClusterByTranscript(tp.getParentTranscript().getTranscriptId());
                transcriptClusters.forEach(tc ->{

                    if(type.equals("both") || tp.getParentTranscript().getSecretionStatus().equals(type)){
                        FragmentDTO fragmentDTO = new FragmentDTO(tp.getParentTranscript().getTranscriptId(),tp.getParentTranscript().getEnsembleTranscriptAccession(), Math.round(tp.getTranscriptStart()/3), Math.round(tp.getTranscriptEnd()/3),
                                tp.getParentTranscript().getFoundIn().stream().map(f->f.getTranscriptstructure().getPdbId()).collect(Collectors.joining (",")),
                                tp.getParentTranscript().getFoundIn().stream().map(f->f.getTranscriptstructure().getTranscriptStructureId().toString()).collect(Collectors.joining (","))
                                , tp.getParentTranscript().getSecretionStatus(), species, tc.getIsTranscriptRepresentative());
                        if(transcriptClusterMap.containsKey(tc.getTranscriptClusterGroupId())){
                            transcriptClusterMap.get(tc.getTranscriptClusterGroupId()).add(fragmentDTO);
                        }else{
                            transcriptClusterMap.put(tc.getTranscriptClusterGroupId(), new ArrayList<>(Arrays.asList(fragmentDTO)));
                        }
                    }

                });

                if(transcriptClusters.isEmpty()){
                    if(type.equals("both") || tp.getParentTranscript().getSecretionStatus().equals(type)){
                        proteinDTO.getMainFragmentDTOs().add(new FragmentDTO(tp.getParentTranscript().getTranscriptId(),tp.getParentTranscript().getEnsembleTranscriptAccession(), Math.round(tp.getTranscriptStart()/3), Math.round(tp.getTranscriptEnd()/3),
                                tp.getParentTranscript().getFoundIn().stream().map(f->f.getTranscriptstructure().getPdbId()).collect(Collectors.joining (",")),
                                tp.getParentTranscript().getFoundIn().stream().map(f->f.getTranscriptstructure().getTranscriptStructureId().toString()).collect(Collectors.joining (","))
                                , tp.getParentTranscript().getSecretionStatus(), species, -1));
                    }
                }
            }

            transcriptClusterMap.forEach((group, clusterList) -> {
                FragmentDTO mainFragment = null;
                for (FragmentDTO fragmentDTO : clusterList) {
                    if(fragmentDTO.getRepresentative() == 1){
                        mainFragment = fragmentDTO;
                    }
                }
                if(mainFragment != null){
                    mainFragment.getChildFragmentDTOs().addAll(clusterList.stream().filter(fragment -> fragment.getRepresentative() ==0).collect(Collectors.toList()));
                    proteinDTO.getMainFragmentDTOs().add(mainFragment);
                }
            });
            Collections.sort(proteinDTO.getMainFragmentDTOs(), (o1, o2) -> new Integer(o1.getFragmentStart()).compareTo(o2.getFragmentStart()));
            proteinDTO.getMainFragmentDTOs().forEach(fragDTO ->{
                Collections.sort(fragDTO.getChildFragmentDTOs(), (o1, o2) -> new Integer(o1.getFragmentStart()).compareTo(o2.getFragmentStart()));
            });

            proteinDTOs.add(proteinDTO);
        });

        return proteinDTOs;
    }

}
