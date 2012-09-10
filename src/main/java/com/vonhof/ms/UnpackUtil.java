package com.vonhof.ms;

import java.io.File;

public class UnpackUtil {
    public static boolean unpack(File file,File destDir) {
        if (!file.exists()) return false;
        
        if (file.getName().toLowerCase().endsWith(".rar")) {
            RarUtil.extractArchive(file, destDir);
            return true;
        }
        
        if (file.getName().toLowerCase().endsWith(".zip")) {
            ZipUtil.extractZip(file.getAbsolutePath(), file.getAbsolutePath());
            return true;
        }
        
        return false;
    }
}
