package top.kanetah.planH.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.util.*;

@Controller
@RequestMapping("/download")
public class DownloadController {

    @Value(value = "${kanetah.planH.downloadFileDirectories}")
    private String downloadFileDirectories;

    @RequestMapping("/{fileName:.+}")
    public ResponseEntity<byte[]> download(
            @PathVariable(value = "fileName") String fileName
    ) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData(
                "attachment",
                new String(
                        fileName.getBytes("UTF-8"),
                        "iso-8859-1"
                )
        );
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(
                FileUtils.readFileToByteArray(new File(
                        downloadFileDirectories + "/" + fileName
                )),
                headers,
                HttpStatus.CREATED
        );
    }

    @ResponseBody
    @RequestMapping("/fileNames/get")
    public List<String> getFileNames() {
        return getFileList(downloadFileDirectories);
    }

    private List<String> getFileList(String strPath) {
        List<String> fileNames = new ArrayList<>();
        File[] files = new File(strPath).listFiles();
        if (files != null)
            for (File file : files)
                fileNames.add(file.getName());
        return fileNames;
    }
}
