package com.compomics.secretesite;


import com.compomics.secretesite.controllers.IndexController;
import com.compomics.secretesite.domain.*;
import com.compomics.secretesite.domain.repositories.*;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

//@SpringBootApplication
//@EnableAsync
public class SecreteSiteApplicationCopy {

    private SpeciesRepository speciesRepository;
    private GeneRepository geneRepository;
    private ProteinRepository proteinRepository;
    private TranscriptEarlyFoldingRepository transcriptEarlyFoldingRepository;
    private IndexController indexController;
    private TranscriptRepository transcriptRepository;
    private DomainRepository domainRepository;

    public SecreteSiteApplicationCopy(SpeciesRepository speciesRepository, GeneRepository geneRepository,
                                      ProteinRepository proteinRepository, TranscriptEarlyFoldingRepository transcriptEarlyFoldingRepository,
                                      IndexController indexController, TranscriptRepository transcriptRepository,
                                      DomainRepository domainRepository) {
        this.speciesRepository = speciesRepository;
        this.geneRepository = geneRepository;
        this.proteinRepository = proteinRepository;
        this.transcriptEarlyFoldingRepository = transcriptEarlyFoldingRepository;
        this.indexController = indexController;
        this.transcriptRepository = transcriptRepository;
        this.domainRepository = domainRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SecreteSiteApplicationCopy.class, args);
    }


    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {

            Map<String, Gene> referenceGenes = new HashMap<>();

            Map<String, Domain> mappedDomains = new HashMap<>();

            Map<String, List<Transcript>> transcriptAccessions = new HashMap<>();

            DualHashBidiMap<Integer, String> seqNumberToAccession = new DualHashBidiMap<>();

            Species pichiaPastoris = new Species(4922, "Pichia pastoris");

            speciesRepository.save(pichiaPastoris);

            Map<String, List<String>> uniprotTrembl = new HashMap<>();

            try (Stream<String> mylines = Files.lines(Paths.get(args[0], "uniprot-trembl.tab"))) {
                mylines.map(martline -> martline.split("\t")).forEach(filteredlines -> {

                    List<String> nameLabel = new ArrayList<>();
                    int iend = filteredlines[2].indexOf("(");
                    if(iend != -1){
                        nameLabel.add(filteredlines[2].substring(0 , iend));
                    }else{
                        nameLabel.add(filteredlines[2]);
                    }

                    nameLabel.add(filteredlines[3]);

                    uniprotTrembl.put(filteredlines[0], nameLabel);

                });
            }

            Map<String, List<String>> uniprotSwiss = new HashMap<>();

            try (Stream<String> mylines = Files.lines(Paths.get(args[0], "uniprot-swiss.tab"))) {
                mylines.map(martline -> martline.split("\t")).forEach(filteredlines -> {

                    List<String> nameLabel = new ArrayList<>();
                    int iend = filteredlines[2].indexOf("(");
                    if(iend != -1){
                        nameLabel.add(filteredlines[2].substring(0 , iend));
                    }else{
                        nameLabel.add(filteredlines[2]);
                    }

                    nameLabel.add(filteredlines[3]);

                    uniprotSwiss.put(filteredlines[0], nameLabel);

                });
            }

            Map<String, String> pfam = new HashMap<>();

            try (Stream<String> mylines = Files.lines(Paths.get(args[0], "pfam.tsv"))) {
                mylines.map(martline -> martline.split("\t")).forEach(filteredlines -> {

                    pfam.put(filteredlines[0], filteredlines[4]);

                });
            }
            // key ensemble protein accession, value protein
            Map<String, Protein> mappedProteins = new HashMap<>();

            try (Stream<String> mylines = Files.lines(Paths.get(args[0], "protein_mapping.txt"))) {
                mylines.map(martline -> martline.split("\t")).forEach(filteredlines -> {
                    Protein aProtein = new Protein();

                    if (filteredlines.length == 3) {
                        aProtein.setProteinEnsemblAccession(filteredlines[0]);
                        aProtein.setSwissProtAccession(filteredlines[1]);
                        if(filteredlines[1] != null && !filteredlines[1].equals("") && uniprotSwiss.containsKey(filteredlines[1])){
                            aProtein.setSwissProtName(uniprotSwiss.get(filteredlines[1]).get(0));
                            aProtein.setSwissProtLabel(uniprotSwiss.get(filteredlines[1]).get(1));
                        }
                        aProtein.setTrEmblAccession(filteredlines[2]);
                        if(filteredlines[2] != null && !filteredlines[2].equals("") && uniprotTrembl.containsKey(filteredlines[2])){
                            aProtein.setTrEmblName(uniprotTrembl.get(filteredlines[2]).get(0));
                            aProtein.setTrEmblLabel(uniprotTrembl.get(filteredlines[2]).get(1));
                        }

                    }else if(filteredlines.length == 2){
                        aProtein.setProteinEnsemblAccession(filteredlines[0]);
                        aProtein.setSwissProtAccession(filteredlines[1]);
                        if(filteredlines[1] != null && !filteredlines[1].equals("") && uniprotSwiss.containsKey(filteredlines[1])){
                            aProtein.setSwissProtName(uniprotSwiss.get(filteredlines[1]).get(0));
                            aProtein.setSwissProtLabel(uniprotSwiss.get(filteredlines[1]).get(1));
                        }

                    }else{
                        aProtein.setProteinEnsemblAccession(filteredlines[0]);
                    }
                    mappedProteins.put(filteredlines[0], aProtein);
                });
            }

            System.out.println("protein is done" + mappedProteins.size());




            ArrayListValuedHashMap<Integer, TranscriptStructure> transcriptStructureMap = new ArrayListValuedHashMap<>();
            HashMap<Integer, Transcript> transcriptsPP = new HashMap<>();
            ArrayListValuedHashMap<Transcript, ProteinDomain> transcriptsdomains = new ArrayListValuedHashMap<>();

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

                    Transcript transcript = new Transcript(splitline[2], splitline[9], gene);
                    if(transcriptAccessions.containsKey(transcript.getEnsembleTranscriptAccession())){
                        transcriptAccessions.get(transcript.getEnsembleTranscriptAccession()).add(transcript);
                    }else{
                        transcriptAccessions.put(transcript.getEnsembleTranscriptAccession(), new ArrayList<Transcript>(Arrays.asList(transcript)));
                    }

                    transcriptsPP.put(Integer.parseInt(splitline[0]), transcript);

                    seqNumberToAccession.put(Integer.parseInt(splitline[0]), splitline[2]);

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

                    Transcript transcript = transcriptsPP.get(Integer.parseInt(splitline[0].split("_")[1]));

                    Domain domain;

                    if (mappedDomains.containsKey(splitline[4])) {
                        domain = mappedDomains.get(splitline[4]);
                    } else {
                        domain = new Domain();
                        domain.setDomainAccession(splitline[4]);
                        domain.setDomainName(pfam.get(splitline[4]));
                        mappedDomains.put(splitline[4], domain);
                    }

                    ProteinDomain proteinDomain = new ProteinDomain();

                    proteinDomain.setDomain(domain);
                    proteinDomain.setDomainStart(Integer.parseInt(splitline[6]));
                    proteinDomain.setDomainEnd(Integer.parseInt(splitline[7]));

                    transcriptsdomains.put(transcript, proteinDomain);
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

                    Transcript transcript = transcriptsPP.get(Integer.parseInt(splitline[0].split("_")[1]));

                    Domain domain;

                    if (mappedDomains.containsKey(splitline[4])) {
                        domain = mappedDomains.get(splitline[4]);
                    } else {
                        domain = new Domain();
                        domain.setDomainAccession(splitline[4]);
                        domain.setDomainName(pfam.get(splitline[4]));
                        mappedDomains.put(splitline[4], domain);
                    }

                    ProteinDomain proteinDomain = new ProteinDomain();

                    proteinDomain.setDomain(domain);
                    proteinDomain.setDomainStart(Integer.parseInt(splitline[6]));
                    proteinDomain.setDomainEnd(Integer.parseInt(splitline[7]));

                    transcriptsdomains.put(transcript, proteinDomain);
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

                    Transcript transcript = new Transcript(splitline[2], splitline[9], gene);
                    if(transcriptAccessions.containsKey(transcript.getEnsembleTranscriptAccession())){
                        transcriptAccessions.get(transcript.getEnsembleTranscriptAccession()).add(transcript);
                    }else{
                        transcriptAccessions.put(transcript.getEnsembleTranscriptAccession(), new ArrayList<Transcript>(Arrays.asList(transcript)));
                    }

                    transcriptsPP.put(Integer.parseInt(splitline[0]), transcript);

                    seqNumberToAccession.put(Integer.parseInt(splitline[0]), splitline[2]);

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

            HashMap<Integer, Transcript> transcriptsSC = new HashMap<>();

            transcriptStructureMap = new ArrayListValuedHashMap<>();


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

                    Transcript transcript = new Transcript(splitline[2], splitline[9], gene);

                    transcript.setSecretionStatus("enriched");

                    if(transcriptAccessions.containsKey(transcript.getEnsembleTranscriptAccession())){
                        transcriptAccessions.get(transcript.getEnsembleTranscriptAccession()).add(transcript);
                    }else{
                        transcriptAccessions.put(transcript.getEnsembleTranscriptAccession(), new ArrayList<Transcript>(Arrays.asList(transcript)));
                    }
                    transcriptsSC.put(Integer.parseInt(splitline[0]), transcript);

                    seqNumberToAccession.put(Integer.parseInt(splitline[0]), splitline[2]);

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

                    Transcript transcript = transcriptsSC.get(Integer.parseInt(splitline[0].split("_")[1]));

                    Domain domain;

                    if (mappedDomains.containsKey(splitline[4])) {
                        domain = mappedDomains.get(splitline[4]);
                    } else {
                        domain = new Domain();
                        domain.setDomainAccession(splitline[4]);
                        domain.setDomainName(pfam.get(splitline[4]));
                        mappedDomains.put(splitline[4], domain);
                    }

                    ProteinDomain proteinDomain = new ProteinDomain();

                    proteinDomain.setDomain(domain);
                    proteinDomain.setDomainStart(Integer.parseInt(splitline[6]));
                    proteinDomain.setDomainEnd(Integer.parseInt(splitline[7]));

                    transcriptsdomains.put(transcript, proteinDomain);
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

                    Transcript transcript = transcriptsPP.get(Integer.parseInt(splitline[0].split("_")[1]));

                    Domain domain;

                    if (mappedDomains.containsKey(splitline[4])) {
                        domain = mappedDomains.get(splitline[4]);
                    } else {
                        domain = new Domain();
                        domain.setDomainAccession(splitline[4]);
                        domain.setDomainName(pfam.get(splitline[4]));
                        mappedDomains.put(splitline[4], domain);
                    }

                    ProteinDomain proteinDomain = new ProteinDomain();

                    proteinDomain.setDomain(domain);
                    proteinDomain.setDomainStart(Integer.parseInt(splitline[6]));
                    proteinDomain.setDomainEnd(Integer.parseInt(splitline[7]));

                    transcriptsdomains.put(transcript, proteinDomain);
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

                    Transcript transcript = new Transcript(splitline[2], splitline[9], gene);

                    transcript.setSecretionStatus("depleted");
                    if(transcriptAccessions.containsKey(transcript.getEnsembleTranscriptAccession())){
                        transcriptAccessions.get(transcript.getEnsembleTranscriptAccession()).add(transcript);
                    }else{
                        transcriptAccessions.put(transcript.getEnsembleTranscriptAccession(), new ArrayList<Transcript>(Arrays.asList(transcript)));
                    }
                    transcriptsSC.put(Integer.parseInt(splitline[0]), transcript);
                    seqNumberToAccession.put(Integer.parseInt(splitline[0]), splitline[2]);

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
            int groupcounter = 0;
            List<String> ppClusterFiles = new ArrayList<String>() {{
                this.add("PpD_Cluster_info.txt");
                this.add("PpE_cluster_info.txt");
            }};

            for(String filename :  ppClusterFiles){

                try (LineNumberReader reader = new LineNumberReader(new FileReader(new File(args[0], filename)))) {

                    String line = reader.readLine();

                    line = reader.readLine();



                    while (line != null) {

                        String[] lines = line.split("\t");

                        if (lines.length > 1) {

                            Integer tempcounter = groupcounter;
                            Transcript transcript = transcriptsPP.get(Integer.parseInt(lines[0].split("Seq_")[1]));

                            TranscriptCluster cluster = new TranscriptCluster();

                            cluster.setTranscriptClusterMember(transcript);

                            cluster.setIsTranscriptRepresentative(1);

                            transcript.getTranscriptCluster().add(cluster);

                            cluster.setTranscriptClusterGroupId(tempcounter);

                            for(String sequences : lines[1].split(",")){
                                TranscriptCluster internalcluster = new TranscriptCluster();

                                internalcluster.setTranscriptClusterMember(transcriptsPP.get(Integer.parseInt(sequences.split("Seq_")[1])));
                                internalcluster.setIsTranscriptRepresentative(0);
                                internalcluster.setTranscriptClusterGroupId(tempcounter);
                                transcriptsPP.get(Integer.parseInt(sequences.split("Seq_")[1])).getTranscriptCluster().add(internalcluster);
                                transcript.getTranscriptCluster().add(internalcluster);

                            }
                            groupcounter++;
                        }
                        line = reader.readLine();

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            List<String> scClusterFiles = new ArrayList<String>() {{
                this.add("ScD_Cluster_info.txt");
                this.add("ScE_Cluster_info.txt");
            }};

            for(String filename :  scClusterFiles){

                try (LineNumberReader reader = new LineNumberReader(new FileReader(new File(args[0], filename)))) {

                    String line = reader.readLine();

                    line = reader.readLine();



                    while (line != null) {

                        String[] lines = line.split("\t");

                        if (lines.length > 1) {

                            Integer tempcounter = groupcounter;
                            Transcript transcript = transcriptsSC.get(Integer.parseInt(lines[0].split("Seq_")[1]));

                            TranscriptCluster cluster = new TranscriptCluster();

                            cluster.setTranscriptClusterMember(transcript);

                            cluster.setIsTranscriptRepresentative(1);

                            transcript.getTranscriptCluster().add(cluster);

                            cluster.setTranscriptClusterGroupId(tempcounter);

                            for(String sequences : lines[1].split(",")){
                                TranscriptCluster internalcluster = new TranscriptCluster();

                                internalcluster.setTranscriptClusterMember(transcriptsSC.get(Integer.parseInt(sequences.split("Seq_")[1])));
                                internalcluster.setIsTranscriptRepresentative(0);
                                internalcluster.setTranscriptClusterGroupId(tempcounter);
                                transcriptsSC.get(Integer.parseInt(sequences.split("Seq_")[1])).getTranscriptCluster().add(internalcluster);
                                transcript.getTranscriptCluster().add(internalcluster);

                            }
                            groupcounter++;
                        }
                        line = reader.readLine();

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Map<Transcript, List<TranscriptProtein>> mappedTranscriptProteins = new HashMap<>();

            try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(args[0], "mart_export_transcript_protein.txt"))) {
                //start reading the file
                final StringBuilder sequenceBuilder = new StringBuilder();
                String transAcc = "";
                String protAcc = "";
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if(!line.startsWith(">")){
                        sequenceBuilder.append(line);
                    }
                    System.out.println(line.split("\\|").length);
                    if(line.startsWith(">") && line.split("\\|").length == 3){

                        //add limiting check for protein store to avoid growing
                        if (sequenceBuilder.length() > 0) {
                            if(transcriptAccessions.containsKey(transAcc)){
                                for(Transcript tr : transcriptAccessions.get(transAcc)){
                                    TranscriptProtein transcriptProtein = new TranscriptProtein();
                                    transcriptProtein.setParentTranscript(tr);
                                    int startIndex = sequenceBuilder.toString().indexOf(tr.getTranscriptSequence());
                                    transcriptProtein.setTranscriptStart(startIndex);
                                    transcriptProtein.setTranscriptEnd(startIndex + tr.getTranscriptSequence().length());
                                    transcriptProtein.setProteinStableId(protAcc);

                                    if(mappedTranscriptProteins.containsKey(tr)){
                                        mappedTranscriptProteins.get(tr).add(transcriptProtein);
                                    }else{
                                        mappedTranscriptProteins.put(tr, new ArrayList<TranscriptProtein>(Arrays.asList(transcriptProtein)));
                                    }

                                }
                            }

                            sequenceBuilder.setLength(0);
                        }
                        transAcc = line.split("\\|")[1];
                        protAcc = line.split("\\|")[2];
                    }else{
                        continue;
                    }


                }
                //last line
                if (sequenceBuilder.length() > 0) {
                    if(transcriptAccessions.containsKey(transAcc)){
                        for(Transcript tr : transcriptAccessions.get(transAcc)){
                            TranscriptProtein transcriptProtein = new TranscriptProtein();
                            transcriptProtein.setParentTranscript(tr);
                            int startIndex = sequenceBuilder.toString().indexOf(tr.getTranscriptSequence());
                            transcriptProtein.setTranscriptStart(startIndex);
                            transcriptProtein.setTranscriptEnd(startIndex + tr.getTranscriptSequence().length());
                            transcriptProtein.setProteinStableId(protAcc);

                            if(mappedTranscriptProteins.containsKey(tr)){
                                mappedTranscriptProteins.get(tr).add(transcriptProtein);
                            }else{
                                mappedTranscriptProteins.put(tr, new ArrayList<TranscriptProtein>(Arrays.asList(transcriptProtein)));
                            }
                        }
                    }

                    sequenceBuilder.setLength(0);
                }
            }

            Set<Transcript> allTranscripts = new HashSet<>();
            allTranscripts.addAll(transcriptsPP.values());
            allTranscripts.addAll(transcriptsSC.values());
            transcriptsPP.values().forEach(transcript -> {
                if(mappedTranscriptProteins.containsKey(transcript)){
                    transcript.getTranscriptProteins().addAll(mappedTranscriptProteins.get(transcript));
                }

            });

            transcriptsSC.values().forEach(transcript -> {
                if(mappedTranscriptProteins.containsKey(transcript)){
                    transcript.getTranscriptProteins().addAll(mappedTranscriptProteins.get(transcript));
                }
            });


            try (Stream<String> mylines = Files.lines(Paths.get(args[0], "mart_export_domains.txt"))) {
                mylines.map(martline -> martline.split("\t")).forEach(filteredlines -> {
                    Domain aDomain;
                    if (filteredlines.length > 2) {
                        if (mappedDomains.containsKey(filteredlines[1])) {
                            aDomain = mappedDomains.get(filteredlines[1]);
                        } else {
                            aDomain = new Domain();
                            aDomain.setDomainAccession(filteredlines[1]);
                            aDomain.setDomainName(pfam.get(filteredlines[1]));
                            mappedDomains.put(filteredlines[1], aDomain);
                        }
                        ProteinDomain aProteinDomain = new ProteinDomain();


                        aProteinDomain.setDomain(aDomain);
                        aProteinDomain.setProteinStableId(filteredlines[0]);
                        aProteinDomain.setDomainEnd(Integer.parseInt(filteredlines[3]));
                        aProteinDomain.setDomainStart(Integer.parseInt(filteredlines[2]));

                        aDomain.getProteinDomains().add(aProteinDomain);
                    }
                });
            }



            geneRepository.saveAll(referenceGenes.values());
            proteinRepository.saveAll(mappedProteins.values());
            transcriptRepository.saveAll(allTranscripts);
            domainRepository.saveAll(mappedDomains.values());
            //transcriptEarlyFoldingRepository.saveAll(foldingMap.values());
        };
    }

}