package atm;

import atm.service.AtmMachine;

import java.util.Map;

import static atm.model.Banknote.B10;
import static atm.model.Banknote.B1000;
import static atm.model.Banknote.B50;
import static atm.model.Banknote.B500;
import static atm.model.Banknote.B5000;

// Банкомат.
// Инициализируется набором купюр и умеет выдавать купюры для заданной суммы, либо отвечать отказом.
// При выдаче купюры списываются с баланса банкомата.
// Допустимые номиналы: 50₽, 100₽, 500₽, 1000₽, 5000₽.
public class Main {

    public static void main(String[] args) {

        AtmMachine atm = new AtmMachine(B50, B500, B5000);

        System.out.println(
                atm.load(
                        Map.of(
                                B50, 10,
                                B500, 10,
                                B5000, 10,
                                B10, 10,
                                B1000, 10
                        )
                )
        );

        System.out.println(atm.getBalance());

        System.out.println(atm.issuing(50450));

        System.out.println(atm.getBalance());


    }

}