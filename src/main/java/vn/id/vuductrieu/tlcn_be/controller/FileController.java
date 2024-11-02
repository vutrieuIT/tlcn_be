package vn.id.vuductrieu.tlcn_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.id.vuductrieu.tlcn_be.service.ImageService;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileController {

    private final ImageService imageService;

    @PostMapping()
    public String uploadImage(@RequestPart("file") MultipartFile file){
        try {
            return imageService.saveImage(file, "test");
        } catch (Exception e){
            return "server";
        }
    }
}
