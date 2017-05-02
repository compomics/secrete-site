package com.compomics.secretesite.domain;

import com.compomics.secretesite.controllers.services.SequenceService;
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
@EqualsAndHashCode(exclude = {"parentGene","foundIn","expressableIn"})
public class Transcript implements Serializable {

    /**
     * internal database id
     */
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transcript_id;

    /**
     * the ensembl identifier for the transcript
     */
    @Column
    private String ensembleTranscriptAccession;

    @Column
    private Integer sequence_start;

    @Column
    private Integer sequence_end;

    /**
     * cDNA sequence of the experimental transcript
     */
    @Column(length = 12200)
    private String transcript_sequence;

    /**
     * the gene this transcript has been mapped back to
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gene_id", nullable = false)
    private Gene parentGene;

    @ManyToMany
    @JoinTable(
            name = "transcripts_expressable_in_species",
            joinColumns = @JoinColumn(name = "transcript_id"),
            inverseJoinColumns = @JoinColumn(name = "species_id")
    )
    private Set<Species> expressableIn = new HashSet<>(0);

    @OneToMany(mappedBy = "transcript",cascade = CascadeType.ALL)
    private Set<TranscriptsFoundInStructure> foundIn = new HashSet<>();

    @Transient
    private static SequenceService sequenceService;

    /**
     * private String proteinProduct
     */
     @Transient
     public String getProteinProduct(){
          return sequenceService.translateDNAtoProtein(this.transcript_sequence);
      }

    public Transcript() {
    }

    public Transcript(String ensembleTranscriptAccession, String transcript_sequence, Integer sequence_end, Integer sequence_start, Gene parentGene) {
        this.ensembleTranscriptAccession = ensembleTranscriptAccession;
        this.sequence_start = sequence_start;
        this.sequence_end = sequence_end;
        this.transcript_sequence = transcript_sequence;
        this.parentGene = parentGene;
    }
}

