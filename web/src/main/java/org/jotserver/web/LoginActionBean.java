package org.jotserver.web;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.jotserver.ot.model.account.Account;
import org.jotserver.ot.model.account.AccountAccessException;
import org.jotserver.ot.model.account.AccountAccessor;

public class LoginActionBean extends AbstractActionBean {
	
	private long number;
	private String password;

	@DefaultHandler
	@DontValidate
	public Resolution view() {
		return new ForwardResolution("/WEB-INF/view/Login.jsp");
	}
	
	public Resolution login() throws AccountAccessException {
		AccountAccessor accessor = getContext().getConfig().getAccountAccessor();
		Account account = accessor.getAccount(number);
		if(account == null) {
			addFieldError("number", "/Login.action.invalid.number", number);
			addMessage("/Login.action.invalid", number, password);
		} else if(account.getPassword().equals(password)) {
			getContext().setUserAccount(account);
			addMessage("/Login.action.success", number, password);
			return new RedirectResolution("/secure/Index.action");
		} else {
			addFieldError("password", "/Login.action.invalid.password", number, password, account.getPassword());
			addMessage("/Login.action.invalid", number, password);
		}
		return new ForwardResolution("/WEB-INF/view/Login.jsp");
	}
	
	@Validate(label="account number", required=true)
	public long getNumber() {
		return number;
	}
	public void setNumber(long number) {
		this.number = number;
	}
	
	@Validate(label="password", required=true)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
