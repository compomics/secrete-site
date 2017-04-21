package com.compomics.secretesite.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
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
public class Species {

    /**
     * internal database id
     */
    @Column(name = "species_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer species_id;

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
    @ManyToMany
    @JoinTable(
            name = "transcripts_expressable_in_species",
            joinColumns = @JoinColumn(name = "l_species_id",referencedColumnName = "species_id")
    )
    private Set<Transcript> expressableTranscripts = new HashSet<>(0);

    public Species(Integer speciesTaxonomyNumber, String speciesName) {
        this.speciesTaxonomyNumber = speciesTaxonomyNumber;
        this.speciesName = speciesName;
    }
}
