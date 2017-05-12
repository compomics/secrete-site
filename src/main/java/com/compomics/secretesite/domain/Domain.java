package com.compomics.secretesite.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
    private Integer domain_id;

    @NaturalId
    private String domainAccession;

    private String domainName;

    @OneToMany(mappedBy = "domain")
    private Set<ProteinDomain> proteinsContainingDomain;


}
