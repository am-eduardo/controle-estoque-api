package com.eduardo.controleestoque.service;

import com.eduardo.controleestoque.exception.InsufficientStockException;
import com.eduardo.controleestoque.model.MovementType;
import com.eduardo.controleestoque.model.Product;
import com.eduardo.controleestoque.model.StockMovement;
import com.eduardo.controleestoque.repository.ProductRepository;
import com.eduardo.controleestoque.repository.StockMovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class StockMovementService {

    private final StockMovementRepository movementRepository;
    private final ProductRepository productRepository;

    public StockMovementService(StockMovementRepository movementRepository, ProductRepository productRepository) {
        this.movementRepository = movementRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public StockMovement registerMovement(Long productId, MovementType type, java.math.BigDecimal quantity, String reason) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        if (type == MovementType.OUT) {
            if (product.getCurrentStock().compareTo(quantity) < 0) {
                throw new InsufficientStockException("Estoque insuficiente. Saldo atual: " + product.getCurrentStock());
            }
            product.setCurrentStock(product.getCurrentStock().subtract(quantity));
        } else {
            product.setCurrentStock(product.getCurrentStock().add(quantity));
        }

        productRepository.save(product);

        StockMovement movement = new StockMovement();
        movement.setProduct(product);
        movement.setMovementType(type);
        movement.setQuantity(quantity);
        movement.setMovementDate(LocalDateTime.now());
        movement.setReason(reason);

        return movementRepository.save(movement);
    }
}