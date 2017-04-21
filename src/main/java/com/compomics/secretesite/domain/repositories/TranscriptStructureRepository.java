package com.compomics.secretesite.domain.repositories;

import com.compomics.secretesite.domain.TranscriptStructure;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by davy on 4/21/2017.
 */

@RepositoryRestResource(collectionResourceRel = "transcriptstructures",path = "transcriptstructures")
public interface TranscriptStructureRepository extends PagingAndSortingRepository<TranscriptStructure,Integer> {
}
