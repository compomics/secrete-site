package com.compomics.secretesite.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer protein_id;

    @NaturalId
    private String proteinAccession;

    @OneToMany(mappedBy = "protein",cascade = CascadeType.ALL)
    private Set<ProteinDomain> domainsContainedInProtein = new HashSet<>();


    @OneToMany(mappedBy = "proteinProduct")
    @Column(name = "translation_products")
    private Set<TranscriptProtein> parent_transcripts = new HashSet<>();

}
