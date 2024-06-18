package vn.id.vuductrieu.tlcn_be.service;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CloudaryService {

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile image, String path) throws IOException {

        String namefile = path + "/file" + + System.currentTimeMillis();

        Map options = ObjectUtils.asMap(
                "use_filename", true,
                "unique_filename", false,
                "overwrite", true,
                "public_id", namefile
        );
        Map result = cloudinary.uploader().upload(image.getBytes(), options);
        return (String) result.get("url");
    }
}
