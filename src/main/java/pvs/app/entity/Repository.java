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

    // 新版的 hibernate @JoinColumn 與 mappedBy 互斥，所以對應的 entity 不可以放 @ManyToOne，參考下方網址
    // https://www.twblogs.net/a/5b9847512b717736c6238e56
    @OneToMany(cascade = { CascadeType.ALL })
    @JoinColumn(name = "repository_id")
    private Set<GithubCommit> githubCommitSet = new HashSet<>();

    @OneToMany(cascade = { CascadeType.ALL })
    @JoinColumn(name = "repository_id")
    private Set<GithubComment> githubCommentsSet = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "repositorySet")
    private Set<Project> projectSet;

    public void removeProject(Project project) {
        projectSet.remove(project);
    }

    public Set<GithubCommit> getGithubCommitSet() {
        return githubCommitSet;
    }

    public Set<GithubComment> getGithubCommentSet() {
        return githubCommentsSet;
    }
}
