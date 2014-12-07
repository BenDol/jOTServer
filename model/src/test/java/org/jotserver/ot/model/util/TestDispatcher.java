package org.jotserver.ot.model.util;


import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

public class TestDispatcher {

	private Mockery context;
	private ScheduledExecutorService executor;
	private Dispatcher dispatcher;

	@Before
	public void setUp() throws Exception {
		context = new Mockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
		}};
		executor = context.mock(ScheduledExecutorService.class);
		dispatcher = new Dispatcher(executor);
	}
	
	@Test
	public void canExecuteTask() {
		Task t = new Task() {
			public void execute(Dispatcher dispatcher) {
			}
		};
		
		context.checking(new Expectations() {{
			oneOf(executor).execute(with(any(Runnable.class)));
		}});
		
		dispatcher.run(t);
		
	}
	
	@Test
	public void canScheduleTask() {
		Task t = new Task() {
			public void execute(Dispatcher dispatcher) {
			}
		};
		
		context.checking(new Expectations() {{
			oneOf(executor).schedule(with(any(Runnable.class)), with(any(Integer.class)), with(any(TimeUnit.class)));
		}});
		
		dispatcher.run(t, 1337);
		
	}
	
	
	
}
