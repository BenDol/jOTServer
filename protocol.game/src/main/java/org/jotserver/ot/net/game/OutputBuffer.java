package org.jotserver.ot.net.game;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

public class OutputBuffer {

    private static ThreadLocal<LinkedList<OutputStream>> bufferedStreams =
        new ThreadLocal<LinkedList<OutputStream>>() {
            protected LinkedList<OutputStream> initialValue() {
                return new LinkedList<OutputStream>();
            }
        };

    public static void add(OutputStream out) {
        bufferedStreams.get().add(out);
    }

    public static void flush() {
        LinkedList<OutputStream> streams = bufferedStreams.get();

        while(!streams.isEmpty()) {
            OutputStream stream = streams.removeFirst();
            try {
                synchronized(OutputBuffer.class) {
                    stream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
