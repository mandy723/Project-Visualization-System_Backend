package pvs.app.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
public class GithubCommit {

    @Id
    @NotNull
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotNull
    private String repoOwner;

    @NotNull
    private String repoName;

    @NotNull
    private Date committedDate;

    @NotNull
    private int additions;

    @NotNull
    private int deletions;

    @NotNull
    private int changeFiles;

    @NotNull
    private String authorName;

    @NotNull
    private String authorEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="repository_id")
    private Repository repository;
}
