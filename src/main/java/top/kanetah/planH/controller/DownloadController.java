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
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/download")
public class DownloadController implements InitializingBean {

    private Map<String, String> pathMap = new HashMap<>();
    @Value(value = "${kanetah.planH.downloadFilePropertiesPath}")
    private Resource downloadFilePropertiesResource;

    @Override
    @SuppressWarnings("unchecked")
    public void afterPropertiesSet() throws Exception {
        Map jsonData =
                new ObjectMapper().readValue(downloadFilePropertiesResource.getFile(), Map.class);
        System.out.println("POI__________________________________________________________________");
        jsonData.forEach((k, v) -> {
            System.out.println(k);
            System.out.println(v);
        });
    }

    @RequestMapping("/{fileName}")
    public ResponseEntity<byte[]> download() throws IOException {
        String path = "D:\\workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\springMVC\\WEB-INF\\upload\\图片10（定价后）.xlsx";
        File file = new File(path);
        HttpHeaders headers = new HttpHeaders();
        String fileName = new String("你好.xlsx".getBytes("UTF-8"), "iso-8859-1");//为了解决中文名称乱码问题
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file),
                headers, HttpStatus.CREATED);
    }
}
