package agentie.network.dto;

import agentie.model.Agent;
import agentie.model.Bilet;


public class DTOUtils {
    public static Agent getFromDTO(AgentDTO usdto){
        String id=usdto.getId();
        String pass=usdto.getPassd();
        return new Agent(id, pass);

    }
    public static AgentDTO getDTO(Agent Agent){
        String id=Agent.getId();
        String pass=Agent.getPassword();
        return new AgentDTO(id, pass);
    }

    public static Bilet getFromDTO(BiletDTO mdto){
        String sender=mdto.getNume();
        String turist=mdto.getTurist();
        String adresa=mdto.getAdresa();
        int loc=mdto.getLocuri();
        int id=mdto.getId();
        return new Bilet(id,sender,turist,adresa,loc);

    }

    public static BiletDTO getDTO(Bilet bilet){

        //Agent sender=new Agent(bilet.getNume().getUsername());
        String sender=bilet.getNume();
        String turist=bilet.getTurist();
        String adresa=bilet.getAdresa();
        int loc=bilet.getLocuri();
        int id=bilet.getId();
        return new BiletDTO(id,sender,turist,adresa,loc);
    }

    public static AgentDTO[] getDTO(Agent[] Agents){
        AgentDTO[] frDTO=new AgentDTO[Agents.length];
        for(int i=0;i<Agents.length;i++)
            frDTO[i]=getDTO(Agents[i]);
        return frDTO;
    }

    public static Agent[] getFromDTO(AgentDTO[] Agents){
        Agent[] friends=new Agent[Agents.length];
        for(int i=0;i<Agents.length;i++){
            friends[i]=getFromDTO(Agents[i]);
        }
        return friends;
    }
}
