package org.jotserver.ot.model.util;

public abstract class Brain implements Task {
    private boolean cancelled;

    public abstract void think();
    public abstract long getDelay();

    public Brain() {
        cancelled = false;
    }

    public void execute(Dispatcher dispatcher) {
        if(!isCancelled()) {
            think();
            dispatcher.run(this, getDelay());
        }
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        cancelled = true;
    }

    protected void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
