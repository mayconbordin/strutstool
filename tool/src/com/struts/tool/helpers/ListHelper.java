package com.struts.tool.helpers;

import java.util.HashSet;
import java.util.List;

/**
 *
 * @author maycon
 */
public class ListHelper {
    public static void removeDuplicate(List arlList) {
        HashSet h = new HashSet(arlList);
        arlList.clear();
        arlList.addAll(h);
    }

    public static String[] toStringArray(List list) {
        String[] array = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof String) {
                array[i] = (String) list.get(i);
            } else {
                array[i] = list.get(i).toString();
            }
        }

        return array;
    }
}
