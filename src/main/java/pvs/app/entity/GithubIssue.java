package pvs.app.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
public class GithubIssue {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String repoOwner;

    @NotNull
    private String repoName;

    @NotNull
    private Date createdAt;

    @NotNull
    private Date closedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="repository_id")
    private Repository repository;
}
