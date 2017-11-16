package com.compomics.secretesite.domain.repositories;

import com.compomics.secretesite.domain.TranscriptProtein;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by demet on 10/27/2017.
 */
public interface TranscriptProteinRepository extends PagingAndSortingRepository<TranscriptProtein,Integer> {

    List<TranscriptProtein> findByProteinStableId(String proteinStableId);

}
