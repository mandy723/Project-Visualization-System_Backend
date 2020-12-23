package pvs.app.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResponseProjectDTO {
    Long projectId;
    String projectName;
    String avatarURL;
    List<RepositoryDTO> repositoryDTOList = new ArrayList<>();

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public List<RepositoryDTO> getRepositoryDTOList() {
        return repositoryDTOList;
    }

    public void setRepositoryDTOList(List<RepositoryDTO> repositoryDTOList) {
        this.repositoryDTOList = repositoryDTOList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseProjectDTO that = (ResponseProjectDTO) o;
        return Objects.equals(projectId, that.projectId) &&
                Objects.equals(projectName, that.projectName) &&
                Objects.equals(avatarURL, that.avatarURL) &&
                Objects.equals(repositoryDTOList, that.repositoryDTOList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, projectName, avatarURL, repositoryDTOList);
    }
}
