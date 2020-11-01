package rvs.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "git_commit")
public class GitCommit {
    @Id
    @NotNull
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name="OWNER")
    private String owner;

    @NotNull
    @Column(name="NAME")
    private String name;

    @NotNull
    @Column(name="COMMITTED_DATE")
    private Date committedDate;

    public GitCommit(){}

    public GitCommit(String owner, String name, Date committedDate) {
        this.owner = owner;
        this.name = name;
        this.committedDate = committedDate;
    }

    @Override
    public String toString() {
        return "GitCommit{" +
                "owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", committedDate=" + committedDate +
                '}';
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCommittedDate() {
        return committedDate;
    }

    public void setCommittedDate(Date committedDate) {
        this.committedDate = committedDate;
    }
}
