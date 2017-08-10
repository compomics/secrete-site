package com.compomics.secretesite;


import com.compomics.secretesite.domain.*;
import com.compomics.secretesite.domain.repositories.*;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
@EnableAsync
public class SecreteSiteApplicationCopy {

    private SpeciesRepository speciesRepository;
    private GeneRepository geneRepository;
    private TranscriptRepository transcriptRepository;
    private ProteinRepository proteinRepository;
    private TranscriptEarlyFoldingRepository transcriptEarlyFoldingRepository;

    public SecreteSiteApplicationCopy(SpeciesRepository speciesRepository, GeneRepository geneRepository,TranscriptRepository transcriptRepository,ProteinRepository proteinRepository,TranscriptEarlyFoldingRepository transcriptEarlyFoldingRepository) {
        this.speciesRepository = speciesRepository;
        this.geneRepository = geneRepository;
        this.transcriptRepository = transcriptRepository;
        this.proteinRepository = proteinRepository;
        this.transcriptEarlyFoldingRepository = transcriptEarlyFoldingRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SecreteSiteApplicationCopy.class, args);
    }


    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {


            Map<String, Gene> referenceGenes = new HashMap<>();

            Map<String, Domain> mappedDomains = new HashMap<>();

            Map<Integer,String> seqNumberToAccession = new HashMap<>();

            Species pichiaPastoris = new Species(4922, "Pichia pastoris");

            speciesRepository.save(pichiaPastoris);

            ArrayListValuedHashMap<Integer, TranscriptStructure> transcriptStructureMap = new ArrayListValuedHashMap<>();
            HashMap<Integer, Transcript> transcripts = new HashMap<>();
            ArrayListValuedHashMap<Transcript,ProteinDomain>transcriptsdomains = new ArrayListValuedHashMap<>();

            try (LineNumberReader reader = new LineNumberReader(new FileReader(new File(args[0], "PpE_PDB_hit.txt")))) {
                reader.readLine();

                String data = reader.readLine();

                while (data != null) {

                    String[] splitdata = data.split("\t");

                    TranscriptStructure structure = new TranscriptStructure();

                    structure.setPdbId(splitdata[1].split("\\|")[3]);
                    structure.setChain(splitdata[2].split(",")[0]);
                    structure.setFragmentStart(Integer.parseInt(splitdata[5]));
                    structure.setFragmentEnd(Integer.parseInt(splitdata[6]));
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

                    transcripts.put(Integer.parseInt(splitline[0]), transcript);

                    seqNumberToAccession.put(Integer.parseInt(splitline[0]),splitline[2]);

                    transcript.setSecretionStatus("enriched");

                    TranscriptsExpressableInSpecies transcriptsExpressableInSpecies = new TranscriptsExpressableInSpecies();

                    transcriptsExpressableInSpecies.setSpecies(pichiaPastoris);

                    transcriptsExpressableInSpecies.setTranscript(transcript);

                    transcript.getTranscriptsExpressableInSpecies().add(transcriptsExpressableInSpecies);

                    transcriptStructureMap.get(Integer.parseInt(splitline[0])).forEach(e -> {
                        TranscriptsFoundInStructure found = new TranscriptsFoundInStructure();
                        transcript.getFoundIn().add(found);
                        found.setTranscript(transcript);
                        found.setTranscriptstructure(e);
                    });

                    foldingMap.get(splitline[0]).forEach(e -> {
                        e.setTranscript(transcript);
                        //transcript.getEarlyFoldingLocations().add(e);
                    });

                    line = reader.readLine();

                }
            }

                try (LineNumberReader reader = new LineNumberReader(new FileReader(new File(args[0], "PPE_scan.txt")))) {

                    String line = reader.readLine();

                    while (line != null) {

                        String[] splitline = line.split("\t");

                        Transcript transcript = transcripts.get(Integer.parseInt(splitline[0].split("_")[1]));

                        Domain domain;

                        if (mappedDomains.containsKey(splitline[4])) {
                            domain = mappedDomains.get(splitline[4]);
                        } else {
                            domain = new Domain();
                            domain.setDomainAccession(splitline[4]);
                            mappedDomains.put(splitline[4], domain);
                        }

                        ProteinDomain proteinDomain = new ProteinDomain();

                        proteinDomain.setDomain(domain);
                        proteinDomain.setDomainStart(Integer.parseInt(splitline[6]));
                        proteinDomain.setDomainEnd(Integer.parseInt(splitline[7]));

                        transcriptsdomains.put(transcript,proteinDomain);
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
                    structure.setFragmentStart(Integer.parseInt(splitdata[5]));
                    structure.setFragmentEnd(Integer.parseInt(splitdata[6]));
                    structure.setIdentityScore(Double.parseDouble(splitdata[8]));
                    structure.setNumberOfMatchedResidues(Integer.parseInt(splitdata[9]));

                    transcriptStructureMap.put(Integer.parseInt(splitdata[0].split("Seq_")[1]), structure);

                    data = reader.readLine();

                }
            }

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

            try (LineNumberReader reader = new LineNumberReader(new FileReader(new File(args[0], "PPD_scan.txt")))) {

                String line = reader.readLine();

                while (line != null) {

                    String[] splitline = line.split("\t");

                    Transcript transcript = transcripts.get(Integer.parseInt(splitline[0].split("_")[1]));

                    Domain domain;

                    if (mappedDomains.containsKey(splitline[4])) {
                        domain = mappedDomains.get(splitline[4]);
                    } else {
                        domain = new Domain();
                        domain.setDomainAccession(splitline[4]);
                        mappedDomains.put(splitline[4], domain);
                    }

                    ProteinDomain proteinDomain = new ProteinDomain();

                    proteinDomain.setDomain(domain);
                    proteinDomain.setDomainStart(Integer.parseInt(splitline[6]));
                    proteinDomain.setDomainEnd(Integer.parseInt(splitline[7]));

                    transcriptsdomains.put(transcript,proteinDomain);
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

                    Transcript transcript = new Transcript(splitline[2], splitline[9], Integer.valueOf(splitline[7]), Integer.valueOf(splitline[8]), gene);

                    transcripts.put(Integer.parseInt(splitline[0]), transcript);

                    seqNumberToAccession.put(Integer.parseInt(splitline[0]),splitline[2]);

                    transcript.setSecretionStatus("depleted");

                    TranscriptsExpressableInSpecies transcriptsExpressableInSpecies = new TranscriptsExpressableInSpecies();

                    transcriptsExpressableInSpecies.setSpecies(pichiaPastoris);

                    transcriptsExpressableInSpecies.setTranscript(transcript);

                    transcript.getTranscriptsExpressableInSpecies().add(transcriptsExpressableInSpecies);


                    transcriptStructureMap.get(Integer.parseInt(splitline[0])).forEach(e -> {
                        TranscriptsFoundInStructure found = new TranscriptsFoundInStructure();
                        transcript.getFoundIn().add(found);
                        found.setTranscript(transcript);
                        found.setTranscriptstructure(e);
                    });

                    foldingMap.get(splitline[0]).forEach(e -> {
                        e.setTranscript(transcript);
                        //transcript.getEarlyFoldingLocations().add(e);
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
                    structure.setFragmentStart(Integer.parseInt(splitdata[5]));
                    structure.setFragmentEnd(Integer.parseInt(splitdata[6]));
                    structure.setIdentityScore(Double.parseDouble(splitdata[8]));
                    structure.setNumberOfMatchedResidues(Integer.parseInt(splitdata[9]));


                    transcriptStructureMap.put(Integer.parseInt(splitdata[0].split("Seq_")[1]), structure);
                }
            }


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

            try (LineNumberReader reader = new LineNumberReader(new FileReader(new File(args[0], "SCE_scan.txt")))) {

                String line = reader.readLine();

                while (line != null) {

                    String[] splitline = line.split("\t");

                    Transcript transcript = transcripts.get(Integer.parseInt(splitline[0].split("_")[1]));

                    Domain domain;

                    if (mappedDomains.containsKey(splitline[4])) {
                        domain = mappedDomains.get(splitline[4]);
                    } else {
                        domain = new Domain();
                        domain.setDomainAccession(splitline[4]);
                        mappedDomains.put(splitline[4], domain);
                    }

                    ProteinDomain proteinDomain = new ProteinDomain();

                    proteinDomain.setDomain(domain);
                    proteinDomain.setDomainStart(Integer.parseInt(splitline[6]));
                    proteinDomain.setDomainEnd(Integer.parseInt(splitline[7]));

                    transcriptsdomains.put(transcript,proteinDomain);
                    line = reader.readLine();
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

                    transcripts.put(Integer.parseInt(splitline[0]), transcript);

                    seqNumberToAccession.put(Integer.parseInt(splitline[0]),splitline[2]);

                    TranscriptsExpressableInSpecies transcriptsExpressableInSpecies = new TranscriptsExpressableInSpecies();

                    transcriptsExpressableInSpecies.setSpecies(saccharomyces);

                    transcriptsExpressableInSpecies.setTranscript(transcript);

                    transcript.getTranscriptsExpressableInSpecies().add(transcriptsExpressableInSpecies);

                    transcriptStructureMap.get(Integer.parseInt(splitline[0])).forEach(e -> {
                        TranscriptsFoundInStructure found = new TranscriptsFoundInStructure();
                        transcript.getFoundIn().add(found);
                        found.setTranscript(transcript);
                        found.setTranscriptstructure(e);
                    });

                    foldingMap.get(splitline[0]).forEach(e -> {
                        e.setTranscript(transcript);
                        //transcript.getEarlyFoldingLocations().add(e);
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
                    structure.setFragmentStart(Integer.parseInt(splitdata[5]));
                    structure.setFragmentEnd(Integer.parseInt(splitdata[6]));
                    structure.setIdentityScore(Double.parseDouble(splitdata[8]));
                    structure.setNumberOfMatchedResidues(Integer.parseInt(splitdata[9]));


                    transcriptStructureMap.put(Integer.parseInt(splitdata[0].split("Seq_")[1]), structure);
                }
            }



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

            try (LineNumberReader reader = new LineNumberReader(new FileReader(new File(args[0], "PPD_scan.txt")))) {

                String line = reader.readLine();

                while (line != null) {

                    String[] splitline = line.split("\t");

                    Transcript transcript = transcripts.get(Integer.parseInt(splitline[0].split("_")[1]));

                    Domain domain;

                    if (mappedDomains.containsKey(splitline[4])) {
                        domain = mappedDomains.get(splitline[4]);
                    } else {
                        domain = new Domain();
                        domain.setDomainAccession(splitline[4]);
                        mappedDomains.put(splitline[4], domain);
                    }

                    ProteinDomain proteinDomain = new ProteinDomain();

                    proteinDomain.setDomain(domain);
                    proteinDomain.setDomainStart(Integer.parseInt(splitline[6]));
                    proteinDomain.setDomainEnd(Integer.parseInt(splitline[7]));

                    transcriptsdomains.put(transcript,proteinDomain);
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

                    Transcript transcript = new Transcript(splitline[2], splitline[9], Integer.valueOf(splitline[3]), Integer.valueOf(splitline[4]), gene);

                    transcript.setSecretionStatus("depleted");

                    transcripts.put(Integer.parseInt(splitline[0]), transcript);
                    seqNumberToAccession.put(Integer.parseInt(splitline[0]),splitline[2]);

                    TranscriptsExpressableInSpecies transcriptsExpressableInSpecies = new TranscriptsExpressableInSpecies();

                    transcriptsExpressableInSpecies.setSpecies(pichiaPastoris);

                    transcriptsExpressableInSpecies.setTranscript(transcript);

                    transcript.getTranscriptsExpressableInSpecies().add(transcriptsExpressableInSpecies);


                    transcriptStructureMap.get(Integer.parseInt(splitline[0])).forEach(e -> {
                        TranscriptsFoundInStructure found = new TranscriptsFoundInStructure();
                        transcript.getFoundIn().add(found);
                        found.setTranscript(transcript);
                        found.setTranscriptstructure(e);
                    });

                    foldingMap.get(splitline[0]).forEach(e -> {
                        e.setTranscript(transcript);
                        //transcript.getEarlyFoldingLocations().add(e);
                    });

                    line = reader.readLine();
                }

                Map<String, Protein> mappedProteins = new HashMap<>();


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

                            seqNumberToAccession.entrySet().stream().filter(transcript -> transcript.getValue().equals(filteredlines[0])).forEach(filteredTranscript ->{
                                    TranscriptProtein product = new TranscriptProtein();
                            product.setProteinProduct(aProtein);
                            product.setTranscriptStart(Integer.parseInt(filteredlines[4]));
                            product.setTranscriptEnd(Integer.parseInt(filteredlines[5]));
                            product.setParentTranscript(transcripts.get(filteredTranscript.getKey()));

                            aProtein.getParentTranscripts().add(product);
                            });

                        });
                    }
                geneRepository.saveAll(referenceGenes.values());
                transcriptRepository.saveAll(transcripts.values());
                proteinRepository.saveAll(mappedProteins.values());
                //transcriptEarlyFoldingRepository.saveAll(foldingMap.values());
                }
        };
    }
;

}