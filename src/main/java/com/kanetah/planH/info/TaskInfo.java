package com.kanetah.planH.info;

import com.kanetah.planH.entity.node.Task;
import com.kanetah.planH.entity.relationship.Submit;

import java.util.Date;

public class TaskInfo {

    private Long taskId;
    private String subject;
    private String title;
    private String content;
    private Date deadline;
    private Date submitDate;
    private String submitFilePath;

    public TaskInfo(Task task, Submit submit) {
        this.taskId = task.getId();
        this.subject = task.getSubject();
        this.title = task.getTitle();
        this.content = task.getContent();
        this.deadline = task.getDeadline();

        if(submit != null) {
            this.submitDate = submit.getSubmitDate();
            this.submitFilePath = submit.getSubmitFilePath();
        } else {
            this.submitDate = null;
            this.submitFilePath = null;
        }
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

    public Date getSubmitDate() {
        return submitDate;
    }

    public String getSubmitFilePath() {
        return submitFilePath;
    }
}
