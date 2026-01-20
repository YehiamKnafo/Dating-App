package forrealdatingapp.utilities;

import static forrealdatingapp.routes.RouterUtils.getCludinaryUrl;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryUtils {
  
    public static String Upload(File file) {
        try {
            String CLOUDINARY_URL = getCludinaryUrl();
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
}
