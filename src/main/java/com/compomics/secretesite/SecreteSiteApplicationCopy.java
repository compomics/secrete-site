package com.compomics.secretesite;

import com.compomics.secretesite.domain.repositories.GeneRepository;
import com.compomics.secretesite.domain.repositories.SpeciesRepository;
import com.compomics.secretesite.domain.Gene;
import com.compomics.secretesite.domain.Species;
import com.compomics.secretesite.domain.Transcript;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.*;
import java.util.stream.Collectors;

//@SpringBootApplication
public class SecreteSiteApplicationCopy {

    private final SpeciesRepository speciesRepository;

    private final GeneRepository geneRepository;

    public SecreteSiteApplicationCopy(SpeciesRepository speciesRepository, GeneRepository geneRepository) {
        this.speciesRepository = speciesRepository;
        this.geneRepository = geneRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SecreteSiteApplication.class, args);
    }


    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {


            Map<String, Gene> referenceGenes = new HashMap<>();

            Species pichiaPastoris = new Species(4922, "Picha pastoris");

            speciesRepository.save(pichiaPastoris);

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

                    Transcript transcript = new Transcript(splitline[2], splitline[9],Integer.valueOf(splitline[3]),Integer.valueOf(splitline[4]), gene);

                    transcript.getExpressableIn().add(pichiaPastoris);

                    gene.getTranscripts().add(transcript);
                    line = reader.readLine();

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

                    Transcript transcript = new Transcript(splitline[2], splitline[9],Integer.valueOf(splitline[3]),Integer.valueOf(splitline[4]), gene);

                    if (!gene.getTranscripts().contains((transcript))) {
                        gene.getTranscripts().add(transcript);
                    }
                    line = reader.readLine();
                }
            }

            Species saccharomyces = new Species(4932, "Saccharomyces cerevisiae");

            speciesRepository.save(saccharomyces);
            //todo still saccharomyces, also check if transcripts are already present, if so don't overwrite just update

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

                    Transcript transcript = new Transcript(splitline[2], splitline[9],Integer.valueOf(splitline[3]),Integer.valueOf(splitline[4]), gene);

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
                    line = reader.readLine();
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

                    Transcript transcript = new Transcript(splitline[2], splitline[9],Integer.valueOf(splitline[3]),Integer.valueOf(splitline[4]), gene);

                    List<Transcript> transcripts = gene.getTranscripts()
                            .stream()
                            .filter(e -> e.equals(transcript))
                            .collect(Collectors.toList());

                    if (transcripts.isEmpty()) {
                        transcript.getExpressableIn().add(saccharomyces);
                        gene.getTranscripts().add(transcript);
                    }
                line = reader.readLine();
                }
                geneRepository.save(referenceGenes.values());
            }
        }
                ;
    }
}