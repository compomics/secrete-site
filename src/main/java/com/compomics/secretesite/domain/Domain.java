package com.compomics.secretesite.domain;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

/**
 * The domains present in the experimentally found proteins
 * Created by davy on 5/11/2017.
 */

@Entity
@Data
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

}
