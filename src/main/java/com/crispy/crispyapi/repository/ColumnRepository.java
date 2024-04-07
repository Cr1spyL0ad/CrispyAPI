package com.crispy.crispyapi.repository;

import com.crispy.crispyapi.model.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColumnRepository extends JpaRepository<Column, Long> {
    Optional<Column> findColumnById(Long id);
    Optional<Void> deleteColumnById(Long id);
}
