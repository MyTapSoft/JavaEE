package Lesson1.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "RELATIONSHIP")
public class Relationship implements Serializable {
    private Long userIdFrom;
    private Long userIdTo;
    private RelationshipStatus status;
    private Date friendsRequestDate;


    @Id
    @Column(name = "USER_ID_FROM")
    public Long getUserIdFrom() {
        return userIdFrom;
    }

    public void setUserIdFrom(Long userIdFrom) {
        this.userIdFrom = userIdFrom;
    }

    @Id
    @Column(name = "USER_ID_TO")
    public Long getUserIdTo() {
        return userIdTo;
    }

    public void setUserIdTo(Long userIdTo) {
        this.userIdTo = userIdTo;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    public RelationshipStatus getStatus() {
        return status;
    }

    public void setStatus(RelationshipStatus status) {
        this.status = status;
    }

    @Column(name = "FRIENDS_REQUEST_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-DD HH:mm:ss")
    public Date getFriendsRequestDate() {
        return friendsRequestDate;
    }

    public void setFriendsRequestDate(Date friendsRequestDate) {
        this.friendsRequestDate = friendsRequestDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relationship that = (Relationship) o;
        return Objects.equals(userIdFrom, that.userIdFrom) &&
                Objects.equals(userIdTo, that.userIdTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userIdFrom, userIdTo);
    }

    @Override
    public String toString() {
        return "Relationship{" +
                "userIdFrom=" + userIdFrom +
                ", userIdTo=" + userIdTo +
                ", certainChainStatus=" + status +
                '}';
    }

}
