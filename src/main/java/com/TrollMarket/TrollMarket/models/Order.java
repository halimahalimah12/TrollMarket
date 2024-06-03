package com.TrollMarket.TrollMarket.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Orders")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "ProductId")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "ShipmentId")
    private Shipment shipment;
    @ManyToOne
    @JoinColumn(name = "BuyerId")
    private Buyer buyer;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "UnitPrice")
    private Double unitPrice;
    @Column(name = "Freight")
    private Double freight;
    @Column(name = "OrderDate")
    private LocalDate orderDate;


}
