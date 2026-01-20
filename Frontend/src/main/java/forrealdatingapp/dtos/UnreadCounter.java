package forrealdatingapp.dtos;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnreadCounter {
    private String user_id;
    private String matched_user_id;
    private int messageCounter;
    public UnreadCounter(){}
    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getMatched_user_id() {
        return matched_user_id;
    }
    public void setMatched_user_id(String matched_user_id) {
        this.matched_user_id = matched_user_id;
    }
    public int getMessageCounter() {
        return messageCounter;
    }
    public void setMessageCounter(int messageCounter) {
        this.messageCounter = messageCounter;
    };
    


    
}
