package org.jotserver.ot.model.util;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Dispatcher {
	
	private ScheduledExecutorService executor;
	
	public Dispatcher(ScheduledExecutorService executor) {
		this.executor = executor;
	}
	
	public void run(Task task) {
		executor.execute(new RunnableTask(task));
	}
	
	public void run(Task task, long delay) {
		executor.schedule(new RunnableTask(task), delay, TimeUnit.MILLISECONDS);
	}
	
	private class RunnableTask implements Runnable {
		
		private Task task;

		public RunnableTask(Task task) {
			this.task = task;
		}

		public void run() {
			try {
				task.execute(Dispatcher.this);
			} catch(RuntimeException e) {
				e.printStackTrace();
				throw e;
			}
		}
	}
	
}