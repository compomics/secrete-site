package com.compomics.secretesite.controllers.services;

import com.compomics.secretesite.domain.Transcript;
import com.compomics.secretesite.domain.TranscriptCluster;
import com.compomics.secretesite.domain.repositories.TranscriptClusterRepository;
import org.springframework.stereotype.Service;

/**
 * Created by demet on 9/28/2017.
 */
@Service
public class TranscriptService {
    private final TranscriptClusterRepository transcriptClusterRepository;

    public TranscriptService(TranscriptClusterRepository transcriptClusterRepository){
        this.transcriptClusterRepository = transcriptClusterRepository;
    }

    public TranscriptCluster findTranscriptClusterByTranscript(Integer transcriptId){
        Transcript transcript = new Transcript();
        transcript.setTranscriptId(transcriptId);
        TranscriptCluster transcriptCluster = transcriptClusterRepository.findByTranscriptClusterMember(transcript);
        return transcriptCluster;
    }
}
