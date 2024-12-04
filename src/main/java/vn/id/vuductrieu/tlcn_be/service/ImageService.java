package vn.id.vuductrieu.tlcn_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import vn.id.vuductrieu.tlcn_be.dto.mongodb.ResponsePredictDto;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))

public class ImageService {

    @Value("${myapp.url.predict}")
    private final String PREDICT_URL;

    private final String STATIC_PATH = "static";

    private final String PATH_IMAGE = "images";

    private final String userDir = System.getProperty("user.dir");

    private final WebClient webClient = WebClient.create();

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


    public List<String> predictImage(List<MultipartFile> files) {
        try {
            // Create a multi-value map for the multipart body
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            // Add files to the body, treating them as FilePart
            for (MultipartFile file : files) {
                body.add("file", file.getResource()); // You can use the "file" as a key here
            }

            // Perform the POST request with multipart form-data
            ResponseEntity<List<ResponsePredictDto>> requestEntity = webClient.post()
                .uri(PREDICT_URL + "/predict")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))  // Use fromMultipartData to handle file parts
                .retrieve()
                .toEntityList(ResponsePredictDto.class)
                .block();

            // Process the response and return the list of filenames
            List<String> result = requestEntity.getBody().stream()
                .map(dto -> {
                    if (dto.getCode() != 0) {
                        return dto.getFilename();
                    }
                    return null;
                })
                // filter out the null values
                .filter(filename -> filename != null).toList();

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to predict files: " + e.getMessage());
        }
    }

}
