package atm.service;

import atm.model.ATM;
import atm.model.Banknote;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;

public class AtmMachine implements ATM {

    private static final BinaryOperator<Integer> SUBTRACTION = (a, b) -> a - b;

    private final Banknote[] permitBanknotes;
    private final Map<Banknote, Integer> storage;

    public AtmMachine(Banknote... banknotes) {
        Set<Banknote> set = new HashSet<>(List.of(banknotes));
        List<Banknote> list = new ArrayList<>(set);
        list.sort(Comparator.comparingInt(Banknote::getNominal).reversed());
        permitBanknotes = list.toArray(new Banknote[0]);
        storage = getInitBanknoteMap();
    }

    @Override
    public String load(Map<Banknote, Integer> loadMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("ЗАГРУЗКА:").append(System.lineSeparator());
        for (Map.Entry<Banknote, Integer> entry : loadMap.entrySet()) {
            sb.append("\t");
            String banknoteName = entry.getKey().name();
            try {
                load(entry.getKey(), entry.getValue());
                sb.append("Добавлено ").append(banknoteName)
                        .append(" - ").append(entry.getValue())
                        .append(System.lineSeparator());
            } catch (Exception e) {
                sb.append("АТМ не работает с ").append(banknoteName)
                        .append(System.lineSeparator());
            }
        }
        sb.setLength(sb.length() - System.lineSeparator().length());
        return sb.toString();
    }

    private void load(Banknote banknote, Integer countToAdd) throws Exception {
        if (storage.containsKey(banknote)) {
            storage.merge(banknote, countToAdd, Integer::sum);
        } else throw new Exception();
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

    private Map<Banknote, Integer> getInitBanknoteMap() {
        Map<Banknote, Integer> map = new EnumMap<>(Banknote.class);
        for (Banknote banknote : permitBanknotes) {
            map.put(banknote, 0);
        }
        return map;
    }

    private Map<Banknote, Integer> getIssueMap(int amount) throws Exception {
        Map<Banknote, Integer> issueMap = getInitBanknoteMap();
        for (Banknote banknote : permitBanknotes) {
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
            storage.merge(banknote, countToWriteOf, SUBTRACTION);
        }
    }

}
