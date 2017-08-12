package kanetah.planH.info;

public class TaskInfo {

    private java.lang.Long taskId = null;
    private java.lang.String subject = null;
    private java.lang.String title = null;
    private java.lang.String content = null;
    private java.util.Date deadline = null;
    private java.util.Date submitDate = null;
    private java.lang.String submitFileName = null;

    public TaskInfo(
            kanetah.planH.entity.node.Task task,
            kanetah.planH.entity.relationship.Submit submit
    ) {
        if(task != null) {
            this.taskId = task.getId();
            this.subject = task.getSubject();
            this.title = task.getTitle();
            this.content = task.getContent();
            this.deadline = task.getDeadline();
        }

        if(submit != null) {
            this.submitDate = submit.getSubmitDate();
            this.submitFileName = submit.getSubmitFileName();
        }
    }

    public java.lang.Long getTaskId() { return taskId; }

    public java.lang.String getSubject() { return subject; }

    public java.lang.String getTitle() { return title; }

    public java.lang.String getContent() { return content; }

    public java.util.Date getDeadline() { return deadline; }

    public java.util.Date getSubmitDate() { return submitDate; }

    public java.lang.String getSubmitFileName() { return submitFileName; }
}
