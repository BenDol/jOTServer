package org.jotserver.configuration;

import java.sql.Connection;

public interface ConnectionProvider {
    Connection getConnection();
}
