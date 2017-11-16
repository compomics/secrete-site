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
 * class representing proteins in the database
 * Created by davy on 5/10/2017.
 */

@Data
@Entity
public class Protein {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "protein_id")
    private Integer proteinId;

    /**
     * The uniprot id of a protein
     */
    private String swissProtAccession;
    private String trEmblAccession;
    private String proteinEnsemblAccession;
    private String swissProtName;
    private String swissProtLabel;
    private String trEmblName;
    private String trEmblLabel;


}
