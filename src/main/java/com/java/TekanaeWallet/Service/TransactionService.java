package com.java.TekanaeWallet.Service;

import com.java.TekanaeWallet.Model.Transaction;

import java.util.List;

public interface  TransactionService {
    Transaction createTransaction(String senderAccount, String receiverAccount, Double amount, String user);

    List<Transaction> getAllTransactions();

    Transaction getTransactionById(Long id);


}
