package com.TrollMarket.TrollMarket.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class CartId implements Serializable {
    @Column(name = "ProductId")
    private Integer productId;
    @Column(name = "ShipmentId")
    private Integer shipmentId;
    @Column(name = "BuyerId")
    private Integer buyerId;
}
