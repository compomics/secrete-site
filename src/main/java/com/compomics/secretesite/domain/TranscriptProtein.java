package com.compomics.secretesite.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by davy on 5/12/2017.
 */

@Data
@Entity
public class TranscriptProtein {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer translationproductid;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "protein_id", name = "l_protein_id")
    private Protein proteinProduct;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "transcript_id",name = "l_transcript_id")
    private Transcript parentTranscript;
}
