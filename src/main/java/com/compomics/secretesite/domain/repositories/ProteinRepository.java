package com.compomics.secretesite.domain.repositories;

import com.compomics.secretesite.domain.Protein;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by davy on 5/16/2017.
 */
public interface ProteinRepository extends PagingAndSortingRepository<Protein,Integer> {

    @RestResource(path = "filterbyid")
    Protein findByProteinId(@RequestParam  Integer id);

    @RestResource(path = "filterbySwissProtAccession")
    List<Protein> findBySwissProtAccession(@RequestParam String accession);

    @RestResource(path = "filterbyTrEmblAccession")
    List<Protein> findByTrEmblAccession(@RequestParam String accession);

    @RestResource(path = "filterbyProteinEnsemblAccession")
    List<Protein> findByProteinEnsemblAccession(@RequestParam String accession);

    List<Protein> findBySwissProtNameContaining(@RequestParam String name);

    List<Protein> findByTrEmblNameContaining(@RequestParam String name);
}
