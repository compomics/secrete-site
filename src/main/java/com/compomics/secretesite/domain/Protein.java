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
 * Created by davy on 5/10/2017.
 */

@Data
@Entity
@EqualsAndHashCode(exclude = {"domainsContainedInProtein","parentTranscripts"})
@ToString(exclude = {"domainsContainedInProtein","parentTranscripts"})
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@proteinId")
public class Protein {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer proteinId;

    @NaturalId
    private String proteinAccession;

    @OneToMany(mappedBy = "protein",cascade = CascadeType.ALL)
    private Set<ProteinDomain> domainsContainedInProtein = new HashSet<>();


    @OneToMany(mappedBy = "proteinProduct",cascade = CascadeType.ALL)
    private Set<TranscriptProtein> parentTranscripts = new HashSet<>();

    public Protein() {
    }
}
