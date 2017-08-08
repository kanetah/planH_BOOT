package com.kanetah.planH.entity.node;

import com.kanetah.planH.entity.base.BaseEntity;
import com.kanetah.planH.entity.relationship.Submit;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.Date;

@NodeEntity
public class Task extends BaseEntity {

    private String subject;
    private String content;
    private Date deadline;
    @Relationship(type = "USER_SUBMIT_TASK", direction = Relationship.UNDIRECTED)
    private ArrayList<Submit> submits = new ArrayList<>();

    public Task() {
    }

    public Task(String subject, String content, String date) {
        this.subject = subject;
        this.content = content;
        this.deadline = new Date();
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public Date getDeadline() {
        return deadline;
    }

    public ArrayList<Submit> getSubmits() {
        return submits;
    }
}
