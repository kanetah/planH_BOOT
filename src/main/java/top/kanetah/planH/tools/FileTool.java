package top.kanetah.planH.tools;

import java.io.*;

public class FileTool {

    public static File inputStreamToFile(InputStream ins) {
        try {
            File file = File.createTempFile("planHTemplate", ".tmp");
            OutputStream os = new FileOutputStream(file);
            int bytesRead;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
            return file;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static boolean deleteAll(File file) {
        if (!file.isDirectory())
            return file.delete();
        File[] files = file.listFiles();
        if (files != null)
            for (File f : files)
                if (!deleteAll(f))
                    return false;
        return file.delete();
    }
}
