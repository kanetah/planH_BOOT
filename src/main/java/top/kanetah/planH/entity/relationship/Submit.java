package top.kanetah.planH.entity.relationship;

import top.kanetah.planH.entity.node.User;
import top.kanetah.planH.entity.base.BaseRelationshipEntity;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import top.kanetah.planH.entity.node.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@RelationshipEntity(type = "USER_SUBMIT_TASK")
public class Submit extends BaseRelationshipEntity {

    private Collection<String> submits = new ArrayList<>();
    @StartNode
    private User user;
    @EndNode
    private Task task;
    private Date submitDate;
    private String submitFileName;
    private String saveFileName;

    private static Submit emptySubmitObject;
    static {
        emptySubmitObject = new Submit();
    }

    public Submit() {
    }

    public Submit(User user, Task task, String submitFileName, Date submitDate, String saveFileName) {
        this.user = user;
        this.task = task;
        this.submitFileName = submitFileName;
        this.saveFileName = saveFileName;
        this.submitDate = submitDate;
    }

    public static Submit emptySubmit(){
        return emptySubmitObject;
    }

    public void addSubmitName(String name) {
        this.submits.add(name);
    }

    public Collection<String> getSubmits() {
        return submits;
    }

    public User getUser() {
        return user;
    }

    public Task getTask() {
        return task;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public String getSubmitFileName() {
        return submitFileName;
    }

    public String getSaveFileName() {
        return saveFileName;
    }
}
