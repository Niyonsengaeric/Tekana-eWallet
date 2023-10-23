package com.java.TekanaeWallet.ServiceImp;

import com.java.TekanaeWallet.Exceptions.BadRequestException;
import com.java.TekanaeWallet.Exceptions.NotFoundException;
import com.java.TekanaeWallet.Model.Customer;
import com.java.TekanaeWallet.Model.Wallet;
import com.java.TekanaeWallet.Repo.WalletRepository;
import com.java.TekanaeWallet.Service.CustomerService;
import com.java.TekanaeWallet.Service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WalletServiceImp implements WalletService {


    private final WalletRepository walletRepository;
    private final CustomerService customerService;

    @Autowired
    public WalletServiceImp(WalletRepository walletRepository, CustomerService customerService) {
        this.walletRepository = walletRepository;
        this.customerService = customerService;
    }

    @Override
    public Wallet createWallet(Wallet wallet) {
        String customerEmail = wallet.getCustomer().getEmail();
        Customer customer = customerService.findByEmail(customerEmail);

        if (customer == null) {
            throw new NotFoundException("Customer with email " + customerEmail + " not found.");
        }
        wallet.setCustomer(customer);
        return walletRepository.save(wallet);
    }

    @Override
    public List<Wallet> getAllWallets() {
        return walletRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Wallet depositToWallet(Long walletId, double amount) {

        if (amount <= 0) {
            throw new BadRequestException("Deposit amount must be greater than zero.");
        }
        Optional<Wallet> optionalWallet = walletRepository.findById(walletId);
        if (optionalWallet.isPresent()) {
            Wallet wallet = optionalWallet.get();
            // Update the wallet's balance by adding the deposit amount
            double currentBalance = wallet.getBalance();
            wallet.setBalance(currentBalance + amount);
            return walletRepository.save(wallet);
        } else {
            throw new BadRequestException("Invalid wallet");
        }

    }

    @Override
    public List<Wallet> getCustomerWallets(Long customerId) {
        return walletRepository.findAllByCustomer_Id(customerId);
    }

    public Wallet getWalletByAccountNumber(String accountNumber) {
        return walletRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException("Wallet not found for account number: " + accountNumber));
    }

    @Override
    public void updateWallet(Wallet wallet) {
        walletRepository.save(wallet);
    }

}
