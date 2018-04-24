package agentie.model;

import java.io.Serializable;
import java.util.Objects;

public class Agent implements Serializable,Comparable<Agent>,Identifiable<String>{
    private int id;
    private String nume;
    private String username;
    private String password;

    public Agent() {
    }

    public  Agent(String username){
        this(username,"","");
    }
    public Agent( String nume, String username, String password) {

        this.nume = nume;
        this.username = username;
        this.password = password;
    }

    public Agent(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void setId(String agent) {
        username =agent;
    }

    @Override
    public String getId() {
        return this.username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public int compareTo(Agent o) {
        return username.compareTo(o.username);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;
        return Objects.equals(username, agent.username);
    }


}
