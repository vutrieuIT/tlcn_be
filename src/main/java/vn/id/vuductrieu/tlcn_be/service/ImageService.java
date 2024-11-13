package vn.id.vuductrieu.tlcn_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))

public class ImageService {

    private final String STATIC_PATH = "static";

    private final String PATH_IMAGE = "images";

    private final String userDir = System.getProperty("user.dir");

    public String saveImage(MultipartFile file, String folder) {

        String generatedFileName = folder + File.separator + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String image_path = PATH_IMAGE + File.separator + generatedFileName;

        String fullPath = userDir + File.separator + STATIC_PATH + File.separator + image_path;

        File directory = new File(Path.of(userDir, STATIC_PATH, PATH_IMAGE, folder).toString());
        if (!directory.exists()) {
            boolean mked = directory.mkdirs();
            if (!mked) {
                throw new RuntimeException("Failed to create directory: " + directory.getAbsolutePath());
            }
        }

        File image = new File(fullPath);
        try {
            file.transferTo(image);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save file: " + e.getMessage());
        }

        return File.separator + image_path;
    }

    public List<String> saveImages(List<MultipartFile> files, String folder) {
        return files.stream().map(file -> saveImage(file, folder)).toList();
    }
}
