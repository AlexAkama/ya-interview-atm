package atm.model;

import java.util.Map;

public interface ATM {

    void load(Map<Banknote, Integer> banknoteMap);

    void load(Banknote banknote, Integer countToAdd);

    String issuing(int amount);

    String getBalance();

}
