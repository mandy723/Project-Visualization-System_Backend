package pvs.app.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import pvs.app.Application;
import pvs.app.entity.Member2;
import pvs.app.entity.Project2;
import pvs.app.entity.Repository;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProjectDAOIntegrationTest2 {
    @Autowired
    private Project2DAO projectDAO;

    private Project2 project01;
    private Project2 project02;
    private Repository repository01;
    private Repository repository02;
    private Member2 member2;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        member2 = new Member2();
        project01 = new Project2();
//        project01.setMemberId(1L);
        project01.setName("react");
        member2.getProject2Set().add(project01);

        project02 = new Project2();
//        project02.setMemberId(2L);
        project02.setName("angular");
        member2.getProject2Set().add(project02);


        repository01 = new Repository();
        repository01.setType("github");
        repository01.setUrl("facebook/react");

        repository02 = new Repository();
        repository02.setType("github");
        repository02.setUrl("angular/angular");

        project01.getRepositorySet().add(repository01);
        project01.getRepositorySet().add(repository02);
        projectDAO.save(project01);
        projectDAO.save(project02);
    }

    @Test
    public void whenFindAll_thenReturnProjectList() {
        List<Project2> foundEntityList = projectDAO.findAll();
        assertEquals(2, foundEntityList.size());
        assertEquals(2, foundEntityList.get(0).getRepositorySet().size());
    }
}
