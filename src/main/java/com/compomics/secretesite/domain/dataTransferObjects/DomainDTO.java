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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DomainDTO domainDTO = (DomainDTO) o;

        if (domainStart != domainDTO.domainStart) return false;
        if (domainEnd != domainDTO.domainEnd) return false;
        if (domainAccession != null ? !domainAccession.equals(domainDTO.domainAccession) : domainDTO.domainAccession != null)
            return false;
        return domainName != null ? domainName.equals(domainDTO.domainName) : domainDTO.domainName == null;
    }

    @Override
    public int hashCode() {
        int result = domainAccession != null ? domainAccession.hashCode() : 0;
        result = 31 * result + (domainName != null ? domainName.hashCode() : 0);
        result = 31 * result + domainStart;
        result = 31 * result + domainEnd;
        return result;
    }
}
