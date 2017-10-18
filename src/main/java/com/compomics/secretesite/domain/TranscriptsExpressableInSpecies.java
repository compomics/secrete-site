package com.compomics.secretesite.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Two way join table between the transcript and the species the transcript was expressed in
 * Created by demet on 6/19/2017.
 */
@Data
@Entity
@EqualsAndHashCode(exclude = {"transcript","species"})
@ToString(exclude = {"transcript","species"})
public class TranscriptsExpressableInSpecies implements Serializable{


    @Id
    @ManyToOne(targetEntity = Species.class)
    @JoinColumn(name = "species_id",referencedColumnName = "species_id")
    @JsonBackReference
    private Species species;

    @Id
    @ManyToOne(targetEntity = Transcript.class)
    @JoinColumn(name = "transcript_id",referencedColumnName = "transcript_id")
    @JsonBackReference
    private Transcript transcript;

}