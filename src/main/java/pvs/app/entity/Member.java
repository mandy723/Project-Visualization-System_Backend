package pvs.app.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Member {
    @Id
    @NotNull
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
