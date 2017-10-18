package com.compomics.secretesite.controllers;

import com.compomics.secretesite.domain.Transcript;
import com.compomics.secretesite.domain.TranscriptStructure;
import com.compomics.secretesite.domain.dataTransferObjects.StructureDTO;
import com.compomics.secretesite.domain.repositories.TranscriptRepository;
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
    private final TranscriptRepository transcriptRepository;

    public TranscriptStructureController(TranscriptStructureRepository transcriptStructureRepository, TranscriptRepository transcriptRepository) {
        this.transcriptStructureRepository = transcriptStructureRepository;
        this.transcriptRepository = transcriptRepository;
    }

    @RequestMapping("/3dProtein")
    String protein(@RequestParam(value="transcriptId",defaultValue = "") Integer transcriptId, Model model){
        Map<String,Object> attr = new HashMap<>();
        List<StructureDTO> transcriptStructures = new ArrayList<>();

        if(transcriptId != null && !transcriptId.equals("")){
            Transcript transcript = transcriptRepository.findByTranscriptId(transcriptId);
            transcript.getFoundIn().forEach(f -> {
                StructureDTO structureDTO = new StructureDTO(f.getTranscriptstructure().getPdbId(), f.getTranscriptstructure().getChain(), f.getTranscriptstructure().getFragmentStart(),
                        f.getTranscriptstructure().getFragmentEnd(), transcript.getSecretionStatus());
                transcriptStructures.add(structureDTO);
            });
            attr.put("structure", transcriptStructures);

            model.addAllAttributes(attr);
            return "3dProtein";
        }else{
            return "";
        }

    }
}
