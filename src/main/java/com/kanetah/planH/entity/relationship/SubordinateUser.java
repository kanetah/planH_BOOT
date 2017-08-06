package com.kanetah.planH.entity.relationship;

import com.kanetah.planH.entity.base.BaseRelationshipEntity;
import com.kanetah.planH.entity.node.User;
import com.kanetah.planH.entity.node.UserRoot;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = "Root_Has_User")
public class SubordinateUser extends BaseRelationshipEntity {

    @StartNode
    private UserRoot userRoot;
    @EndNode
    private User user;

    public SubordinateUser() {
    }

    public SubordinateUser(UserRoot userRoot, User user) {
        this.userRoot = userRoot;
        this.user = user;
    }

    public UserRoot getUserRoot() {
        return userRoot;
    }

    public void setUserRoot(UserRoot userRoot) {
        this.userRoot = userRoot;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
