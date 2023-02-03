package ru.khuzint;

import java.util.ArrayList;
import java.util.Collections;

public final class Game {

    //------------fields----------------------------------------------------------------
    /**
     * Константное поле размера доски
     */
    public static final int K_BOARD_SIZE = 8;

    /**
     * Поле --- список башен
     */
    private final ArrayList<Tower> towers = new ArrayList<>();
    //----------------------------------------------------------------------------------


    //------------API-------------------------------------------------------------------

    /**
     * Функция, чтобы положить башни на доску
     *
     * @param towersDescription --- список строк-описаний башен
     */
    public void placeTowers(String[] towersDescription) throws GameException {
        for (String description : towersDescription) {
            placeTower(description);
        }
    }

    /**
     * Функция чтобы сделать ход
     *
     * @param move --- строка-описание хода башни
     */
    public void makeMove(String move) throws GameException {
        if (move.contains("-")) {
            makeSimpleMove(move);
        } else {
            makeAttackMove(move);
        }
    }

    /**
     * Функция возвращает список строк-описаний башен по цвету
     *
     * @param color --- цвет башен, которые нужно вернуть
     * @return возвращает список строк-описаний башен
     */
    public String[] getTowersDescriptionByColor(Color color) {
        var towersByColor = getTowersByColor(color);

        ArrayList<String> towersDescriptions = new ArrayList<>();
        for (var tower : towersByColor) {
            towersDescriptions.add(tower.getTowerDescription());
        }

        Collections.sort(towersDescriptions);
        return towersDescriptions.toArray(new String[0]);
    }
    //----------------------------------------------------------------------------------


    //------------for place towers------------------------------------------------------

    /**
     * Функция чтобы положить башню на доску
     *
     * @param towerDescription --- строка-описание башни
     */
    private void placeTower(String towerDescription) throws GameException {
        checkCorrectInput(towerDescription, "[a-hA-H][1-8]_(w|W|b|B)+");

        Tower newTower = new Tower(towerDescription);

        towers.add(newTower);
        checkValidCell(newTower.getCell());
    }
    //----------------------------------------------------------------------------------


    //------------for make move---------------------------------------------------------

    /**
     * Функция чтобы сделать ход без атаки
     *
     * @param move --- строка-описание хода башни
     */
    private void makeSimpleMove(String move) throws GameException {
        String[] descriptions = move.split("-");
        Cell from = Checker.getCellFromPos(descriptions[0].substring(0, 2));
        Cell to = Checker.getCellFromPos(descriptions[1].substring(0, 2));

        checkNotEmptyCell(from);
        checkEmptyCell(to);

        Tower current = getTowerByCell(from);
        ArrayList<Tower> currentTeam = getTowersByColor(current.getColor());
        for (var tower : currentTeam) {
            checkNotNeedAttack(tower);
        }
        move(from, to, false);
    }

    /**
     * Функия чтобы сделать ход с атакой
     *
     * @param move --- строка-описание хода башни
     */
    private void makeAttackMove(String move) throws GameException {
        String[] descriptions = move.split(":");

        int lastIdx = descriptions.length - 1;
        for (int idx = 0; idx < lastIdx; ++idx) {
            Cell from = Checker.getCellFromPos(descriptions[idx].substring(0, 2));
            Cell to = Checker.getCellFromPos(descriptions[idx + 1].substring(0, 2));

            checkNotEmptyCell(from);
            checkEmptyCell(to);

            move(from, to, true);
        }
    }

    /**
     * Функция чтобы сделать ход с одного поля на другое
     *
     * @param from       --- поле, откуда башня перемещается
     * @param to         --- поле, куда башня перемещается
     * @param isItAttack --- булева переменная, true если перемещение является атакой, false иначе
     */
    private void move(Cell from, Cell to, boolean isItAttack) throws GameException {
        Tower current = getTowerByCell(from);
        checkCorrectMove(current, to, isItAttack);

        if (isItAttack) {
            killTowerOnMove(from, to);
        }

        current.setCell(to);
        if (current.isItOnLastRow()) {
            current.makeQueen();
        }
    }

    /**
     * Функция, чтобы переместить все шашки, убитые за одно перемещение
     *
     * @param from --- поле, откуда башня перемещается
     * @param to   --- поле, куда башня перемещается
     */
    private void killTowerOnMove(Cell from, Cell to) throws GameException {
        Tower current = getTowerByCell(from);

        Tower victim = checkCorrectKillAndReturnVictim(current, to);
        current.shiftChecker(victim);
        if (victim.isItEmptyTower()) {
            towers.remove(victim);
        }
    }
    //----------------------------------------------------------------------------------


    //------------for get towers--------------------------------------------------------

