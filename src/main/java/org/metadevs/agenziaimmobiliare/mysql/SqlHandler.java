package org.metadevs.agenziaimmobiliare.mysql;


import com.mysql.cj.jdbc.MysqlDataSource;
import org.metadevs.agenziaimmobiliare.AgenziaImmobiliare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class SqlHandler {
        private final Database<AgenziaImmobiliare> database;
        private final AgenziaImmobiliare plugin;
        private MysqlDataSource dataSource;

        public SqlHandler(AgenziaImmobiliare plugin) {
            this.plugin = plugin;
            database = new Database<>(plugin);
        }

        public boolean init() throws SQLException {
            if (!database.loadDatabase()) {

                return false;
            }
            dataSource = new MysqlDataSource();

            dataSource.setServerName(database.getHost());
            dataSource.setPortNumber(database.getPort());
            dataSource.setDatabaseName(database.getDatabase());
            dataSource.setUser(database.getUsername());
            dataSource.setPassword(database.getPassword());


            dataSource.setAllowMultiQueries(true);

            try (Connection connection = dataSource.getConnection()) {
                if (!connection.isValid(1000)) {
                    throw new SQLException();
                }
            }

            return true;
        }

        public CompletableFuture<Boolean> loadDatabase() {
            return CompletableFuture.supplyAsync(() -> {
                final String SQL =
                        "CREATE TABLE IF NOT EXISTS `Immobile` (\n" +
                                "    `nome` varchar(50) primary key ,\n" +
                                "    `regionId` varchar(100) not null unique \n," +
                                "    `tipo` varchar(50) not null, \n" +
                                "    `proprietario` char(36) default null,\n " +
                                "    `prezzo` bigint not null check (prezzo > 0) \n" +
                                " );";

                try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {

                    preparedStatement.execute();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            });
        }

        public MysqlDataSource getDataSource() {
            return dataSource;
        }




    }

