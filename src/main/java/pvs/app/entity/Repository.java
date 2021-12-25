package pvs.app.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
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

    @OneToMany(mappedBy = "repository", cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "repository_id")
    private Set<GithubCommit> githubCommitSet;

    @OneToMany(mappedBy = "repository", cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "repository_id")
    private Set<GithubComment> githubCommentsSet;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "repositorySet")
    private Set<Project> projectSet;

    public Set<GithubCommit> getGithubCommitSet() {
        return githubCommitSet;
    }

    public Set<GithubComment> getGithubCommentSet() {
        return githubCommentsSet;
    }
}
