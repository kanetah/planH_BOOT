package planH.service;//package planH.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.neo4j.template.Neo4jOperations;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Repository;
//import planH.entity.node.User;
//import planH.repository.UserRepository;
//
//@Repository
//public class UserDataService implements UserRepository{
//
//    private Neo4jOperations neo4jl;
//
//    @Autowired
//    public void setNeo4jl(Neo4jOperations neo4jl) {
//        this.neo4jl = neo4jl;
//    }
//
//    @Override
//    public User getUserCodeByUserName(String userName) {
//        return null;
//    }
//
//    @Override
//    public void save(User user) {
//
//    }
//
//    @Override
//    public void update(User user) {
//
//    }
//
//    @Override
//    public void delete(User user) {
//
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return neo4jl.findOne(123L, User.class);
//    }
//}
