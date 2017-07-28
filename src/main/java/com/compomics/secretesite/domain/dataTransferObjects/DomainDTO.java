package com.compomics.secretesite.domain.dataTransferObjects;

/**
 * Created by demet on 7/27/2017.
 */
public class DomainDTO {

    private String domainAccession;

    private String domainName;

    private int domainStart;

    private int domainEnd;

    public DomainDTO(String domainAccession, String domainName, int domainStart, int domainEnd) {
        this.domainAccession = domainAccession;
        this.domainName = domainName;
        this.domainStart = domainStart;
        this.domainEnd = domainEnd;
    }

    public String getDomainAccession() {
        return domainAccession;
    }

    public void setDomainAccession(String domainAccession) {
        this.domainAccession = domainAccession;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public int getDomainStart() {
        return domainStart;
    }

    public void setDomainStart(int domainStart) {
        this.domainStart = domainStart;
    }

    public int getDomainEnd() {
        return domainEnd;
    }

    public void setDomainEnd(int domainEnd) {
        this.domainEnd = domainEnd;
    }
}
