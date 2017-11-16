package com.compomics.secretesite.domain.dataTransferObjects;

import com.compomics.secretesite.domain.Domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by demet on 7/5/2017.
 */
public class ProteinDTO {

    private String proteinAccession;

    private String proteinName;

    private String proteinLabel;

    private List<DomainDTO> domainDTOs = new ArrayList<>();

    private List<FragmentDTO> mainFragmentDTOs = new ArrayList<>();


    public ProteinDTO() {
    }

    public ProteinDTO(String proteinAccession, String proteinName) {
        this.proteinAccession = proteinAccession;
        this.proteinName = proteinName;
    }

    public String getProteinAccession() {
        return proteinAccession;
    }

    public void setProteinAccession(String proteinAccession) {
        this.proteinAccession = proteinAccession;
    }

    public String getProteinName() {
        return proteinName;
    }

    public void setProteinName(String proteinName) {
        this.proteinName = proteinName;
    }

    public String getProteinLabel() {
        return proteinLabel;
    }

    public void setProteinLabel(String proteinLabel) {
        this.proteinLabel = proteinLabel;
    }

    public List<DomainDTO> getDomainDTOs() {
        return domainDTOs;
    }

    public void setDomainDTOs(List<DomainDTO> domainDTOs) {
        this.domainDTOs = domainDTOs;
    }

    public List<FragmentDTO> getMainFragmentDTOs() {
        return mainFragmentDTOs;
    }

    public void setMainFragmentDTOs(List<FragmentDTO> mainFragmentDTOs) {
        this.mainFragmentDTOs = mainFragmentDTOs;
    }
}
