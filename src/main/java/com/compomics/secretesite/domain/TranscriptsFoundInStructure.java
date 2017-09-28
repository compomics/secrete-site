package com.compomics.secretesite.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

/**
 * Two way join table between the structure mapping and the dataset transcripts
 * Created by davy on 4/21/2017.
 */
@Entity
@Data
@EqualsAndHashCode(exclude = {"transcriptstructure"})
@ToString(exclude = {"transcript","transcriptstructure"})
public class TranscriptsFoundInStructure {

    @Id
    @GeneratedValue
    public Integer transcriptsFoundInStructureId;

    @ManyToOne(targetEntity = Transcript.class)
    @JoinColumn(name = "l_transcript_id",referencedColumnName = "transcript_id")
    @JsonBackReference
    public Transcript transcript;

    @ManyToOne(targetEntity = TranscriptStructure.class,cascade = CascadeType.ALL)
    @JoinColumn(name = "l_transcript_structure_id",referencedColumnName = "transcript_structure_id")
    @JsonBackReference
    public TranscriptStructure transcriptstructure;

}