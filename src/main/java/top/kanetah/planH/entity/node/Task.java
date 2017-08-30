package top.kanetah.planH.entity.node;

import top.kanetah.planH.entity.relationship.Submit;
import top.kanetah.planH.entity.base.BaseEntity;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

@NodeEntity
public class Task extends BaseEntity {

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
        String[] d = deadline.split("[.\\\\/-]");
        this.deadline = new GregorianCalendar(
                Integer.valueOf(d[0]),
                Integer.valueOf(d[1]) - 1,
                Integer.valueOf(d[2]) + 1,
                7, 59, 59
        ).getTime();
    }

    public Long getTaskId() {
        return id;
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
