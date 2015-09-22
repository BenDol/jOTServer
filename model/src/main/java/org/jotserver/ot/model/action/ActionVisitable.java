package org.jotserver.ot.model.action;

public interface ActionVisitable {
    boolean test(Tester tester);

    void execute(Visitor visitor);
}
