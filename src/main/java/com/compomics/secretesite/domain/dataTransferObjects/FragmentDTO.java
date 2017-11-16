package com.compomics.secretesite.domain.dataTransferObjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by demet on 7/27/2017.
 */
public class FragmentDTO {

    private Integer fragmentId;

    private String fragmentAccession;

    private String secretionStatus;

    private int fragmentStart;

    private int fragmentEnd;

    private String pdbIds;

    private String structureIds;

    private String species;

    private int representative;

    private List<FragmentDTO> childFragmentDTOs = new ArrayList<>();

    public FragmentDTO() {
    }

    public FragmentDTO(Integer fragmentId, String fragmentAccession, int fragmentStart, int fragmentEnd, String pdbIds,
                       String structureIds, String secretionStatus, String species, int representative) {
        this.fragmentId = fragmentId;
        this.fragmentAccession = fragmentAccession;
        this.fragmentStart = fragmentStart;
        this.fragmentEnd = fragmentEnd;
        this.pdbIds = pdbIds;
        this.structureIds = structureIds;
        this.secretionStatus = secretionStatus;
        this.species = species;
        this.representative = representative;
    }

    public String getFragmentAccession() {
        return fragmentAccession;
    }

    public void setFragmentAccession(String fragmentAccession) {
        this.fragmentAccession = fragmentAccession;
    }

    public String getSecretionStatus() {
        return secretionStatus;
    }

    public void setSecretionStatus(String secretionStatus) {
        this.secretionStatus = secretionStatus;
    }

    public int getFragmentStart() {
        return fragmentStart;
    }

    public void setFragmentStart(int fragmentStart) {
        this.fragmentStart = fragmentStart;
    }

    public int getFragmentEnd() {
        return fragmentEnd;
    }

    public void setFragmentEnd(int fragmentEnd) {
        this.fragmentEnd = fragmentEnd;
    }

    public String getPdbIds() {
        return pdbIds;
    }

    public void setPdbIds(String pdbIds) {
        this.pdbIds = pdbIds;
    }

    public String getStructureIds() {
        return structureIds;
    }

    public void setStructureIds(String structureIds) {
        this.structureIds = structureIds;
    }

    public Integer getFragmentId() {
        return fragmentId;
    }

    public void setFragmentId(Integer fragmentId) {
        this.fragmentId = fragmentId;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public int getRepresentative() {
        return representative;
    }

    public void setRepresentative(int representative) {
        this.representative = representative;
    }

    public List<FragmentDTO> getChildFragmentDTOs() {
        return childFragmentDTOs;
    }

    public void setChildFragmentDTOs(List<FragmentDTO> childFragmentDTOs) {
        this.childFragmentDTOs = childFragmentDTOs;
    }
}
