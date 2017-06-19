package com.compomics.secretesite.domain.repositories;

import com.compomics.secretesite.domain.Protein;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by davy on 5/16/2017.
 */
public interface ProteinRepository extends PagingAndSortingRepository<Protein,Integer> {

    @RestResource(path = "filterbyid")
    Protein findByProteinId(@RequestParam  Integer id);

    @RestResource(path = "filterbyaccession")
    Protein findByProteinAccession(@RequestParam String accession);

}
