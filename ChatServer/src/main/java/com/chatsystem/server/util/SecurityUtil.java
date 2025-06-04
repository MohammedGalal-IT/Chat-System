package com.chatsystem.server.util;

import org.mindrot.jbcrypt.BCrypt;

public class SecurityUtil {

    // تشفير كلمة المرور
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // تحقق من كلمة المرور
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
    
}
