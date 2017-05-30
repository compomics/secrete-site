package com.compomics.secretesite.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by davy on 5/9/2017.
 */
@Entity
@Data
public class ProteinCluster {

    @Id
    private Integer proteinClusterId;

    private Integer proteinRep;

    private Integer proteinClusterMember;

}
