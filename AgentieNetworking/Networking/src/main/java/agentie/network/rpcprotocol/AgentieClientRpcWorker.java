package agentie.network.rpcprotocol;

import agentie.model.Agent;
import agentie.model.Bilet;
import agentie.model.Zbor;
import agentie.network.dto.AgentDTO;
import agentie.network.dto.BiletDTO;
import agentie.network.dto.DTOUtils;
import agentie.service.AgentException;
import agentie.service.IObserver;
import agentie.service.IServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AgentieClientRpcWorker implements Runnable,IObserver {
    private IServer server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public AgentieClientRpcWorker(IServer server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cumparareBilet(Bilet bilet) throws AgentException {
        BiletDTO biletDTO= DTOUtils.getDTO(bilet);

        Response response=new Response.Builder().type(ResponseType.NEW_CUMPARA).data(server.getZbor()).build();
        System.out.println("Biletul: "+ bilet);
        try{
            sendResponse(response);
        }catch (IOException e){
            throw  new AgentException("Sending error"+e);
        }
    }

    @Override
    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }
    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request) {
        Response response=null;
        if(request.type() == RequestType.LOGIN){
            System.out.println("Login request ..."+request.type());
            AgentDTO udto=(AgentDTO) request.data();
            Agent user= DTOUtils.getFromDTO(udto);
            try {
                server.login(user, this);
                return okResponse;
            } catch (AgentException e) {
                connected=false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type()==RequestType.LOGOUT){
            System.out.println("Logout request");
            // LogoutRequest logReq=(LogoutRequest)request;
            AgentDTO udto=(AgentDTO) request.data();
            Agent user=DTOUtils.getFromDTO(udto);
            try {
                server.logout(user, this);
                connected=false;
                return okResponse;

            } catch (AgentException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type()==RequestType.CUMPARA){
            System.out.println("CumparaRequest ...");
            BiletDTO mdto=(BiletDTO) request.data();
            Bilet message=DTOUtils.getFromDTO(mdto);
            try {
                server.update(message);
                return new Response.Builder().type(ResponseType.NEW_CUMPARA).data(mdto).build();
            } catch (AgentException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type()==RequestType.GET){
            try {
                //server.getZbor();
                return new Response.Builder().type(ResponseType.OK).data(server.getZbor()).build();
            } catch (AgentException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type()==RequestType.CAUTA){
            try {
                //server.getZbor();
                Zbor z= (Zbor) request.data();
                return new Response.Builder().type(ResponseType.OK).data(server.cautare(z.getDestinatie(),z.getPlecare())).build();
            } catch (AgentException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        return response;

    }
    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }
}

