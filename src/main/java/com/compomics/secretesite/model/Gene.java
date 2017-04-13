package com.compomics.secretesite.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by davy on 4/10/2017.
 */
@Entity
public class Gene {

    /**
     * The ensembl gene accession
     */
    private String geneAccession;

    /**
     * The database specific identifier
     */
    private Integer gene_id;

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
     * {@link Species} this gene belongs to
     */
    private Species speciesForGene;

    /**
     * {@link Transcript}s this gene encodes for
     */
    private Set<Transcript> transcripts = new HashSet<>(0);

    public Gene(){}

    public Gene(String geneName,Species speciesForGene){
        this.geneName = geneName;
        this.speciesForGene = speciesForGene;
    }

    public Gene(String geneAccession, String chromosome, String geneName, String geneSequence, Species speciesForGene) {
        this.geneAccession = geneAccession;
        this.chromosome = chromosome;
        this.geneName = geneName;
        this.geneSequence = geneSequence;
        this.speciesForGene = speciesForGene;
    }

    @Column
    @NaturalId
    public String getGeneAccession() {
        return geneAccession;
    }

    public void setGeneAccession(String geneAccession) {
        this.geneAccession = geneAccession;
    }

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getGene_id() {
        return gene_id;
    }

    public void setGene_id(Integer gene_id) {
        this.gene_id = gene_id;
    }

    @Column
    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    @Column
    public String getGeneName() {
        return geneName;
    }

    public void setGeneName(String geneName) {
        this.geneName = geneName;
    }

    @Column
    public String getGeneSequence() {
        return geneSequence;
    }

    public void setGeneSequence(String geneSequence) {
        this.geneSequence = geneSequence;
    }


    @OneToMany(cascade = CascadeType.ALL,mappedBy = "parentGene")
    public Set<Transcript> getTranscripts() {
        return transcripts;
    }

    public void setTranscripts(Set<Transcript> transcripts) {
        this.transcripts = transcripts;
    }
}