    /**
     * Функция возвращает список башен по цвету
     *
     * @param color --- цвет башен, которые нужно вернуть
     * @return возвращает список башен
     */
    private ArrayList<Tower> getTowersByColor(Color color) {
        ArrayList<Tower> towersByColor = new ArrayList<>();
        for (var current : towers) {
            if (current.getColor() == color) {
                towersByColor.add(current);
            }
        }

        return towersByColor;
    }

    /**
     * Функция, которая возращает башню на текущем поле или null
     *
     * @param cell --- поле, с которого хотим получить башню
     * @return возвращает башню на поле или null
     */
    private Tower getTowerByCellOrNull(Cell cell) {
        for (var current : towers) {
            if (Cell.equals(cell, current.getCell())) {
                return current;
            }
        }

        return null;
    }

    /**
     * Функция, которая возвращает башню на текущем поле или выбрасывает исключение, если поле пусто.
     * Следует использовать, когда уверены, что башня на поле есть
     *
     * @param cell --- поле, с которого хотим получить башню
     * @return возвращает башню на поле
     */
    private Tower getTowerByCell(Cell cell) throws EmptyCellException {
        var current = getTowerByCellOrNull(cell);
        if (current == null) {
            throw new EmptyCellException();
        }
        return current;
    }

    /**
     * Функция проверяет, есть ли на поле башня или нет
     *
     * @param cell --- поле для проверки
     * @return возвращает true, если на поле есть башня, false иначе
     */
    private boolean isItEmptyCell(Cell cell) {
        var current = getTowerByCellOrNull(cell);
        return current == null;
    }
    //----------------------------------------------------------------------------------


    //------------for throw exceptions--------------------------------------------------
    //------------about input-----------------------------------------------------------

    /**
     * Функция проверяет, что ввод корректный
     *
     * @param input  --- строка ввода
     * @param regexp --- строка-РВ, ввод должен распознаваться РВ
     */
    private static void checkCorrectInput(String input, String regexp) throws IncorrectInputException {
        if (!input.matches(regexp)) {
            throw new IncorrectInputException();
        }
    }

    //------------about valid cell-------------------------------------------------------

    /**
     * Функция проверяет, что поле - не белое
     */
    private static void checkNotWhiteCell(Cell current) throws WhiteCellException {
        if (!current.isItBlackCell()) {
            throw new WhiteCellException();
        }
    }

    /**
     * Функция проверяет, что поле находится внутри доски
     */
    private static void checkCellOnBoard(Cell current) throws NotOnBoardException {
        if (!current.isItCellInBoard()) {
            throw new NotOnBoardException();
        }
    }

    /**
     * Функция проверяет, что поле валидно, то есть оно не белое и внутри доски
     */
    private static void checkValidCell(Cell current) throws GameException {
        checkNotWhiteCell(current);
        checkCellOnBoard(current);
    }

    //------------about (not) busy cell--------------------------------------------------

    /**
     * Функция проверяет, что поле не пусто
     */
    private void checkNotEmptyCell(Cell current) throws GameException {
        checkValidCell(current);
        if (isItEmptyCell(current)) {
            throw new EmptyCellException();
        }
    }

    /**
     * Функция проверяет, что поле пусто
     */
    private void checkEmptyCell(Cell current) throws GameException {
        checkValidCell(current);
        if (!isItEmptyCell(current)) {
            throw new BusyCellException();
        }
    }

    //------------about move------------------------------------------------------------

    /**
     * Функция проверяет, что перемещение корректно (координатно)
     *
     * @param current    --- башня, которая перемещается
     * @param to         --- поле, куда башня должна переместиться
     * @param isItAttack --- булева переменная, true если перемещение является атакой, false иначе
     */
    private void checkCorrectMove(Tower current, Cell to, boolean isItAttack) throws IncorrectMoveException {
        Cell from = current.getCell();

        int xDiff = Math.abs(from.getX() - to.getX());
        int yDiff = Math.abs(from.getY() - to.getY());
        if (current.isItQueen()) {
            if (xDiff != yDiff) {
                throw new IncorrectMoveException();
            }
        } else {
            int diff = isItAttack ? 2 : 1;
            if (xDiff != diff || yDiff != diff) {
                throw new IncorrectMoveException();
            }
        }
    }

    //------------about attack-----------------------------------------------------------
    enum AttackStatus {
        UNKNOWN,
        POSSIBLE_ATTACK,
        IMPOSSIBLE_ATTACK
    }

