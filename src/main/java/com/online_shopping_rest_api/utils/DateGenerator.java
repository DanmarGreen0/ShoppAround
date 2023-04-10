package com.online_shopping_rest_api.utils;

import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDateTime;

@Component
public class DateGenerator {

    // consider to delete this function
    public Date getSQLDate() {

        long millis = System.currentTimeMillis();
        Date date = new java.sql.Date(millis);

        return date;
    }

    public static LocalDateTime getLocalDate() {
        return LocalDateTime.now();
    }

}
