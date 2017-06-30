
package com.compomics.secretesite.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * class representing the table between transcripts and the pdb structures they have been found in.
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

    /**
     * the pdb ID connected to this transcript
     */
    private String pdbId;

    /**
     * specific chain in the structure in case of multiple repeats
     */
    private String chain;

    /**
     * start on the chain
     */
    private Integer fragmentStart;

    /**
     * end on the chain
     */
    private Integer fragmentEnd;

    /**
     * how many residues match with the mapped structure. Can be taken as a measure of accuracy of the mapping algorithm
     */
    private Integer numberOfMatchedResidues;

    /**
     * score assigned to the mapping by the algorithm
     */
    private Double identityScore;

    public TranscriptStructure() {
    }
}
