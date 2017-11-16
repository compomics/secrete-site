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
@EqualsAndHashCode(exclude = {"parentGene","foundIn","transcriptsExpressableInSpecies","transcriptProteins","transcriptCluster"})
@ToString(exclude = {"parentGene","foundIn","transcriptsExpressableInSpecies","transcriptProteins","transcriptCluster"})
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

    /**
     * the species this transcript can be expressed in
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "transcript",cascade = CascadeType.ALL)
    private Set<TranscriptsExpressableInSpecies> transcriptsExpressableInSpecies = new HashSet<>();

    /**
     * the pdb structures this transcript is found in
     */
    @OneToMany(mappedBy = "transcript",cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<TranscriptsFoundInStructure> foundIn = new HashSet<>();

    /**
     * the classification of the secretion
     */
    private String secretionStatus;

    /**
     * the proteins that contain this transcript
     */
    @OneToMany(mappedBy = "parentTranscript",cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<TranscriptProtein> transcriptProteins = new HashSet<>();

    @OneToMany(mappedBy = "transcriptClusterMember",cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<TranscriptCluster> transcriptCluster = new HashSet<>();


    public Transcript(String ensembleTranscriptAccession, String transcript_sequence, Gene parentGene) {
        this.ensembleTranscriptAccession = ensembleTranscriptAccession;
        this.transcriptSequence = transcript_sequence;
        this.parentGene = parentGene;
    }

}

