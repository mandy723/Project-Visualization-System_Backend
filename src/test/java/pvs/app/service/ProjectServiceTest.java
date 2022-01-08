package pvs.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pvs.app.Application;
import pvs.app.dao.ProjectDAO;
import pvs.app.dao.RepositoryDAO;
import pvs.app.dto.CreateProjectDTO;
import pvs.app.dto.ResponseProjectDTO;
import pvs.app.entity.Project;
import pvs.app.entity.Repository;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ProjectServiceTest {
    @Autowired
    private ProjectService projectService;

    @MockBean
    private GithubApiService githubApiService;

    @MockBean
    private ProjectDAO projectDAO;

    @MockBean
    private RepositoryDAO repositoryDAO;

    CreateProjectDTO projectDTO;

    Project project;
    Project project2;

    Set<Repository> repositorySet;
    Set<Repository> repositorySet2;

    Repository githubRepository;
    Set<Project> projectSet;

    Repository githubRepositoryWithMultipleProjects;
    Set<Project> projectSetWithMultipleProjects;

    final String responseJson = "{\"avatarUrl\":\"https://avatars3.githubusercontent.com/u/17744001?u=038d9e068c4205d94c670d7d89fb921ec5b29782&v=4\"}";
    Optional<JsonNode> mockAvatar;

    @Before
    public void setup() throws IOException {
        projectDTO = new CreateProjectDTO();
        projectDTO.setProjectName("react");
        projectDTO.setGithubRepositoryURL("https://github.com/facebook/react");
        projectDTO.setSonarRepositoryURL("http://localhost:9000/dashboard?id=pvs-springboot");

        project = new Project();
        project.setProjectId(1L);
        project.setMemberId(1L);
        project.setName(projectDTO.getProjectName());

        project2 = new Project();
        project2.setProjectId(1L);
        project2.setMemberId(1L);
        project2.setName(projectDTO.getProjectName());

        repositorySet2 = new HashSet<>();
        repositorySet2.add(githubRepositoryWithMultipleProjects);
        project2.setRepositorySet(repositorySet2);

        githubRepository = new Repository();
        githubRepository.setType("github");
        githubRepository.setUrl("https://github.com/facebook/react");
        githubRepository.setRepositoryId(1L);

        githubRepositoryWithMultipleProjects = new Repository();
        githubRepositoryWithMultipleProjects.setType("github");
        githubRepositoryWithMultipleProjects.setUrl("https://github.com/SEWinWinWin/PVS_backend");
        githubRepositoryWithMultipleProjects.setRepositoryId(1L);

        projectSetWithMultipleProjects = new HashSet<>();
        projectSetWithMultipleProjects.add(project);
        projectSetWithMultipleProjects.add(project2);
        githubRepositoryWithMultipleProjects.setProjectSet(projectSetWithMultipleProjects);

        repositorySet = new HashSet<>();
        repositorySet.add(githubRepository);
        repositorySet.add(githubRepositoryWithMultipleProjects);
        project.setRepositorySet(repositorySet);

        projectSet = new HashSet<>();
        projectSet.add(project);
        githubRepository.setProjectSet(projectSet);


        ObjectMapper mapper = new ObjectMapper();
        mockAvatar = Optional.ofNullable(mapper.readTree(responseJson));
    }

    @Test
    public void create() throws IOException {
        //context
        when(githubApiService.getAvatarURL("facebook"))
                .thenReturn(mockAvatar.orElse(null));

        when(projectDAO.save(any(Project.class)))
                .thenReturn(project);
        when(projectDAO.findById(1L))
                .thenReturn(Optional.of(project));

        //when
        projectService.create(projectDTO);

        //then
        verify(githubApiService, times(1)).getAvatarURL("facebook");
    }

    @Test
    public void getMemberProjects() {
        //given
        project.setAvatarURL("https://avatars3.githubusercontent.com/u/17744001?u=038d9e068c4205d94c670d7d89fb921ec5b29782&v=4");

        List<ResponseProjectDTO> projectDTOList = new ArrayList<>();
        ResponseProjectDTO projectDTO = new ResponseProjectDTO();
        projectDTO.setProjectId(project.getProjectId());
        projectDTO.setProjectName(project.getName());
        projectDTO.setAvatarURL(project.getAvatarURL());

        projectDTOList.add(projectDTO);

        //when
        when(projectDAO.findByMemberId(1L))
                .thenReturn(List.of(project));
        //then
        assertEquals(1, projectService.getMemberProjects(1L).size());
//        assertTrue(projectDTOList.equals(projectService.getMemberProjects(1L)));
    }

    @Test
    public void deleteProject() {
        when(projectDAO.findById(1L))
                .thenReturn(Optional.of(project));
        doNothing().when(repositoryDAO).deleteById(1L);
        when(repositoryDAO.save(githubRepositoryWithMultipleProjects)).thenReturn(githubRepositoryWithMultipleProjects);
        doNothing().when(projectDAO).deleteById(1L);

        try {
            projectService.deleteProject(1L);
        } catch (IOException e) {
            e.printStackTrace();
        }
        verify(repositoryDAO, times(1)).deleteById(1L);
        verify(repositoryDAO, times(1)).save(githubRepositoryWithMultipleProjects);
        verify(projectDAO, times(1)).deleteById(1L);
    }
}
