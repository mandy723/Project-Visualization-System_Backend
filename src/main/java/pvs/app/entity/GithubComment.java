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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        GithubComment that = (GithubComment) o;
//        return id.equals(that.id) &&
//                repoOwner.equals(that.repoOwner) &&
//                repoName.equals(that.repoName) &&
//                createdAt.equals(that.createdAt) &&
//                author.equals(that.author) &&
//                repository.equals(that.repository);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, repoOwner, repoName, createdAt, repository);
//    }
}
