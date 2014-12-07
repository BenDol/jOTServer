package org.jotserver.ot.model.action;


public abstract class Action {
	
	public static enum State { NONE, SUCCESS, FAILURE, DELAYED };
	
	private ErrorType error;
	private State state;
	
	public Action() {
		error = ErrorType.NONE;
		state = State.NONE;
	}
	
	public abstract boolean execute();
	
	public ErrorType getError() {
		return error;
	}

	public boolean hasFailed() {
		return state == State.FAILURE || error != ErrorType.NONE;
	}
	
	protected void fail(ErrorType error) {
		this.error = error;
		setState(State.FAILURE);
	}

	public State getState() {
		return state;
	}
	
	protected void setState(State state) {
		this.state = state;
	}
}