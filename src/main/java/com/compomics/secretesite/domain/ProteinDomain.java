package com.compomics.secretesite.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by davy on 5/10/2017.
 */
@Entity
@Data
public class ProteinDomain {

    @Id
    private Integer protein_domain_id;

    @ManyToOne
    @JoinColumn(name = "l_domain_id",referencedColumnName = "domain_id")
    private Domain domain;

    @ManyToOne
    @JoinColumn(name = "l_protein_id",referencedColumnName = "protein_id")
    private Protein protein;

    private Integer domainStart;

    private Integer domainEnd;

}
