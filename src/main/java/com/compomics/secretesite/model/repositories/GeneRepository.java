package com.compomics.secretesite.model.repositories;

import com.compomics.secretesite.model.Gene;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Created by davy on 4/11/2017.
 */
public interface GeneRepository extends PagingAndSortingRepository<Gene,Integer> {

    @RestResource(path = "filterbyid")
    Gene findByGeneId(@RequestParam  Integer id);

    @RestResource(path = "filterbyaccession")
    Gene findByGeneAccession(@RequestParam String accession);

}
