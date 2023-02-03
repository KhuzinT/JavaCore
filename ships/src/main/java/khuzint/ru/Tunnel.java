package khuzint.ru;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Класс тоннель. Определяется размером - количеством кораблей, которые он может пропустить через себя за 1 секунду.
 * Для реализации имеет внутри себя thread pool заданного размера.
 */
public final class Tunnel {
    private final int tunnelSize;
    private final ExecutorService pool;

    public Tunnel(int tunnelSize) {
        this.tunnelSize = tunnelSize;
        this.pool = Executors.newFixedThreadPool(tunnelSize);
    }

    /**
     * Функция пропускает корабли через тоннель.
     * Для каждого корабля делает submit корабля-задачи в свой thread pool и получает оттуда Future
     * @param ships - массив кораблей
     * @return возвращает полученный массив Future
     */
    public ArrayList<Future<?>> letPassShipsAndReturnFutureShips(ArrayList<Ship> ships) {
        var futureShips = new ArrayList<Future<?>>();
        for (var ship : ships) {
            var futureShip = pool.submit(ship);
            futureShips.add(futureShip);
        }
        pool.shutdown();
        return futureShips;
    }

}
