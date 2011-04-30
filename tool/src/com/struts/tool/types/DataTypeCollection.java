package com.struts.tool.types;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mayconbordin
 * @version 0.1
 */
public class DataTypeCollection {
    public static final Map<String, DataType> types = new HashMap<String, DataType>();

    static {
        //Numeric
        types.put("int", new DataType("int", "int", null, "integer", "numeric"));
        types.put("integer", new DataType("integer", "int", null, "integer", "numeric"));
        types.put("long", new DataType("long", "long", null, "long", "numeric"));
        types.put("short", new DataType("short", "short", null, "short", "numeric"));
        types.put("float", new DataType("float", "float", null, "float", "numeric"));
        types.put("double", new DataType("double", "double", null, "double", "numeric"));
        types.put("bigdecimal", new DataType("bigdecimal", "BigDecimal",
                "java.math.BigDecimal", "big_decimal", "numeric"));

        //Boolean
        types.put("boolean", new DataType("boolean", "boolean", null, "boolean", "boolean"));

        //Character
        types.put("string", new DataType("string", "String", null, "string", "character"));
        types.put("char", new DataType("char", "char", null, "character", "character"));
        types.put("character", new DataType("char", "char", null, "character", "character"));
        types.put("text", new DataType("text", "String", null, "text", "character"));

        //Date
        types.put("date", new DataType("date", "Date", "java.util.Date", "date", "date"));
        types.put("time", new DataType("time", "Date", "java.util.Date", "time", "date"));
        types.put("timestamp", new DataType("timestamp", "Date", "java.util.Date", "timestamp", "date"));

        //Currency
        types.put("currency", new DataType("currency", "Currency", "java.util.Currency", null, "currency"));

        //Clob e blob
        types.put("clob", new DataType("clob", "Clob", "java.sql.Clob", "clob", "clob"));
        types.put("blob", new DataType("blob", "Blob", "java.sql.Blob", "blob", "blob"));

        //Collection
        types.put("list", new DataType("list", "List", "java.util.List", null, "collection"));
        types.put("set", new DataType("set", "Set", "java.util.Set", null, "collection"));
        types.put("map", new DataType("map", "Map", "java.util.Map", null, "collection"));
    }

    public static DataType get(String key) {
        key = key.toLowerCase();

        for (Map.Entry<String, DataType> type : types.entrySet()) {
            String index = type.getKey();
            DataType dataType = type.getValue();

            if (index.equals(key)) {
                return dataType;
            }
        }

        return null;
    }

    public static boolean isValid(String key) {
        key = key.toLowerCase();
        
        for (Map.Entry<String, DataType> type : types.entrySet()) {
            String index = type.getKey();

            if (index.equals(key)) {
                return true;
            }
        }

        return false;
    }
}
