package rvs.demo.model;

import com.fasterxml.jackson.databind.JsonNode;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.Date;

public class GithubIssueDTO {
    private String repoOwner;
    private String repoName;
    private Date createdAt;
    private Date closedAt;

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(JsonNode createdAt) {
        DateTimeFormatter isoParser = ISODateTimeFormat.dateTimeNoMillis();
        this.createdAt = isoParser.parseDateTime(createdAt.toString().replace("\"", "")).toDate();
    }

    public Date getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(JsonNode closedAt) {
        DateTimeFormatter isoParser = ISODateTimeFormat.dateTimeNoMillis();
        this.closedAt = isoParser.parseDateTime(closedAt.toString().replace("\"", "")).toDate();
    }
}
