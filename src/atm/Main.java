package atm;

import atm.model.Banknote;
import atm.service.AtmMachine;

// Банкомат.
// Инициализируется набором купюр и умеет выдавать купюры для заданной суммы, либо отвечать отказом.
// При выдаче купюры списываются с баланса банкомата.
// Допустимые номиналы: 50₽, 100₽, 500₽, 1000₽, 5000₽.
public class Main {

    public static void main(String[] args) {

        AtmMachine atm = new AtmMachine();

        atm.load(Banknote.B50, 10);
        atm.load(Banknote.B500, 10);
        atm.load(Banknote.B5000, 10);

        System.out.println(atm.getBalance());

        System.out.println(atm.issuing(50450));

        System.out.println(atm.getBalance());


    }

}