package com.java.TekanaeWallet.Controller;

import com.java.TekanaeWallet.Exceptions.NotFoundException;
import com.java.TekanaeWallet.Filter.CustomAuthorizationFilter;
import com.java.TekanaeWallet.Model.Transaction;
import com.java.TekanaeWallet.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(
            @RequestParam("senderAccount") String senderAccount,
            @RequestParam("receiverAccount") String receiverAccount,
            @RequestParam("amount") Double amount,
            HttpServletRequest request
    ) {
        String user = request.getAttribute(CustomAuthorizationFilter.email).toString();
        Transaction transaction = transactionService.createTransaction(senderAccount, receiverAccount, amount,user);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        Transaction transaction = transactionService.getTransactionById(id);
        if (transaction == null) {
            throw new NotFoundException("transaction not found");
        }
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

}
