package com.java.TekanaeWallet.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
@Table(name = "wallets")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false)
    private Long id;
    @NotNull(message = "Account number must not be null")
    @NotBlank(message = "Account number must not be empty")
    @Pattern(regexp = "\\d{5}", message = "Account number must contain exactly five digits")
    private String accountNumber;
    @DecimalMin(value = "0.01", message = "Balance must be greater than 0")
    private double balance;
    private String walletType;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @NotNull(message = "customer number must not be null")
    private Customer customer;
    private Date createdAt;
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }
    @NotNull
    private boolean status = true;
}
