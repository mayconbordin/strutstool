package com.struts.tool.util;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author maycon
 */
public class ExtArrayList<E extends Object> extends ArrayList<E> {
    public ExtArrayList(E... objects) {
        addAll(Arrays.asList(objects));
    }

    public E get(String key) {
        for (E obj : this) {
            if (obj.toString().equals(key)) {
                return obj;
            }
        }

        return null;
    }

    public void addAll(E... objects) {
        addAll(Arrays.asList(objects));
    }
}
