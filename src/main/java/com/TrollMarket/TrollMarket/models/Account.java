package com.TrollMarket.TrollMarket.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "Accounts")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @Column(name = "Username")
    private String username;
    @Column(name = "Password")
    private String password;


}
