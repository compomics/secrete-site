package com.compomics.secretesite.controllers;

import com.compomics.secretesite.domain.TranscriptStructure;
import com.compomics.secretesite.domain.repositories.TranscriptStructureRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by demet on 4/19/2017.
 */
@Controller
public class TranscriptStructureController {

    private final TranscriptStructureRepository transcriptStructureRepository;

    public TranscriptStructureController(TranscriptStructureRepository transcriptStructureRepository) {
        this.transcriptStructureRepository = transcriptStructureRepository;
    }

    @RequestMapping("/3dProtein")
    String protein(@RequestParam(value="structureId",defaultValue = "") String structureId,Model model){
        Map<String,Object> attr = new HashMap<>();
        List<TranscriptStructure> transcriptStructures = new ArrayList<>();
        if(structureId != null && !structureId.equals("")){
            if(structureId.contains(",")){
                Pattern.compile(",")
                        .splitAsStream(structureId)
                        .collect(Collectors.toList()).forEach(id -> {
                    transcriptStructures.add(transcriptStructureRepository.findByTranscriptStructureId(Integer.valueOf(id)));
                });
            }else{
                transcriptStructures.add(transcriptStructureRepository.findByTranscriptStructureId(Integer.valueOf(structureId)));
            }
            attr.put("structure", transcriptStructures);

            model.addAllAttributes(attr);
            return "3dProtein";
        }else{
            return "";
        }

    }
}
