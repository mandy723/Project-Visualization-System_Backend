package pvs.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import pvs.app.dao.ProjectDAO;
import pvs.app.dto.*;
import pvs.app.entity.Project;
import pvs.app.entity.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectDAO projectDAO;

    private final GithubApiService githubApiService;

    static final Logger logger = LogManager.getLogger(ProjectService.class.getName());

    public ProjectService(ProjectDAO projectDAO, GithubApiService githubApiService) {
        this.projectDAO = projectDAO;
        this.githubApiService = githubApiService;
    }

    public void create(CreateProjectDTO projectDTO) throws IOException {
        Project savedProject;
        Project project = new Project();
        project.setMemberId(1L);
        project.setName(projectDTO.getProjectName());
        savedProject = projectDAO.save(project);

        if(!projectDTO.getGithubRepositoryURL().equals("")){
            AddGithubRepositoryDTO addGithubRepositoryDTO = new AddGithubRepositoryDTO();
            addGithubRepositoryDTO.setProjectId(savedProject.getProjectId());
            addGithubRepositoryDTO.setRepositoryURL(projectDTO.getGithubRepositoryURL());
            addGithubRepo(addGithubRepositoryDTO);
        }

        if(!projectDTO.getSonarRepositoryURL().equals("")){
            AddSonarRepositoryDTO addSonarRepositoryDTO = new AddSonarRepositoryDTO();
            addSonarRepositoryDTO.setProjectId(savedProject.getProjectId());
            addSonarRepositoryDTO.setRepositoryURL(projectDTO.getSonarRepositoryURL());
            addSonarRepo(addSonarRepositoryDTO);
        }
    }

    public List<ResponseProjectDTO> getMemberProjects(Long memberId) {
        List<Project> projectList = projectDAO.findByMemberId(memberId);
        List<ResponseProjectDTO> projectDTOList = new ArrayList<>();

        for (Project project:projectList) {
            ResponseProjectDTO projectDTO = new ResponseProjectDTO();
            projectDTO.setProjectId(project.getProjectId());
            projectDTO.setProjectName(project.getName());
            projectDTO.setAvatarURL(project.getAvatarURL());
            for(Repository repository: project.getRepositorySet()) {
                RepositoryDTO repositoryDTO = new RepositoryDTO();
                repositoryDTO.setUrl(repository.getUrl());
                repositoryDTO.setType(repository.getType());
                projectDTO.getRepositoryDTOList().add(repositoryDTO);
            }
            projectDTOList.add(projectDTO);
        }
        return projectDTOList;
    }

    public void addSonarRepo(AddSonarRepositoryDTO addSonarRepositoryDTO) {
        Project project = projectDAO.findById(addSonarRepositoryDTO.getProjectId()).get();

        Repository repository = new Repository();
        repository.setUrl(addSonarRepositoryDTO.getRepositoryURL());
        repository.setType("sonar");
        project.getRepositorySet().add(repository);
        projectDAO.save(project);
    }

    public void addGithubRepo(AddGithubRepositoryDTO addGithubRepositoryDTO) throws IOException {
        Project project = projectDAO.findById(addGithubRepositoryDTO.getProjectId()).get();
        String url = addGithubRepositoryDTO.getRepositoryURL();
        Repository repository = new Repository();
        repository.setUrl(url);
        repository.setType("github");
        project.getRepositorySet().add(repository);
        String owner = url.split("/")[3];
        JsonNode responseJson = githubApiService.getAvatarURL(owner);

        if(responseJson != null) {
            String json = responseJson.textValue();
            project.setAvatarURL(json);
        }

        projectDAO.save(project);
    }
}
