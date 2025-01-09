package atm.service;

import atm.model.ATM;
import atm.model.Banknote;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BinaryOperator;

public class AtmMachine implements ATM {

    private static final BinaryOperator<Integer> SUB = (a, b) -> a - b;

    private final Map<Banknote, Integer> storage = getInitBanknoteMap();

    @Override
    public void load(Map<Banknote, Integer> loadMap) {
        for (Map.Entry<Banknote, Integer> entry : loadMap.entrySet()) {
            load(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void load(Banknote banknote, Integer countToAdd) {
        storage.merge(banknote, countToAdd, Integer::sum);
    }

    @Override
    public String issuing(int amount) {
        System.out.printf("Запрошено %s денег.%n", amount);
        String response;
        try {
            Map<Banknote, Integer> issueMap = getIssueMap(amount);
            writeOff(issueMap);
            response = String.format("Выдано %s денег. ", amount);
        } catch (Exception e) {
            response = String.format("Невозможно выдать %s денег.", amount);
        }
        return response;
    }

    @Override
    public String getBalance() {
        int sum = 0;
        for (Map.Entry<Banknote, Integer> entry : storage.entrySet()) {
            sum += entry.getValue() * entry.getKey().getNominal();
        }
        return String.format("В банкомате: %s денег", sum);
    }

    private static Map<Banknote, Integer> getInitBanknoteMap() {
        Map<Banknote, Integer> map = new EnumMap<>(Banknote.class);
        for (Banknote banknote : Banknote.values()) {
            map.put(banknote, 0);
        }
        return map;
    }

    private Map<Banknote, Integer> getIssueMap(int amount) throws Exception {
        Map<Banknote, Integer> issueMap = AtmMachine.getInitBanknoteMap();
        for (Banknote banknote : Banknote.values()) {
            int nominal = banknote.getNominal();
            int storageCount = storage.get(banknote);
            if (amount >= nominal) {
                int need = amount / nominal;
                int toIssue = Math.min(storageCount, need);
                issueMap.put(banknote, toIssue);
                amount -= toIssue * banknote.getNominal();
            }
        }
        if (amount != 0) {
            throw new Exception();
        }
        return issueMap;
    }

    private void writeOff(Map<Banknote, Integer> issueMap) {
        for (Map.Entry<Banknote, Integer> entry : storage.entrySet()) {
            Banknote banknote = entry.getKey();
            int countToWriteOf = issueMap.get(banknote);
            System.out.printf("\tСписано %s - %s/%s%n", banknote.name(), countToWriteOf, entry.getValue());
            storage.merge(banknote, countToWriteOf, SUB);
        }
    }

}
