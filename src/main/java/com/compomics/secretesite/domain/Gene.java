package com.compomics.secretesite.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by davy on 4/10/2017.
 */
@Entity
@Data
@EqualsAndHashCode(exclude = "transcripts")
@ToString(exclude = "transcripts")
public class Gene {

    /**
     * The ensembl gene accession
     */
    @NaturalId
    private String geneAccession;

    /**
     * The database specific identifier
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer geneid;

    /**
     * The chromosome the gene is located on
     */
    private String chromosome;

    /**
     * Human readable gene name
     */
    private String geneName;

    /**
     * Full genetic canonical sequence
     */
    private String geneSequence;

    /**
     * {@link Transcript}s this gene encodes for
     */
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "parentGene")
    private Set<Transcript> transcripts = new HashSet<>(0);


    public Gene(String geneAccession, String chromosome, String geneName, String geneSequence) {
        this.geneAccession = geneAccession;
        this.chromosome = chromosome;
        this.geneName = geneName;
        this.geneSequence = geneSequence;
    }
}
