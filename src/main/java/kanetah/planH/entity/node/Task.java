package kanetah.planH.entity.node;

import kanetah.planH.entity.relationship.Submit;
import kanetah.planH.entity.base.BaseEntity;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.Date;

@NodeEntity
public class Task extends BaseEntity {

    private Long taskId = id;
    private String subject;
    private String title;
    private String content;
    private Date deadline;
    @Relationship(type = "USER_SUBMIT_TASK", direction = Relationship.UNDIRECTED)
    private ArrayList<Submit> submits = new ArrayList<>();

    public Task() {
    }

    public Task(String subject, String title, String content, String deadline) {
        this.subject = subject;
        this.title = title;
        this.content = content;
        this.deadline = new Date();
    }

    public Long getTaskId() {
        return taskId;
    }

    public String getSubject() {
        return subject;
    }

    public String getTitle() {
        return title;
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
