package top.kanetah.planH.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.kanetah.planH.info.InfoImpl;

import java.io.*;
import java.util.*;

@Controller
@RequestMapping("/download")
public class DownloadController implements InitializingBean {

    private Map<String, String> pathMap = new HashMap<>();
    @Value(value = "${kanetah.planH.downloadFilePropertiesPath}")
    private Resource downloadFilePropertiesResource;

    @SuppressWarnings("unchecked")
    @Override
    public void afterPropertiesSet() throws Exception {
        new ObjectMapper().readValue(
                InfoImpl.inputStreamToFile(
                        downloadFilePropertiesResource.getInputStream()
                ),
                Map.class
        ).forEach((k, v) -> {
            String value = v.toString();
            value = value.substring(1, value.length() - 1);
            String[] values = value.split(", ");
            for (String fileName : values)
                pathMap.put(fileName, k + fileName);
        });
    }

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
                FileUtils.readFileToByteArray(
                        new File(pathMap.get(fileName))
                ),
                headers,
                HttpStatus.CREATED
        );
    }

    @ResponseBody
    @RequestMapping("/fileNames/get")
    public Set<String> getFileNames() {
        return pathMap.keySet();
    }
}
