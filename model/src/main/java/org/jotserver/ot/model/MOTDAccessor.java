package org.jotserver.ot.model;


public interface MOTDAccessor {
    MOTD getMOTD() throws MOTDAccessException;
}
