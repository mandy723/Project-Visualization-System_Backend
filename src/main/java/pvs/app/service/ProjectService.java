package pvs.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pvs.app.dao.ProjectDAO;
import pvs.app.dao.RepositoryDAO;
import pvs.app.dto.*;
import pvs.app.entity.Project;
import pvs.app.entity.Repository;
import java.io.IOException;
import java.util.*;

@Service
public class ProjectService {
    private final ProjectDAO projectDAO;
    private final RepositoryDAO repositoryDAO;
    private final GithubApiService githubApiService;

    public ProjectService(ProjectDAO projectDAO, RepositoryDAO repositoryDAO, GithubApiService githubApiService) {
        this.projectDAO = projectDAO;
        this.repositoryDAO = repositoryDAO;
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

    public boolean addSonarRepo(AddSonarRepositoryDTO addSonarRepositoryDTO) {
        Optional<Project> projectOptional = projectDAO.findById(addSonarRepositoryDTO.getProjectId());
        if(projectOptional.isPresent()) {
            Project project = projectOptional.get();
            Repository repository = new Repository();
            repository.setUrl(addSonarRepositoryDTO.getRepositoryURL());
            repository.setType("sonar");
            project.getRepositorySet().add(repository);
            repositoryDAO.save(repository);
            projectDAO.save(project);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean addGithubRepo(AddGithubRepositoryDTO addGithubRepositoryDTO) throws IOException {
        Optional<Project> projectOptional = projectDAO.findById(addGithubRepositoryDTO.getProjectId());
        if(projectOptional.isPresent()) {
            Project project = projectOptional.get();
            String url = addGithubRepositoryDTO.getRepositoryURL();

            Repository repository = repositoryDAO.findByUrl(url);
            if(repository == null){
                repository = new Repository();
                repository.setUrl(url);
                repository.setType("github");
                repositoryDAO.save(repository);
            }
            project.getRepositorySet().add(repository);

            String owner = url.split("/")[3];
            JsonNode responseJson = githubApiService.getAvatarURL(owner);
            if(null != responseJson) {
                String json = responseJson.textValue();
                project.setAvatarURL(json);
            }
            projectDAO.save(project);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public void deleteProject(Long projectId) throws IOException {
        Project project = projectDAO.findById(projectId).get();
        for (Repository repo : project.getRepositorySet()) {
            if (repo.getProjectSet().size() == 1) {
                repositoryDAO.deleteById(repo.getRepositoryId());
            }
            else {
                repo.removeProject(project);
                repositoryDAO.save(repo);
            }
        }
        // owner 方會自動清除 repository 關聯，及清空 repositorySet，但不會刪掉該筆 repository，因為沒有設定 CascadeType.REMOVE
        projectDAO.deleteById(projectId);
    }
}
