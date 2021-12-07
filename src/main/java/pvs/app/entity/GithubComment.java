package pvs.app.entity;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
public class GithubComment {
    @Id
    @NotNull
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotNull
    private String repoName;

    @NotNull
    private String repoOwner;

    @NotNull
    private String author;

    @NotNull
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="repository_id")
    private Repository repository;
}
