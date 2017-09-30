package top.kanetah.planH.format;

import top.kanetah.planH.tools.CompactTool;
import top.kanetah.planH.tools.FileTool;

import java.io.File;

class CompactFileProcessor {
    static void handleCompactFile(File target, String path, String fileType){
        switch (fileType.toLowerCase()) {
            case ".zip":
                CompactTool.unZip(target, path + "/");
                break;
            case ".rar":
                CompactTool.unRar(target, path + "/");
                break;
            default:
                throw new FileTypeException();
        }
        File[] files = new File(path).listFiles();
        if (files == null)
            throw new CompactFileException();
        else if (files.length == 1 && files[0].isDirectory()) {
            File dir = files[0];
            files = dir.listFiles();
            if (files == null)
                throw new CompactFileException();
            for (File f : files)
                if (!f.renameTo(new File(path + "/" + f.getName()))
                        && !FileTool.deleteAll(f))
                    throw new FileException();
            if (!dir.delete())
                throw new FileException();
        }
        if (!target.delete())
            throw new FileException();
    }
}
