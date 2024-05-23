package com.crispy.crispyapi.service;

import com.crispy.crispyapi.model.Board;
import com.crispy.crispyapi.model.Column;
import com.crispy.crispyapi.repository.ColumnRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ColumnService implements ServiceInterface<Column> {
    private final ColumnRepository columnRepository;
    public ColumnService(ColumnRepository columnRepository) {
        this.columnRepository = columnRepository;
    }
    @Override
    public boolean create(Column column) {
        columnRepository.save(column);
        return true;
    }

    @Override
    public Column read(Long id) throws Exception {
        return columnRepository.findColumnById(id).orElseThrow(() -> new Exception("Column not found"));
    }

    public Column read(Board board, int position) throws Exception {
        return columnRepository.findColumnByBoardAndPosition(board, position).orElseThrow(() -> new Exception("Column not found"));
    }

    @Override
    public boolean update(Column column) {
        try {
            columnRepository.save(column);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean delete(Long id) throws Exception {
        columnRepository.deleteColumnById(id).orElseThrow(() -> new Exception("Column not found"));
        return true;
    }
}
