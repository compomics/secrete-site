package com.compomics.secretesite.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 * Created by davy on 5/4/2017.
 */

@Entity
@Data
@EqualsAndHashCode(exclude = {"transcript"})
@ToString(exclude = "transcript")
public class TranscriptEarlyFolding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transcriptEarlyFoldingId;

    @ManyToOne
    @JoinColumn(name = "transcript", nullable = false)
    private Transcript transcript;

    private int foldingLocation;

}
