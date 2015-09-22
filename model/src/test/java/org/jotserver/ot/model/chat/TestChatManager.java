package org.jotserver.ot.model.chat;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestChatManager {

    private static final int ID = 1337;
    private ChatManager chat;

    @Before
    public void setUp() throws Exception {
        chat = new ChatManager();
    }

    @Test
    public void hasDefaultChatChannel() {
        assertNotNull(chat.getDefaultChannel());
        assertTrue(chat.getDefaultChannel() instanceof DefaultChatChannel);
    }

    @Test
    public void unknownChannelReturnsNull() {
        assertNull(chat.getPublicChannel(ID));
    }

    @Test
    public void newManagerContainsNoPublicChannels() {
        assertTrue(chat.getPublicChannels().isEmpty());
    }

    @Test
    public void canAddChannel() {
        PublicChatChannel ch = new PublicChatChannel(ID, "SomeChannel");
        assertNull(chat.getPublicChannel(ID));
        chat.addPublicChannel(ch);
        assertSame(ch, chat.getPublicChannel(ID));
    }

}
