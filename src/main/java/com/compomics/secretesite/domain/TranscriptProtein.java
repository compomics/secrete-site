package com.compomics.secretesite.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 * Created by davy on 5/12/2017.
 */

@Data
@Entity
@EqualsAndHashCode(exclude = {"proteinProduct","parentTranscript"})
@ToString(exclude = {"proteinProduct","parentTranscript"})
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@translation_product_id")
public class TranscriptProtein {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer translation_product_id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "proteinId", name = "l_protein_id")
    private Protein proteinProduct;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "transcriptId",name = "l_transcript_id")
    private Transcript parentTranscript;


}
