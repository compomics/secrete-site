package com.compomics.secretesite.domain;

import lombok.*;

import javax.persistence.*;

/**
 * Created by davy on 4/21/2017.
 */
@Entity
@Data
@EqualsAndHashCode(exclude = {"transcript","transcriptstructure"})
@ToString(exclude = {"transcript","transcriptstructure"})
public class TranscriptsFoundInStructure {

    @Id
    @GeneratedValue
    public Integer transcripts_found_in_structure_id;

    @ManyToOne(targetEntity = Transcript.class)
    @JoinColumn(name = "l_transcript_id",referencedColumnName = "transcript_id")
    public Transcript transcript;

    @ManyToOne(targetEntity = TranscriptStructure.class,cascade = CascadeType.ALL)
    @JoinColumn(name = "l_transcript_structure_id",referencedColumnName = "transcript_structure_id")
    public TranscriptStructure transcriptstructure;
}