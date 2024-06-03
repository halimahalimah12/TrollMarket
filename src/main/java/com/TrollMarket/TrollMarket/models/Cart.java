package com.TrollMarket.TrollMarket.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "Carts")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @EmbeddedId
    private CartId id;
    @Column(name = "Quantity")
    private Integer quantity;
    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "ProductId", insertable = false, updatable = false)
    private Product product;
    @ManyToOne
    @MapsId("shipmentId")
    @JoinColumn(name = "ShipmentId", insertable = false, updatable = false)
    private Shipment shipment;
    @ManyToOne
    @MapsId("buyerId")
    @JoinColumn(name = "BuyerId", insertable = false, updatable = false)
    private Buyer buyer;
}
