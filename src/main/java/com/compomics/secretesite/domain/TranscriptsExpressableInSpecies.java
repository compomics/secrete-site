package com.compomics.secretesite.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by demet on 6/19/2017.
 */
@Entity
@Data
@EqualsAndHashCode(exclude = {"transcript","species"})
@ToString(exclude = {"transcript","species"})
public class TranscriptsExpressableInSpecies implements Serializable{

    @Id
    @ManyToOne(targetEntity = Species.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "species_id",referencedColumnName = "species_id")
    public Species species;

    @Id
    @ManyToOne(targetEntity = Transcript.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "transcript_id",referencedColumnName = "transcript_id")
    public Transcript transcript;

    public TranscriptsExpressableInSpecies() {
    }
}
