//package pvs.app.entity;
//
//import lombok.Data;
//import javax.persistence.*;
//import javax.validation.constraints.NotNull;
//import java.util.Date;
//
//@Entity
//@Data
//public class GithubPullRequest {
//
//    @Id
//    @NotNull
//    @GeneratedValue(strategy= GenerationType.AUTO)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="repository_id")
//    private Repository repository;
//
//    @NotNull
//    private String repoName;
//
//    @NotNull
//    private String status;
//
//    @NotNull
//    private Date pullRequestDate;
//
//    @NotNull
//    private String userName;
//
//}
