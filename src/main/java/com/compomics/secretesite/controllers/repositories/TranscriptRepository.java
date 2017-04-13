package com.compomics.secretesite.controllers.repositories;

import com.compomics.secretesite.model.Transcript;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by davy on 4/11/2017.
 */

@RepositoryRestResource(collectionResourceRel = "transcripts",path = "transcripts")
public interface TranscriptRepository extends PagingAndSortingRepository<Transcript,Integer> {

    List<Transcript> findByEnsembleTranscriptId(String ensemblTranscriptId);

}
