package com.TrollMarket.TrollMarket.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "Products")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "SellerId")
    private Seller seller;
    @ManyToOne
    @JoinColumn(name = "CategoryId")
    private Category category;
    @Column(name = "Name")
    private String name;
    @Column(name = "Price")
    private Double price;
    @Column(name = "Description")
    private String description;
    @Column(name = "Discountinue")
    private Boolean discontinue;

}
