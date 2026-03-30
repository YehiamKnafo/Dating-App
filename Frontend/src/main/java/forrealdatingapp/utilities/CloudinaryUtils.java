package forrealdatingapp.utilities;

import static forrealdatingapp.utilities.RouterUtils.getCloudinaryUrl;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryUtils {
  
    public static String Upload(File file) {
        try {
            String CLOUDINARY_URL = getCloudinaryUrl();
            Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);
            Map params = ObjectUtils.asMap(
                    "overwrite", true,
                    "resource_type", "image"
            );
            Map uploadResult = cloudinary.uploader().upload(file, params);
            return (String) uploadResult.get("secure_url");
            
        } catch (IOException ex) {
            return null;
        }
    }
    public static boolean deleteFromCloudinaryByUrl(String url) {
        try {
            String CLOUDINARY_URL = getCloudinaryUrl();
            Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);

            String publicId = extractPublicId(url);

            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            System.out.println(result);
            return "ok".equals(result.get("result"));

        } catch (Exception e) {
            return false;
        }
    }
    private static String extractPublicId(String url) {

        String[] parts = url.split("/upload/")[1].split("/");

        StringBuilder publicId = new StringBuilder();

        for (int i = 1; i < parts.length; i++) {
            if (i > 1) publicId.append("/");
            publicId.append(parts[i]);
        }

        String result = publicId.toString();

        int dotIndex = result.lastIndexOf(".");
        if (dotIndex != -1) {
            result = result.substring(0, dotIndex);
        }

        return result;
    }
}
