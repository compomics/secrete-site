package com.compomics.secretesite.controllers.services;

import com.compomics.secretesite.domain.Domain;
import com.compomics.secretesite.domain.ProteinDomain;
import com.compomics.secretesite.domain.repositories.DomainRepository;
import com.compomics.secretesite.domain.repositories.ProteinDomainRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by demet on 7/31/2017.
 */
@Service
public class DomainService {
    private final DomainRepository domainRepository;
    private final ProteinDomainRepository proteinDomainRepository;

    public DomainService(DomainRepository domainRepository, ProteinDomainRepository proteinDomainRepository) {
        this.domainRepository = domainRepository;
        this.proteinDomainRepository = proteinDomainRepository;
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

    public List<ProteinDomain> getProteinDomainsbyProteinStableId(String proteinStableId){
        return proteinDomainRepository.findByProteinStableId(proteinStableId);
    }

    public Set<Domain> getByDomainName(String name){
        Set<Domain> domains = new HashSet<>();
        domains.addAll(domainRepository.findByDomainNameContaining(name));
        return domains;
    }
}
