package com.compomics.secretesite.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by davy on 5/10/2017.
 */
@Entity
@Data
public class ProteinDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer protein_domain_id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "l_domain_id",referencedColumnName = "domain_id")
    private Domain domain;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "l_protein_id",referencedColumnName = "protein_id")
    private Protein protein;

    private Integer domainStart;

    private Integer domainEnd;

}
