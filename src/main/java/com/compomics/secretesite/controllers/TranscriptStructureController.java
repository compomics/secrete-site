package com.compomics.secretesite.controllers;

import com.compomics.secretesite.domain.repositories.TranscriptStructureRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

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
        if(structureId != null && !structureId.equals("")){
            attr.put("structure", transcriptStructureRepository.findByTranscriptStructureId(Integer.valueOf(structureId)));
            model.addAllAttributes(attr);
            return "3dProtein";
        }else{
            return "";
        }

    }
}
