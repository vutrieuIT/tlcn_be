package vn.id.vuductrieu.tlcn_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.id.vuductrieu.tlcn_be.service.CloudaryService;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileController {

    private final CloudaryService cloudaryService;

    @PostMapping()
    public String uploadImage(@RequestPart("file") MultipartFile file){
        try {
            return cloudaryService.uploadImage(file, "test");
        } catch (Exception e){
            return "server";
        }
    }
}
