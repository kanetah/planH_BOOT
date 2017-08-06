package planH.entity.relationship;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import planH.entity.node.Task;
import planH.entity.node.User;
import planH.entity.base.BaseRelationshipEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@RelationshipEntity(type = "USER_SUBMIT_TASK")
public class Submit extends BaseRelationshipEntity{

    private Collection<String> submits = new ArrayList<>();
    @StartNode
    private User user;
    @EndNode
    private Task task;
    private Date submitDate;

    public Submit() {
    }

    public Submit(User user, Task task) {
        this.user = user;
        this.task = task;
        this.submitDate = new Date();
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
}
