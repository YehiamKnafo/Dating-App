package forrealdatingapp.dtos;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginClass {
    private String username;
    private String password;

    public LoginClass() {}

    public LoginClass(String username, String password) {
        this.username = username;
        this.password = password;
        
        
    }

    public String getusername() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LoginClass{");
        sb.append("username=").append(username);
        sb.append(", password=").append(password);
        sb.append('}');
        return sb.toString();
    }
    
    
    

}
