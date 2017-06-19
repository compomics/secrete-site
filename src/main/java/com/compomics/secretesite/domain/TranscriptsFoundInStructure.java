package com.compomics.secretesite.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;

/**
 * Created by davy on 4/21/2017.
 */
@Entity
@Data
@EqualsAndHashCode(exclude = {"transcript","transcriptstructure"})
@ToString(exclude = {"transcript","transcriptstructure"})
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@transcriptsFoundInStructureId")
public class TranscriptsFoundInStructure {

    @Id
    @GeneratedValue
    public Integer transcriptsFoundInStructureId;

    @ManyToOne(targetEntity = Transcript.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "l_transcript_id",referencedColumnName = "transcript_id")
    public Transcript transcript;

    @ManyToOne(targetEntity = TranscriptStructure.class,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "l_transcript_structure_id",referencedColumnName = "transcript_structure_id")
    public TranscriptStructure transcriptstructure;

    public TranscriptsFoundInStructure() {
    }
}