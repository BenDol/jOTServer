package org.jotserver.ot.model.util;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

public class TestBrain {

    private Brain brain;
    private Mockery context;
    private Dispatcher dispatcher;
    private boolean b;

    @Before
    public void setUp() throws Exception {
        context = new Mockery() {{
            setImposteriser(ClassImposteriser.INSTANCE);
        }};
        dispatcher = context.mock(Dispatcher.class);
        brain = new TestableBrain();
    }

    @Test
    public void newBrainIsNotCancelled() {
        assertFalse(brain.isCancelled());
    }

    @Test
    public void canCancel() {
        brain.cancel();
        assertTrue(brain.isCancelled());
    }

    @Test
    public void cancelledBrainStaysCancelled() {
        brain.cancel();
        brain.cancel();
        assertTrue(brain.isCancelled());
    }

    @Test
    public void canSetCancellation() {
        brain.cancel();
        brain.setCancelled(false);
        assertFalse(brain.isCancelled());
    }

    @Test
    public void willNotThinkIfCancelled() {
        brain = new TestableBrain() {
            @Override
            public void think() {
                fail("Brain should not think when cancelled!");
            }
        };
        brain.cancel();
        brain.execute(dispatcher);
    }

    @Test
    public void shouldThinkIfNotCancelled() {
        b = false;
        brain = new TestableBrain() {
            @Override
            public void think() {
                b = true;
            }
        };

        context.checking(new Expectations() {{
            oneOf(dispatcher).run(brain, 0);
        }});

        brain.execute(dispatcher);
        assertTrue(b);
        context.assertIsSatisfied();
    }

}

@Ignore
class TestableBrain extends Brain {

    @Override
    public long getDelay() {
        return 0;
    }

    @Override
    public void think() {
    }

}
