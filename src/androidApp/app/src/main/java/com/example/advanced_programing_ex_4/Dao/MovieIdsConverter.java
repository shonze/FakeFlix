package com.example.advanced_programing_ex_4.Dao;

import androidx.room.TypeConverter;
import java.util.Arrays;
import java.util.List;
public class MovieIdsConverter {
    @TypeConverter
    public static List<String> fromString(String value) {
        if (value == null || value.isEmpty()) return null;
        return Arrays.asList(value.split(","));
    }

    @TypeConverter
    public static String fromList(List<String> list) {
        if (list == null || list.isEmpty()) return "";
        return String.join(",", list);
    }
}