package org.jotserver.ot.net.login;

import java.io.IOException;
import java.io.OutputStream;

import org.jotserver.net.CData;
import org.jotserver.ot.model.MOTD;
import org.jotserver.ot.net.Writer;

public class MOTDWriter implements Writer {

    public static final int OPBYTE = 0x14;

    private int number;
    private String message;

    public MOTDWriter() {
        number = 0;
        message = null;
    }

    public MOTDWriter(int number, String message) {
        this();
        this.number = number;
        this.message = message;
    }

    public MOTDWriter(MOTD motd) {
        this(motd.getNumber(), motd.getMessage());
    }

    public void write(OutputStream out) throws IOException {
        CData.writeByte(out, OPBYTE);
        CData.writeString(out, Integer.toString(number) + "\n" + message);
    }

    public void setNumber(int i) {
        number = i;
    }

    public void setMessage(String string) {
        message = string;
    }

}
