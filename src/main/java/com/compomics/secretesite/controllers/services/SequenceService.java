package com.compomics.secretesite.controllers.services;

import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by davy on 4/19/2017.
 */
@Service
public class SequenceService {

    /**
     * hashmap containing all non stop codons and their resulting amino acid after translating
     */
    private static final Map<String,String> DNA_TO_PROTEIN = new HashMap(){
        {
            put("ATT","I");
            put("ATC","I");
            put("ATA","I");
            put("CTT","L");
            put("CTC","L");
            put("CTA","L");
            put("CTG","L");
            put("TTA","L");
            put("TTG","L");
            put("GTT","V");
            put("GTC","V");
            put("GTA","V");
            put("GTG","V");
            put("TTT","F");
            put("TTC","F");
            put("ATG","M");
            put("TGT","C");
            put("TGC","C");
            put("GCT","A");
            put("GCC","A");
            put("GCA","A");
            put("GCG","A");
            put("GGT","G");
            put("GGC","G");
            put("GGA","G");
            put("GGG","G");
            put("CCT","P");
            put("CCC","P");
            put("CCA","P");
            put("CCG","P");
            put("ACT","T");
            put("ACC","T");
            put("ACA","T");
            put("ACG","T");
            put("TCT","S");
            put("TCC","S");
            put("TCA","S");
            put("TCG","S");
            put("AGT","S");
            put("AGC","S");
            put("TAT","Y");
            put("TAC","Y");
            put("TGG","W");
            put("CAA","Q");
            put("CAG","Q");
            put("AAT","N");
            put("AAC","N");
            put("CAT","H");
            put("CAC","H");
            put("GAA","E");
            put("GAG","E");
            put("GAT","D");
            put("GAC","D");
            put("AAA","K");
            put("AAG","K");
            put("CGT","R");
            put("CGC","R");
            put("CGA","R");
            put("CGG","R");
            put("AGA","R");
            put("AGG","R");
        }
    };

    /**
     * translates a DNA sequence to its protein product.
     * @param dnaSequence the DNA sequence to translate
     * @throws IllegalArgumentException if the passed sequence does not consist of triplets or contains characters that are not in [ATGC]
     * @return the translated protein product
     */
    public String translateDNAtoProtein(String dnaSequence) throws IllegalArgumentException{
        if (dnaSequence.length()%3 != 0){
            throw new IllegalArgumentException("sequence does not consist of triplets");
        }
        if(!dnaSequence.matches("^[ATGC]+$")){
            throw new IllegalArgumentException("sequence does not consist of only A T G C");
        }
        return ListUtils.partition(Arrays.asList(dnaSequence.split("")),3).stream()
                .map(e ->String.join("",e))
                .map(DNA_TO_PROTEIN::get)
                .collect(Collectors.joining());
        }

    Map<String,String> getDnaToProteinMap(){
        return Collections.unmodifiableMap(DNA_TO_PROTEIN);
    }

}
