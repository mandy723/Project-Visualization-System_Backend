package pvs.app.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.Date;

@Data
public class GithubIssueDTO {
    private String repoOwner;
    private String repoName;
    private Date createdAt;
    private Date closedAt;
    private String author;

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
        if(closedAt != null && closedAt.textValue() != null) {
            DateTimeFormatter isoParser = ISODateTimeFormat.dateTimeNoMillis();
            this.closedAt = isoParser.parseDateTime(closedAt.toString().replace("\"", "")).toDate();
        }
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
