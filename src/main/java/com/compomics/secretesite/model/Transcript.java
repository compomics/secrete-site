package com.compomics.secretesite.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * The model class for a transcript.
 * <p>
 * This class represents the transcripts that have been experimentally determined to be expressed.
 * Created by davy on 4/10/2017.
 */

@Entity
public class Transcript implements Serializable {

    /**
     * internal database id
     */
    private Integer transcript_id;

    /**
     * the ensembl identifier for the transcript
     */
    private String ensembleTranscriptId;

    private Integer sequence_start;

    private Integer sequence_end;

    /**
     * cDNA sequence of the experimental transcript
     */
    private String transcript_sequence;

    /**
     * the gene this transcript has been mapped back to
     */
    private Gene parentGene;

    private Set<Species> expressableIn = new HashSet<>(0);

    //TODO either write on the fly DNA to protein translator or just store the sequences.

    /**
     * private String proteinProduct
     * @Transient or @Column
     * public String getProteinProduct(){
     *     return this.proteinProdutc;
     * }
     */

    public Transcript() {
    }

    /**
     * natural constructor for a transcript
     *
     * @param ensembleTranscriptId the ensembl transcript ID
     * @param parentGene           the (@link {@link Gene}) this transcript is derived from
     */
    public Transcript(String ensembleTranscriptId, Gene parentGene) {
        this.ensembleTranscriptId = ensembleTranscriptId;
        this.parentGene = parentGene;
    }

    /**
     * full constructor for a transcript
     * @param ensembleTranscriptId the ensembl transcript ID
     * @param transcript_sequence the sequence of the transcript
     * @param parentGene the {@link Gene} this transcript is derived from
     */
    public Transcript(String ensembleTranscriptId, String transcript_sequence,Integer sequence_start,Integer sequence_end, Gene parentGene) {
        this.ensembleTranscriptId = ensembleTranscriptId;
        this.transcript_sequence = transcript_sequence;
        this.sequence_start = sequence_start;
        this.sequence_end = sequence_end;
        this.parentGene = parentGene;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gene_id", nullable = false)
    public Gene getParentGene() {
        return parentGene;
    }

    public void setParentGene(Gene parentGene){
        this.parentGene = parentGene;
    }

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getTranscript_id() {
        return transcript_id;
    }

    @Column(length = 12200)
    public String getTranscript_sequence() {
        return transcript_sequence;
    }

    @Column
    public String getEnsembleTranscriptId() {
        return ensembleTranscriptId;
    }

    public void setEnsembleTranscriptId(String ensembleTranscriptId) {
        this.ensembleTranscriptId = ensembleTranscriptId;
    }

    @Column
    public Integer getSequence_start() {
        return sequence_start;
    }

    public void setSequence_start(Integer sequence_start) {
        this.sequence_start = sequence_start;
    }

    @Column
    public Integer getSequence_end() {
        return sequence_end;
    }

    public void setSequence_end(Integer sequence_end) {
        this.sequence_end = sequence_end;
    }

    public void setTranscript_id(Integer transcript_id) {
        this.transcript_id = transcript_id;
    }

    public void setTranscript_sequence(String transcript_sequence) {
        this.transcript_sequence = transcript_sequence;
    }

    @ManyToMany(mappedBy = "expressableTranscripts")
    public Set<Species> getExpressableInSpecies() {
        return expressableIn;
    }

    public void setExpressableInSpecies(Set<Species> expressableSpecies){
        expressableIn.addAll(expressableSpecies);
    }
}

