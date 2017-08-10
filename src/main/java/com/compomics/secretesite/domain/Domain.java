package com.compomics.secretesite.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * The domains present in the experimentally found proteins
 * Created by davy on 5/11/2017.
 */

@Entity
@Data
@EqualsAndHashCode(exclude = {"proteinDomains"})
@ToString(exclude = {"proteinDomains"})
public class Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "domain_id")
    private Integer domainId;

    /**
     *the pfam identifiers of a domain
     */
    @NaturalId
    private String domainAccession;

    /**
     * the fully qualified name of the domain, currently empty
     */
    private String domainName;

    @OneToMany(mappedBy = "domain",cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<ProteinDomain> proteinDomains = new HashSet<>();

}
