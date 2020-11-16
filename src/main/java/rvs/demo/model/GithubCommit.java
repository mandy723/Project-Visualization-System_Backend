package rvs.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "github_commit")
public class GithubCommit {

    @Id
    @NotNull
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name="REPO_OWNER")
    private String repoOwner;

    @NotNull
    @Column(name="REPO_NAME")
    private String repoName;

    @NotNull
    @Column(name="COMMITTED_DATE")
    private Date committedDate;

    @NotNull
    @Column(name="ADDITIONS")
    private int additions;

    @NotNull
    @Column(name="DELETIONS")
    private int deletions;

    @NotNull
    @Column(name="CHANGE_FILES")
    private int changeFiles;

    @NotNull
    @Column(name="AUTHOR_NAME")
    private String authorName;

    @NotNull
    @Column(name="AUTHOR_EMAIL")
    private String authorEmail;

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
}
