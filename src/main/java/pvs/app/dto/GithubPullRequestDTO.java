package pvs.app.dto;

import com.fasterxml.jackson.databind.JsonNode;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import java.util.Date;

public class GithubPullRequestDTO {
    private Date createdAt;
    private Date mergedAt;
    private String state;
    private String author;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(JsonNode createdAt) {
        DateTimeFormatter isoParser = ISODateTimeFormat.dateTimeNoMillis();
        this.createdAt = isoParser.parseDateTime(createdAt.toString().replace("\"", "")).toDate();
    }

    public Date getMergedAt() {
        return mergedAt;
    }

    public void setMergedAt(JsonNode mergedAt) {
        DateTimeFormatter isoParser = ISODateTimeFormat.dateTimeNoMillis();
        String result = mergedAt.toString();
        if(result != "null"){
            this.mergedAt = isoParser.parseDateTime(result.replace("\"", "")).toDate();
        }
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
