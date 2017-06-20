package com.compomics.secretesite.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

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
@Data
@Entity
@EqualsAndHashCode(exclude = {"parentGene","foundIn","transcriptsExpressableInSpecies","earlyFoldingLocations","transcriptProteins"})
@ToString(exclude = {"parentGene","foundIn","transcriptsExpressableInSpecies","earlyFoldingLocations","transcriptProteins"})
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@transcriptId")
public class Transcript implements Serializable {

    /**
     * internal database id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transcript_id")
    private Integer transcriptId;

    /**
     * the ensembl identifier for the transcript
     */
    private String ensembleTranscriptAccession;

    private Integer sequenceStart;

    private Integer sequenceEnd;

    /**
     * cDNA sequence of the experimental transcript
     */
    @Column(length = 12200)
    private String transcriptSequence;

    /**
     * the gene this transcript has been mapped back to
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gene_id", nullable = false)
    private Gene parentGene;

    @OneToMany(mappedBy = "transcript",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TranscriptsExpressableInSpecies> transcriptsExpressableInSpecies = new HashSet<>();

    @OneToMany(mappedBy = "transcript",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TranscriptsFoundInStructure> foundIn = new HashSet<>();



    private String secretionStatus;

    @OneToMany(mappedBy = "parentTranscript",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<TranscriptProtein> transcriptProteins = new HashSet<>();



    public Transcript(String ensembleTranscriptAccession, String transcript_sequence, Integer sequence_start,Integer sequence_end, Gene parentGene) {
        this.ensembleTranscriptAccession = ensembleTranscriptAccession;
        this.sequenceStart = sequence_start;
        this.sequenceEnd = sequence_end;
        this.transcriptSequence = transcript_sequence;
        this.parentGene = parentGene;
    }

    public Transcript() {
    }
}

