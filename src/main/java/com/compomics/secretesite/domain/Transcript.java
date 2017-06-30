package com.compomics.secretesite.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * The model class for a transcript.
 * <p>
 * This class represents the transcripts that have been experimentally determined to be expressed.
 * Created by davy on 4/10/2017.
 */
@Entity
@Data
@EqualsAndHashCode(exclude = {"parentGene","foundIn","transcriptsExpressableInSpecies","transcriptProteins"})
@ToString(exclude = {"parentGene","foundIn","transcriptsExpressableInSpecies","transcriptProteins"})
@NoArgsConstructor
public class Transcript {

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
    @ManyToOne
    @JoinColumn(name = "gene_id", nullable = false)
    private Gene parentGene;

    @JsonManagedReference
    @OneToMany(mappedBy = "transcript",cascade = CascadeType.ALL)
    private Set<TranscriptsExpressableInSpecies> transcriptsExpressableInSpecies = new HashSet<>();

    @OneToMany(mappedBy = "transcript",cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<TranscriptsFoundInStructure> foundIn = new HashSet<>();

    private String secretionStatus;

    @OneToMany(mappedBy = "parentTranscript",cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<TranscriptProtein> transcriptProteins = new HashSet<>();

    public Transcript(String ensembleTranscriptAccession, String transcript_sequence, Integer sequence_start,Integer sequence_end, Gene parentGene) {
        this.ensembleTranscriptAccession = ensembleTranscriptAccession;
        this.sequenceStart = sequence_start;
        this.sequenceEnd = sequence_end;
        this.transcriptSequence = transcript_sequence;
        this.parentGene = parentGene;
    }

}

