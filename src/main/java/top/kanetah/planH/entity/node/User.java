package top.kanetah.planH.entity.node;

import top.kanetah.planH.entity.base.BaseEntity;
import top.kanetah.planH.entity.relationship.Authority;
import top.kanetah.planH.entity.relationship.Submit;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@NodeEntity
public class User extends BaseEntity implements UserDetails {

    private String userName;
    private long userCode;
    @Relationship(type = "USER_HAS_ROLE", direction = Relationship.INCOMING)
    private List<Authority> roleAuthorities = new ArrayList<>();
    @Relationship(type = "USER_SUBMIT_TASK", direction = Relationship.INCOMING)
    private List<Submit> submits = new ArrayList<>();

    public User() {
    }

    public User(long userCode, String userName) {
        this.userCode = userCode;
        this.userName = userName;
    }

    public void addRoleAuthorities(Authority authority) {
        roleAuthorities.add(authority);
    }

    public void addSubmit(Submit submit) {
        submits.add(submit);
    }

    public String getUserName() {
        return userName;
    }

    public long getUserCode() {
        return userCode;
    }

    public List<Authority> getRoleAuthorities() {
        return roleAuthorities;
    }

    public List<Submit> getSubmits() {
        return submits;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> set = new HashSet<>();
        roleAuthorities.forEach(r ->
                set.add(r.getRole()));
        return set;
    }

    @Override
    public String getPassword() {
        return String.valueOf(userCode);
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        User user = (User) o;

        return userCode == user.userCode && (userName != null ? userName.equals(user.userName) : user.userName == null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (int) (userCode ^ (userCode >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", userCode=" + userCode +
                ", roleAuthorities=" + roleAuthorities +
                ", submits=" + submits +
                '}';
    }
}
