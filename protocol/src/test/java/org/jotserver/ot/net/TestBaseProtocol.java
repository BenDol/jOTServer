package org.jotserver.ot.net;


import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.net.CData;
import org.jotserver.net.ClientSession;
import org.jotserver.ot.net.io.MessageInputStream;
import org.junit.Before;
import org.junit.Test;

public class TestBaseProtocol {

    private Mockery context;
    private ClientSession mockedClient;
    private BaseProtocol prot;
    private int i;

    @Before
    public void setUp() throws Exception {
        context = new Mockery() {{
            setImposteriser(ClassImposteriser.INSTANCE);
        }};
        mockedClient = context.mock(ClientSession.class);
        prot = new BaseProtocol(mockedClient);
    }

    @Test
    public void defaultProtocolIsItself() {
        assertSame(prot, prot.getCurrentProtocol());
    }

    @Test
    public void containsClientSession() {
        assertSame(mockedClient, prot.getClientSession());
    }

    @Test
    public void changeProtocol() {
        final Protocol protocol = context.mock(Protocol.class);
        context.checking(new Expectations() {{
            oneOf(protocol).init(mockedClient);
        }});
        prot.setCurrentProtocol(protocol);
        context.assertIsSatisfied();
        assertSame(protocol, prot.getCurrentProtocol());
    }

    @Test
    public void createProtocol() {
        final Protocol protocol = context.mock(Protocol.class);
        final ProtocolProvider provider = context.mock(ProtocolProvider.class);
        context.checking(new Expectations() {{
            allowing(provider).getProtocol(1337); will(returnValue(protocol));
            allowing(provider).getProtocol(with(any(Integer.class))); will(returnValue(null));
        }});
        prot.addProtocolProvider(provider);
        assertEquals(protocol, prot.getProtocolById(1337));
        assertNull(prot.getProtocolById(1));
    }

    @Test
    public void parseValidHeader() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CData.writeU16(out, 2);
        CData.writeU16(out, 1337);
        InputStream in = prot.parseHeader(new ByteArrayInputStream(out.toByteArray()));
        assertEquals(2, in.available());
        assertEquals(1337, CData.readU16(in));
        assertTrue(in instanceof MessageInputStream);
    }

    @Test
    public void parseInvalidHeader() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CData.writeU16(out, 2);
        InputStream in = prot.parseHeader(new ByteArrayInputStream(out.toByteArray()));
        assertNull(in);
    }

    @Test
    public void receiveSinglePacket() throws IOException {
        i = 0;
        final Protocol protocol = new Protocol() {
            public void init(ClientSession session) {
            }
            public void parseFirst(InputStream message) throws IOException {
            }
            public void parsePacket(InputStream message) throws IOException {
                assertEquals(2, message.available());
                assertEquals(1337, CData.readU16(message));
                i++;
            }
        };
        prot.setCurrentProtocol(protocol);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CData.writeU16(out, 2);
        CData.writeU16(out, 1337);
        prot.messageReceived(mockedClient, new ByteArrayInputStream(out.toByteArray()));

        assertEquals(1, i);

    }

    @Test
    public void receiveMultiplePackets() throws IOException {
        i = 0;
        final Protocol protocol = new Protocol() {
            public void init(ClientSession session) {
            }
            public void parseFirst(InputStream message) throws IOException {
            }
            public void parsePacket(InputStream message) throws IOException {
                assertEquals(2, message.available());
                assertEquals(1337+i, CData.readU16(message));
                i++;
            }
        };
        prot.setCurrentProtocol(protocol);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CData.writeU16(out, 2);
        CData.writeU16(out, 1337);
        CData.writeU16(out, 2);
        CData.writeU16(out, 1338);
        prot.messageReceived(mockedClient, new ByteArrayInputStream(out.toByteArray()));

        assertEquals(2, i);

    }

    @Test
    public void receivePacketsSkipsUnusedData() throws IOException {
        i = 0;
        final Protocol protocol = new Protocol() {
            public void init(ClientSession session) {
            }
            public void parseFirst(InputStream message) throws IOException {
            }
            public void parsePacket(InputStream message) throws IOException {
                assertEquals(2, message.available());
                if(i == 1) {
                    assertEquals(1338, CData.readU16(message));
                }
                i++;
            }
        };
        prot.setCurrentProtocol(protocol);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CData.writeU16(out, 2);
        CData.writeU16(out, 1337);
        CData.writeU16(out, 2);
        CData.writeU16(out, 1338);
        prot.messageReceived(mockedClient, new ByteArrayInputStream(out.toByteArray()));

        assertEquals(2, i);

    }

    @Test
    public void exceptionInProtocolClosesClientSession() throws IOException {
        i = 0;
        final Protocol protocol = new Protocol() {
            public void init(ClientSession session) {
            }
            public void parseFirst(InputStream message) throws IOException {
            }
            public void parsePacket(InputStream message) throws IOException {
                i++;
                throw new IOException("Test");
            }
        };

        context.checking(new Expectations() {{
            oneOf(mockedClient).close();
        }});

        prot.setCurrentProtocol(protocol);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CData.writeU16(out, 2);
        CData.writeU16(out, 1337);
        prot.messageReceived(mockedClient, new ByteArrayInputStream(out.toByteArray()));

        assertEquals(1, i);

        context.assertIsSatisfied();
    }

    @Test
    public void changesToCorrectProtocol() throws IOException {
        final Protocol protocol = context.mock(Protocol.class);
        final ProtocolProvider provider = context.mock(ProtocolProvider.class);
        context.checking(new Expectations() {{
            allowing(provider).getProtocol(123); will(returnValue(protocol));
            oneOf(protocol).parseFirst(with(any(InputStream.class)));
            oneOf(protocol).init(mockedClient);
        }});
        prot.addProtocolProvider(provider);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CData.writeU16(out, 1);
        CData.writeByte(out, 123);
        prot.messageReceived(mockedClient, new ByteArrayInputStream(out.toByteArray()));

        assertSame(protocol, prot.getCurrentProtocol());
        context.assertIsSatisfied();
    }

    @Test
    public void disconnectsClientUponUnknownProtocol() throws IOException {
        context.checking(new Expectations() {{
            oneOf(mockedClient).close();
        }});

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CData.writeU16(out, 1);
        CData.writeByte(out, 123);
        prot.messageReceived(mockedClient, new ByteArrayInputStream(out.toByteArray()));

        context.assertIsSatisfied();
    }

}
