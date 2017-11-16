package com.compomics.secretesite.domain.repositories;

import com.compomics.secretesite.domain.ProteinDomain;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by demet on 10/27/2017.
 */
public interface ProteinDomainRepository extends PagingAndSortingRepository<ProteinDomain,Integer> {

    @RestResource(path = "filterbyProteinStableId")
    List<ProteinDomain> findByProteinStableId(@RequestParam String proteinStableId);

}
