package org.jotserver.ot.model.chat;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;


public class TestChatChannelList {

    private static final int ID = 1337;
    private ChatChannelList<IdentifiableChatChannel> list;
    private Mockery context;

    @Before
    public void setUp() {
        context = new Mockery() {{
            setImposteriser(ClassImposteriser.INSTANCE);
        }};
        list = new ChatChannelList<IdentifiableChatChannel>();
    }

    @Test
    public void newListHasNoChannels() {
        assertTrue(list.getChannels().isEmpty());
    }

    @Test
    public void unknownChannelReturnsNull() {
        assertNull(list.getChannel(ID));
    }

    @Test
    public void canAddChannel() {
        final IdentifiableChatChannel channel = context.mock(IdentifiableChatChannel.class);
        context.checking(new Expectations() {{
            allowing(channel).getId(); will(returnValue(ID));
        }});
        list.addChannel(channel);

        assertFalse(list.getChannels().isEmpty());
        assertEquals(1, list.getChannels().size());

        assertNull(list.getChannel(ID+1));
        assertSame(channel, list.getChannel(ID));
    }

    @Test
    public void removingUnknownChannelFails() {
        final IdentifiableChatChannel channel = context.mock(IdentifiableChatChannel.class);
        context.checking(new Expectations() {{
            allowing(channel).getId(); will(returnValue(ID));
        }});

        assertFalse(list.removeChannel(ID));
        assertFalse(list.removeChannel(channel));
    }

    @Test
    public void removeKnownChannel() {
        final IdentifiableChatChannel channel = context.mock(IdentifiableChatChannel.class);
        context.checking(new Expectations() {{
            allowing(channel).getId(); will(returnValue(ID));
        }});
        list.addChannel(channel);

        assertNotNull(list.getChannel(ID));
        assertTrue(list.removeChannel(ID));
        assertNull(list.getChannel(ID));

    }
}
