package com.compomics.secretesite.domain.repositories;

import com.compomics.secretesite.domain.Transcript;
import com.compomics.secretesite.domain.TranscriptsFoundInStructure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by davy on 4/21/2017.
 */
@RepositoryRestResource(collectionResourceRel = "foundIn",path = "foundIn")
public interface TranscriptsFoundInStructureRepository extends CrudRepository<TranscriptsFoundInStructure,Integer>{

    List<TranscriptsFoundInStructure> findByTranscript(@RequestParam Integer transcriptId);
}
