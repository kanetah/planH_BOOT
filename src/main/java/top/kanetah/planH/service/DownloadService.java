package top.kanetah.planH.service;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DownloadService {

    @Value(value = "${kanetah.planH.downloadFileDirectories}")
    private String downloadFileDirectories;

    public ResponseEntity<byte[]> download(String fileName) throws IOException {
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

    public List<String> getFileList() {
        List<String> fileNames = new ArrayList<>();
        File[] files = new File(downloadFileDirectories).listFiles();
        if (files != null)
            for (File file : files)
                fileNames.add(file.getName());
        return fileNames;
    }
}
