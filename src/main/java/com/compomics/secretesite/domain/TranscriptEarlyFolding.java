package com.compomics.secretesite.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 * Class representing the early folding predictions for transcripts
 * Created by davy on 5/4/2017.
 */

//TODO check if this table layout is performant or if the way folding locations are stored need to be revised

@Entity
@Data
@EqualsAndHashCode(exclude = {"transcript"})
@ToString(exclude = "transcript")
public class TranscriptEarlyFolding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transcriptEarlyFoldingId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "transcript_id", nullable = false)
    @JsonBackReference
    private Transcript transcript;

    /**
     * location in the transcript where early folding occurs
     */
    private int foldingLocation;

}
