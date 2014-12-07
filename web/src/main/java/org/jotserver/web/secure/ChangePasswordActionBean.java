package org.jotserver.web.secure;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.jotserver.ot.model.account.Account;
import org.jotserver.ot.model.account.AccountAccessException;
import org.jotserver.web.AbstractActionBean;

public class ChangePasswordActionBean extends AbstractActionBean {
	
	private String password;
	private String newPassword;
	private String newPasswordRepeat;
	
	@DefaultHandler
	@DontValidate
	public Resolution view() {
		return new ForwardResolution("/WEB-INF/view/secure/ChangePassword.jsp");
	}
	
	public Resolution change() throws AccountAccessException {
		Account account = getContext().getUserAccount();
		account.setPassword(newPassword);
		getContext().getConfig().getAccountAccessor().saveAccount(account);
		addMessage("/secure/ChangePassword.action.success", password, newPassword);
		return new RedirectResolution("/secure/Index.action");
	}
	
	@Validate(label="current password", required = true, expression="this == context.userAccount.password")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Validate(label="new password", required=true, minlength=5, maxlength=40)
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	@Validate(label="re-typed new password", required=true, expression="newPassword==this")
	public String getNewPasswordRepeat() {
		return newPasswordRepeat;
	}
	public void setNewPasswordRepeat(String newPasswordRepeat) {
		this.newPasswordRepeat = newPasswordRepeat;
	}
	
	
	
}
