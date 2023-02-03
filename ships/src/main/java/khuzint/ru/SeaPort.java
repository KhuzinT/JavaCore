package khuzint.ru;

import java.util.ArrayList;

/**
 * Класс морской порт. Нужен только для создания кораблей.
 */
public final class SeaPort {
    private SeaPort() {
    }

    private static int currentShipNumber = 1;

    /**
     * Функция чтобы обнулить нумерацию
     */
    public static void resetNumbering() {
        currentShipNumber = 1;
    }

    /**
     * Функция создает новый корабль с рандомными значениями продукта и вместимости
     */
    public static Ship createShip() {
        return new Ship(currentShipNumber++, Product.getRandomProduct(), Capacity.getRandomCapacity());
    }

    /**
     * Функция создает несколько рандомных кораблей.
     * @param numberOfShips - количество кораблей, которые нужно создать
     * @return возвращает массив созданных кораблей
     */
    public static ArrayList<Ship> createShips(int numberOfShips) {
        ArrayList<Ship> ships = new ArrayList<>();
        for (int idx = 0; idx < numberOfShips; ++idx) {
            var ship = createShip();
            ships.add(ship);
        }
        return ships;
    }
}
