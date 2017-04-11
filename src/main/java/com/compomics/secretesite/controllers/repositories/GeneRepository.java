package com.compomics.secretesite.controllers.repositories;

import com.compomics.secretesite.model.Gene;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


/**
 * Created by davy on 4/11/2017.
 */
@RepositoryRestResource(collectionResourceRel = "genes", path = "genes")
public interface GeneRepository extends PagingAndSortingRepository<Gene,Long> {

    Gene findByGeneAccession(String geneAccession);

}
