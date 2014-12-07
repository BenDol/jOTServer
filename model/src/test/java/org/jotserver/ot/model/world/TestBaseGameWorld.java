package org.jotserver.ot.model.world;


import static org.junit.Assert.*;

import java.net.InetSocketAddress;

import org.junit.Before;
import org.junit.Test;

public class TestBaseGameWorld {

	private static final String HOST = "localhost";
	private static final int PORT = 7171;
	private static final String NAME = "SomeWorld";
	private static final String ID = "someworld";
	private BaseGameWorld world;
	private InetSocketAddress address;

	@Before
	public void setUp() throws Exception {
		address = new InetSocketAddress(HOST, PORT);
		world = new BaseGameWorld(ID, NAME, address, false);
	}
	
	@Test
	public void hasIdentifier() {
		assertEquals(ID, world.getIdentifier());
	}
	
	@Test
	public void hasName() {
		assertEquals(NAME, world.getName());
	}
	
	@Test
	public void hasAddress() {
		assertEquals(address, world.getAddress());
	}
	
	@Test
	public void hasLocality() {
		assertFalse(world.isLocal());
		world.setLocal(true);
		assertTrue(world.isLocal());
	}

}
