package com.compomics.secretesite.domain.repositories;

import com.compomics.secretesite.domain.Transcript;
import com.compomics.secretesite.domain.TranscriptCluster;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by demet on 9/28/2017.
 */
@RepositoryRestResource(collectionResourceRel = "transcriptCluster",path = "transcriptCluster")
public interface TranscriptClusterRepository   extends PagingAndSortingRepository<TranscriptCluster,Integer> {
    List<TranscriptCluster> findByTranscriptClusterMember(@RequestParam Transcript transcript);

    List<TranscriptCluster> findByTranscriptClusterGroupIdAndIsTranscriptRepresentative(@RequestParam Integer transcriptClusterGroupId, @RequestParam Integer isTranscriptRepresentative);
}
