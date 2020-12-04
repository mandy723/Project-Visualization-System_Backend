package rvs.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Repository {
    @Id
    @NotNull
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotNull
    private String url;

    @NotNull
    private String type;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "repository")
    private Set<GithubCommit> githubCommitSet;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "repository")
    private Set<GithubIssue> githubIssueSet;

    @ManyToMany(mappedBy = "repositorySet", fetch=FetchType.EAGER)
    private Set<Project> projectSet = new HashSet<>();

    public Set<Project> getProjectSet() {
        return projectSet;
    }

    public void setProjectSet(Set<Project> projectSet) {
        this.projectSet = projectSet;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
