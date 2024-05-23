package com.crispy.crispyapi.repository;

import com.crispy.crispyapi.model.Board;
import com.crispy.crispyapi.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findCardById(Long id);
    Optional<Void> deleteCardById(Long id);
}
