package org.jotserver.ot.model.account;

import static org.junit.Assert.*;

import org.junit.Test;


public class TestAccount {
	
	private static final int NUMBER = 1337;
	private static final String PASSWORD = "Password";

	@Test
	public void defaultAccountHasNoPremium() {
		Account a = new Account();
		assertEquals(0, a.getPremium().getDaysLeft());
	}
	
	@Test
	public void hasNumber() {
		Account a = new Account(NUMBER);
		assertEquals(NUMBER, a.getNumber());
	}
	
	@Test
	public void hasPassword() {
		Account a = new Account(NUMBER, PASSWORD);
		assertEquals(PASSWORD, a.getPassword());
	}
	
	@Test
	public void changeNumberAndPassword() {
		Account a = new Account();
		a.setNumber(NUMBER);
		a.setPassword(PASSWORD);
		assertEquals(NUMBER, a.getNumber());
		assertEquals(PASSWORD, a.getPassword());
	}
	
}
