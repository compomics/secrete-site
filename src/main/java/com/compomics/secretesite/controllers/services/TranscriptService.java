package com.compomics.secretesite.controllers.services;

import com.compomics.secretesite.domain.Transcript;
import com.compomics.secretesite.domain.TranscriptCluster;
import com.compomics.secretesite.domain.TranscriptProtein;
import com.compomics.secretesite.domain.repositories.TranscriptClusterRepository;
import com.compomics.secretesite.domain.repositories.TranscriptProteinRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by demet on 9/28/2017.
 */
@Service
public class TranscriptService {
    private final TranscriptClusterRepository transcriptClusterRepository;
    private final TranscriptProteinRepository transcriptProteinRepository;

    public TranscriptService(TranscriptClusterRepository transcriptClusterRepository, TranscriptProteinRepository transcriptProteinRepository){
        this.transcriptClusterRepository = transcriptClusterRepository;
        this.transcriptProteinRepository = transcriptProteinRepository;
    }

    public List<TranscriptCluster> findTranscriptClusterByTranscript(Integer transcriptId){
        Transcript transcript = new Transcript();
        transcript.setTranscriptId(transcriptId);
        return transcriptClusterRepository.findByTranscriptClusterMember(transcript);
    }

    public List<TranscriptProtein> findTranscriptProteinsbyProteinStableId(String proteinStableId){
        return transcriptProteinRepository.findByProteinStableId(proteinStableId);
    }

    public List<TranscriptCluster> findTranscriptClusterByTranscriptClusterGroupIdAndIsTranscriptRepresentative(Integer groupId, Integer isRepresentative){
        return transcriptClusterRepository.findByTranscriptClusterGroupIdAndIsTranscriptRepresentative(groupId, isRepresentative);
    }
}
