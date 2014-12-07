package org.jotserver.ot.model.account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.jotserver.configuration.ConnectionProvider;

public class MySQLAccountAccessor implements AccountAccessor {
	private static final Logger logger = Logger.getLogger(MySQLAccountAccessor.class);
	
	private ConnectionProvider provider;
	
	public MySQLAccountAccessor(ConnectionProvider provider) {
		this.provider = provider;
	}
	
	
	public Account getAccount(long number) throws AccountAccessException {
		try {
			return loadAccount(number);
		} catch (SQLException e) {
			logger.error("Failed to load account", e);
			throw new AccountAccessException("Failed to load account.\nPlease contact an administrator.");
		}
	}
	
	
	public Account getAccount(long number, String playerName) throws AccountAccessException {
		try {
			return loadAccount(number, playerName);
		} catch (SQLException e) {
			logger.error("Failed to load account", e);
			throw new AccountAccessException("Failed to load account.\nPlease contact an administrator.");
		}
	}
	
	private Account loadAccount(long number) throws SQLException {
		Connection connection = provider.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement("SELECT * FROM `accounts` WHERE `accounts`.`number` = ?");
			stmt.setLong(1, number);
			return loadAccount(stmt);
		} finally {
			if(stmt != null) {
				stmt.close();
			}
			connection.close();
		}
	}
	
	private Account loadAccount(long number, String playerName) throws SQLException {
		Connection connection = provider.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement("SELECT * FROM `accounts`, `players` WHERE `accounts`.`number` = ? AND `accounts`.`number` = `players`.`account_number`");
			stmt.setLong(1, number);
			return loadAccount(stmt);
		} finally {
			if(stmt != null) {
				stmt.close();
			}
			connection.close();
		}
	}

	private Account loadAccount(PreparedStatement stmt) throws SQLException {
		ResultSet result = stmt.executeQuery();
		try {
			if(result.next()) {
				return parseAccount(result);
			} else {
				return null;
			}
		} finally {
			result.close();
		}
	}
	
	private Account parseAccount(ResultSet result) throws SQLException {
		Account ret = new Account();
		ret.setNumber(result.getLong("accounts.number"));
		ret.setPassword(result.getString("accounts.password"));
		ret.getPremium().setEnd(result.getLong("accounts.premium_end"));
		return ret;
	}


	public void createAccount(Account account) throws AccountAccessException {
		try {
			internalCreateAccount(account);
		} catch(SQLException e) {
			throw new AccountAccessException("Failed to create account", e);
		}
	}
	
	private void internalCreateAccount(Account account) throws SQLException {
		Connection connection = provider.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement("INSERT INTO `accounts` (`number`, `password`, `premium_end`) VALUES(?, ?, ?)");
			stmt.setLong(1, account.getNumber());
			stmt.setString(2, account.getPassword());
			stmt.setLong(3, account.getPremium().getEndMilliSeconds());
			stmt.execute();
			int result = stmt.getUpdateCount();
			if(result != 1) {
				throw new SQLException("Expected one updated row. Found " + result + " updated rows.");
			}
		} finally {
			if(stmt != null) {
				stmt.close();
			}
			connection.close();
		}
	}


	public void saveAccount(Account account) throws AccountAccessException {
		try {
			internalSaveAccount(account);
		} catch(SQLException e) {
			throw new AccountAccessException("Failed to save account.", e);
		}
	}
	
	private void internalSaveAccount(Account account) throws SQLException {
		Connection connection = provider.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement("UPDATE `accounts` SET `password` = ?, `premium_end` = ? WHERE `number` = ?");
			stmt.setString(1, account.getPassword());
			stmt.setLong(2, account.getPremium().getEndMilliSeconds());
			stmt.setLong(3, account.getNumber());
			stmt.execute();
			int result = stmt.getUpdateCount();
			if(result != 1) {
				throw new SQLException("Expected one updated row. Found " + result + " updated rows.");
			}
		} finally {
			if(stmt != null) {
				stmt.close();
			}
			connection.close();
		}
	}

}
