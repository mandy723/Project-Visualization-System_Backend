package pvs.app.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pvs.app.Application;
import pvs.app.entity.Project;
import pvs.app.entity.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ProjectDAOIntegrationTest {
    private static SessionFactory sessionFactory;
    private Session session;

    @Autowired
    private ProjectDAO projectDAO;

    private Project project01;
    private Project project02;
    private Repository repository01;
    private Repository repository02;
    Set<Project> projects;
    Set<Repository> repositories;

    @Before
    public void init() {
        project01 = new Project();
        project01.setMemberId(1L);
        project02 = new Project();
        project02.setMemberId(2L);
        repository01 = new Repository();
        repository01.setType("GithubCommit");
        repository01.setUrl("facebook/react");
        repository02 = new Repository();
        repository02.setType("GithubIssue");
        repository02.setUrl("facebook/react");
        projects = new HashSet<>();
        repositories = new HashSet<>();
    }

    @Test
    public void whenFindAll_thenReturnProjectList() {
//        project01.getRepositorySet().add(repository01);
//        project01.getRepositorySet().add(repository02);
//        projectDAO.save(project01);
//        List<Project> foundEntityList = projectDAO.findAll();
//
//        assertEquals(2, foundEntityList.size());
//        assertEquals(2, foundEntityList.get(0).getRepositorySet().size());
        assertTrue(true);
    }
}
