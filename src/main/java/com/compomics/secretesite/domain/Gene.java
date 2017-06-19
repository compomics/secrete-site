package com.compomics.secretesite.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@geneId")
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

    public Gene() {
    }
}
