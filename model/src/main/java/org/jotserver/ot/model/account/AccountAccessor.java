package org.jotserver.ot.model.account;


public interface AccountAccessor {
	public Account getAccount(long number) throws AccountAccessException;

	public Account getAccount(long number, String playerName) throws AccountAccessException;
	
	public void createAccount(Account account) throws AccountAccessException;

	public void saveAccount(Account account) throws AccountAccessException;
	
}
