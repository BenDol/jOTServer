package org.jotserver.web.secure;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import org.jotserver.web.AbstractActionBean;

public class LogoutActionBean extends AbstractActionBean {

    @DefaultHandler
    public Resolution view() {
        if(getContext().getUserAccount() != null) {
            addMessage("/secure/Logout.action.success");
        }
        getContext().invalidate();
        return new RedirectResolution("/Login.action");
    }

}
