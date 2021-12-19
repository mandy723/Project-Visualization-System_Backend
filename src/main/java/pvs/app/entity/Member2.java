package pvs.app.entity;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Member2 implements UserDetails {
    @Id
    @NotNull
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long memberId;

    @Column(unique=true)
    @NotNull
    private String username;

    @NotNull
    private String password;

    @ManyToMany(cascade = { CascadeType.ALL },fetch=FetchType.EAGER)
    @JoinTable(
            name = "member2_role",
            joinColumns = { @JoinColumn(name = "member_id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id") }
    )
    private Set<Role> authorities;

    public Set<Project2> getProject2Set() {
        return project2Set;
    }

    public void setProject2Set(Set<Project2> project2Set) {
        this.project2Set = project2Set;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member2")
    private Set<Project2> project2Set = new HashSet<>();

    /**
     * 用户账号是否过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 用户账号是否被锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 用户密码是否过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 用户是否可用
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
