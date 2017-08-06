package planH.entity.relationship;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import planH.entity.base.BaseRelationshipEntity;
import planH.entity.node.Role;
import planH.entity.node.User;

import java.util.ArrayList;
import java.util.Collection;

@RelationshipEntity(type = "USER_HAS_ROLE")
public class Authority extends BaseRelationshipEntity {

    private Collection<String> roles = new ArrayList<>();
    @StartNode
    private User user;
    @EndNode
    private Role role;

    public Authority() {
    }

    public Authority(User user, Role role) {
        this.user = user;
        this.role = role;
    }

    public void addRoleName(String name) {
        roles.add(name);
    }

    public Collection<String> getRoles() {
        return roles;
    }

    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
