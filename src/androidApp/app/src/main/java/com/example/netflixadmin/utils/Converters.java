package com.example.netflixadmin.utils;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.List;

public class Converters {
    @TypeConverter
    public static String fromList(List<String> categories) {
        return categories != null ? String.join(",", categories) : null;
    }

    @TypeConverter
    public static List<String> toList(String data) {
        return data != null ? Arrays.asList(data.split(",")) : null;
    }
}

