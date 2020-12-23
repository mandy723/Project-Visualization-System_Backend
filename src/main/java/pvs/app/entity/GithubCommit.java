package pvs.app.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRepoOwner() {
        return repoOwner;
    }

    public void setRepoOwner(String repoOwner) {
        this.repoOwner = repoOwner;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public Date getCommittedDate() {
        return committedDate;
    }

    public void setCommittedDate(Date committedDate) {
        this.committedDate = committedDate;
    }

    public int getAdditions() {
        return additions;
    }

    public void setAdditions(int additions) {
        this.additions = additions;
    }

    public int getDeletions() {
        return deletions;
    }

    public void setDeletions(int deletions) {
        this.deletions = deletions;
    }

    public int getChangeFiles() {
        return changeFiles;
    }

    public void setChangeFiles(int changeFiles) {
        this.changeFiles = changeFiles;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GithubCommit that = (GithubCommit) o;
        return additions == that.additions &&
                deletions == that.deletions &&
                changeFiles == that.changeFiles &&
                id.equals(that.id) &&
                repoOwner.equals(that.repoOwner) &&
                repoName.equals(that.repoName) &&
                committedDate.equals(that.committedDate) &&
                authorName.equals(that.authorName) &&
                authorEmail.equals(that.authorEmail) &&
                repository.equals(that.repository);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, repoOwner, repoName, committedDate, additions, deletions, changeFiles, authorName, authorEmail, repository);
    }
}
