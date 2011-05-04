package com.struts.tool.builder.components;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mayconbordin
 * @version 0.1
 */
public class TypeCollection {
    public static final Map<String, Type> types = new HashMap<String, Type>();

    static {
        //Void
        types.put("void", new Type("void", "void", null, null, "void"));

        //Numeric
        types.put("int", new Type("int", "int", null, "integer", "numeric"));
        types.put("integer", new Type("integer", "int", null, "integer", "numeric"));
        types.put("long", new Type("long", "long", null, "long", "numeric"));
        types.put("short", new Type("short", "short", null, "short", "numeric"));
        types.put("float", new Type("float", "float", null, "float", "numeric"));
        types.put("double", new Type("double", "double", null, "double", "numeric"));
        types.put("bigdecimal", new Type("bigdecimal", "BigDecimal",
                "java.math.BigDecimal", "big_decimal", "numeric"));

        //Boolean
        types.put("boolean", new Type("boolean", "boolean", null, "boolean", "boolean"));

        //Character
        types.put("string", new Type("string", "String", null, "string", "character"));
        types.put("char", new Type("char", "char", null, "character", "character"));
        types.put("character", new Type("char", "char", null, "character", "character"));
        types.put("text", new Type("text", "String", null, "text", "character"));

        //Date
        types.put("date", new Type("date", "Date", "java.util.Date", "date", "date"));
        types.put("time", new Type("time", "Date", "java.util.Date", "time", "date"));
        types.put("timestamp", new Type("timestamp", "Date", "java.util.Date", "timestamp", "date"));

        //Currency
        types.put("currency", new Type("currency", "Currency", "java.util.Currency", null, "currency"));

        //Clob e blob
        types.put("clob", new Type("clob", "Clob", "java.sql.Clob", "clob", "clob"));
        types.put("blob", new Type("blob", "Blob", "java.sql.Blob", "blob", "blob"));

        //Collection
        types.put("set", new Type("set", "Set", "java.util.Set", null, "collection", "new HashSet()", "java.util.HashSet"));
    }

    public static Type get(String key) {
        key = key.toLowerCase();

        for (Map.Entry<String, Type> type : types.entrySet()) {
            String index = type.getKey();
            Type Type = type.getValue();

            if (index.equals(key)) {
                return Type;
            }
        }

        return null;
    }

    public static boolean isValid(String key) {
        key = key.toLowerCase();
        
        for (Map.Entry<String, Type> type : types.entrySet()) {
            String index = type.getKey();

            if (index.equals(key)) {
                return true;
            }
        }

        return false;
    }
}
