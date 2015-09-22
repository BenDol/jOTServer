package org.jotserver.ot.model.account;


public interface AccountAccessor {
    Account getAccount(long number) throws AccountAccessException;

    Account getAccount(long number, String playerName) throws AccountAccessException;

    void createAccount(Account account) throws AccountAccessException;

    void saveAccount(Account account) throws AccountAccessException;

}
