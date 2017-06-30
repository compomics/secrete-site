package com.compomics.secretesite.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@EqualsAndHashCode(exclude = "transcriptsExpressableInSpecies")
@ToString(exclude = {"transcriptsExpressableInSpecies"})
@NoArgsConstructor
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
    @OneToMany(mappedBy = "species",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<TranscriptsExpressableInSpecies> transcriptsExpressableInSpecies = new HashSet<>();

    public Species(Integer speciesTaxonomyNumber, String speciesName) {
        this.speciesTaxonomyNumber = speciesTaxonomyNumber;
        this.speciesName = speciesName;
    }
}
