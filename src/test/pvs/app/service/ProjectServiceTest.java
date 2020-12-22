package pvs.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pvs.app.Application;
import pvs.app.dao.ProjectDAO;
import pvs.app.dto.CreateProjectDTO;
import pvs.app.dto.RepositoryDTO;
import pvs.app.dto.ResponseProjectDTO;
import pvs.app.entity.Project;
import pvs.app.entity.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ProjectServiceTest {
    @Autowired
    private ProjectService projectService;

    @MockBean
    private GithubApiService githubApiService;

    @MockBean
    private ProjectDAO projectDAO;

    @Test
    public void create() throws IOException {
        CreateProjectDTO projectDTO = new CreateProjectDTO();
        projectDTO.setProjectName("react");
        projectDTO.setRepositoryURL("https://github.com/facebook/react");

        Project mockProject = new Project();
        mockProject.setMemberId(1L);
        mockProject.setName(projectDTO.getProjectName());
        Repository mockRepository = new Repository();
        mockRepository.setUrl(projectDTO.getRepositoryURL());
        mockRepository.setType("github");
        mockProject.setRepositorySet(Set.of(mockRepository));

        String responseJson = "{\"avatarUrl\":\"https://avatars3.githubusercontent.com/u/17744001?u=038d9e068c4205d94c670d7d89fb921ec5b29782&v=4\"}";
        ObjectMapper mapper = new ObjectMapper();
        Optional<JsonNode> mockAvatar = Optional.ofNullable(mapper.readTree(responseJson));

        when(githubApiService.getAvatarURL("facebook"))
                .thenReturn(mockAvatar.orElse(null));

        when(projectDAO.save(new Project()))
                .thenReturn(mockProject);

        projectService.create(projectDTO);
    }

    @Test
    public void getMemberProjects() {
        //mock
        Project mockProject = new Project();
        mockProject.setMemberId(1L);
        mockProject.setName("react");
        mockProject.setAvatarURL("https://avatars3.githubusercontent.com/u/17744001?u=038d9e068c4205d94c670d7d89fb921ec5b29782&v=4");

        Repository mockRepository = new Repository();
        mockRepository.setUrl("https://github.com/facebook/react");
        mockRepository.setType("github");
        mockProject.setRepositorySet(Set.of(mockRepository));

        when(projectDAO.findByMemberId(1L))
                .thenReturn(List.of(mockProject));

        //except
        List<ResponseProjectDTO> projectDTOList = new ArrayList<>();
        ResponseProjectDTO projectDTO = new ResponseProjectDTO();
        projectDTO.setProjectId(mockProject.getProjectId());
        projectDTO.setProjectName(mockProject.getName());
        projectDTO.setAvatarURL(mockProject.getAvatarURL());

        RepositoryDTO repositoryDTO = new RepositoryDTO();
        repositoryDTO.setUrl(mockRepository.getUrl());
        repositoryDTO.setType(mockRepository.getType());
        projectDTO.getRepositoryDTOList().add(repositoryDTO);

        projectDTOList.add(projectDTO);

        assertEquals(projectDTOList.toString(), projectService.getMemberProjects(1L).toString());
    }
}
