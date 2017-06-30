package com.compomics.secretesite.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 * Created by davy on 5/10/2017.
 */
@Entity
@Data
@EqualsAndHashCode(exclude = {"domain","protein"})
@ToString(exclude = {"domain","protein"})
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

    private Integer domainStart;

    private Integer domainEnd;

}
