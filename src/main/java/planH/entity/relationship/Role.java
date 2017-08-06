package planH.entity.relationship;


import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import planH.entity.base.BaseRelationshipEntity;
import planH.entity.node.Authority;
import planH.entity.node.User;

import java.util.ArrayList;
import java.util.Collection;

@RelationshipEntity(type = "USER_HAS_ROLE")
public class Role extends BaseRelationshipEntity {

    private Collection<String> roles = new ArrayList<>();
    @StartNode
    private User user;
    @EndNode
    private Authority authority;

    public Role() {
    }

    public Role(User user, Authority authority) {
        this.user = user;
        this.authority = authority;
    }

    public void addRoleName(String name) {
        roles.add(name);
    }

    public Collection<String> getRoles() {
        return roles;
    }

    public User getUser() {
        return user;
    }

    public Authority getAuthority() {
        return authority;
    }
}
