package com.compomics.secretesite.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 * Created by davy on 5/12/2017.
 */

@Entity
@Data
@EqualsAndHashCode(exclude = {"proteinProduct","parentTranscript"})
@ToString(exclude = {"proteinProduct","parentTranscript"})
public class TranscriptProtein {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer translationproductid;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "protein_id", name = "l_protein_id")
    @JsonBackReference
    private Protein proteinProduct;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "transcript_id",name = "l_transcript_id")
    @JsonBackReference
    private Transcript parentTranscript;

    private Integer transcriptStart;

    private Integer transcriptEnd;
}
