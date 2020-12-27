package pvs.app.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class Repository {
    @Id
    @NotNull
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long repositoryId;

    @NotNull
    private String url;

    @NotNull
    private String type;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "repository")
    private Set<GithubCommit> githubCommitSet;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "repository")
    private Set<GithubIssue> githubIssueSet;

    public Long getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(Long repositoryId) {
        this.repositoryId = repositoryId;
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

    public Set<GithubCommit> getGithubCommitSet() {
        return githubCommitSet;
    }

    public void setGithubCommitSet(Set<GithubCommit> githubCommitSet) {
        this.githubCommitSet = githubCommitSet;
    }

    public Set<GithubIssue> getGithubIssueSet() {
        return githubIssueSet;
    }

    public void setGithubIssueSet(Set<GithubIssue> githubIssueSet) {
        this.githubIssueSet = githubIssueSet;
    }
}
