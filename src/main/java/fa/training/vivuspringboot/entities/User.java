package fa.training.vivuspringboot.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import java.util.Set;

@Entity
@Table(name = "users")
public class User extends MasterEntity {

    @Column(nullable = false, length = 50)
    @Nationalized
    private String firstName;

    @Column(nullable = false, length = 50)
    @Nationalized
    private String lastName;

    @Transient
    private String displayName;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 12)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    private String avatar;

    @ManyToMany
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public String getDisplayName() {
        return firstName + " " + lastName;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public User() {
    }

    public User(String firstName, String lastName, String username, String phoneNumber, String password, String avatar) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.avatar = avatar;
    }
}
