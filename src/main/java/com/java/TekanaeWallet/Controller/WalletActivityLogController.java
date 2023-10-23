package com.java.TekanaeWallet.Controller;

import com.java.TekanaeWallet.Model.WalletActivityLog;
import com.java.TekanaeWallet.Service.WalletActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wallet-activity-logs")
public class WalletActivityLogController {
    private final WalletActivityLogService walletActivityLogService;

    @Autowired
    public WalletActivityLogController(WalletActivityLogService walletActivityLogService) {
        this.walletActivityLogService = walletActivityLogService;
    }

    @GetMapping
    public List<WalletActivityLog> getWalletActivityLogs() {
        return walletActivityLogService.getWalletActivityLogs();
    }

    @GetMapping("/transaction/{transactionId}")
    public List<WalletActivityLog> getLogsByTransaction(@PathVariable Long transactionId) {
        return walletActivityLogService.getLogsByTransaction(transactionId);
    }
    }
