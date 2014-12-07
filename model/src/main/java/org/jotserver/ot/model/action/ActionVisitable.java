package org.jotserver.ot.model.action;

public interface ActionVisitable {
	public boolean test(Tester tester);
	public void execute(Visitor visitor);
}
