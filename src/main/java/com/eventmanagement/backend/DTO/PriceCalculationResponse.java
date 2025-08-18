package com.eventmanagement.backend.DTO;

import java.math.BigDecimal;

public class PriceCalculationResponse {
    private BigDecimal finalPrice;
    private BigDecimal subtotal;
    private BigDecimal eventCost;
    private BigDecimal venueCost;
    private BigDecimal totalBeforeDiscount;
    private Integer discountPercentage;

    public BigDecimal getFinalPrice() { return finalPrice; }
    public void setFinalPrice(BigDecimal finalPrice) { this.finalPrice = finalPrice; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getEventCost() { return eventCost; }
    public void setEventCost(BigDecimal eventCost) { this.eventCost = eventCost; }
    public BigDecimal getVenueCost() { return venueCost; }
    public void setVenueCost(BigDecimal venueCost) { this.venueCost = venueCost; }
    public BigDecimal getTotalBeforeDiscount() { return totalBeforeDiscount; }
    public void setTotalBeforeDiscount(BigDecimal totalBeforeDiscount) { this.totalBeforeDiscount = totalBeforeDiscount; }
    public Integer getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(Integer discountPercentage) { this.discountPercentage = discountPercentage; }
}
