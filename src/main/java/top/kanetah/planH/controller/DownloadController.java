package top.kanetah.planH.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.kanetah.planH.service.DownloadService;

import java.io.*;
import java.util.*;

@Controller
@RequestMapping("/download")
public class DownloadController {

    private final DownloadService downloadService;

    @Autowired
    public DownloadController(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    @RequestMapping("/{fileName:.+}")
    public ResponseEntity<byte[]> download(
            @PathVariable(value = "fileName") String fileName
    ) throws IOException {
        return downloadService.download(fileName);
    }

    @ResponseBody
    @RequestMapping("/fileNames/get")
    public List<String> getFileNames() {
        return downloadService.getFileList();
    }
}
