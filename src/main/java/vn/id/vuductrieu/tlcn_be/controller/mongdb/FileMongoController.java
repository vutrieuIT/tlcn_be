package vn.id.vuductrieu.tlcn_be.controller.mongdb;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.id.vuductrieu.tlcn_be.service.MongoService.ImageService;

import java.util.List;
import java.util.Map;

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

    @PostMapping("/admin/list")
    public ResponseEntity uploadImages(@RequestPart("files") List<MultipartFile> files){
        try {
            List<String> result = imageService.predictImage(files);
            List<String> images = imageService.saveImages(files, "test");

            return ResponseEntity.ok(Map.of(
                "predict", result,
                "images", images
            ));
        } catch (Exception e){
            return null;
        }
    }
}

