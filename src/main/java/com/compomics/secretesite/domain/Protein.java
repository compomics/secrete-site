package com.compomics.secretesite.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by davy on 5/10/2017.
 */

@Data
@Entity
@EqualsAndHashCode(exclude = {"domainsContainedInProtein","parent_transcripts"})
@ToString(exclude = {"domainsContainedInProtein","parent_transcripts"})
public class Protein {


    @Id
    private Integer protein_id;

    @NaturalId
    private String proteinAccession;

    @Column(length = 2000)
    private String proteinSequence;

    @OneToMany(mappedBy = "protein")
    private Set<ProteinDomain> domainsContainedInProtein;


    @ManyToMany(mappedBy = "proteinProducts")
    @Column(name = "translation_products")
    private Set<Transcript> parent_transcripts;

}
