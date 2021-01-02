package pvs.app.controller;

import org.apache.coyote.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pvs.app.dto.AddGithubRepositoryDTO;
import pvs.app.dto.AddSonarRepositoryDTO;
import pvs.app.dto.CreateProjectDTO;
import pvs.app.dto.ResponseProjectDTO;
import pvs.app.service.ProjectService;
import pvs.app.service.RepositoryService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectController {

    static final Logger logger = LogManager.getLogger(ProjectController.class.getName());

    private final ProjectService projectService;
    private final RepositoryService repositoryService;

    public ProjectController(ProjectService projectService, RepositoryService repositoryService){
        this.projectService = projectService;
        this.repositoryService = repositoryService;
    }

    @GetMapping("/repository/github/check")
    public ResponseEntity<String> checkGithubURL(@RequestParam("url") String url) throws InterruptedException {
        //我在檢查
        if(repositoryService.checkGithubURL(url)) {
            return ResponseEntity.status(HttpStatus.OK).body("你成功了");
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("你連URL都不會打嗎");
        }
    }

    @GetMapping("/repository/sonar/check")
    public ResponseEntity<String> checkSonarURL(@RequestParam("url") String url) {
        //我在檢查
        if(repositoryService.checkSonarURL(url)) {
            return ResponseEntity.status(HttpStatus.OK).body("你成功了");
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("你連URL都不會打嗎");
        }
    }

    @PostMapping("/project")
    public ResponseEntity<String> createProject(@RequestBody CreateProjectDTO projectDTO) {
        //我在檢查
        try{
            projectService.create(projectDTO);
            return ResponseEntity.status(HttpStatus.OK).body("你成功了");
        }catch(Exception e){
            logger.debug(e.toString());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("你去死吧");
        }
    }

    @PostMapping("/project/{projectId}/repository/sonar")
    public ResponseEntity<String> addSonarRepository(@RequestBody AddSonarRepositoryDTO addSonarRepositoryDTO) {
        //我在檢查
        try{
            if(repositoryService.checkSonarURL(addSonarRepositoryDTO.getRepositoryURL())) {
                projectService.addSonarRepo(addSonarRepositoryDTO);

                return ResponseEntity.status(HttpStatus.OK).body("你成功了");
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("你連URL都不會打嗎");
            }
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("你去死吧");
        }
    }

    @PostMapping("/project/{projectId}/repository/github")
    public ResponseEntity<String> addGithubRepository(@RequestBody AddGithubRepositoryDTO addGithubRepositoryDTO) {
        //我在檢查
        try{
            logger.debug(addGithubRepositoryDTO.getProjectId());
            logger.debug(addGithubRepositoryDTO.getRepositoryURL());

            if(repositoryService.checkGithubURL(addGithubRepositoryDTO.getRepositoryURL())) {
                projectService.addGithubRepo(addGithubRepositoryDTO);

                return ResponseEntity.status(HttpStatus.OK).body("你成功了");
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("你連URL都不會打嗎");
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("你去死吧");
        }
    }


    @GetMapping("/project/{memberId}")
    public ResponseEntity<List<ResponseProjectDTO>> readMemberAllProjects(@PathVariable Long memberId) {
        List<ResponseProjectDTO> projectList = projectService.getMemberProjects(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(projectList);
        //-/-/-/-/-/-/-/-/-/-/
        //    0        0    //
        //         3        //
        //////////\\\\\\\\\\\\
    }

    @GetMapping("/project/{memberId}/{projectId}")
    public ResponseEntity<ResponseProjectDTO> readSelectedProject
            (@PathVariable Long memberId, @PathVariable Long projectId) {
        List<ResponseProjectDTO> projectList = projectService.getMemberProjects(memberId);
        Optional<ResponseProjectDTO> selectedProject =
                projectList.stream()
                           .filter(project -> project.getProjectId().equals(projectId))
                           .findFirst();

        if(selectedProject.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(selectedProject.get());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        //-/-/-/-/-/-/-/-/-/-/
        //    0        0    //
        //         3        //
        //////////\\\\\\\\\\\\
    }
}
