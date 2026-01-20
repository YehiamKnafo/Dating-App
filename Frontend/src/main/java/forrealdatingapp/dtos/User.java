package forrealdatingapp.dtos;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    
    
    private String _id;
    private String firstName;
    private String lastName;
    private int age;
    private int minPreferredAge;
    private int maxPreferredAge;
    private String email;
    private String username;
    private List<String> pictures;
    private String profilePicture;
    private String birthDate;
    private String gender;
    private String preferredGender;
    private String bio;
    private String password;
    public int getMinPreferredAge() {
        return minPreferredAge;
    }
    public void setMinPreferredAge(int minPreferredAge) {
        this.minPreferredAge = minPreferredAge;
    }
    public int getMaxPreferredAge() {
        return maxPreferredAge;
    }
    public void setMaxPreferredAge(int maxPreferredAge) {
        this.maxPreferredAge = maxPreferredAge;
    }


    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getPreferredGender() {
        return preferredGender;
    }
    public void setPreferredGender(String preferredGender) {
        this.preferredGender = preferredGender;
    }
    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }

    


    public User(){};
    // public User(String firstName, String lastName, int age,String img) {
    //     this.firstName = firstName;
    //     this.lastName = lastName;
    //     this.age = age;
    //     this.img = img;
    // }
    // public User(String _id, String firstName, String lastName, int age, String img) {
    //     this._id = _id;
    //     this.firstName = firstName;
    //     this.lastName = lastName;
    //     this.age = age;
    //     this.img = img;
    // }
    

    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public int getAge() {
        return age;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public List<String> getPictures() {
        return pictures;
    }
    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }
    public void setAge(int age) {
        this.age = age;
    }
 
    @Override
    public String toString() {
        return "User [_id=" + _id + ", firstName=" + firstName + ", lastName=" + lastName + ", age=" + age + ", email="
                + email + ", username=" + username + ", pictures=" + pictures + ", birthDate=" + birthDate + ", gender="
                + gender + ", preferredGender=" + preferredGender + ", bio=" + bio + ", password=" + password + "]";
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }


    
    
}