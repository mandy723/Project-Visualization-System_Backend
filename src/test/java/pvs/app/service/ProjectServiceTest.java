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
import pvs.app.dto.ResponseProjectDTO;
import pvs.app.entity.Project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Test
    public void create() throws IOException {
        //given
        CreateProjectDTO projectDTO = new CreateProjectDTO();
        projectDTO.setProjectName("react");
        projectDTO.setRepositoryURL("https://github.com/facebook/react");

        Project project = new Project();
        project.setMemberId(1L);
        project.setName(projectDTO.getProjectName());

        String responseJson = "{\"avatarUrl\":\"https://avatars3.githubusercontent.com/u/17744001?u=038d9e068c4205d94c670d7d89fb921ec5b29782&v=4\"}";
        ObjectMapper mapper = new ObjectMapper();
        Optional<JsonNode> mockAvatar = Optional.ofNullable(mapper.readTree(responseJson));

        //when
        when(githubApiService.getAvatarURL("facebook"))
                .thenReturn(mockAvatar.orElse(null));

        when(projectDAO.save(new Project()))
                .thenReturn(project);

        projectService.create(projectDTO);

        //expect
        verify(githubApiService, times(1)).getAvatarURL("facebook");
    }

    @Test
    public void getMemberProjects() {
        //given
        Project project = new Project();
        project.setMemberId(1L);
        project.setName("react");
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
        //expect
        assertEquals(projectDTOList.toString(), projectService.getMemberProjects(1L).toString());
    }
}
