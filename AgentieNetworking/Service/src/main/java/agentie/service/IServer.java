package agentie.service;

import agentie.model.Agent;
import agentie.model.Bilet;
import agentie.model.Zbor;

import java.rmi.RemoteException;
import java.util.List;

public interface IServer {
    void login(Agent agent,IObserver client) throws AgentException;
    void logout(Agent agent,IObserver client)throws AgentException;
    void update(Bilet bilet)throws AgentException;
     Zbor[] getZbor()throws AgentException;
     Zbor[] cautare(String dest,String data)throws AgentException;
    //List<Zbor> getZbor()throws AgentException;
   // Agent getAgent(String username, String passord);
}
