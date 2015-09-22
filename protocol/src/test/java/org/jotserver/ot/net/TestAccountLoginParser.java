package org.jotserver.ot.net;


import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jotserver.net.CData;
import org.jotserver.ot.model.account.Account;
import org.junit.Before;
import org.junit.Test;

public class TestAccountLoginParser {

    private static final String PASSWORD = "MyPassword";
    private static final int NUMBER = 13371337;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void parseLogin() throws IOException {
        AccountLoginParser p = new AccountLoginParser();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        CData.writeU32(out, NUMBER);
        CData.writeString(out, PASSWORD);

        p.parse(new ByteArrayInputStream(out.toByteArray()));

        assertEquals(NUMBER, p.getNumber());
        assertEquals(PASSWORD, p.getPassword());
    }

    @Test
    public void validateAccounts() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        CData.writeU32(out, NUMBER);
        CData.writeString(out, PASSWORD);

        AccountLoginParser p = new AccountLoginParser(new ByteArrayInputStream(out.toByteArray()));

        Account invalid1 = new Account(NUMBER+1, PASSWORD);
        assertFalse(p.validate(invalid1));
        Account invalid2 = new Account(NUMBER, PASSWORD + "A");
        assertFalse(p.validate(invalid2));
        Account invalid3 = new Account(NUMBER+1, PASSWORD + "A");
        assertFalse(p.validate(invalid3));
        Account valid = new Account(NUMBER, PASSWORD);
        assertTrue(p.validate(valid));
    }

}
