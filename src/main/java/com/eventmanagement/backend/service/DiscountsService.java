package com.eventmanagement.backend.service;

import com.eventmanagement.backend.entity.Discounts;
import com.eventmanagement.backend.repository.DiscountsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DiscountsService {
    @Autowired
    private DiscountsRepository discountsRepository;

    public Discounts getDiscountByCode(String code) {
        return (Discounts) discountsRepository.findByCode(code).orElse(null);
    }

    public BigDecimal applyDiscount(double basePrice, String discountCode) {
        if (discountCode == null || discountCode.isEmpty()) {
            return BigDecimal.valueOf(basePrice); // No discount applied
        }

        // Fetch the discount entity
        Discounts discount = getDiscountByCode(discountCode);
        if (discount == null) {
            return BigDecimal.valueOf(basePrice); // Invalid code, no discount
        }

        // Calculate the discounted price
        double discountPercentage = discount.getPercentage() / 100.0;
        double discountedPrice = basePrice - (basePrice * discountPercentage);

        return BigDecimal.valueOf(discountedPrice);
    }

    public boolean isDiscountValid(String code) {
        return discountsRepository.existsByCode(code);
    }

    public Discounts save(Discounts discount) {
        return discountsRepository.save(discount);
    }

    public List<Discounts> findAll() {
        return discountsRepository.findAll();
    }
}
