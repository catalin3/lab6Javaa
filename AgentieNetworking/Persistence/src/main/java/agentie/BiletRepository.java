package agentie;

import agentie.model.Bilet;

public interface BiletRepository<I extends Number, B> extends IRepository<Integer,Bilet> {
    Iterable<Bilet> findAllTurist(String n);
}
