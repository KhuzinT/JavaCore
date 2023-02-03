package khuzint.ru;

import java.util.Random;

/**
 * Перечисления вместимостей, которые могут иметь корабли
 */
enum Capacity {
    SMALL,
    AVERAGE,
    BIG;

    public static Capacity getRandomCapacity() {
        Random random = new Random();
        int number = random.nextInt(3) + 1;
        switch (number) {
            case 1:
                return SMALL;
            case 2:
                return AVERAGE;
            default:
                return BIG;
        }
    }

    public int getCapacity() {
        //CHECKSTYLE.OFF: MagicNumber
        if (this == SMALL) {
            return 10;
        }
        if (this == AVERAGE) {
            return 50;
        }
        return 100;
        //CHECKSTYLE:OFF: MagicNumber
    }
}
