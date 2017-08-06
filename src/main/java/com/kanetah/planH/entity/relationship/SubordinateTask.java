package com.kanetah.planH.entity.relationship;

import com.kanetah.planH.entity.node.Task;
import com.kanetah.planH.entity.node.TaskRoot;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = "Root_Has_Task")
public class SubordinateTask {

    @StartNode
    private TaskRoot taskRoot;
    @EndNode
    private Task task;

    public SubordinateTask() {
    }

    public SubordinateTask(TaskRoot taskRoot, Task task) {
        this.taskRoot = taskRoot;
        this.task = task;
    }

    public TaskRoot getTaskRoot() {
        return taskRoot;
    }

    public void setTaskRoot(TaskRoot taskRoot) {
        this.taskRoot = taskRoot;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
