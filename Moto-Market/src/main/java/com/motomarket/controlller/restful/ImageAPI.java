package com.motomarket.controlller.restful;

import com.motomarket.service.post.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
public class ImageAPI {

    @Value("${server.rootPath}")
    private String rootPath;

    @Autowired
    private ImageService imageService;

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

    @DeleteMapping("/api/image/detele/{idImage}")
    public ResponseEntity<String> deleteImage(@PathVariable Long idImage){
        imageService.remove(idImage);
        return (ResponseEntity<String>) ResponseEntity.ok();
    }
}
