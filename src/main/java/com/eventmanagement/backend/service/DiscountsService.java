package com.eventmanagement.backend.service;

import com.eventmanagement.backend.entity.Discounts;
import com.eventmanagement.backend.repository.DiscountsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DiscountsService {
    @Autowired
    private DiscountsRepository discountsRepository;

    public Discounts getDiscountByCode(String code) {
        return (Discounts) discountsRepository.findById(code).orElse(null);
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
        return discountsRepository.existsById(code);
    }

}
