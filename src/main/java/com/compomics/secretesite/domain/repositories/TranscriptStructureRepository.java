package com.compomics.secretesite.domain.repositories;


import com.compomics.secretesite.domain.TranscriptStructure;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by davy on 4/21/2017.
 */

public interface TranscriptStructureRepository extends PagingAndSortingRepository<TranscriptStructure,Integer> {
    @RestResource(path = "filterbyid")
    TranscriptStructure findByTranscriptStructureId(@RequestParam Integer id);
}
