package com.java.TekanaeWallet.ServiceImp;

import com.java.TekanaeWallet.Exceptions.BadRequestException;
import com.java.TekanaeWallet.Model.Customer;
import com.java.TekanaeWallet.Model.Transaction;
import com.java.TekanaeWallet.Model.Wallet;
import com.java.TekanaeWallet.Model.WalletActivityLog;
import com.java.TekanaeWallet.Repo.TransactionRepository;
import com.java.TekanaeWallet.Service.CustomerService;
import com.java.TekanaeWallet.Service.TransactionService;
import com.java.TekanaeWallet.Service.WalletActivityLogService;
import com.java.TekanaeWallet.Service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImp implements TransactionService {

    private final TransactionRepository transactionRepo;
    private final WalletActivityLogService walletActivityLogService;
    private final WalletService walletService;
    private final CustomerService customerService;


    @Autowired
    public TransactionServiceImp(TransactionRepository transactionRepo,WalletActivityLogService walletActivityLogService, WalletService walletService, CustomerService customerService) {
        this.transactionRepo = transactionRepo;
        this.walletActivityLogService = walletActivityLogService;
        this.walletService = walletService;
        this.customerService = customerService;
    }

    @Override
    public Transaction createTransaction(String senderAccount, String receiverAccount, Double amount, String user) {
        try {
            // Check if senderAccount and receiverAccount are provided
            if (senderAccount == null || senderAccount.isEmpty() || receiverAccount == null || receiverAccount.isEmpty()) {
                throw new BadRequestException("Sender account and receiver account must be provided and not empty.");
            }

            Wallet senderWallet = walletService.getWalletByAccountNumber(senderAccount);
            Wallet receiverWallet = walletService.getWalletByAccountNumber(receiverAccount);

            // Check if sender and receiver wallets exist
            if (senderWallet == null || receiverWallet == null) {
                throw new BadRequestException("Sender or receiver not found.");
            }

            // Check if sender's balance is sufficient for the transaction
            if (senderWallet.getBalance() < amount) {
                throw new BadRequestException("Insufficient balance in the sender's wallet.");
            }

            Customer sender = senderWallet.getCustomer();
            Customer receiver = receiverWallet.getCustomer();

        if (sender == null || receiver == null) {
            throw new BadRequestException("Sender or receiver not found.");
        }

        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(amount);
            return completeTransaction(processTransaction(saveTransaction(transaction,user), senderWallet),receiverWallet);

        } catch (BadRequestException e) {
        throw e;
    } catch (Exception e) {
        throw new BadRequestException("Transaction processing failed.");
    }
    }

    private Transaction saveTransaction(Transaction transaction,String user) throws BadRequestException {
        Customer createdBy = customerService.findByEmail(user);
        transaction.setStatus("initialize");
        transaction.setCreatedBy(createdBy);
        Transaction savedTransaction = transactionRepo.save(transaction);
        if (savedTransaction == null) {
            throw new BadRequestException("Transaction could not be saved.");
        }
        return savedTransaction;
    }

    private Transaction processTransaction(Transaction transaction,Wallet senderWallet) throws BadRequestException {
        if (senderWallet == null) {
            throw new BadRequestException("sender wallet not found.");
        }
        transaction.setStatus("processing");
        transactionRepo.save(transaction);

        // update sender wallet
        double newSenderBalance = senderWallet.getBalance() - transaction.getAmount();
        senderWallet.setBalance(newSenderBalance);
        walletService.updateWallet(senderWallet);

        // create wallet log
        WalletActivityLog activityLog = new WalletActivityLog();
        activityLog.setTransaction(transaction);
        activityLog.setWallet(senderWallet);
        activityLog.setAction("debiting");
        walletActivityLogService.createWalletActivityLog(activityLog);

        return transaction;
    }

    private Transaction completeTransaction(Transaction transaction , Wallet receiverWallet) throws BadRequestException {
        if (receiverWallet == null) {
            throw new BadRequestException("Receiver wallet not found.");
        }

        // Add the transaction amount to the receiver's balance
        double newReceiverBalance = receiverWallet.getBalance() + transaction.getAmount();
        receiverWallet.setBalance(newReceiverBalance);
        walletService.updateWallet(receiverWallet);

        // Create a WalletActivityLog for crediting
        WalletActivityLog receiverActivityLog = new WalletActivityLog();
        receiverActivityLog.setTransaction(transaction);
        receiverActivityLog.setWallet(receiverWallet);
        receiverActivityLog.setAction("crediting");
        walletActivityLogService.createWalletActivityLog(receiverActivityLog);

        // Update the transaction status to "complete"
        transaction.setStatus("complete");
        transactionRepo.save(transaction);
        return transaction;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepo.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Transaction getTransactionById(Long id) {
        return transactionRepo.findById(id).orElse(null);
    }
}
