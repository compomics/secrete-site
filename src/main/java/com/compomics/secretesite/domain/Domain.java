package com.compomics.secretesite.domain;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
/**
 * Created by davy on 5/11/2017.
 */

@Entity
@Data
public class Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "domain_id")
    private Integer domainId;

    @NaturalId
    private String domainAccession;

    private String domainName;

}
