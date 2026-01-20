package forrealdatingapp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
    private String senderID;
    private String senderUsername;
    private String recieverID;
    private String recieverUsername;
    private String message;

    public Message(){};
    public String getSenderID() {
        return senderID;
    }
    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }
    public String getSenderUsername() {
        return senderUsername;
    }
    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }
    public String getRecieverID() {
        return recieverID;
    }
    public void setRecieverID(String recieverID) {
        this.recieverID = recieverID;
    }
    public String getRecieverUsername() {
        return recieverUsername;
    }
    public void setRecieverUsername(String recieverUsername) {
        this.recieverUsername = recieverUsername;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