    /**
     * Функция, чтобы узнать, может ли башня атаковать или нет
     *
     * @param current    --- башня, которую проверяем на атаку
     * @param victimCell --- поле, которое хотим атаковать
     * @param nextCell   --- поле, на которое встанем после атаки
     * @return возвращает
     * POSSIBLE_ATTACK, если атака возможна
     * IMPOSSIBLE_ATTACK, если атака невозможна в этом направлении
     * UNKNOWN, если атака невозможна сейчас, но возможна далее в этом направлении
     */
    private AttackStatus getAttackStatus(Tower current, Cell victimCell, Cell nextCell) throws GameException {
        if (!nextCell.isItValidCell()) {
            return AttackStatus.IMPOSSIBLE_ATTACK;
        }
        if (!isItEmptyCell(victimCell) && isItEmptyCell(nextCell)) {
            if (getTowerByCell(victimCell).getColor() == current.getColor()) {
                return AttackStatus.IMPOSSIBLE_ATTACK;
            } else {
                return AttackStatus.POSSIBLE_ATTACK;
            }
        }
        return AttackStatus.UNKNOWN;
    }

    /**
     * Функция проверяет, что башня не должна атаковать в направлении
     *
     * @param current   --- башня, которую проверяем на атаку
     * @param direction --- направление, в котором проверяем на атаку,
     *                  это поле типа Cell с координатами, по модулю равными 1
     */
    private void checkNotNeedAttackInDirection(Tower current, Cell direction) throws GameException {
        Cell currentCell = current.getCell();
        Cell victimCell = Cell.makeStepInDirectionAndReturnNew(currentCell, direction);
        Cell nextCell = Cell.makeStepInDirectionAndReturnNew(victimCell, direction);

        var status = getAttackStatus(current, victimCell, nextCell);
        if (status == AttackStatus.IMPOSSIBLE_ATTACK) {
            return;
        }
        if (status == AttackStatus.POSSIBLE_ATTACK) {
            throw new NeedAttackException();
        }

        if (current.isItQueen()) {
            victimCell = nextCell;
            nextCell = Cell.makeStepInDirectionAndReturnNew(victimCell, direction);
            while (nextCell.isItValidCell()) {
                status = getAttackStatus(current, victimCell, nextCell);
                if (status == AttackStatus.IMPOSSIBLE_ATTACK) {
                    return;
                }
                if (status == AttackStatus.POSSIBLE_ATTACK) {
                    throw new NeedAttackException();
                }
                victimCell = nextCell;
                nextCell = Cell.makeStepInDirectionAndReturnNew(victimCell, direction);
            }
        }
    }

    /**
     * Функция проверяет, что башня не должна атаковать
     *
     * @param current --- башня, которую проверяем на атаку
     */
    private void checkNotNeedAttack(Tower current) throws GameException {
        Cell topLeft = new Cell(-1, +1);
        Cell topRight = new Cell(+1, +1);
        Cell downLeft = new Cell(-1, -1);
        Cell downRight = new Cell(+1, -1);


        checkNotNeedAttackInDirection(current, topLeft);
        checkNotNeedAttackInDirection(current, topRight);
        checkNotNeedAttackInDirection(current, downLeft);
        checkNotNeedAttackInDirection(current, downRight);
    }

    //------------about kill------------------------------------------------------------

    /**
     * Функция проверяет, что атака корректна и возвращает убитую башню
     *
     * @param current --- атакующая башня
     * @param to      --- поле, на которое атакующая башня переместится
     * @return возвращает убитую башню или выбрасывает исключение
     */
    private Tower checkCorrectKillAndReturnVictim(Tower current, Cell to) throws GameException {
        Cell from = current.getCell();

        int xDiff = to.getX() - from.getX();
        int yDiff = to.getY() - from.getY();

        int xStep = xDiff / Math.abs(xDiff);
        int yStep = yDiff / Math.abs(yDiff);

        int xVictim = to.getX() - xStep;
        int yVictim = to.getY() - yStep;
        Cell victimCell = new Cell(xVictim, yVictim);

        if (!current.isItQueen()) {
            if (isItEmptyCell(victimCell)) {
                throw new IncorrectMoveException();
            }
            if (getTowerByCell(victimCell).getColor() == current.getColor()) {
                throw new KillFriendException();
            }
        } else {
            int countOfVictims = 0;
            for (int x = from.getX() + xStep, y = from.getY() + yStep;
                 x != to.getX() && y != to.getY(); x += xStep, y += yStep) {
                Cell nextCell = new Cell(x, y);
                if (!isItEmptyCell(nextCell)) {
                    Tower nextChecker = getTowerByCell(nextCell);
                    if (nextChecker.getColor() == current.getColor()) {
                        throw new KillFriendException();
                    } else {
                        countOfVictims++;
                        victimCell = nextCell;
                    }
                }
            }
            if (countOfVictims != 1) {
                throw new IncorrectMoveException();
            }

        }

        return getTowerByCell(victimCell);
    }
    //----------------------------------------------------------------------------------
}
