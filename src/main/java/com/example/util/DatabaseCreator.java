package com.example.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DatabaseCreator {
    @Value("${database.url}")
    private String url1;
    @Value("${database.username}")
    private String username1;
    @Value("${database.password}")
    private String password1;

    public static Connection connection = null;
    public static Statement statement = null;

    @Bean
    public void createDatabase() throws SQLException {
        connection = DriverManager.getConnection(url1, username1, password1);
        System.out.println("Connection to database is success");
        statement = connection.createStatement();
        try {
            statement.execute("create table person\n" +
                    "(\n" +
                    "    id            serial\n" +
                    "        constraint person_pk\n" +
                    "            primary key,\n" +
                    "    username      varchar not null,\n" +
                    "    password      varchar not null,\n" +
                    "    year_of_birth integer not null,\n" +
                    "    role          varchar not null,\n" +
                    "timer         bigint not null"+
                    ");\n" +
                    "\n" +
                    "alter table person\n" +
                    "    owner to postgres;\n" +
                    "\n" +
                    "create unique index person_id_uindex\n" +
                    "    on person (id);" );
            System.out.println("Person table is created");

        } catch (Exception e) {
            System.out.println("Person table is already created");
        }

        try {
            statement.execute("INSERT INTO person(id, username, password, year_of_birth, role, timer) " +
                    "values (0, 'admin', '$2a$10$1fGl7V0Z9xb6gC0AVtcXfu2Rjy3HsQyaqCjQ7mW0Fg6mkyD9DtnjO', 1900, 'ROLE_ADMIN', 1690830000000)");

            System.out.println("DEFAULT USERNAME: ADMIN  \n DEFAULT PASSWORD: 123");
        }
        catch (Exception e) {
            System.out.println("Admin is already created");
        }
        try {
            statement.execute("create table items\n" +
                    "(\n" +
                    "    id       serial\n" +
                    "        constraint items_pk\n" +
                    "            primary key,\n" +
                    "    name     varchar not null,\n" +
                    "    path     varchar not null,\n" +
                    "    username varchar not null,\n" +
                    "    token    varchar\n" +
                    ");\n" +
                    "\n" +
                    "alter table items\n" +
                    "    owner to postgres;\n" +
                    "\n" +
                    "create unique index items_id_uindex\n" +
                    "    on items (id);");
            System.out.println("Item table is created");
        } catch (Exception e) {
            System.out.println("Item table is already created");
        }


    }
}
