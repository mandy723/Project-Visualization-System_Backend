package pvs.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import pvs.app.dao.ProjectDAO;
import pvs.app.dto.CreateProjectDTO;
import pvs.app.dto.ResponseProjectDTO;
import pvs.app.entity.Project;
import pvs.app.entity.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class ProjectService {
    private final ProjectDAO projectDAO;

    private final GithubApiService githubApiService;

    static final Logger logger = LogManager.getLogger(ProjectService.class.getName());

    private List<String> typeList = Arrays.asList("github", "sonar");

    public ProjectService(ProjectDAO projectDAO, GithubApiService githubApiService) {
        this.projectDAO = projectDAO;
        this.githubApiService = githubApiService;
    }

    public void create(CreateProjectDTO projectDTO) throws IOException {
        Project project = new Project();
        project.setMemberId(1L);
        project.setName(projectDTO.getProjectName());

        String url = projectDTO.getRepositoryURL();
        String owner = url.split("/")[3];

        logger.debug(owner);

        JsonNode responseJson = githubApiService.getAvatarURL(owner);

        logger.debug(responseJson);

        if(responseJson != null) {
            String json = responseJson.textValue();
            project.setAvatarURL(json);
        }

        Repository repository = new Repository();
        repository.setUrl(projectDTO.getRepositoryURL());
        for(String type : typeList) {
            if(projectDTO.getRepositoryURL().contains(type))
                repository.setType(type);
        }
        project.setRepositorySet(Set.of(repository));
        projectDAO.save(project);
    }

    public List<ResponseProjectDTO> getMemberProjects(Long memberId) {
        List<Project> projectList = projectDAO.findByMemberId(memberId);
        List<ResponseProjectDTO> projectDTOList = new ArrayList<>();
        logger.debug(memberId);
        logger.debug(projectList);

        for (Project project:projectList) {
            ResponseProjectDTO projectDTO = new ResponseProjectDTO();
            projectDTO.setProjectId(project.getProjectId());
            projectDTO.setProjectName(project.getName());
            projectDTO.setAvatarURL(project.getAvatarURL());
            projectDTOList.add(projectDTO);
        }
        return projectDTOList;
    }
}
