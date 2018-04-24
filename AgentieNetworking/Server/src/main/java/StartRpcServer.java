import agentie.BiletRepository;
import agentie.UserRepository;
import agentie.ZborRepository;
import agentie.network.utils.AbstractServer;
import agentie.network.utils.AgentieRpcConcurrentServer;
import agentie.persistence.BiletJdbcRepository;
import agentie.persistence.UserRepositoryJdbc;
import agentie.persistence.ZborJdbcRepository;
import agentie.server.AgentieServerImpl;
import agentie.service.IServer;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {
        // UserRepository userRepo=new UserRepositoryMock();
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatserver.properties "+e);
            return;
        }
        UserRepository userRepo=new UserRepositoryJdbc(serverProps);
        BiletRepository biletRepo=new BiletJdbcRepository(serverProps);
        ZborRepository zborRepository=  new ZborJdbcRepository(serverProps);
        IServer chatServerImpl=new AgentieServerImpl(userRepo, biletRepo,zborRepository);
        int chatServerPort=defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("chat.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+chatServerPort);
        AbstractServer server = new AgentieRpcConcurrentServer(chatServerPort, chatServerImpl);
        try {
            server.start();
        } catch (agentie.network.utils.ServerException e) {
            e.printStackTrace();
        }
    }
}
