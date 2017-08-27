package top.kanetah.planH.entity.node;

import top.kanetah.planH.entity.base.BaseEntity;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class UserRoot extends BaseEntity {

    private String root = "UserRoot";

    public UserRoot() {
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }
}
