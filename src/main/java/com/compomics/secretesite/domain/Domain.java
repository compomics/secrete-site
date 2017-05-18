package com.compomics.secretesite.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by davy on 5/11/2017.
 */

@Entity
@Data
@EqualsAndHashCode(exclude = "proteinsContainingDomain")
@ToString(exclude = "proteinsContainingDomain")
public class Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer domain_id;

    @NaturalId
    private String domainAccession;

    private String domainName;

    @OneToMany(mappedBy = "domain")
    private Set<ProteinDomain> proteinsContainingDomain = new HashSet<>();


}
