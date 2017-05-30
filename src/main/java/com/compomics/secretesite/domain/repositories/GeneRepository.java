package com.compomics.secretesite.domain.repositories;

import com.compomics.secretesite.domain.Gene;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Created by davy on 4/11/2017.
 */
public interface GeneRepository extends PagingAndSortingRepository<Gene,Integer> {

    @RestResource(path = "filterbyid")
    Gene findBygeneId(@RequestParam  Integer id);

    @RestResource(path = "filterbyaccession")
    Gene findByGeneAccession(@RequestParam String accession);

    @RestResource(path = "filterbyname")
    Gene findByGeneName(@RequestParam String geneName);


}
