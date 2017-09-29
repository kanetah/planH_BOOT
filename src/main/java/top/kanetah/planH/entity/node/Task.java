package top.kanetah.planH.entity.node;

import top.kanetah.planH.entity.relationship.Submit;
import top.kanetah.planH.entity.base.BaseEntity;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

@NodeEntity
public class Task extends BaseEntity {

    private String subject;
    private String title;
    private String content;
    private Date deadline;
    private String fileFormat;
    private String saveFormat;
    private String saveProcessor;
    @Relationship(type = "USER_SUBMIT_TASK", direction = Relationship.UNDIRECTED)
    private ArrayList<Submit> submits = new ArrayList<>();

    public Task() {
    }

    public Task(
            String subject,
            String title,
            String content,
            String fileFormat,
            String saveFormat,
            String deadline,
            String saveProcessor
    ) {
        this.subject = subject;
        this.title = title;
        this.content = content;
        this.fileFormat = fileFormat;
        this.saveFormat = saveFormat;
        this.saveProcessor = saveProcessor;
        setDeadline(deadline);
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

    public String getFileFormat() {
        return fileFormat;
    }

    public String getSaveFormat() {
        return saveFormat;
    }

    public Date getDeadline() {
        return deadline;
    }

    public String getSaveProcessor() {
        return saveProcessor;
    }

    public ArrayList<Submit> getSubmits() {
        return submits;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDeadline(String deadline) {
        String[] d = deadline.split("[.\\\\/-]");
        this.deadline = new GregorianCalendar(
                Integer.valueOf(d[0]),
                Integer.valueOf(d[1]) - 1,
                Integer.valueOf(d[2]) + 1,
                7, 59, 59
        ).getTime();
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public void setSaveFormat(String saveFormat) {
        this.saveFormat = saveFormat;
    }

    public void setSaveProcessor(String saveProcessor) {
        this.saveProcessor = saveProcessor;
    }

    public Date getDeadlineOnJVM() {
        return new Date(deadline.getTime() - 8 * 60 * 60 * 1000);
    }
}
