package com.java.TekanaeWallet.Controller;

import com.java.TekanaeWallet.Model.Wallet;
import com.java.TekanaeWallet.Service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {


    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }


    @PostMapping("")
    public ResponseEntity<Wallet> createWallet(@Valid @RequestBody Wallet wallet) {
        Wallet createdWallet = walletService.createWallet(wallet);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWallet);
    }

    @GetMapping("")
    public ResponseEntity<List<Wallet>> getAllWallets() {
        List<Wallet> wallets = walletService.getAllWallets();
        return ResponseEntity.ok(wallets);
    }

    @PostMapping("/deposit/{walletId}")
    public ResponseEntity<Wallet> depositToWallet(@PathVariable Long walletId, @RequestParam double amount) {
        Wallet updatedWallet = walletService.depositToWallet(walletId, amount);
        return ResponseEntity.ok(updatedWallet);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Wallet>> getCustomerWallet(@PathVariable Long customerId) {
        List<Wallet> customerWallets = walletService.getCustomerWallets(customerId);
        return ResponseEntity.ok(customerWallets);
    }

    @GetMapping("/customer")
    public ResponseEntity<Wallet> getWalletByAccountNumber(@RequestParam("accountNumber") String accountNumber) {
        Wallet wallet = walletService.getWalletByAccountNumber(accountNumber);
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }
}
