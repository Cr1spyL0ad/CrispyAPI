package com.crispy.crispyapi.controller;

import com.crispy.crispyapi.dto.BoardDto;
import com.crispy.crispyapi.dto.ChangeNameAndColorRequest;
import com.crispy.crispyapi.dto.WorkspaceDto;
import com.crispy.crispyapi.model.Board;
import com.crispy.crispyapi.model.User;
import com.crispy.crispyapi.model.Workspace;
import com.crispy.crispyapi.service.BoardService;
import com.crispy.crispyapi.service.RoleService;
import com.crispy.crispyapi.service.WorkspaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/main")
public class BoardController {
    private final BoardService boardService;
    private final WorkspaceService workspaceService;
    private final RoleService roleService;
    @Autowired
    public BoardController(WorkspaceService workspaceService, BoardService boardService, RoleService roleService) {
        this.workspaceService = workspaceService;
        this.boardService = boardService;
        this.roleService = roleService;
    }


    @Operation(summary = "Создать доску", responses = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping("/workspaces/{workspaceId}/boards")
    public ResponseEntity<?> createBoard(@PathVariable Long workspaceId, @AuthenticationPrincipal User user, @RequestBody String boardName) {
        try {
            Workspace workspace = workspaceService.read(workspaceId);
            if (!roleService.isUserAdmin(user, workspace))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can't create board because you aren't admin");
            Board board = new Board(boardName, workspace);
            boardService.create(board);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @Operation(summary = "Возвращает доску", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content =
            @Content(schema = @Schema(type = "object", implementation = BoardDto.class))),

            @ApiResponse(responseCode = "400", description = "BAD REQUEST")
    })
    @GetMapping("/boards/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable Long boardId, @AuthenticationPrincipal User user) {
        try {
            Board board = boardService.read(boardId);
            if(!roleService.isUserExists(user, board.getWorkspace()))
                return ResponseEntity.badRequest().body("Can't find this user in this board");
            return ResponseEntity.ok(boardService.convertToDto(board));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @Operation(summary = "Удалить доску", responses = {
            @ApiResponse(responseCode = "200", description = "ОК"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId, @AuthenticationPrincipal User user) {
        try {
            if(!roleService.isUserAdmin(user, boardService.read(boardId).getWorkspace()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can't delete this board because you aren't admin");
            boardService.delete(boardId);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @Operation(summary = "Сменить имя доски", responses = {
            @ApiResponse(responseCode = "200", description = "ОК"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PatchMapping("/boards/{boardId}")
    public ResponseEntity<?> changeBoardName(@PathVariable long boardId, @AuthenticationPrincipal User user, @RequestBody ChangeNameAndColorRequest request) {
        try {
            Board board = boardService.read(boardId);
            if(!roleService.isUserAdmin(user, board.getWorkspace()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can't change name of this board because you aren't admin");
            if(!request.getName().isEmpty())
                board.setName(request.getName());
            if (!request.getColor().isEmpty())
                board.setColor(request.getColor());
            boardService.update(board);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }
}
