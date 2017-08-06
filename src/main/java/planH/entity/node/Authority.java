package planH.entity.node;

import org.neo4j.ogm.annotation.Relationship;
import org.springframework.security.core.GrantedAuthority;
import planH.entity.base.BaseNodeEntity;
import planH.entity.relationship.Role;

import java.util.ArrayList;

public class Authority extends BaseNodeEntity implements GrantedAuthority {

    public final static Authority ROLE_USER =
            new Authority("ROLE_USER");
    public final static Authority ROLE_ADMIN =
            new Authority("ROLE_ADMIN");

    private String authority;
    @Relationship(type = "USER_HAS_ROLE", direction = Relationship.UNDIRECTED)
    private ArrayList<Role> roles = new ArrayList<>();

    public Authority() {
        this.authority = ROLE_USER.getAuthority();
    }

    public Authority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }
}
