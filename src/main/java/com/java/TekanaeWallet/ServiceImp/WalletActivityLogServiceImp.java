package com.java.TekanaeWallet.ServiceImp;

import com.java.TekanaeWallet.Model.WalletActivityLog;
import com.java.TekanaeWallet.Repo.WalletActivityLogRepo;
import com.java.TekanaeWallet.Service.WalletActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class WalletActivityLogServiceImp implements WalletActivityLogService {
    private final WalletActivityLogRepo walletActivityLogRepo;

    @Autowired
    public WalletActivityLogServiceImp(WalletActivityLogRepo walletActivityLogRepo) {
        this.walletActivityLogRepo = walletActivityLogRepo;
    }

    @Override
    public void createWalletActivityLog(WalletActivityLog log) {
        walletActivityLogRepo.save(log);
    }

    @GetMapping
    public List<WalletActivityLog> getWalletActivityLogs() {
        return walletActivityLogRepo.findAllByOrderByCreatedAtDesc();
    }
    @Override
    public List<WalletActivityLog> getLogsByTransaction(Long transactionId) {
        return walletActivityLogRepo.findByTransactionId(transactionId);
    }
}
