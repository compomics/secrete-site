package com.compomics.secretesite.domain.repositories;

import com.compomics.secretesite.domain.Transcript;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by davy on 4/11/2017.
 */

@RepositoryRestResource(collectionResourceRel = "transcripts",path = "transcripts")
public interface TranscriptRepository extends PagingAndSortingRepository<Transcript,Integer> {

    List<Transcript> findByEnsembleTranscriptAccession(String ensemblTranscriptAccession);


}
