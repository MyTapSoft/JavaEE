package Lesson1.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "RELATIONSHIP")
public class Relationship implements Serializable {
    private long userIdFrom;
    private long userIdTo;
    private short status;

    @Id
    @Column(name = "USER_ID_FROM")
    public long getUserIdFrom() {
        return userIdFrom;
    }

    public void setUserIdFrom(long userIdFrom) {
        this.userIdFrom = userIdFrom;
    }

    @Id
    @Column(name = "USER_ID_TO")
    public long getUserIdTo() {
        return userIdTo;
    }

    public void setUserIdTo(long userIdTo) {
        this.userIdTo = userIdTo;
    }

    @Column(name = "STATUS")//	0 - Pending, 1 - Accepted, 2 - Declined, 3 - Deleted
    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relationship that = (Relationship) o;
        return userIdFrom == that.userIdFrom &&
                userIdTo == that.userIdTo &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userIdFrom, userIdTo, status);
    }

    @Override
    public String toString() {
        return "Relationship{" +
                "userIdFrom=" + userIdFrom +
                ", userIdTo=" + userIdTo +
                ", status=" + status +
                '}';
    }
}
