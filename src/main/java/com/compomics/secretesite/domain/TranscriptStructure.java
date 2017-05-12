package com.compomics.secretesite.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by davy on 4/20/2017.
 */

@Data
@Entity
@EqualsAndHashCode(exclude = "transcriptscontained")
public class TranscriptStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transcriptstructure_id")
    private Integer transcriptStructureId;

    @OneToMany(mappedBy = "transcriptstructure")
    private Set<TranscriptsFoundInStructure> transcriptscontained = new HashSet<>();

    @Column
    private String pdbId;

    @Column
    private String chain;

    @Column
    private Integer fragmentStart;

    @Column
    private Integer fragmentEnd;

    @Column
    private Integer numberOfMatchedResidues;

    @Column
    private Double identityScore;
}
