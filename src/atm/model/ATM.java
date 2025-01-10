package atm.model;

import java.util.Map;

public interface ATM {

    String load(Map<Banknote, Integer> banknoteMap);

    String issuing(int amount);

    String getBalance();

}
