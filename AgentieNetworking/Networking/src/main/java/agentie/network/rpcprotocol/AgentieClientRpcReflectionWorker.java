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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class AgentieClientRpcReflectionWorker implements Runnable,IObserver{
    private IServer server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public AgentieClientRpcReflectionWorker(IServer server, Socket connection) {
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

    public void biletReceived(Bilet bilet)throws AgentException{

    }
    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();

    private Response handleLOGIN(Request request){
        System.out.println("Login request ..."+request.type());
        AgentDTO udto=(AgentDTO)request.data();
        Agent user=DTOUtils.getFromDTO(udto);
        try {
            server.login(user, this);
            return okResponse;
        } catch (AgentException e) {
            connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
    private Response handleLOGOUT(Request request){
        System.out.println("Logout request...");
        AgentDTO udto=(AgentDTO)request.data();
        Agent user=DTOUtils.getFromDTO(udto);
        try {
            server.logout(user, this);
            connected=false;
            return okResponse;

        } catch (AgentException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
    private Response handleCUMPARA(Request request){
        System.out.println("SendUpdateRequest ...");
        BiletDTO biletDTO=(BiletDTO)request.data();
        Bilet bilet=DTOUtils.getFromDTO(biletDTO);
        try {
            server.update(bilet);
            return new Response.Builder().type(ResponseType.NEW_CUMPARA).data(bilet).build();
        } catch (AgentException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
    private Response handleGET(Request request){
        try {
            //server.getZbor();
            return new Response.Builder().type(ResponseType.OK).data(server.getZbor()).build();
        } catch (AgentException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
    private Response handleCAUTA(Request request){
        try {
            //server.getZbor();
            Zbor z= (Zbor) request.data();
            return new Response.Builder().type(ResponseType.OK).data(server.cautare(z.getDestinatie(),z.getPlecare())).build();
        } catch (AgentException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
    private Response handleNEW_CUMPARA(Request request){
        System.out.println("SendUpdateRequest ...");
        BiletDTO biletDTO=(BiletDTO)request.data();
        Bilet bilet=DTOUtils.getFromDTO(biletDTO);
        try{
            server.update(bilet);
            return okResponse;
        } catch (AgentException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }


    @Override
    public void cumparareBilet(Bilet bilet) throws AgentException {
        BiletDTO biletDTO= DTOUtils.getDTO(bilet);
        Response response=new Response.Builder().type(ResponseType.NEW_CUMPARA).data(biletDTO).build();
        System.out.println("Biletul: "+ bilet);
        try{
            sendResponse(response);
        }catch (IOException e){
            throw  new AgentException("Sending error"+e);
        }
    }
    private Response handleRequest(Request request){
        Response response=null;
        String handlerName="handle"+(request).type();
        System.out.println("HandlerName "+handlerName);
        try {
            Method method=this.getClass().getDeclaredMethod(handlerName, Request.class);
            response=(Response)method.invoke(this,request);
            System.out.println("Method "+handlerName+ " invoked");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return response;
    }
}
