package com.compomics.secretesite.controllers.services;

import com.compomics.secretesite.domain.Protein;
import com.compomics.secretesite.domain.repositories.ProteinRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public Set<Protein> getProteinByAccession(String accession){
        Set<Protein> proteins = new HashSet<>();
        proteins.addAll(proteinRepository.findBySwissProtAccession(accession));
        if(proteins.isEmpty()){
            proteins.addAll(proteinRepository.findByTrEmblAccession(accession));
        }
        return proteins;
    }

    public Set<Protein> getProteinByEnsemblAccession(String accession){
        Set<Protein> proteins = new HashSet<>();
        proteins.addAll(proteinRepository.findByProteinEnsemblAccession(accession));
        return proteins;
    }

    public Set<Protein> getProteinByName(String name){
        Set<Protein> proteins = new HashSet<>();
        proteins.addAll(proteinRepository.findBySwissProtNameContaining(name));
        if(proteins.isEmpty()){
            proteins.addAll(proteinRepository.findByTrEmblNameContaining(name));
        }
        return proteins;
    }
}
