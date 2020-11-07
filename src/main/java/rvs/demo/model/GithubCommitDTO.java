package rvs.demo.model;

import com.fasterxml.jackson.databind.JsonNode;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Date;
import java.util.Optional;

public class GithubCommitDTO {
    private String repoOwner;
    private String repoName;
    private Date committedDate;
    private int additions;
    private int deletions;
    private int changeFiles;
    private String authorName;
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

    public void setAuthor(Optional<JsonNode> authorJson) {
        Optional<JsonNode> authorName = authorJson.map(s -> s.get("name"));
        Optional<JsonNode> authorEmail = authorJson.map(s -> s.get("email"));
        authorName.ifPresent(s -> this.authorName = s.toString());
        authorEmail.ifPresent(s -> this.authorEmail = s.toString());
    }

    public void setCommittedDate(JsonNode committedDate) {
        DateTimeFormatter isoParser = ISODateTimeFormat.dateTimeNoMillis();
        this.committedDate =
                isoParser.parseDateTime(committedDate.toString().replace("\"", "")).toDate();
    }
}
