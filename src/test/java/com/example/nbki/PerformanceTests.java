package com.example.nbki;

import com.example.nbki.entity.MyRecord;
import com.example.nbki.repository.RecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class PerformanceTests {

    @Autowired
    private RecordRepository recordRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void testCreate100kRecords() {
        long startTime = System.currentTimeMillis();

        IntStream.range(0, 100000).forEach(i -> {
            MyRecord myRecord = new MyRecord();
            myRecord.setData("Record " + i);
            recordRepository.save(myRecord);
        });

        long endTime = System.currentTimeMillis();
        System.out.println("Time taken to create 100k records: " + (endTime - startTime) + "ms");
    }

    @Test
    public void testSelect1MRecordsWith100Connections() {
        List<Long> times = Collections.synchronizedList(new ArrayList<>());

        IntStream.range(0, 100).parallel().forEach(i -> {
            IntStream.range(0, 10000).forEach(j -> {
                long startTime = System.currentTimeMillis();
                recordRepository.findById((long) (Math.random() * 100000));
                long endTime = System.currentTimeMillis();
                times.add(endTime - startTime);
            });
        });

        calculateStatistics(times);
    }

    private void calculateStatistics(List<Long> times) {
        Collections.sort(times);
        long total = times.stream().mapToLong(Long::longValue).sum();
        long count = times.size();
        double mean = (double) total / count;
        long median = times.get((int) (count / 2));
        long p95 = times.get((int) (count * 0.95));
        long p99 = times.get((int) (count * 0.99));

        System.out.println("Total time: " + total + "ms");
        System.out.println("Mean time: " + mean + "ms");
        System.out.println("Median time: " + median + "ms");
        System.out.println("95th percentile: " + p95 + "ms");
        System.out.println("99th percentile: " + p99 + "ms");
    }
}