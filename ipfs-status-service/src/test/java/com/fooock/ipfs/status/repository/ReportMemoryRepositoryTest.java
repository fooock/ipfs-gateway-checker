package com.fooock.ipfs.status.repository;

import com.fooock.ipfs.status.model.Report;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class ReportMemoryRepositoryTest {
    private ReportMemoryRepository reportMemoryRepository;

    @Before
    public void setUp() {
        reportMemoryRepository = new ReportMemoryRepository();
    }

    @After
    public void tearDown() {
        reportMemoryRepository = null;
    }

    @Test
    public void testSaveReports() {
        Report r1 = new Report();
        r1.setName("a");
        r1.setLastUpdate(123456789L);
        r1.setUrl("http://localhost1/ipfs");
        r1.setStatusCode(200);
        r1.setCors(Collections.emptyList());

        reportMemoryRepository.save(r1.getName(), r1);
        reportMemoryRepository.save(r1.getName(), r1);
        assertEquals(1, reportMemoryRepository.all().size());
    }

    @Test
    public void testGetOnline() {
        Report r1 = new Report();
        r1.setName("a");
        r1.setLastUpdate(123456789L);
        r1.setUrl("http://localhost1/ipfs");
        r1.setStatusCode(200);
        r1.setCors(Collections.emptyList());

        Report r2 = new Report();
        r2.setName("b");
        r2.setLastUpdate(123456789L);
        r2.setUrl("http://localhost1/ipfs");
        r2.setStatusCode(404);
        r2.setCors(Collections.emptyList());

        Report r3 = new Report();
        r3.setName("c");
        r3.setLastUpdate(123456789L);
        r3.setUrl("http://localhost1/ipfs");
        r3.setStatusCode(200);
        r3.setCors(Collections.emptyList());

        reportMemoryRepository.save(r1.getName(), r1);
        reportMemoryRepository.save(r2.getName(), r2);
        reportMemoryRepository.save(r3.getName(), r3);

        assertEquals(3, reportMemoryRepository.all().size());
        assertEquals(2, reportMemoryRepository.findOnline().size());
    }
}
