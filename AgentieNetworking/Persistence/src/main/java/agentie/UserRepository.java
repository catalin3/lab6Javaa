package agentie;

import agentie.model.Agent;

;


public interface UserRepository extends IRepository<String,Agent> {
    Agent findBy(String username);
    Agent findBy(String username, String pass);
    //Iterable<Agent> getFriendsOf(Agent user);
}
