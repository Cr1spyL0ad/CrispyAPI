package com.crispy.crispyapi.service;

import com.crispy.crispyapi.dto.BoardDto;
import com.crispy.crispyapi.model.Board;
import com.crispy.crispyapi.model.Column;
import com.crispy.crispyapi.repository.BoardRepository;
import com.crispy.crispyapi.repository.ColumnRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService implements ServiceInterface<Board> {

    private final BoardRepository boardRepository;
    private final ColumnRepository columnRepository;
    @Autowired
    public BoardService(BoardRepository boardRepository, ColumnRepository columnRepository) {
        this.boardRepository = boardRepository;
        this.columnRepository = columnRepository;
    }
    @Override
    @Transactional
    public boolean create(Board board) {
        try {
            boardRepository.save(board);
            board.getColumns().add(columnRepository.save(new Column("Done", board, 0)));
            board.getColumns().add(columnRepository.save(new Column("Doing", board, 1)));
            board.getColumns().add(columnRepository.save(new Column("To do", board, 2)));
            boardRepository.save(board);
            return true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Board read(Long id) throws Exception {
        return boardRepository.findBoardById(id).orElseThrow(() -> new Exception("Board not found"));
    }

    @Override
    public boolean update(Board board) {
        try {
            boardRepository.save(board);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public boolean delete(Long id) throws Exception {
        boardRepository.deleteBoardById(id).orElseThrow(() -> new Exception("Board not found"));
        return true;
    }

    public BoardDto convertToDto(Board board) {
        BoardDto boardDto = new BoardDto();
        boardDto.setId(board.getId());
        boardDto.setName(board.getName());
        boardDto.setWorkspaceId(board.getWorkspace().getId());
        board.getColumns().forEach(column -> {
            BoardDto.BoardColumnDto columnDto = new BoardDto.BoardColumnDto(column.getId(), column.getName());
            column.getCards().forEach(card -> columnDto.getCards().add(new BoardDto.BoardCardDto(card.getId(), card.getName())));
            boardDto.getColumns().add(columnDto);
        });
        return boardDto;
    }
}
