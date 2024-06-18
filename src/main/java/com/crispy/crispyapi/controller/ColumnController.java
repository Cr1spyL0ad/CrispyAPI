package com.crispy.crispyapi.controller;

import com.crispy.crispyapi.model.Board;
import com.crispy.crispyapi.model.Column;
import com.crispy.crispyapi.model.User;
import com.crispy.crispyapi.service.BoardService;
import com.crispy.crispyapi.service.ColumnService;
import com.crispy.crispyapi.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/main")
public class ColumnController {
    private final ColumnService columnService;
    private final BoardService boardService;
    private final RoleService roleService;
    @Autowired
    public ColumnController(ColumnService columnService, BoardService boardService, RoleService roleService) {
        this.columnService = columnService;
        this.boardService = boardService;
        this.roleService = roleService;
    }

    @Operation(summary = "Создать колонку", responses = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping("/boards/{boardId}/columns")
    public ResponseEntity<?> createColumn(@PathVariable long boardId, @AuthenticationPrincipal User user, @RequestBody String name) {
        try {
            Board board = boardService.read(boardId);
            if(!roleService.isUserAdmin(user, board.getWorkspace()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can't create column because you aren't admin");
            Column column = new Column(name, board, board.getColumns().size());
            columnService.create(column);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }


    @Operation(summary = "Удалить колонку", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @DeleteMapping("/columns/{columnId}")
    public ResponseEntity<?> deleteColumn(@PathVariable long columnId, @AuthenticationPrincipal User user) {
        try {
            if (!roleService.isUserAdmin(user, columnService.read(columnId).getBoard().getWorkspace()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can't delete this column because you aren't admin");
            columnService.delete(columnId);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }


    @Operation(summary = "Изменить имя колонки", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PatchMapping("/columns/{columnId}")
    public ResponseEntity<?> changeColumnName(@PathVariable long columnId, @AuthenticationPrincipal User user, @RequestBody String name) {
        try {
            Column column = columnService.read(columnId);
            if (!roleService.isUserAdmin(user, column.getBoard().getWorkspace()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can't change name of this column because you aren't admin");
            column.setName(name);
            columnService.update(column);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @Operation(summary = "Изменить положение колонки", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PutMapping("/columns/{columnId}/{newPosition}")
    public ResponseEntity<?> changeColumnPosition(@PathVariable long columnId, @PathVariable int newPosition, @AuthenticationPrincipal User user) {
        try {
            Column column = columnService.read(columnId);
            Board board = column.getBoard();
            if (!roleService.isUserAdmin(user, board.getWorkspace()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can't delete this column because you aren't admin");
            board.getColumns().get(newPosition).setPosition(column.getPosition());
            board.getColumns().remove(column);
            column.setPosition(newPosition);
            board.getColumns().add(column);
            boardService.update(board);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }


}
