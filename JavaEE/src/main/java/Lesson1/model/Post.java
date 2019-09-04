package Lesson1.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "POST")
public class Post implements IdEntity {
    private Long id;
    private String message;
    private Date datePosted;
    private String location;
    private Set<User> usersTagged = new HashSet<>();
    private User userPosted;
    private User userPagePosted;

    @Id
    @SequenceGenerator(name = "POST_SEQ", sequenceName = "POST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "POST_SEQ")
    @Column(name = "POST_ID")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Column(name = "MESSAGE")
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    @Column(name = "DATE_POSTED")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-DD HH:mm:ss")
    public Date getDatePosted() {
        return datePosted;
    }
    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }
    @Column(name = "LOCATION")
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(
            name = "USERS_POST",
            joinColumns = {@JoinColumn(name = "POST_ID")},
            inverseJoinColumns = {@JoinColumn(name = "USER_ID")}
    )
    public Set<User> getUsersTagged() {
        return usersTagged;
    }
    public void setUsersTagged(Set<User> usersTagged) {
        this.usersTagged = usersTagged;
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_POSTED")
    @JsonBackReference
    public User getUserPosted() {
        return userPosted;
    }

    public void setUserPosted(User userPosted) {
        this.userPosted = userPosted;
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_PAGE_POSTED")
    public User getUserPagePosted() {
        return userPagePosted;
    }

    public void setUserPagePosted(User userPagePosted) {
        this.userPagePosted = userPagePosted;
    }
    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", datePosted=" + datePosted +
                ", location='" + location + '\'' +
                ", usersTagged=" + usersTagged.size() +
                ", userPosted=" + userPosted.getId() +
                ", userPagePosted=" + userPagePosted.getId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) &&
                Objects.equals(message, post.message) &&
                Objects.equals(datePosted, post.datePosted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, datePosted);
    }
}