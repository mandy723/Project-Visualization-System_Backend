package rvs.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Column(name="ACCOUNT")
    private String account;
    @Column(name="PASSWORD")
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
