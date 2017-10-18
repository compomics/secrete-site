package com.compomics.secretesite.domain;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * class representing the results of the protein clustering algorithm
 * Created by davy on 5/9/2017.
 */
@Entity
@Data
public class TranscriptCluster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transcriptClusterId;

    private Integer isTranscriptRepresentative;

    @JoinColumn(name = "transcriptId")
    @ManyToOne(cascade = CascadeType.ALL)
    private Transcript transcriptClusterMember;

    private Integer transcriptClusterGroupId;

}
