package com.compomics.secretesite;


import com.compomics.secretesite.domain.*;
import com.compomics.secretesite.domain.repositories.GeneRepository;
import com.compomics.secretesite.domain.repositories.SpeciesRepository;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//@SpringBootApplication
//@EnableAsync
public class SecreteSiteApplicationCopy {

    private SpeciesRepository speciesRepository;
    private GeneRepository geneRepository;

    public SecreteSiteApplicationCopy(SpeciesRepository speciesRepository, GeneRepository geneRepository) {
        this.speciesRepository = speciesRepository;
        this.geneRepository = geneRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SecreteSiteApplicationCopy.class, args);
    }


    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {


            Map<String, Gene> referenceGenes = new HashMap<>();

            Species pichiaPastoris = new Species(4922, "Pichia pastoris");

            speciesRepository.save(pichiaPastoris);

            ArrayListValuedHashMap<Integer, TranscriptStructure> transcriptStructureMap = new ArrayListValuedHashMap<>();
            ArrayListValuedHashMap<String,Transcript> transcripts = new ArrayListValuedHashMap<>();

            try (LineNumberReader reader = new LineNumberReader(new FileReader(new File(args[0], "PpE_PDB_hit.txt")))) {
                reader.readLine();

                String data = reader.readLine();

                while (data != null) {

                    String[] splitdata = data.split("\t");

                    TranscriptStructure structure = new TranscriptStructure();

                    structure.setPdbId(splitdata[1].split("\\|")[3]);
                    structure.setChain(splitdata[2].split(",")[0]);
                    structure.setFragmentStart(Integer.parseInt(splitdata[3]));
                    structure.setFragmentEnd(Integer.parseInt(splitdata[4]));
                    structure.setIdentityScore(Double.parseDouble(splitdata[8]));
                    structure.setNumberOfMatchedResidues(Integer.parseInt(splitdata[9]));

                    transcriptStructureMap.put(Integer.parseInt(splitdata[0].split("Seq_")[1]), structure);

                    data = reader.readLine();
                }
            }

            final ArrayListValuedHashMap<String, TranscriptEarlyFolding> foldingMap = new ArrayListValuedHashMap<>();

            try (LineNumberReader reader = new LineNumberReader(new FileReader(new File(args[0], "PpE_EF_positions.txt")))) {

                reader.readLine();

                String[] splitdata;

                String data = reader.readLine();

                while (data != null) {

                    splitdata = data.split("\t");


                    String seqid = splitdata[0].split("Seq_")[1];

                    if (splitdata.length > 1) {
                        String[] numberarray = splitdata[1].split(",");
                        if (numberarray.length == 1) {
                            TranscriptEarlyFolding folding = new TranscriptEarlyFolding();
                            folding.setFoldingLocation(Integer.parseInt(splitdata[1]));
                            foldingMap.put(seqid, folding);
                        } else {
                            Arrays.stream(numberarray).map(e -> {
                                TranscriptEarlyFolding folding = new TranscriptEarlyFolding();
                                folding.setFoldingLocation(Integer.parseInt(e));
                                return folding;
                            }).forEach(e2 -> foldingMap.put(seqid, e2));

                        }
                    }
                    data = reader.readLine();
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

                    transcripts.put(transcript.getEnsembleTranscriptAccession(),transcript);

                    transcript.setSecretionStatus("enriched");

                    transcript.getExpressableIn().add(pichiaPastoris);

          //          gene.getTranscripts().add(transcript);


                    transcriptStructureMap.get(Integer.parseInt(splitline[0])).forEach(e -> {
                        TranscriptsFoundInStructure found = new TranscriptsFoundInStructure();
                        transcript.getFoundIn().add(found);
                        found.setTranscript(transcript);
                        found.setTranscriptstructure(e);
                    });

                    foldingMap.get(splitline[0]).forEach(e -> {
                        e.setTranscript(transcript);
                        transcript.getEarlyFoldingLocations().add(e);
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

                    TranscriptStructure structure = new TranscriptStructure();

                    structure.setPdbId(splitdata[1].split("\\|")[3]);
                    structure.setChain(splitdata[2].split(",")[0]);
                    structure.setFragmentStart(Integer.parseInt(splitdata[3]));
                    structure.setFragmentEnd(Integer.parseInt(splitdata[4]));
                    structure.setIdentityScore(Double.parseDouble(splitdata[8]));
                    structure.setNumberOfMatchedResidues(Integer.parseInt(splitdata[9]));

                    transcriptStructureMap.put(Integer.parseInt(splitdata[0].split("Seq_")[1]), structure);

                    data = reader.readLine();

                }
            }

            foldingMap.clear();

            try (LineNumberReader reader = new LineNumberReader(new FileReader(new File(args[0], "PpD_EF_positions.txt")))) {

                reader.readLine();

                String[] splitdata;

                String data = reader.readLine();

                while (data != null) {

                    splitdata = data.split("\t");


                    String seqid = splitdata[0].split("Seq_")[1];

                    if (splitdata.length > 1) {
                        String[] numberarray = splitdata[1].split(",");
                        if (numberarray.length == 1) {
                            TranscriptEarlyFolding folding = new TranscriptEarlyFolding();
                            folding.setFoldingLocation(Integer.parseInt(splitdata[1]));
                            foldingMap.put(seqid, folding);
                        } else {
                            Arrays.stream(numberarray).map(e -> {
                                TranscriptEarlyFolding folding = new TranscriptEarlyFolding();
                                folding.setFoldingLocation(Integer.parseInt(e));
                                return folding;
                            }).forEach(e2 -> foldingMap.put(seqid, e2));

                        }
                    }
                    data = reader.readLine();
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

                    Transcript transcript = new Transcript(splitline[2], splitline[9], Integer.valueOf(splitline[7]), Integer.valueOf(splitline[8]), gene);

                    transcripts.put(transcript.getEnsembleTranscriptAccession(),transcript);

                    transcript.setSecretionStatus("depleted");

          //          if (!gene.getTranscripts().contains((transcript))) {
       //                 gene.getTranscripts().add(transcript);
        //            }


                    transcriptStructureMap.get(Integer.parseInt(splitline[0])).forEach(e -> {
                        TranscriptsFoundInStructure found = new TranscriptsFoundInStructure();
                        transcript.getFoundIn().add(found);
                        found.setTranscript(transcript);
                        found.setTranscriptstructure(e);
                    });

                    foldingMap.get(splitline[0]).forEach(e -> {
                        e.setTranscript(transcript);
                        transcript.getEarlyFoldingLocations().add(e);
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

            foldingMap.clear();

            try (LineNumberReader reader = new LineNumberReader(new FileReader(new File(args[0], "ScE_EF_positions.txt")))) {

                reader.readLine();

                String[] splitdata;

                String data = reader.readLine();

                while (data != null) {

                    splitdata = data.split("\t");


                    String seqid = splitdata[0].split("Seq_")[1];

                    if (splitdata.length > 1) {
                        String[] numberarray = splitdata[1].split(",");
                        if (numberarray.length == 1) {
                            TranscriptEarlyFolding folding = new TranscriptEarlyFolding();
                            folding.setFoldingLocation(Integer.parseInt(splitdata[1]));
                            foldingMap.put(seqid, folding);
                        } else {
                            Arrays.stream(numberarray).map(e -> {
                                TranscriptEarlyFolding folding = new TranscriptEarlyFolding();
                                folding.setFoldingLocation(Integer.parseInt(e));
                                return folding;
                            }).forEach(e2 -> foldingMap.put(seqid, e2));

                        }
                    }
                    data = reader.readLine();
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

                    transcript.setSecretionStatus("enriched");

                    transcripts.put(transcript.getEnsembleTranscriptAccession(),transcript);

              //      List<Transcript> genetranscripts = gene.getTranscripts()
           //                 .stream()
           //                 .filter(e -> e.getEnsembleTranscriptAccession().equals(transcript.getEnsembleTranscriptAccession()) && e.getTranscriptSequence().equals(transcript.getTranscriptSequence()))
           //                .collect(Collectors.toList());

             //       if (!genetranscripts.isEmpty()) {
           //            genetranscripts.forEach(transcript1 -> transcript1.getExpressableIn().add(saccharomyces));
            //        } else {
            //            transcript.getExpressableIn().add(saccharomyces);
            //            gene.getTranscripts().add(transcript);
           //         }


                    transcriptStructureMap.get(Integer.parseInt(splitline[0])).forEach(e -> {
                        TranscriptsFoundInStructure found = new TranscriptsFoundInStructure();
                        transcript.getFoundIn().add(found);
                        found.setTranscript(transcript);
                        found.setTranscriptstructure(e);
                    });

                    foldingMap.get(splitline[0]).forEach(e -> {
                        e.setTranscript(transcript);
                        transcript.getEarlyFoldingLocations().add(e);
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


            foldingMap.clear();

            try (LineNumberReader reader = new LineNumberReader(new FileReader(new File(args[0], "ScD_EF_positions.txt")))) {

                reader.readLine();

                String[] splitdata;

                String data = reader.readLine();

                while (data != null) {

                    splitdata = data.split("\t");


                    String seqid = splitdata[0].split("Seq_")[1];

                    if (splitdata.length > 1) {
                        String[] numberarray = splitdata[1].split(",");
                        if (numberarray.length == 1) {
                            TranscriptEarlyFolding folding = new TranscriptEarlyFolding();
                            folding.setFoldingLocation(Integer.parseInt(splitdata[1]));
                            foldingMap.put(seqid, folding);
                        } else {
                            Arrays.stream(numberarray).map(e -> {
                                TranscriptEarlyFolding folding = new TranscriptEarlyFolding();
                                folding.setFoldingLocation(Integer.parseInt(e));
                                return folding;
                            }).forEach(e2 -> foldingMap.put(seqid, e2));

                        }
                    }
                    data = reader.readLine();
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

                    transcript.setSecretionStatus("depleted");

                    transcripts.put(transcript.getEnsembleTranscriptAccession(),transcript);

           //         List<Transcript> genetranscripts = gene.getTranscripts()
            //                .stream()
          //                  .filter(e -> e.equals(transcript))
          //                  .collect(Collectors.toList());

           //         if (genetranscripts.isEmpty()) {
         //               transcript.getExpressableIn().add(saccharomyces);
         //               gene.getTranscripts().add(transcript);
          //          }

                    transcriptStructureMap.get(Integer.parseInt(splitline[0])).forEach(e -> {
                        TranscriptsFoundInStructure found = new TranscriptsFoundInStructure();
                        transcript.getFoundIn().add(found);
                        found.setTranscript(transcript);
                        found.setTranscriptstructure(e);
                    });

                    foldingMap.get(splitline[0]).forEach(e -> {
                        e.setTranscript(transcript);
                        transcript.getEarlyFoldingLocations().add(e);
                    });

                    line = reader.readLine();
                }

                Map<String, Protein> mappedProteins = new HashMap<>();

                Map<String, Domain> mappedDomains = new HashMap<>();

                try (Stream<String> mylines = Files.lines(Paths.get(args[0], "mart_export.txt"))) {
                    mylines.map(martline -> martline.split("\t")).filter(splitlines -> splitlines.length > 4).forEach(filteredlines -> {

                        Protein aProtein;
                        if (mappedProteins.containsKey(filteredlines[3])) {
                            aProtein = mappedProteins.get(filteredlines[3]);
                        } else {
                            aProtein = new Protein();
                            aProtein.setProteinAccession(filteredlines[3]);
                            mappedProteins.put(filteredlines[3], aProtein);
                        }
                        Domain aDomain;
                        if (mappedDomains.containsKey(filteredlines[2])) {
                            aDomain = mappedDomains.get(filteredlines[2]);
                        } else {
                            aDomain = new Domain();
                            aDomain.setDomainAccession(filteredlines[2]);
                            mappedDomains.put(filteredlines[2], aDomain);
                        }

                        ProteinDomain aProteinDomain = new ProteinDomain();

                        aProteinDomain.setDomain(aDomain);
                        aProteinDomain.setProtein(aProtein);
                        aProteinDomain.setDomainEnd(Integer.parseInt(filteredlines[5]));
                        aProteinDomain.setDomainStart(Integer.parseInt(filteredlines[4]));

                        aProtein.getDomainsContainedInProtein().add(aProteinDomain);
                   //     aDomain.getProteinsContainingDomain().add(aProteinDomain);

                        List<Transcript> transcriptsofinterest = transcripts.get(filteredlines[1]);
                        transcriptsofinterest.forEach(transcript -> {

                                    TranscriptProtein product = new TranscriptProtein();
                                    product.setProteinProduct(aProtein);
                                    aProtein.getParentTranscripts().add(product);
                         //           transcript.getTranscriptProteins().add(product);
                                    product.setParentTranscript(transcript);


                                }
                        );
                    });
                }
            }
            geneRepository.saveAll(referenceGenes.values());

        };
    }
}