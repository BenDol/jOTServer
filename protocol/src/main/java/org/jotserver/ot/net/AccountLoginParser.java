package org.jotserver.ot.net;

import java.io.IOException;
import java.io.InputStream;

import org.jotserver.net.CData;
import org.jotserver.ot.model.account.Account;
import org.jotserver.ot.net.Parser;

public class AccountLoginParser implements Parser {
	
	protected long number;
	protected String password;
	
	public AccountLoginParser() {
		number = 0;
		password = null;
	}
	
	public AccountLoginParser(InputStream message) throws IOException {
		this();
		parse(message);
	}

	
	public void parse(InputStream in) throws IOException {
		number = CData.readU32(in);
		password = CData.readString(in);
	}

	public long getNumber() {
		return number;
	}

	public String getPassword() {
		return password;
	}

	public boolean validate(Account account) {
		return number == account.getNumber() && password.equals(account.getPassword());
	}

}
