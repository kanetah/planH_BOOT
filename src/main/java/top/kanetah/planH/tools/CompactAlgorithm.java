package top.kanetah.planH.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CompactAlgorithm {

    private File targetFile;

    private CompactAlgorithm(File target) {
        targetFile = target;
        if (targetFile.exists())
            if (!targetFile.delete())
                throw new RuntimeException("Can nor delete file '" + targetFile.getName() + "'.");
    }

    public CompactAlgorithm(String targetPath) {
        this(new File(targetPath));
    }

    private File zipFiles(File srcFile) {
        try {
            ZipOutputStream out = null;
            try {
                out = new ZipOutputStream(new FileOutputStream(targetFile));
                if (srcFile.isFile())
                    zipFile(srcFile, out, "");
                else {
                    File[] list = srcFile.listFiles();
                    assert list != null;
                    for (File aList : list)
                        compress(aList, out, "");
                }
            } finally {
                if (out != null)
                    out.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return targetFile;
    }

    public File zipFiles(String srcPath) {
        return zipFiles(new File(srcPath));
    }

    private void compress(File file, ZipOutputStream out, String basedir) {
        if (file.isDirectory())
            this.zipDirectory(file, out, basedir);
        else
            this.zipFile(file, out, basedir);
    }

    private void zipFile(File srcFile, ZipOutputStream out, String basedir) {
        if (!srcFile.exists())
            return;

        byte[] buf = new byte[1024];
        FileInputStream in = null;
        try {
            try {
                int len;
                in = new FileInputStream(srcFile);
                out.putNextEntry(new ZipEntry(basedir + srcFile.getName()));

                while ((len = in.read(buf)) > 0)
                    out.write(buf, 0, len);
            } finally {
                if (out != null)
                    out.closeEntry();
                if (in != null)
                    in.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void zipDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists())
            return;

        File[] files = dir.listFiles();
        assert files != null;
        for (File file : files)
            compress(file, out, basedir + dir.getName() + "/");
    }
}
