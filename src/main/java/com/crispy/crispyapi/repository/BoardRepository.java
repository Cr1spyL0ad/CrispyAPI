package com.crispy.crispyapi.repository;

import com.crispy.crispyapi.model.Board;
import com.crispy.crispyapi.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findBoardById(Long id);
    Optional<Void> deleteBoardById(Long id);
}
