package com.compomics.secretesite.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by davy on 4/10/2017.
 */
@Data
@Entity
@EqualsAndHashCode(exclude = "expressableTranscripts")
@ToString(exclude = {"expressableTranscripts"})
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@speciesId")
public class Species {

    /**
     * internal database id
     */
    @Column(name = "species_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer speciesId;

    /**
     * the ensembl taxonomy identifier
     */
    @Column
    @NaturalId
    private Integer speciesTaxonomyNumber;

    /**
     * human readable name of species
     */
    @Column
    private String speciesName;

    /**
     * all the {@link Gene}s that belong to this species
     */
    @ManyToMany(mappedBy = "expressableIn")
    private Set<Transcript> expressableTranscripts = new HashSet<>(0);

    public Species(Integer speciesTaxonomyNumber, String speciesName) {
        this.speciesTaxonomyNumber = speciesTaxonomyNumber;
        this.speciesName = speciesName;
    }

    public Species() {
    }
}
