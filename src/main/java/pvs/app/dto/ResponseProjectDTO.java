package pvs.app.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseProjectDTO {
    Long projectId;
    String projectName;
    String avatarURL;
    List<RepositoryDTO> repositoryDTOList = new ArrayList<>();
}
