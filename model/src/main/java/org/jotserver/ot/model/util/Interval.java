package org.jotserver.ot.model.util;

public class Interval {

    public final int start, end;

    public Interval(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getLength() {
        return end-start;
    }

}
