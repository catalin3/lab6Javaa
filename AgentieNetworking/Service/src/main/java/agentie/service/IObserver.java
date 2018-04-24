package agentie.service;

import agentie.model.Bilet;

import java.rmi.Remote;

public interface IObserver {
    void cumparareBilet(Bilet bilet) throws AgentException;
}
