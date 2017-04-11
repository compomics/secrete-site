package com.compomics.secretesite.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;

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
    private Integer transcriptId;

    private String ensembleTranscriptId;


    /**
     * cDNA sequence of the experimental transcript
     */
    private String transcript_sequence;

    /**
     * the gene this transcript has been mapped back to
     */
    private Gene parentGene;

    /**
     * if the transcript is secretable
     */
    private Boolean secretable;

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
     * @param transcriptId database id of the transcript
     * @param ensembleTranscriptId the ensembl transcript ID
     * @param transcript_sequence the sequence of the transcript
     * @param parentGene the {@link Gene} this transcript is derived from
     */
    public Transcript(Integer transcriptId, String ensembleTranscriptId, String transcript_sequence, Gene parentGene) {
        this.transcriptId = transcriptId;
        this.ensembleTranscriptId = ensembleTranscriptId;
        this.transcript_sequence = transcript_sequence;
        this.parentGene = parentGene;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gene_id", nullable = false)
    public Gene getParentGene() {
        return parentGene;
    }

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getTranscriptId() {
        return transcriptId;
    }

    @Column
    public String getTranscript_sequence() {
        return transcript_sequence;
    }

    @Column
    @NaturalId
    public String getEnsembleTranscriptId() {
        return ensembleTranscriptId;
    }

    public void setEnsembleTranscriptId(String ensembleTranscriptId) {
        this.ensembleTranscriptId = ensembleTranscriptId;
    }

    public void setTranscriptId(Integer transcriptId) {
        this.transcriptId = transcriptId;
    }

    public void setTranscript_sequence(String transcript_sequence) {
        this.transcript_sequence = transcript_sequence;
    }

    public void setParentGene(Gene parentGene) {
        this.parentGene = parentGene;
    }

    @Column
    public Boolean getSecretable() {
        return secretable;
    }

    public void setSecretable(Boolean secretable) {
        this.secretable = secretable;
    }
}

