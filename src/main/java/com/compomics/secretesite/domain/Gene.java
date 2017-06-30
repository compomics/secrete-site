package com.compomics.secretesite.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

/**
 * The expressable genes found in the experiments
 * Created by davy on 4/10/2017.
 */
@Entity
@Data
@NoArgsConstructor
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
    private Integer geneId;

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



    public Gene(String geneAccession, String chromosome, String geneName, String geneSequence) {
        this.geneAccession = geneAccession;
        this.chromosome = chromosome;
        this.geneName = geneName;
        this.geneSequence = geneSequence;
    }

}
