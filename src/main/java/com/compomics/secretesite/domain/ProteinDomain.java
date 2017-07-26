package com.compomics.secretesite.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 * join table between known domains and domains present in a protein
 * Created by davy on 5/10/2017.
 */
@Entity
@Data
public class ProteinDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer proteinDomainId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "l_domain_id",referencedColumnName = "domain_id")
    @JsonBackReference
    private Domain domain;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    @JoinColumn(name = "l_protein_id",referencedColumnName = "protein_id")
    private Protein protein;

    /**
     * the start location of the domain on the primary protein sequence
     */
    private Integer domainStart;

    /**
     * the stop location of the domain on the primary protein sequence
     */
    private Integer domainEnd;

}
