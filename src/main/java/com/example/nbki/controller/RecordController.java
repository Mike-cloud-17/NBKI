package com.example.nbki.controller;

import com.example.nbki.entity.MyRecord;
import com.example.nbki.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/records")
public class RecordController {

    @Autowired
    private RecordRepository recordRepository;

    @PostMapping
    public MyRecord createRecord(@RequestBody MyRecord record) {
        return recordRepository.save(record);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MyRecord> getRecordById(@PathVariable Long id) {
        Optional<MyRecord> record = recordRepository.findById(id);
        return record.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MyRecord> updateRecord(@PathVariable Long id, @RequestBody MyRecord recordDetails) {
        Optional<MyRecord> record = recordRepository.findById(id);
        if (record.isPresent()) {
            MyRecord existingRecord = record.get();
            existingRecord.setData(recordDetails.getData());
            return ResponseEntity.ok(recordRepository.save(existingRecord));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        Optional<MyRecord> record = recordRepository.findById(id);
        if (record.isPresent()) {
            recordRepository.delete(record.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
