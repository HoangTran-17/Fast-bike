package com.motomarket.controlller.restful;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
public class LoadImageAPI {

    @Value("${server.rootPath}")
    private String rootPath;

    @RequestMapping(path = "/api/image/load/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@PathVariable String id) throws IOException {
        File file = new File(rootPath + "/" + id + ".png");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.valueOf(MediaType.IMAGE_PNG_VALUE))
                .body(resource);
    }
}
