package vn.id.vuductrieu.tlcn_be.controller.mongdb;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.id.vuductrieu.tlcn_be.service.ImageService;

import java.util.List;

@RestController
@RequestMapping("/api/mongo/file")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileMongoController {

    private final ImageService imageService;

    @PostMapping()
    public String uploadImage(@RequestPart("file") MultipartFile file){
        try {
            return imageService.saveImage(file, "test");
        } catch (Exception e){
            return "server";
        }
    }

    @PostMapping("/list")
    public List<String> uploadImages(@RequestPart("files") List<MultipartFile> files){
        try {
            return imageService.saveImages(files, "test");
        } catch (Exception e){
            return null;
        }
    }
}

