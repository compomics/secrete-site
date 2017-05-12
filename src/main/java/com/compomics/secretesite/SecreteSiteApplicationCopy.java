package com.compomics.secretesite;


import com.compomics.secretesite.domain.*;
import com.compomics.secretesite.domain.repositories.GeneRepository;
import com.compomics.secretesite.domain.repositories.SpeciesRepository;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@SpringBootApplication
//@EnableAsync
public class SecreteSiteApplicationCopy {

    private SpeciesRepository speciesRepository;
    private GeneRepository geneRepository;

    public SecreteSiteApplicationCopy(SpeciesRepository speciesRepository, GeneRepository geneRepository) {
        this.speciesRepository = speciesRepository;
     //   this.geneRepository = geneRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SecreteSiteApplicationCopy.class, args);
    }


    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {


            Map<String, Gene> referenceGenes = new HashMap<>();

            Species pichiaPastoris = new Species(4922, "Picha pastoris");

            speciesRepository.save(pichiaPastoris);

            ArrayListValuedHashMap<Integer, TranscriptStructure> transcriptStructureMap = new ArrayListValuedHashMap<>();

            try (LineNumberReader reader = new LineNumberReader(new FileReader(new File(args[0], "PpE_PDB_hit.txt")))) {
                reader.readLine();

                String data = reader.readLine();

                while (data != null) {

                    String[] splitdata = data.split("\t");

                    data = reader.readLine();

                    TranscriptStructure structure = new TranscriptStructure();

                    structure.setPdbId(splitdata[1].split("\\|")[3]);
                    structure.setChain(splitdata[2].split(",")[0]);
                    structure.setFragmentStart(Integer.parseInt(splitdata[3]));
                    structure.setFragmentEnd(Integer.parseInt(splitdata[4]));
                    structure.setIdentityScore(Double.parseDouble(splitdata[8]));
                    structure.setNumberOfMatchedResidues(Integer.parseInt(splitdata[9]));


                    transcriptStructureMap.put(Integer.parseInt(splitdata[0].split("Seq_")[1]), structure);
                }
            }

            try (LineNumberReader reader = new LineNumberReader(new FileReader(new File(args[0], "Pp_resultstable_enriched.txt")))) {
                reader.readLine();
                String line = reader.readLine();
                while (line != null) {
                    String[] splitline = line.split("\t");
                    Gene gene;
                    if (!referenceGenes.containsKey(splitline[1])) {
                        gene = new Gene(splitline[1], splitline[5], splitline[6], "");
                        referenceGenes.put(gene.getGeneAccession(), gene);
                    } else {
                        gene = referenceGenes.get(splitline[1]);
                    }

                    Transcript transcript = new Transcript(splitline[2], splitline[9], Integer.valueOf(splitline[3]), Integer.valueOf(splitline[4]), gene);

                    transcript.getExpressableIn().add(pichiaPastoris);

                    gene.getTranscripts().add(transcript);


                    transcriptStructureMap.get(Integer.parseInt(splitline[0])).forEach(e -> {
                        TranscriptsFoundInStructure found = new TranscriptsFoundInStructure();
                        transcript.getFoundIn().add(found);
                        found.setTranscript(transcript);
                        found.setTranscriptstructure(e);
                    });

                    line = reader.readLine();

                }
            }


            transcriptStructureMap = new ArrayListValuedHashMap<>();

            try (LineNumberReader reader = new LineNumberReader(new FileReader(new File(args[0], "PpD_PDB_hit.txt")))) {
                reader.readLine();

                String data = reader.readLine();

                while (data != null) {

                    String[] splitdata = data.split("\t");

                    data = reader.readLine();

                    TranscriptStructure structure = new TranscriptStructure();

                    structure.setPdbId(splitdata[1].split("\\|")[3]);
                    structure.setChain(splitdata[2].split(",")[0]);
                    structure.setFragmentStart(Integer.parseInt(splitdata[3]));
                    structure.setFragmentEnd(Integer.parseInt(splitdata[4]));
                    structure.setIdentityScore(Double.parseDouble(splitdata[8]));
                    structure.setNumberOfMatchedResidues(Integer.parseInt(splitdata[9]));


                    transcriptStructureMap.put(Integer.parseInt(splitdata[0].split("Seq_")[1]), structure);
                }
            }

            try (LineNumberReader reader = new LineNumberReader(new FileReader(new File(args[0], "Pp_resultstable_depleted.txt")))) {
                reader.readLine();
                String line = reader.readLine();
                while (line != null) {
                    String[] splitline = line.split("\t");
                    Gene gene;
                    if (!referenceGenes.containsKey(splitline[1])) {
                        gene = new Gene(splitline[1], splitline[5], splitline[6], "");
                        referenceGenes.put(gene.getGeneAccession(), gene);
                    } else {
                        gene = referenceGenes.get(splitline[1]);
                    }

                    Transcript transcript = new Transcript(splitline[2], splitline[9], Integer.valueOf(splitline[3]), Integer.valueOf(splitline[4]), gene);

                    if (!gene.getTranscripts().contains((transcript))) {
                        gene.getTranscripts().add(transcript);
                    }


                    transcriptStructureMap.get(Integer.parseInt(splitline[0])).forEach(e -> {
                        TranscriptsFoundInStructure found = new TranscriptsFoundInStructure();
                        transcript.getFoundIn().add(found);
                        found.setTranscript(transcript);
                        found.setTranscriptstructure(e);
                    });

                    line = reader.readLine();
                }
            }

            Species saccharomyces = new Species(4932, "Saccharomyces cerevisiae");

            speciesRepository.save(saccharomyces);

            transcriptStructureMap = new ArrayListValuedHashMap<>();

            try (LineNumberReader reader = new LineNumberReader(new FileReader(new File(args[0], "ScE_PDB_hit.txt")))) {
                reader.readLine();

                String data = reader.readLine();

                while (data != null) {

                    String[] splitdata = data.split("\t");

                    data = reader.readLine();

                    TranscriptStructure structure = new TranscriptStructure();

                    structure.setPdbId(splitdata[1].split("\\|")[3]);
                    structure.setChain(splitdata[2].split(",")[0]);
                    structure.setFragmentStart(Integer.parseInt(splitdata[3]));
                    structure.setFragmentEnd(Integer.parseInt(splitdata[4]));
                    structure.setIdentityScore(Double.parseDouble(splitdata[8]));
                    structure.setNumberOfMatchedResidues(Integer.parseInt(splitdata[9]));


                    transcriptStructureMap.put(Integer.parseInt(splitdata[0].split("Seq_")[1]), structure);
                }
            }

            try (LineNumberReader reader = new LineNumberReader(new FileReader(new File(args[0], "Sc_resultstable_enriched.txt")))) {
                reader.readLine();
                String line = reader.readLine();
                while (line != null) {
                    String[] splitline = line.split("\t");

                    Gene gene;
                    if (!referenceGenes.containsKey(splitline[1])) {
                        gene = new Gene(splitline[1], splitline[5], splitline[6], "");
                        referenceGenes.put(gene.getGeneAccession(), gene);
                    } else {
                        gene = referenceGenes.get(splitline[1]);
                    }

                    Transcript transcript = new Transcript(splitline[2], splitline[9], Integer.valueOf(splitline[3]), Integer.valueOf(splitline[4]), gene);

                    List<Transcript> transcripts = gene.getTranscripts()
                            .stream()
                            .filter(e -> e.getEnsembleTranscriptAccession().equals(transcript.getEnsembleTranscriptAccession()) && e.getTranscript_sequence().equals(transcript.getTranscript_sequence()))
                            .collect(Collectors.toList());

                    if (!transcripts.isEmpty()) {
                        transcripts.forEach(transcript1 -> transcript1.getExpressableIn().add(saccharomyces));
                    } else {
                        transcript.getExpressableIn().add(saccharomyces);
                        gene.getTranscripts().add(transcript);
                    }


                    transcriptStructureMap.get(Integer.parseInt(splitline[0])).forEach(e -> {
                        TranscriptsFoundInStructure found = new TranscriptsFoundInStructure();
                        transcript.getFoundIn().add(found);
                        found.setTranscript(transcript);
                        found.setTranscriptstructure(e);
                    });

                    line = reader.readLine();
                }
            }

            transcriptStructureMap = new ArrayListValuedHashMap<>();

            try (LineNumberReader reader = new LineNumberReader(new FileReader(new File(args[0], "ScD_PDB_hit.txt")))) {
                reader.readLine();

                String data = reader.readLine();

                while (data != null) {

                    String[] splitdata = data.split("\t");

                    data = reader.readLine();

                    TranscriptStructure structure = new TranscriptStructure();

                    structure.setPdbId(splitdata[1].split("\\|")[3]);
                    structure.setChain(splitdata[2].split(",")[0]);
                    structure.setFragmentStart(Integer.parseInt(splitdata[3]));
                    structure.setFragmentEnd(Integer.parseInt(splitdata[4]));
                    structure.setIdentityScore(Double.parseDouble(splitdata[8]));
                    structure.setNumberOfMatchedResidues(Integer.parseInt(splitdata[9]));


                    transcriptStructureMap.put(Integer.parseInt(splitdata[0].split("Seq_")[1]), structure);
                }
            }

            try (LineNumberReader reader = new LineNumberReader(new FileReader(new File(args[0], "Sc_resultstable_depleted.txt")))) {
                reader.readLine();
                String line = reader.readLine();
                while (line != null) {
                    String[] splitline = line.split("\t");

                    Gene gene;
                    if (!referenceGenes.containsKey(splitline[1])) {
                        gene = new Gene(splitline[1], splitline[5], splitline[6], "");
                        referenceGenes.put(gene.getGeneAccession(), gene);
                    } else {
                        gene = referenceGenes.get(splitline[1]);
                    }

                    Transcript transcript = new Transcript(splitline[2], splitline[9], Integer.valueOf(splitline[3]), Integer.valueOf(splitline[4]), gene);

                    List<Transcript> transcripts = gene.getTranscripts()
                            .stream()
                            .filter(e -> e.equals(transcript))
                            .collect(Collectors.toList());

                    if (transcripts.isEmpty()) {
                        transcript.getExpressableIn().add(saccharomyces);
                        gene.getTranscripts().add(transcript);
                    }

                    transcriptStructureMap.get(Integer.parseInt(splitline[0])).forEach(e -> {
                        TranscriptsFoundInStructure found = new TranscriptsFoundInStructure();
                        transcript.getFoundIn().add(found);
                        found.setTranscript(transcript);
                        found.setTranscriptstructure(e);
                    });

                    line = reader.readLine();
                }
             //   geneRepository.save(referenceGenes.values());
            }
        }
                ;
    }

}