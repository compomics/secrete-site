package com.compomics.secretesite.domain.dataTransferObjects;

/**
 * Created by demet on 7/5/2017.
 */
public class ProteinDTO {

    private String proteinAccession;

    private Integer domainStart;

    private Integer domainEnd;

    private String domainAccession;

    public ProteinDTO(String proteinAccession, Integer domainStart, Integer domainEnd, String domainAccession) {
        this.proteinAccession = proteinAccession;
        this.domainStart = domainStart;
        this.domainEnd = domainEnd;
        this.domainAccession = domainAccession;
    }

    public String getProteinAccession() {
        return proteinAccession;
    }

    public void setProteinAccession(String proteinAccession) {
        this.proteinAccession = proteinAccession;
    }

    public Integer getDomainStart() {
        return domainStart;
    }

    public void setDomainStart(Integer domainStart) {
        this.domainStart = domainStart;
    }

    public Integer getDomainEnd() {
        return domainEnd;
    }

    public void setDomainEnd(Integer domainEnd) {
        this.domainEnd = domainEnd;
    }

    public String getDomainAccession() {
        return domainAccession;
    }

    public void setDomainAccession(String domainAccession) {
        this.domainAccession = domainAccession;
    }

}
