package com.compomics.secretesite.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by davy on 4/10/2017.
 */
@Entity
public class Species {

    /**
     * internal database id
     */
    private Integer species_id;

    /**
     * the ensembl taxonomy identifier
     */
    private Integer speciesTaxonomyNumber;

    /**
     * all the {@link Gene}s that belong to this species
     */
    private Set<Transcript> expressableTranscripts = new HashSet<>(0);

    /**
     * human readable name of species
     */
    private String speciesName;

    public Species(){}

    public Species(Integer speciesTaxonomyNumber, String speciesName) {
        this.speciesTaxonomyNumber = speciesTaxonomyNumber;
        this.speciesName = speciesName;
    }

    @Column(name = "species_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getSpecies_id() {
        return species_id;
    }

    public void setSpecies_id(Integer species_id) {
        this.species_id = species_id;
    }

    @Column
    @NaturalId
    public Integer getSpeciesTaxonomyNumber() {
        return speciesTaxonomyNumber;
    }

    public void setSpeciesTaxonomyNumber(Integer speciesTaxonomyNumber) {
        this.speciesTaxonomyNumber = speciesTaxonomyNumber;
    }

    @Column
    public String getSpeciesName() {
        return speciesName;
    }


    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    @ManyToMany
    @JoinTable(
            name = "transcripts_expressable_in_species",
            inverseJoinColumns=@JoinColumn(name = "l_transcript_id", referencedColumnName = "transcript_id"),
            joinColumns = @JoinColumn(name = "l_species_id",referencedColumnName = "species_id")
    )
    public Set<Transcript> getExpressableTranscripts() {return expressableTranscripts;}

    public void setExpressableTranscripts(Set<Transcript> transcripts){
        this.expressableTranscripts.addAll(transcripts);
    }
}
