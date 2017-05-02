package com.compomics.secretesite.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
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
public class Gene {

    /**
     * The ensembl gene accession
     */
    @Column
    @NaturalId
    private String geneAccession;

    /**
     * The database specific identifier
     */
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer geneId;

    /**
     * The chromosome the gene is located on
     */
    @Column
    private String chromosome;

    /**
     * Human readable gene name
     */
    @Column
    private String geneName;

    /**
     * Full genetic canonical sequence
     */
    @Column
    private String geneSequence;

    /**
     * {@link Transcript}s this gene encodes for
     */
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "parentGene")
    private Set<Transcript> transcripts = new HashSet<>(0);

    public Gene() {
    }

    public Gene(String geneAccession, String chromosome, String geneName, String geneSequence) {
        this.geneAccession = geneAccession;
        this.chromosome = chromosome;
        this.geneName = geneName;
        this.geneSequence = geneSequence;
    }
}
