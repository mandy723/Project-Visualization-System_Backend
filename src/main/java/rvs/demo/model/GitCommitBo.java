package rvs.demo.model;

import java.util.Date;

public class GitCommitBo {

    private Long id;
    private String owner;
    private String name;
    private Date committedDate;

    @Override
    public String toString() {
        return "GitCommitBo{" +
                "owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", committedDate=" + committedDate +
                '}';
    }

    public GitCommitBo() {

    }

    public GitCommitBo(String owner, String name, Date committedDate) {
        this.owner = owner;
        this.name = name;
        this.committedDate = committedDate;
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
