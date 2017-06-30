
package com.compomics.secretesite.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by davy on 4/20/2017.
 */

@Data
@Entity
@EqualsAndHashCode(exclude = "transcriptscontained")
@ToString(exclude = "transcriptscontained")
public class TranscriptStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transcript_structure_id")
    private Integer transcriptStructureId;

    @OneToMany(mappedBy = "transcriptstructure")
    @JsonManagedReference
    private Set<TranscriptsFoundInStructure> transcriptscontained = new HashSet<>();

    private String pdbId;

    private String chain;

    private Integer fragmentStart;

    private Integer fragmentEnd;

    private Integer numberOfMatchedResidues;

    private Double identityScore;

    public TranscriptStructure() {
    }
}
