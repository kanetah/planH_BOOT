package kanetah.planH.entity.node;

import kanetah.planH.entity.relationship.Authority;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.security.core.GrantedAuthority;
import kanetah.planH.entity.base.BaseEntity;

import java.util.ArrayList;

@NodeEntity
public class Role extends BaseEntity implements GrantedAuthority {

    public final static String ROLE_USER =
            new Role("USER").getRoleName();
    public final static String ROLE_ADMIN =
            new Role("ADMIN").getRoleName();

    private String roleName;
    @Relationship(type = "USER_HAS_ROLE", direction = Relationship.INCOMING)
    private ArrayList<Authority> authorities = new ArrayList<>();

    public Role() {
        this.roleName = ROLE_USER;
    }

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public void addAuthority(Authority authority) {
        authorities.add(authority);
    }

    @Override
    public String getAuthority() {
        return roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public ArrayList<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(ArrayList<Authority> authorities) {
        this.authorities = authorities;
    }
}
