package com.compomics.secretesite.controllers.services;

import com.compomics.secretesite.domain.Protein;
import com.compomics.secretesite.domain.repositories.ProteinRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by demet on 7/4/2017.
 */
@Service
public class ProteinService {

    private final ProteinRepository proteinRepository;

    public ProteinService(ProteinRepository proteinRepository) {
        this.proteinRepository = proteinRepository;
    }

    public List<Protein> getAllProteins(){
        List<Protein> proteins = new ArrayList<>();
        proteinRepository.findAll().forEach(proteins::add);
        return proteins;
    }

    public Protein getProteinById(Integer id){
        Protein protein = proteinRepository.findByProteinId(id);
        return protein;
    }

    public Protein getProteinByAccession(String accession){
        Protein protein = proteinRepository.findByProteinAccession(accession);
        return protein;
    }
}
