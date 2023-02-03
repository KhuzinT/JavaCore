package khuzint.ru;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;


public final class App {
    //CHECKSTYLE.OFF: ConstantName
    private static final int tunnelSize = 5;
    private static final int unloadSpeed = 10;
    //CHECKSTYLE.ON: ConstantName

    private App() {
    }

    public static void printShipsTable(ArrayList<Ship> ships) {
        System.out.println("--> These ships were created in the sea port:");
        System.out.println("--> id  capacity  product");
        for (var ship : ships) {
            //CHECKSTYLE.OFF: MagicNumber
            var id = ship.getId();
            int firstSpaceCount = 6 - Integer.toString(id).length();
            String firstSpaces = " ".repeat(firstSpaceCount);

            var capacity = ship.getCapacity().getCapacity();
            int secondSpaceCount = 8 - Integer.toString(capacity).length();
            String secondSpaces = " ".repeat(secondSpaceCount);

            var product = ship.getProduct();
            System.out.println("--> " + id + firstSpaces + capacity + secondSpaces + product);
            //CHECKSTYLE.ON: MagicNumber
        }
    }

    public static void sendShipsToDocksThroughTunnel(ArrayList<Ship> ships, Map<Product, Dock> docks, Tunnel tunnel) {
        var futureShips = tunnel.letPassShipsAndReturnFutureShips(ships);
        Dock.acceptShips(ships, futureShips, docks);
        for (var futureShip : futureShips) {
            while (true) {
                if (futureShip.isDone()) {
                    break;
                }
            }
        }
        System.out.println("--> All ships arrived at the docks and were unloaded");
    }

    public static void main(String[] args) {
        System.out.println("--> Program to send ships to sail.");
        try (Scanner in = new Scanner(System.in)) {
            System.out.print("--> Please, enter count of ships, which you want to create:\n* ");
            var numberOfShips = in.nextInt();

            SeaPort.resetNumbering();
            var ships = SeaPort.createShips(numberOfShips);
            var docks = Dock.createDocksFromProducts(unloadSpeed);
            var tunnel = new Tunnel(tunnelSize);

            printShipsTable(ships);
            sendShipsToDocksThroughTunnel(ships, docks, tunnel);
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        }
    }
}
