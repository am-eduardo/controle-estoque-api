package com.eduardo.controleestoque.controller;

import com.eduardo.controleestoque.model.MovementType;
import com.eduardo.controleestoque.model.StockMovement;
import com.eduardo.controleestoque.service.StockMovementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/movements")
public class StockMovementController {

    private final StockMovementService movementService;

    public StockMovementController(StockMovementService movementService) {
        this.movementService = movementService;
    }

    public record MovementRequestDTO(Long productId, MovementType type, BigDecimal quantity, String reason) {}

    @PostMapping
    public ResponseEntity<StockMovement> registerMovement(@RequestBody MovementRequestDTO dto) {
        StockMovement savedMovement = movementService.registerMovement(
                dto.productId(),
                dto.type(),
                dto.quantity(),
                dto.reason()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovement);
    }
}