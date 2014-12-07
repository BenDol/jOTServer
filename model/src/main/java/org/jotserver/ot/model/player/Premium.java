package org.jotserver.ot.model.player;

import java.util.Calendar;
import java.util.Date;

public class Premium {
	private static final int DAY = 24 * 60 * 60 * 1000;
	private Date end;
	
	public Premium(Date end) {
		this.end = (Date)end.clone();
	}
	
	public Premium(long endMillis) {
		this(new Date(endMillis));
	}
	
	public Premium() {
		end = new Date(0);
	}

	public boolean hasEnded() {
		return end.before(now());
	}
	
	public int getDaysLeft() {
		if(hasEnded()) {
			return 0;
		} else {
			return (int) (getMilliSecondsLeft() / DAY);
		}
	}
	
	public long getEndMilliSeconds() {
		return end.getTime();
	}
	
	public Date getEnd() {
		return (Date)end.clone();
	}
	
	public long getMilliSecondsLeft() {
		if(hasEnded()) {
			return 0;
		} else {
			return getEndMilliSeconds() - now().getTime();
		}
	}
	
	public void setEnd(Date end) {
		this.end = (Date)end.clone();
	}
	
	public void setEnd(long endMilliSeconds) {
		setEnd(new Date(endMilliSeconds));
	}
	
	public void addDays(int days) {
		if(hasEnded()) {
			setEnd(now());
		}
		addMilliSeconds(((long)days) * DAY);
	}

	public void addMilliSeconds(long l) {
		if(hasEnded()) {
			setEnd(now());
		}
		setEnd(end.getTime()+l);
	}

	private Date now() {
		return Calendar.getInstance().getTime();
	}
}
