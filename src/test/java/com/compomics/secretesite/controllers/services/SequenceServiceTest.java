package com.compomics.secretesite.controllers.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by davy on 4/19/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SequenceServiceTest {

    @Autowired
    private SequenceService sequenceService;

    @Test
    public void translateDNAtoProtein() throws Exception {
        assertThat(sequenceService.translateDNAtoProtein("ATTATCATACTTCTCCTACTGTTATTGGTTGTCGTAGTGTTTTTCATGTGTTGCG" +
                "CTGCCGCAGCGGGTGGCGGAGGGCCTCCCCCACCGACTACCACAACGTCTTCCTCATCGAGTAGCTATTA" +
                "CTGGCAACAGAATAACCATCACGAAGAGGATGACAAAAAGCGTCGCCGACGGAGAAGG"),is(equalTo("IIILLLLLLVVVVFFMCCAAAAGGGGPPPPTTTTSSSSSSYYWQQNNHHEEDDKKRRRRRR")));
    }

    @Test
    public void checkDNATOPROTEINConsistency() throws Exception {
        //0-63 - 3 stop codons
        assertThat(sequenceService.getDnaToProteinMap().size(),is(equalTo(61)));
    }

}