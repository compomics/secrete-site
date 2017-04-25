package com.compomics.secretesite.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by davy on 4/21/2017.
 */
@Entity
public class TranscriptsFoundInStructure {

    @Id
    @GeneratedValue
    @Getter
    public Integer id;

    @ManyToOne(targetEntity = Transcript.class)
    @JoinColumn(name = "l_transcript_id",referencedColumnName = "transcript_id")
    @Getter
    @Setter
    public Transcript transcript;

    @ManyToOne(targetEntity = TranscriptStructure.class,cascade = CascadeType.ALL)
    @JoinColumn(name = "l_transcriptstructure_id",referencedColumnName = "transcriptstructure_id")
    @Getter
    @Setter
    public TranscriptStructure transcriptstructure;

    @Column
    @Getter
    @Setter
    public String clusterRepresentative;
}
