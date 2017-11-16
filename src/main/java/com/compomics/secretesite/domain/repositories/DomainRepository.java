package com.compomics.secretesite.domain.repositories;

import com.compomics.secretesite.domain.Domain;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by demet on 7/4/2017.
 */
public interface DomainRepository extends PagingAndSortingRepository<Domain,Integer> {

    @RestResource(path = "filterbyid")
    Domain findByDomainId(@RequestParam Integer id);

    @RestResource(path = "filterbyaccession")
    Domain findByDomainAccession(@RequestParam String accession);

    List<Domain> findByDomainNameContaining(@RequestParam String name);

}
