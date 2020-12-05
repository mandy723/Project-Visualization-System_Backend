package pvs.app.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String account;

    private String password;

    public Member(){}

    public Member(String account, String password) {
        this.account = account;
        this.password = password;
    }

    @Override
    public String toString() {
        return "MemberAccount{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
