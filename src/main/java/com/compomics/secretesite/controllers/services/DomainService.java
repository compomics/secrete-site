package com.compomics.secretesite.controllers.services;

import com.compomics.secretesite.domain.Domain;
import com.compomics.secretesite.domain.repositories.DomainRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by demet on 7/31/2017.
 */
@Service
public class DomainService {
    private final DomainRepository domainRepository;

    public DomainService(DomainRepository domainRepository) {
        this.domainRepository = domainRepository;
    }

    public List<Domain> getAllDomains(){
        List<Domain> domains = new ArrayList<>();
        domainRepository.findAll().forEach(domains::add);
        return domains;
    }

    public Domain getDomainById(Integer id){
        return domainRepository.findByDomainId(id);
    }

    public Domain getDomainByAccession(String accession){
        return domainRepository.findByDomainAccession(accession);
    }
}
