package forrealdatingapp.credentials;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"msg","token"})
public class Credentials {
    private String expressUrl;
    private String cloudinaryUrl;

    public String getCloudinaryUrl() {
        return cloudinaryUrl;
    }

    public void setCloudinaryUrl(String cloudinaryUrl) {
        this.cloudinaryUrl = cloudinaryUrl;
    }


    public String getExpressUrl() {
        return expressUrl;
    }

    public void setExpressUrl(String expressUrl) {
        this.expressUrl = expressUrl;
    }
}
