package com.java.TekanaeWallet.Service;

import com.java.TekanaeWallet.Model.Wallet;

import java.util.List;

public interface WalletService {
    Wallet createWallet(Wallet wallet);
    List<Wallet> getAllWallets();
    Wallet depositToWallet(Long walletId, double amount);
    List<Wallet> getCustomerWallets(Long customerId);

    Wallet getWalletByAccountNumber(String accountNumber);

    void updateWallet(Wallet senderWallet);
}
