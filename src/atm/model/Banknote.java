package atm.model;

public enum Banknote {
    B5000(5000),
    B1000(1000),
    B100(100),
    B500(500),
    B50(50);

    private final int nominal;


    Banknote(int nominal) {
        this.nominal = nominal;
    }

    public int getNominal() {
        return nominal;
    }

}
