package agentie;

import agentie.model.Zbor;

import java.util.List;


public interface ZborRepository<I extends Number, Z> extends IRepository<Integer, Zbor>{
    Iterable<Zbor> filterBy(String dataPlecarii);
    List<Zbor> filetrBy(String destinatie, String plecare);

}
