package org.jotserver.web;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.validation.LocalizableError;

public class AbstractActionBean implements ActionBean {

	private DefaultActionBeanContext context;

	public AbstractActionBean() {
		super();
	}

	public DefaultActionBeanContext getContext() {
		return context;
	}

	public void setContext(ActionBeanContext context) {
		this.context = (DefaultActionBeanContext)context;
	}

	protected void addFieldError(String field, String key, Object... parameters) {
		getContext().getValidationErrors().add(field, new LocalizableError(key, parameters));
	}

	protected void addMessage(String key, Object... parameters) {
		getContext().getMessages().add(new LocalizableMessage(key, parameters));
	}
}