package planH.entity.node;

import org.neo4j.ogm.annotation.Relationship;
import planH.entity.base.BaseNodeEntity;
import planH.entity.relationship.Submit;

import java.util.ArrayList;
import java.util.Date;

public class Task extends BaseNodeEntity{

    private String subject;
    private String content;
    private Date startline;
    private Date deadline;
    @Relationship(type = "USER_SUBMIT_TASK", direction = Relationship.UNDIRECTED)
    private ArrayList<Submit> submits = new ArrayList<>();

    public Task() {
    }

    public Task(String subject, String content) {
        this.subject = subject;
        this.content = content;
        this.startline = new Date();
        this.deadline = new Date(startline.getTime() + 24 * 60 * 60);
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public Date getStartline() {
        return startline;
    }

    public Date getDeadline() {
        return deadline;
    }

    public ArrayList<Submit> getSubmits() {
        return submits;
    }
}
