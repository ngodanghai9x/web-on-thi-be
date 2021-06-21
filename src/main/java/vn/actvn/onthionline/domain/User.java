package vn.actvn.onthionline.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User implements Serializable, UserDetails {
    private static final long serialVersionUID = 1L;

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_name")
    private String username;

    @Column(name = "full_name")
    private String fullname;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password")
    private String password;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "birthday")
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthday;

    @Column(name = "gender")
    private String gender;

    @Column(name = "city")
    private String city;

    @Column(name = "class")
    private String clazz;

    @Column(name = "school")
    private String school;

    @Column(name = "online_time")
    private Integer onlineTime;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();
//    public void setCreatedDate(Date createdDate) {
//        this.createdDate = new Date();
//    }
//    public void setLastLogin(Date lastLogin) {
//        this.lastLogin = new Date();
//    }

    @Column(name = "last_login")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin = new Date();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "userCreated")
    private Set<ExamHistory> examHistories = new HashSet<>();

    @OneToMany(mappedBy = "userCreated")
    private Set<Comment> comments = new HashSet<>();

    @Transient
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Transient
    @JsonIgnore
    @Override
    public String getUsername() {
        return username;
    }

    @Transient
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Transient
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Transient
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Transient
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, fullname, password, avatar, phone, email, birthday, gender, city, clazz, school, isActive, onlineTime, createdDate, lastLogin);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) &&
                Objects.equals(getUsername(), user.getUsername()) &&
                Objects.equals(getFullname(), user.getFullname()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                Objects.equals(getPhone(), user.getPhone()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getAvatar(), user.getAvatar()) &&
                Objects.equals(getBirthday(), user.getBirthday()) &&
                Objects.equals(getGender(), user.getGender()) &&
                Objects.equals(getCity(), user.getCity()) &&
                Objects.equals(getClazz(), user.getClazz()) &&
                Objects.equals(getSchool(), user.getSchool()) &&
                Objects.equals(getOnlineTime(), user.getOnlineTime()) &&
                Objects.equals(getIsActive(), user.getIsActive()) &&
                Objects.equals(getCreatedDate(), user.getCreatedDate()) &&
                Objects.equals(getLastLogin(), user.getLastLogin()) &&
                Objects.equals(getRoles(), user.getRoles()) &&
                Objects.equals(getExamHistories(), user.getExamHistories()) &&
                Objects.equals(getComments(), user.getComments());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", birthday=" + birthday +
                ", gender='" + gender + '\'' +
                ", city='" + city + '\'' +
                ", clazz='" + clazz + '\'' +
                ", school='" + school + '\'' +
                ", onlineTime=" + onlineTime +
                ", isActive=" + isActive +
                ", createdDate=" + createdDate +
                ", lastLogin=" + lastLogin +
                '}';
    }
}
