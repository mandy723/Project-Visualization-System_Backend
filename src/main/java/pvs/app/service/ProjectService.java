package pvs.app.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import pvs.app.controller.GithubApiController;
import pvs.app.dao.ProjectDAO;
import pvs.app.dto.CreateProjectDTO;
import pvs.app.dto.ResponseProjectDTO;
import pvs.app.entity.Project;
import pvs.app.entity.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ProjectService {
    private final ProjectDAO projectDAO;

    static final Logger logger = LogManager.getLogger(ProjectService.class.getName());

    public ProjectService(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    public void create(CreateProjectDTO projectDTO) {
        Project project = new Project();
        project.setMemberId(1L);
        project.setName(projectDTO.getProjectName());
        Repository repository = new Repository();
        repository.setUrl(projectDTO.getRepositoryURL());
        repository.setType("GitHub");
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
            projectDTO.setProjectName(project.getName());
            projectDTOList.add(projectDTO);
        }
        return projectDTOList;
    }
}
