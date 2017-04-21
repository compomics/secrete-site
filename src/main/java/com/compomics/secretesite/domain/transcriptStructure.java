package com.compomics.secretesite.domain;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.List;

/**
 * Created by davy on 4/20/2017.
 */

@Data
@Entity
public class transcriptStructure {

    @Id
    private Integer transcriptStructure_id;

    @OneToMany(mappedBy = "transcript_id")
    private List<Transcript> transcriptscontained;

    @Column
    @NaturalId
    private String pdbId;

    @Column
    private String chain;

    @Column
    private Integer fragmentStart;

    @Column
    private Integer fragmentEnd;

    @Column
    private Double dynaminePrediction;

    @Column
    private Double identityScore;
}
