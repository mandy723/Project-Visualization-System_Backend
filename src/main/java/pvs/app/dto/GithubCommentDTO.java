package pvs.app.dto;

import com.fasterxml.jackson.databind.JsonNode;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import java.util.Date;

public class GithubCommentDTO {

    private String author;
    private Date createdAt;

    public String getAuthor() {
        return author;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(JsonNode createdAt) {
        DateTimeFormatter isoParser = ISODateTimeFormat.dateTimeNoMillis();
        this.createdAt =
                isoParser.parseDateTime(createdAt.toString().replace("\"", "")).toDate();
    }
}
