package rvs.demo.controller;

import rvs.demo.config.YAMLConfig;
import rvs.demo.mockData.Product;
import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController{

    @Autowired
    private YAMLConfig myConfig;

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable("id") String id) throws IOException {
        //Basic authentication
//        GitHubClient client = new GitHubClient();
//        client.setCredentials("user", "passw0rd");


        RepositoryService repositoryService = new RepositoryService();

        IRepositoryIdProvider repoId = new RepositoryId("imper0502","RVS-springboot");
        Repository repo = repositoryService.getRepository(repoId);
        System.out.println(" URL: " + repo.getGitUrl());


        CommitService commitService = new CommitService();
        List<RepositoryCommit> commits = commitService.getCommits(repoId);
        System.out.println(" Commits Author: " + commits.get(0).getCommit().getMessage());
//        for (Repository repo : repositoryService.getRepositories("defunkt")){
//            commitService( repo.getId(), "master");
//            System.out.println(" URL: " + repo.getGitUrl());
//            System.out.println(repo.getName() + " Watchers: " + repo.getWatchers());
//        }

        System.out.println(myConfig.getFrontend());


        Product product = new Product();
        product.setId(id);
        product.setName("Romantic Story");
        product.setPrice(200);

        return product;
    }

}
