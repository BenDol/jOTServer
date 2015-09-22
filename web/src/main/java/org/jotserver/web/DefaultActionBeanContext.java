package org.jotserver.web;

import java.io.IOException;

import net.sourceforge.stripes.action.ActionBeanContext;

import org.jotserver.configuration.ConfigurationAccessor;
import org.jotserver.configuration.ConfigurationException;
import org.jotserver.configuration.PropertiesConfigurationAccessor;
import org.jotserver.ot.model.account.Account;

public class DefaultActionBeanContext extends ActionBeanContext {

    private static ConfigurationAccessor config = null;
    static {
        try {
            config = new PropertiesConfigurationAccessor(
                    DefaultActionBeanContext.class.getClassLoader()
                            .getResource("config.properties").getPath());
        } catch (IOException e) {
            throw new ConfigurationException(e);
        }
    }

    public DefaultActionBeanContext() throws IOException {

    }

    public ConfigurationAccessor getConfig() {
        return config;
    }

    public void setUserAccount(Account account) {
        getRequest().getSession().setAttribute("userAccount", account);
    }

    public Account getUserAccount() {
        return (Account)getRequest().getSession().getAttribute("userAccount");
    }

    public void invalidate() {
        getRequest().getSession().invalidate();
    }

    public boolean isLoggedIn() {
        return getUserAccount() != null;
    }

}
