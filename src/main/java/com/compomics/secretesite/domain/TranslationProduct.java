package com.compomics.secretesite.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by davy on 5/12/2017.
 */

@Data
@Entity
@EqualsAndHashCode(exclude = {"proteinProduct","parentTranscript"})
@ToString(exclude = {"proteinProduct","parentTranscript"})
public class TranslationProduct {

    @Id
    private Integer translationproductid;

    @ManyToOne
    private Protein proteinProduct;

    @ManyToOne
    private Transcript parentTranscript;

    private Integer transcriptStart;

    private Integer transcriptEnd;
}
