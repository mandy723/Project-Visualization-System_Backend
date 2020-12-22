package pvs.app.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CodeCoverageDTO {
    Date date;
    Double value;

    public CodeCoverageDTO(Date date, Double value) {
        this.date = date;
        this.value = value;
    }
}
