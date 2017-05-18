package com.compomics.secretesite.domain.repositories;

import com.compomics.secretesite.domain.Protein;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by davy on 5/16/2017.
 */
public interface ProteinRepository extends PagingAndSortingRepository<Protein,Integer> {
}
