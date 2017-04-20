package com.compomics.secretesite.model.repositories;

import com.compomics.secretesite.model.Species;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by davy on 4/11/2017.
 */
@RepositoryRestResource
public interface SpeciesRepository extends PagingAndSortingRepository<Species, Integer> {
}
