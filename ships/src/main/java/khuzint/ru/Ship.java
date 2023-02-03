package khuzint.ru;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Класс корабль. Runnable объект, определяется своими id, продуктом и вместимостью.
 */
@Getter @Setter
public final class Ship implements Runnable {
    private final int id;
    private final Product product;
    private final Capacity capacity;

    public Ship(int id, Product product, Capacity capacity) {
        this.id = id;
        this.product = product;
        this.capacity = capacity;
    }

    /** Статическое поле - формат даты для вывода */
    //CHECKSTYLE.OFF: ConstantName
    private static final DateTimeFormatter date = DateTimeFormatter.ofPattern("HH:mm:ss");
    //CHECKSTYLE.ON: ConstantName

    /** Время, когда корадль зашел в тоннель */
    private LocalDateTime departureTime;
    /** Время, когда корадль вышел из тоннеля */
    private LocalDateTime arrivalTime;

    /** Время, когда корабль зашел в док и начал разгрузку */
    private LocalDateTime unloadStartTime;
    /** Время, когда корадль закончил разгрузку */
    private LocalDateTime unloadFinishTime;

    /** Атомарное поле, чтобы знать, что корадль уже вышел из тоннеля */
    private AtomicBoolean isExitTunnel = new AtomicBoolean(false);

    //CHECKSTYLE.OFF: MagicNumber
    private String getSpacesForId() {
        int spaceCount = 4 - Integer.toString(id).length();
        return " ".repeat(spaceCount);
    }

    private String getSpacesForProduct() {
        int spaceCount = 8 - product.toString().length();
        return " ".repeat(spaceCount);
    }
    //CHECKSTYLE.ON: MagicNumber

    public void printDepartureTime() {
        System.out.println("--> Ship number " + id + getSpacesForId()
                + "departed         at " + date.format(departureTime));
    }

    public void printArrivalTime() {
        System.out.println("--> Ship number " + id + getSpacesForId()
                + "arrived to dock  at " + date.format(arrivalTime));
    }

    public void printUnloadStartTime() {
        System.out.println("--> Ship number " + id + getSpacesForId()
                + "start unload     at " + date.format(unloadStartTime));
    }

    public void printUnloadFinishTime() {
        System.out.println("--> Ship number " + id + getSpacesForId()
                + "finish unload    at " + date.format(unloadFinishTime));
    }

    public void printStartDock() {
        System.out.println("--> Dock of " + product
                + getSpacesForProduct() + "accept ship number " + getId()
                + " and start unloading");
    }

    public void printEndDock() {
        System.out.println("--> Dock of " + product
                + getSpacesForProduct() + "finish unloading ship number " + id);
    }

    public void printDockTime() {
        var timeDiff = Duration.between(unloadStartTime, unloadFinishTime);
        System.out.println("--> Ship number " + id + getSpacesForId()
                + "was in the dock of " + product + getSpacesForProduct()
                + "for " + timeDiff.getSeconds() + " seconds");
    }

    /**
     * Функция-задача корабля. Выполняется в thread pool-е тоннеля.
     * Заходим в тоннель - засыпаем на 1 секунду - выходим из тоннеля.
     */
    @Override
    public void run() {
        try {
            departureTime = LocalDateTime.now();
            printDepartureTime();

            //CHECKSTYLE.OFF: MagicNumber
            var oneSecond = 1000L;
            Thread.sleep(oneSecond);
            //CHECKSTYLE.ON: MagicNumber

            arrivalTime = LocalDateTime.now();
            printArrivalTime();


            isExitTunnel.set(true);
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        }
    }

}
