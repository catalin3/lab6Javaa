package agentie.network.dto;

import java.io.Serializable;

public class AgentDTO implements Serializable {
    private  String id;
    private  String passwd;

    public AgentDTO(String id) {
        this(id,"");
    }

    public AgentDTO(String id, String passd) {
        this.id = id;
        this.passwd = passd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassd() {
        return passwd;
    }

    public void setPassd(String passd) {
        this.passwd = passd;
    }
    @Override
    public String toString(){
        return "AgentDTO["+id+' '+passwd+"]";
    }
}
