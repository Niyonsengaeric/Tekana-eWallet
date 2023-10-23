package com.java.TekanaeWallet.Service;

import com.java.TekanaeWallet.Model.WalletActivityLog;

import java.util.List;

public interface WalletActivityLogService {
    void createWalletActivityLog(WalletActivityLog log);
    List<WalletActivityLog> getWalletActivityLogs();

    List<WalletActivityLog> getLogsByTransaction(Long transactionId);
}
