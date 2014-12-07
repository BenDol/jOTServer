package org.jotserver.ot.model.action;

public enum ErrorType {
	NONE(""),
	NOTPOSSIBLE("Sorry, not possible."), 
	UNREACHABLE("Destination is out of reach."),
	NOTMOVEABLE("You cannot move this object."),
	TOOFARAWAY("Too far away."),
	DOWNSTAIRS("First go downstairs."),
	UPSTAIRS("First go upstairs."),
	NOTENOUGHROOM("There is not enough room."),
	CANNOTPICKUP("You cannot pickup this object."),
	CANNOTTHROW("You cannot throw there."),
	THEREISNOWAY("There is no way."),
	IMPOSSIBLE("This is impossible."), 
	CANNOTUSE("You cannot use this item."), 
	DOESNOTFIT("You cannot dress this object there.");
	
	private String message;

	private ErrorType(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
