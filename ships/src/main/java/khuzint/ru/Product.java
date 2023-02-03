package khuzint.ru;

import java.util.Random;

/**
 * Перечисление продуктов, которые могут перевозить корабли
 */
enum Product {
    BREAD,
    BANANAS,
    CLOTHES;

    public static Product getRandomProduct() {
        Random random = new Random();
        int number = random.nextInt(3) + 1;
        switch (number) {
            case 1:
                return BREAD;
            case 2:
                return BANANAS;
            default:
                return CLOTHES;
        }
    }
}
