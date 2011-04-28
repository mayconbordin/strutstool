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
        //Numbers
        types.put("int", new DataType("int", "int", null, "integer"));
        types.put("integer", new DataType("integer", "int", null, "integer"));
        types.put("long", new DataType("long", "long", null, "long"));
        types.put("short", new DataType("short", "short", null, "short"));
        types.put("float", new DataType("float", "float", null, "float"));
        types.put("double", new DataType("double", "double", null, "double"));
        types.put("bigdecimal", new DataType("bigdecimal", "BigDecimal",
                "java.math.BigDecimal", "big_decimal"));

        //Boolean
        types.put("boolean", new DataType("boolean", "boolean", null, "boolean"));

        //Characters
        types.put("string", new DataType("string", "String", null, "string"));
        types.put("char", new DataType("char", "char", null, "character"));
        types.put("character", new DataType("char", "char", null, "character"));
        types.put("text", new DataType("text", "String", null, "text"));

        //Date
        types.put("date", new DataType("date", "Date", "java.util.Date", "date"));
        types.put("time", new DataType("time", "Date", "java.util.Date", "time"));
        types.put("timestamp", new DataType("timestamp", "Date", "java.util.Date", "timestamp"));

        //Currency
        types.put("currency", new DataType("currency", "Currency", "java.util.Currency", null));

        //Clob e blob
        types.put("clob", new DataType("clob", "Clob", "java.sql.Clob", "clob"));
        types.put("blob", new DataType("blob", "Blob", "java.sql.Blob", "blob"));

        //Lists
        types.put("list", new DataType("list", "List", "java.util.List", null));
        types.put("set", new DataType("set", "Set", "java.util.Set", null));
        types.put("map", new DataType("map", "Map", "java.util.Map", null));
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
