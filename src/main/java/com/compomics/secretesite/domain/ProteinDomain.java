package com.compomics.secretesite.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property ="@proteinDomainId" )
public class ProteinDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer proteinDomainId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "l_domain_id",referencedColumnName = "domainId")
    private Domain domain;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "l_protein_id",referencedColumnName = "proteinId")
    private Protein protein;

    private Integer domainStart;

    private Integer domainEnd;

    public ProteinDomain() {
    }
}
