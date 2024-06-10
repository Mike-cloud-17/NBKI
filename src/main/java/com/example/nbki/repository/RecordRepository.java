package com.example.nbki.repository;

import com.example.nbki.entity.MyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<MyRecord, Long> {
}
