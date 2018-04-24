package agentie.server;

import agentie.BiletRepository;
import agentie.UserRepository;
import agentie.ZborRepository;
import agentie.model.Agent;
import agentie.model.Bilet;
import agentie.model.Zbor;
import agentie.persistence.BiletJdbcRepository;
import agentie.persistence.UserRepositoryJdbc;
import agentie.persistence.ZborJdbcRepository;
import agentie.service.AgentException;
import agentie.service.IObserver;
import agentie.service.IServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AgentieServerImpl implements IServer {
    private UserRepository userRepository;
    private BiletRepository biletRepository;
    private ZborRepository zborRepository;
    private Map<String,IObserver> loggedClients;

    public AgentieServerImpl(UserRepository userRepository, BiletRepository biletRepository, ZborRepository zborRepository) {
        this.userRepository = userRepository;
        this.biletRepository = biletRepository;
        this.zborRepository = zborRepository;
        loggedClients=new ConcurrentHashMap<>();
    }




    public synchronized void login(Agent agent, IObserver client)throws AgentException{
        Agent agent1=userRepository.findBy(agent.getUsername(),agent.getPassword());
        if(agent1!=null){
            if(loggedClients.get(agent.getId())!=null)
                throw new AgentException("User already logged in.");
            loggedClients.put(agent.getId(), client);
            //notifyFriendsLoggedIn(user);
        }
        else
            throw new AgentException("Authentication failed.");
    }

    public synchronized void update(Bilet bilet) throws AgentException {
       // String id = bilet.getNume();
        //IObserver receiverClient = loggedClients.get(id);
        //if (receiverClient != null) {
            biletRepository.save(bilet);
            Zbor zbor = (Zbor) zborRepository.findOne(bilet.getId());
            if (zbor != null) {
                Zbor z=new Zbor(zbor.getId(),zbor.getDestinatie(),zbor.getAeroport(),zbor.getPlecare(),zbor.getNrLocuri()-bilet.getLocuri());
                zborRepository.update(zbor.getId(), z);
             //   receiverClient.cumparareBilet(bilet);
            }

            else
                throw new AgentException("Zbor inexistent");

    }

    @Override
    public synchronized Zbor[] getZbor() throws AgentException {
        return (Zbor[]) zborRepository.findAll().toArray(new Zbor[1]);
    }



    @Override
    public synchronized Zbor[] cautare(String dest,String data) throws AgentException {
        return (Zbor[]) zborRepository.filetrBy(dest,data).toArray(new Zbor[1]);
    }

    public synchronized void logout(Agent user, IObserver client) throws AgentException {
        IObserver localClient=loggedClients.remove(user.getId());
        if (localClient==null)
            throw new AgentException("User "+user.getId()+" is not logged in.");

    }
}
