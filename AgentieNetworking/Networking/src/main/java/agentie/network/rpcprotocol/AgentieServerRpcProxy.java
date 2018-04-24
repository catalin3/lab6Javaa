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
import com.sun.corba.se.pept.transport.ReaderThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class AgentieServerRpcProxy implements IServer {

    private String host;
    private int port;

    private IObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public AgentieServerRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<Response>();
    }
    @Override
    public void login(Agent agent, IObserver client) throws AgentException {
        initializeConnection();
        AgentDTO udto= DTOUtils.getDTO(agent);
        Request req=new Request.Builder().type(RequestType.LOGIN).data(udto).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.OK){
            this.client=client;
            return;
        }
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new AgentException(err);
        }
    }

    @Override
    public void logout(Agent agent, IObserver client) throws AgentException {
        AgentDTO udto=DTOUtils.getDTO(agent);
        Request req=new Request.Builder().type(RequestType.LOGOUT).data(udto).build();
        sendRequest(req);
        Response response=readResponse();
        closeConnection();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new AgentException(err);
        }
    }

    @Override
    public void update(Bilet bilet) throws AgentException {
    BiletDTO biletDTO=DTOUtils.getDTO(bilet);
    Request request=new Request.Builder().type(RequestType.CUMPARA).data(biletDTO).build();
    sendRequest(request);
    Response response=readResponse();
        if(response.type()==ResponseType.NEW_CUMPARA){
            client.cumparareBilet(bilet);
        }
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new AgentException(err);
        }
    }

    @Override
    public Zbor[] getZbor() throws AgentException {
        Zbor[] z = new Zbor[0];
        Request request=new Request.Builder().type(RequestType.GET).data(z).build();
        sendRequest(request);
        Response response=readResponse();
         z= (Zbor[]) response.data();
        return z;
    }

    @Override
    public Zbor[] cautare(String dest,String data) throws AgentException {
        Zbor[] z = new Zbor[1];
        Zbor ze=new Zbor(1,dest,data,"s",0);

        //Object obj=new Object(s);
        //StringBuilder s=new StringBuilder(dest,data);
        Request request=new Request.Builder().type(RequestType.CAUTA).data(ze).build();
        sendRequest(request);
        Response response=readResponse();
        z= (Zbor[]) response.data();
        return z;
    }


    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(Request req) throws AgentException {
        try {
            output.writeObject(req);
            output.flush();
        } catch (IOException e) {
            throw new AgentException("Error sending object "+e);
        }
    }
    private Response readResponse() throws AgentException {
       // Response response=ResponseType.OK;
        Response response=null;
        try{
            /*synchronized (responses){
                responses.wait();
            }
            response = responses.remove(0);    */
            response=qresponses.take();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    private void initializeConnection() {
        try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }
    private void handleUpdate(Response response){
        if(response.type()==ResponseType.NEW_CUMPARA){
            Bilet bilet = (Bilet) response.data();
            try {
                client.cumparareBilet(bilet);
            } catch (AgentException e) {
                e.printStackTrace();
            }
        }
    }
    private boolean isUpdate(Response response){
        return response.type()== ResponseType.NEW_CUMPARA;
    }
   private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    System.out.println("response received ds\n\n\n\n\ndsad");
                    if (isUpdate((Response)response)){
                        handleUpdate((Response)response);
                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else{
                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
