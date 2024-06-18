package com.crispy.crispyapi.controller;

import com.crispy.crispyapi.dto.BoardDto;
import com.crispy.crispyapi.dto.CardDto;
import com.crispy.crispyapi.dto.CreateCardRequest;
import com.crispy.crispyapi.model.Card;
import com.crispy.crispyapi.model.Column;
import com.crispy.crispyapi.model.User;
import com.crispy.crispyapi.service.CardService;
import com.crispy.crispyapi.service.ColumnService;
import com.crispy.crispyapi.service.RoleService;
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
public class CardController {
    private final CardService cardService;
    private final RoleService roleService;
    private final ColumnService columnService;
    @Autowired
    public CardController(CardService cardService, RoleService roleService, ColumnService columnService) {
        this.cardService = cardService;
        this.roleService = roleService;
        this.columnService = columnService;
    }

    @Operation(summary = "Создать карту", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping("/columns/{columnId}/cards")
    public ResponseEntity<?> createCard(@PathVariable long columnId, @AuthenticationPrincipal User user, @RequestBody CreateCardRequest request) {
        try {
            Column column = columnService.read(columnId);
            if(!roleService.isUserExists(user, column.getBoard().getWorkspace()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can't create card because you doesn't exist in this workspace");
            Card card = new Card();
            card.setColumn(column);
            card.setUser(user);
            cardService.create(card, request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @Operation(summary = "Возвращает карту", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content =
                @Content(schema = @Schema(type = "object", implementation = CardDto.class))),

            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")
    })
    @GetMapping("/cards/{cardId}")
    public ResponseEntity<?> getCard(@PathVariable long cardId, @AuthenticationPrincipal User user) {
        try {
            Card card = cardService.read(cardId);
            if(!roleService.isUserExists(user, card.getColumn().getBoard().getWorkspace()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can't get this card because you doesn't exist in this workspace");
            return ResponseEntity.ok().body(cardService.convertToDto(card));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @Operation(summary = "Удалить карту", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<?> deleteCard(@PathVariable long cardId, @AuthenticationPrincipal User user) {
        try {
            Card card = cardService.read(cardId);
            if(!(roleService.isUserAdmin(user, card.getColumn().getBoard().getWorkspace())) && (card.getUser() == user))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can't delete this card");
            cardService.delete(cardId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @Operation(summary = "Перенести карту в следующую колонну", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PatchMapping("/cards/{cardId}")
    public ResponseEntity<?> moveCardToNextColumn(@PathVariable long cardId, @AuthenticationPrincipal User user) {
        try {
            Card card = cardService.read(cardId);
            if(!roleService.isUserExists(user, card.getColumn().getBoard().getWorkspace()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can't move this card because you doesn't exist in this workspace");
            card.setColumn(columnService.read(card.getColumn().getBoard(), card.getColumn().getPosition() + 1));
            cardService.update(card);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }
}
