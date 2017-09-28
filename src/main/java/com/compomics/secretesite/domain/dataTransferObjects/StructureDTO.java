package com.compomics.secretesite.domain.dataTransferObjects;

/**
 * Created by demet on 8/11/2017.
 */
public class StructureDTO {

    private String pdbId;

    private String chain;

    private Integer fragmentStart;

    private Integer fragmentEnd;

    private String transcriptStatus;

    public StructureDTO(String pdbId, String chain, Integer fragmentStart, Integer fragmentEnd, String transcriptStatus) {
        this.pdbId = pdbId;
        this.chain = chain;
        this.fragmentStart = fragmentStart;
        this.fragmentEnd = fragmentEnd;
        this.transcriptStatus = transcriptStatus;
    }

    public String getPdbId() {
        return pdbId;
    }

    public void setPdbId(String pdbId) {
        this.pdbId = pdbId;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public Integer getFragmentStart() {
        return fragmentStart;
    }

    public void setFragmentStart(Integer fragmentStart) {
        this.fragmentStart = fragmentStart;
    }

    public Integer getFragmentEnd() {
        return fragmentEnd;
    }

    public void setFragmentEnd(Integer fragmentEnd) {
        this.fragmentEnd = fragmentEnd;
    }

    public String getTranscriptStatus() {
        return transcriptStatus;
    }

    public void setTranscriptStatus(String transcriptStatus) {
        this.transcriptStatus = transcriptStatus;
    }
}
