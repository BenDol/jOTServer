package org.jotserver.web;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.jotserver.ot.model.account.Account;
import org.jotserver.ot.model.account.AccountAccessException;
import org.jotserver.ot.model.account.AccountAccessor;

public class CreateAccountActionBean extends AbstractActionBean {

    private long number;
    private String email;
    private String password;
    private String passwordRepeat;

    @DontValidate
    @DefaultHandler
    public Resolution view() {
        return new ForwardResolution("/WEB-INF/view/CreateAccount.jsp");
    }

    public Resolution create() throws AccountAccessException {
        Account account = new Account(number);
        account.setPassword(password);
        AccountAccessor accessor = getContext().getConfig().getAccountAccessor();
        if(accessor.getAccount(number) != null) {
            addFieldError("number", "/CreateAccount.action.exists", number);
        } else {
            accessor.createAccount(account);
            addMessage("/CreateAccount.action.created", account.getNumber(), account.getPassword());
        }
        return new ForwardResolution("/WEB-INF/view/CreateAccount.jsp");
    }

    @Validate(label="account number", required=true, minvalue=100000, maxvalue=99999999)
    public long getNumber() {
        return number;
    }
    public void setNumber(long number) {
        this.number = number;
    }

    @Validate(label="e-mail", required=true, mask="\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b")
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Validate(label="password", required=true, minlength=5, maxlength=40)
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Validate(label="re-typed password", required=true, expression="password==this")
    public String getPasswordRepeat() {
        return passwordRepeat;
    }
    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

}
