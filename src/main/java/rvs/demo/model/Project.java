package rvs.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Project {
    @Id
    @NotNull
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotNull
    private Long memberId;

    @ManyToMany(cascade = { CascadeType.ALL },fetch=FetchType.EAGER)
    @JoinTable(
            name = "project_repository",
            joinColumns = { @JoinColumn(name = "project_id") },
            inverseJoinColumns = { @JoinColumn(name = "repository_id") }
    )
    private Set<Repository> repositorySet = new HashSet<>();

    public Set<Repository> getRepositorySet() {
        return repositorySet;
    }

    public void setRepositorySet(Set<Repository> repositorySet) {
        this.repositorySet = repositorySet;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
