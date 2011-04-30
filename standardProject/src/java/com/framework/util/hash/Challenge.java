package com.framework.util.hash;

import com.framework.util.string.StringUtil;

/**
 *
 * @author maycon
 */
public class Challenge {
    public static String generate() {
        String str = StringUtil.generateRandomString(40) + System.currentTimeMillis();
        return SHA256.calculate(str);
    }
}
