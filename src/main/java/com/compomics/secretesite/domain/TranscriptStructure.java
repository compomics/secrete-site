
        package com.compomics.secretesite.domain;

        import com.fasterxml.jackson.annotation.JsonIdentityInfo;
        import com.fasterxml.jackson.annotation.ObjectIdGenerators;
        import lombok.Data;
        import lombok.EqualsAndHashCode;
        import lombok.ToString;

        import javax.persistence.*;
        import java.util.HashSet;
        import java.util.Set;

/**
 * Created by davy on 4/20/2017.
 */

@Data
@Entity
@EqualsAndHashCode(exclude = "transcriptscontained")
@ToString(exclude = "transcriptscontained")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@transcriptStructureId")
public class TranscriptStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transcript_structure_id")
    private Integer transcriptStructureId;

    @OneToMany(mappedBy = "transcriptstructure")
    private Set<TranscriptsFoundInStructure> transcriptscontained = new HashSet<>();

    private String pdbId;

    private String chain;

    private Integer fragmentStart;

    private Integer fragmentEnd;

    private Integer numberOfMatchedResidues;

    private Double identityScore;

    public TranscriptStructure() {
    }
}
