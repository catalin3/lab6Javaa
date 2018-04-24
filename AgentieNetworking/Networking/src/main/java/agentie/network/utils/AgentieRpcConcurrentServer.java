package agentie.network.utils;

import agentie.network.rpcprotocol.AgentieClientRpcReflectionWorker;
import agentie.service.IServer;


import java.net.Socket;


public class AgentieRpcConcurrentServer extends AbsConcurrentServer {
    private IServer chatServer;
    public AgentieRpcConcurrentServer(int port, IServer chatServer) {
        super(port);
        this.chatServer = chatServer;
        System.out.println("Chat- ChatRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
       // ChatClientRpcWorker worker=new ChatClientRpcWorker(chatServer, client);
        AgentieClientRpcReflectionWorker worker=new AgentieClientRpcReflectionWorker(chatServer, client);
        Thread tw=new Thread(worker);
        return tw;
    }
}
