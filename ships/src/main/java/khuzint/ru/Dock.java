package khuzint.ru;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Класс причал. Определеяется продуктом, который он разгружает и скоростью разгрузки.
 * Для реализации алгоритма имеет внутри себя thread pool размера 1
 */
@Getter @Setter
public final class Dock {
    private final Product product;
    private final int unloadSpeed;
    private final ExecutorService pool;

    public Dock(Product product, int unloadSpeed) {
        this.product = product;
        this.unloadSpeed = unloadSpeed;
        this.pool = Executors.newFixedThreadPool(1);
    }

    /**
     * Функция создает причал для каждого продукта из класса Product
     * @param unloadSpeed - скорость разгрузки созданных причалов
     * @return возвращает Map с причалами по продуктам
     */
    public static Map<Product, Dock> createDocksFromProducts(int unloadSpeed) {
        var docks = new HashMap<Product, Dock>();
        for (var product : Product.values()) {
            var dock = new Dock(product, unloadSpeed);
            docks.put(product, dock);
        }
        return docks;
    }


    /**
     * Одна из главных функций программы.
     * Разгружает корабли. Для каждого корабля выбирает док, в который тот должен отправиться,
     * затем делает submit задачи разгрузки в thread pool дока.
     * В задаче разгрузки мы ждем, пока корабль выйдет из тоннеля, затем засыпаем на время разгрузки
     * @param ships - массив коралей
     * @param futureShips - массив Future полученный после прохождения тоннеля
     * @param docks - Map с доками по продуктам
     */
    public static void acceptShips(ArrayList<Ship> ships, ArrayList<Future<?>> futureShips, Map<Product, Dock> docks) {
        for (int idx = 0; idx < ships.size(); ++idx) {
            var ship = ships.get(idx);
            var dock = docks.get(ship.getProduct());
            Runnable unloadShipTask = () -> {
                try {
                    while (true) {
                        if (ship.getIsExitTunnel().get()) {
                            // ship.printStartDock();
                            ship.setUnloadStartTime(LocalDateTime.now());
                            ship.printUnloadStartTime();

                            //CHECKSTYLE.OFF: MagicNumber
                            var oneSecond = 1000L;
                            Thread.sleep((ship.getCapacity().getCapacity() / dock.unloadSpeed) * oneSecond);
                            //CHECKSTYLE.ON: MagicNumber

                            ship.setUnloadFinishTime(LocalDateTime.now());
                            ship.printUnloadFinishTime();
                            // ship.printEndDock();
                            ship.printDockTime();

                            break;
                        }
                    }
                } catch (Exception exp) {
                    System.out.println(exp.getMessage());
                }
            };
            futureShips.set(idx, dock.pool.submit(unloadShipTask));
        }
        for (var key : docks.keySet()) {
            var dock = docks.get(key);
            dock.pool.shutdown();
        }
    }
}
