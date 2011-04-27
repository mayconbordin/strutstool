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
        types.put("int", new DataType("int", "int", null, "integer"));
        types.put("integer", new DataType("integer", "int", null, "integer"));
        types.put("double", new DataType("double", "double", null, "double"));
        types.put("string", new DataType("string", "String", null, "string"));
        types.put("char", new DataType("char", "char", null, "character"));
        types.put("date", new DataType("date", "Date", "java.util.Date", "date"));
        types.put("timestamp", new DataType("timestamp", "Date", "java.util.Date", "timestamp"));
        types.put("list", new DataType("list", "List", "java.util.List", null));
        types.put("currency", new DataType("currency", "Currency", "java.util.Currency", null));
        types.put("set", new DataType("set", "Set", "java.util.Set", null));
        types.put("map", new DataType("map", "Map", "java.util.Map", null));
    }

    public static boolean isValid(String key) {
        key = key.toLowerCase();
        
        for (Map.Entry<String, DataType> type : types.entrySet()) {
            String index = type.getKey();

            if (index.indexOf(key) != -1) {
                return true;
            }
        }

        return false;
    }
}
