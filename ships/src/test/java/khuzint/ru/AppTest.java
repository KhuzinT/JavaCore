package khuzint.ru;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AppTest {

    private void checkShipTime(Ship ship) {
        var departure = ship.getDepartureTime();
        var arrival = ship.getArrivalTime();
        var unloadStart = ship.getUnloadStartTime();
        var unloadEnd = ship.getUnloadFinishTime();

        // проверка что корабль зашел в тоннель happens-before вышел из него
        Assertions.assertTrue(arrival.isAfter(departure));
        // проверка что корабль вышел из тоннеля h-b начал разгрузку
        Assertions.assertTrue(unloadStart.isAfter(arrival));
        // проверка что корабль начал разгрузку h-b закончил ее
        Assertions.assertTrue(unloadEnd.isAfter(unloadStart));

        // проверка что корабль был в тоннеле ровно одну секунду
        var timeInTunnel = Duration.between(departure, arrival).getSeconds();
        Assertions.assertEquals(timeInTunnel, 1);

        // проверка что корабль был в доке ровно столько, сколько нужно
        var timeInDock = Duration.between(unloadStart, unloadEnd).getSeconds();
        Assertions.assertEquals(timeInDock, ship.getCapacity().getCapacity() / 10);
    }

    @Test
    void tunnelTest() {
        try {
            System.out.println("--> Test for tunnel");
            System.out.println("--> ----------------------------------------------------------");
            SeaPort.resetNumbering();
            var ships = SeaPort.createShips(10);
            var docks = Dock.createDocksFromProducts(10);
            var tunnel = new Tunnel(5);

            App.printShipsTable(ships);
            App.sendShipsToDocksThroughTunnel(ships, docks, tunnel);

            Set<Integer> firstEntry = new HashSet<>();
            Set<Integer> secondEntry = new HashSet<>();

            Set<Integer> firstExit = new HashSet<>();
            Set<Integer> secondExit = new HashSet<>();

            for (int idx = 0; idx < ships.size(); ++idx) {
                var ship = ships.get(idx);
                if (idx < 5) {
                    firstEntry.add(ship.getDepartureTime().getSecond());
                    firstExit.add(ship.getArrivalTime().getSecond());
                } else {
                    secondEntry.add(ship.getDepartureTime().getSecond());
                    secondExit.add(ship.getArrivalTime().getSecond());
                }
            }

            // 10 кораблей - два захода в тоннель

            // проверка что первая половина короблей одновременно вышла и зашла (с точностью до секунд)
            Assertions.assertEquals(1, firstEntry.size());
            Assertions.assertEquals(1, firstExit.size());

            // проверка что вторая половина короблей одновременно вышла и зашла (с точностью до секунд)
            Assertions.assertEquals(1, secondEntry.size());
            Assertions.assertEquals(1, secondExit.size());

            // проверка что вторая половина зашла, когда вышла первая
            Assertions.assertEquals(firstExit.iterator().next(), secondEntry.iterator().next());

            for (var ship : ships) {
                checkShipTime(ship);
            }

            System.out.println("--> ----------------------------------------------------------");
            System.out.println("--> (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧ ");
            System.out.println("--> ----------------------------------------------------------");
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        }
    }

    @Test
    void parallelTest() {
        try {
            System.out.println("--> Test for parallel");
            System.out.println("--> ----------------------------------------------------------");

            var ships = new ArrayList<Ship>();
            ships.add(new Ship(1, Product.BREAD, Capacity.BIG));
            ships.add(new Ship(2, Product.BANANAS, Capacity.BIG));
            ships.add(new Ship(3, Product.CLOTHES, Capacity.BIG));

            var docks = Dock.createDocksFromProducts(10);
            var tunnel = new Tunnel(5);

            App.printShipsTable(ships);
            App.sendShipsToDocksThroughTunnel(ships, docks, tunnel);

            // корабли не должны мешать друг другу и проходить все этапы одновременно

            Set<Integer> departure = new HashSet<>();
            Set<Integer> arrival = new HashSet<>();

            Set<Integer> unloadStart = new HashSet<>();
            Set<Integer> unloadEnd = new HashSet<>();

            for (var ship : ships) {
                departure.add(ship.getDepartureTime().getSecond());
                arrival.add(ship.getArrivalTime().getSecond());

                unloadStart.add(ship.getUnloadStartTime().getSecond());
                unloadEnd.add(ship.getUnloadFinishTime().getSecond());
            }

            // проверка что все одновременно зашли в тоннель
            Assertions.assertEquals(1, departure.size());

            // проверка что все одновременно вышли из тоннеля
            Assertions.assertEquals(1, arrival.size());

            // проверка что все одновременно начали разгрузку
            Assertions.assertEquals(1, unloadStart.size());

            // проверка что все одновременно завершили разгрузку
            Assertions.assertEquals(1, unloadEnd.size());

            for (var ship : ships) {
                checkShipTime(ship);
            }

            System.out.println("--> ----------------------------------------------------------");
            System.out.println("--> (⌒‿⌒)");
            System.out.println("--> ----------------------------------------------------------");
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        }
    }

    private void randomTest(int numberOfShips) {
        try {
            SeaPort.resetNumbering();
            var ships = SeaPort.createShips(numberOfShips);
            var docks = Dock.createDocksFromProducts(10);
            var tunnel = new Tunnel(5);

            App.printShipsTable(ships);
            App.sendShipsToDocksThroughTunnel(ships, docks, tunnel);
            for (var ship : ships) {
                checkShipTime(ship);
            }

        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        }
    }

    @Test
    void randomCreatingTest() {
        System.out.println("--> Test with random ships");

        System.out.println("--> ----------------------------------------------------------");
        System.out.println("--> Subtest with 1 ship:");
        randomTest(1);
        System.out.println("--> ----------------------------------------------------------");
        System.out.println("--> ＼(＾▽＾)／ ");

        System.out.println("--> ----------------------------------------------------------");
        System.out.println("--> Subtest with 2 ships:");
        randomTest(2);
        System.out.println("--> ----------------------------------------------------------");
        System.out.println("--> (((o(*°▽°*)o)))");

        System.out.println("--> ----------------------------------------------------------");
        System.out.println("--> Subtest with 5 ships:");
        randomTest(5);
        System.out.println("--> ----------------------------------------------------------");
        System.out.println("--> (◕‿◕)");

        System.out.println("--> ----------------------------------------------------------");
        System.out.println("--> Subtest with 8 ships:");
        randomTest(8);
        System.out.println("--> ----------------------------------------------------------");
        System.out.println("--> (╯✧▽✧)╯");

        System.out.println("--> ----------------------------------------------------------");
        System.out.println("--> Subtest with 16 ships:");
        randomTest(16);
        System.out.println("--> ----------------------------------------------------------");
        System.out.println("--> o(≧▽≦)o");

        System.out.println("--> ----------------------------------------------------------");
        System.out.println("--> Subtest with 21 ships:");
        randomTest(21);
        System.out.println("--> ----------------------------------------------------------");
        System.out.println("--> (* ^ ω ^)");
        System.out.println("--> ----------------------------------------------------------");
    }
}
