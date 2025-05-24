package com.eventmanagement.backend.controller;

import com.eventmanagement.backend.entity.Discounts;
import com.eventmanagement.backend.repository.DiscountsRepository;
import com.eventmanagement.backend.service.DiscountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/discounts")
public class DiscountsController {

    @Autowired
    private DiscountsService discountsService;

    @Autowired
    private DiscountsRepository discountsRepository;

    @GetMapping("/validate/{code}")
    public ResponseEntity<String> validateDiscount(@PathVariable String code) {
        if (discountsService.isDiscountValid(code)) {
            return new ResponseEntity<>("Discount code is valid", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid discount code", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/apply")
    public ResponseEntity<BigDecimal> applyDiscount(@RequestParam double basePrice, @RequestParam String discountCode) {
        BigDecimal discountedPrice = discountsService.applyDiscount(basePrice, discountCode);
        return new ResponseEntity<>(discountedPrice, HttpStatus.OK);
    }
    @GetMapping("/{code}")
    public ResponseEntity<Object> getDiscountByCode(@PathVariable String code) {
        var discount = discountsService.getDiscountByCode(code);
        if (discount != null) {
            return new ResponseEntity<>(discount, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Discount not found", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("")
    public List<Discounts> getAllDiscounts() {
        return discountsService.findAll();
    }
    @PostMapping("")
    public Discounts createDiscount(@RequestBody Discounts discount) {
        // Optionally, add validation for unique code, valid percentage, etc.
        return discountsService.save(discount);
    }

    // Update discount percentage by code
    @PutMapping("/{code}")
    public ResponseEntity<Discounts> updateDiscount(@PathVariable String code, @RequestBody Map<String, Integer> body) {
        Optional<Object> optional =  discountsRepository.findByCode(code);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();
        Discounts discount = (Discounts) optional.get();
        Integer percentage = body.get("percentage");
        if (percentage == null) return ResponseEntity.badRequest().build();
        discount.setPercentage(percentage);
        discountsRepository.save(discount);
        return ResponseEntity.ok(discount);
    }

    // Delete discount by code
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteDiscount(@PathVariable String code) {
        Optional<Object> optional = discountsRepository.findByCode(code);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();
        discountsRepository.delete((Discounts) optional.get());
        return ResponseEntity.noContent().build();
    }

}
