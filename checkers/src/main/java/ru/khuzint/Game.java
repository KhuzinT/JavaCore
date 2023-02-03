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
     * Поле --- список шашек
     */
    private final ArrayList<Checker> checkers = new ArrayList<>();
    //----------------------------------------------------------------------------------


    //------------API-------------------------------------------------------------------

    /**
     * Функция, чтобы положить шашки на доску
     *
     * @param checkersPos --- список строк-позиций шашек
     * @param color       --- цвет шашек, которые нужно положить
     */
    public void placeCheckers(String[] checkersPos, Color color) throws GameException {
        for (String position : checkersPos) {
            placeChecker(position, color);
        }
    }

    /**
     * Функция чтобы сделать ход
     *
     * @param move --- строка-описание хода шашки
     */
    public void makeMove(String move) throws GameException {
        checkCorrectInput(move, "([a-hA-H][1-8]-[a-hA-H][1-8])|(([a-hA-H][1-8]:)+[a-hA-H][1-8])");
        if (move.contains("-")) {
            makeSimpleMove(move);
        } else {
            makeAttackMove(move);
        }
    }

    /**
     * Функция возвращает список строк-позиций шашек по цвету
     *
     * @param color --- цвет шашек, которые нужно вернуть
     * @return возвращает список строк-позиций шашек
     */
    public String[] getCheckersPosByColor(Color color) {
        var checkersByColor = getCheckersByColor(color);

        ArrayList<String> checkersPos = new ArrayList<>();
        for (var checker : checkersByColor) {
            checkersPos.add(checker.getStringPos());
        }

        Collections.sort(checkersPos);
        return checkersPos.toArray(new String[0]);
    }
    //----------------------------------------------------------------------------------


    //------------for place checkers----------------------------------------------------

    /**
     * Функция чтобы положить шашку на доску
     *
     * @param checkerPos --- строка-позиция шашки
     * @param color      --- цвет шашки
     */
    private void placeChecker(String checkerPos, Color color) throws GameException {
        checkCorrectInput(checkerPos, "[a-hA-H][1-8]");

        Checker newChecker = new Checker(checkerPos, color);

        if (checkerPos.matches("[A-H][1-8]")) {
            newChecker.makeQueen();
        }

        checkers.add(newChecker);
        checkValidCell(newChecker.getCell());
    }
    //----------------------------------------------------------------------------------


    //------------for make move---------------------------------------------------------

    /**
     * Функция чтобы сделать ход без атаки
     *
     * @param move --- строка-описание хода шашки
     */
    private void makeSimpleMove(String move) throws GameException {
        String[] positions = move.split("-");
        Cell from = Checker.getCellFromPos(positions[0]);
        Cell to = Checker.getCellFromPos(positions[1]);

        checkNotEmptyCell(from);
        checkEmptyCell(to);

        Checker current = getCheckerByCell(from);
        ArrayList<Checker> currentTeam = getCheckersByColor(current.getColor());
        for (var checker : currentTeam) {
            checkNotNeedAttack(checker);
        }
        move(from, to, false);

    }

    /**
     * Функия чтобы сделать ход с атакой
     *
     * @param move --- строка-описание хода шашки
     */
    private void makeAttackMove(String move) throws GameException {
        String[] positions = move.split(":");

        int lastIdx = positions.length - 1;
        for (int idx = 0; idx < lastIdx; ++idx) {
            Cell from = Checker.getCellFromPos(positions[idx]);
            Cell to = Checker.getCellFromPos(positions[idx + 1]);

            checkNotEmptyCell(from);
            checkEmptyCell(to);

            move(from, to, true);
        }
    }

    /**
     * Функция чтобы сделать ход с одного поля на другое
     *
     * @param from       --- поле, откуда шашка перемещается
     * @param to         --- поле, куда шашка перемещается
     * @param isItAttack --- булева переменная, true если перемещение является атакой, false иначе
     */
    private void move(Cell from, Cell to, boolean isItAttack) throws GameException {
        Checker current = getCheckerByCell(from);
        checkCorrectMove(current, to, isItAttack);

        if (isItAttack) {
            killCheckerOnMove(from, to);
        }

        current.setCell(to);
        if (current.isItOnLastRow()) {
            current.makeQueen();
        }
    }

    /**
     * Функция, чтобы убрать с доски все шашки, убитые за одно перемещение
     *
     * @param from --- поле, откуда шашка премещается
     * @param to   --- поле, куда шашка перемещается
     */
    private void killCheckerOnMove(Cell from, Cell to) throws GameException {
        Checker current = getCheckerByCell(from);

        var victim = checkCorrectKillAndReturnVictim(current, to);
        checkers.remove(victim);
    }
    //----------------------------------------------------------------------------------


    //------------for get checkers------------------------------------------------------

    /**
     * Функция возвращает список шашек по цвету
     *
     * @param color --- цвет шашек, которые нужно вернуть
     * @return возвращает список шашек
     */
    private ArrayList<Checker> getCheckersByColor(Color color) {
        ArrayList<Checker> checkersByColor = new ArrayList<>();
        for (var current : checkers) {
            if (current.getColor() == color) {
                checkersByColor.add(current);
            }
        }

        return checkersByColor;
    }

    /**
     * Функция, которая возращает шашку на текущем поле или null
     *
     * @param cell --- поле, с которого хотим получить шашку
     * @return возвращает шашку на поле или null
     */
    private Checker getCheckerByCellOrNull(Cell cell) {
        for (var current : checkers) {
            if (Cell.equals(cell, current.getCell())) {
                return current;
            }
        }

        return null;
    }

    /**
     * Функция, которая возвращает шашку на текущем поле или выбрасывает исключение, если поле пусто.
     * Следует использовать, когда уверены, что шашка на поле есть
     *
     * @param cell --- поле, с которого хотим получить шашку
     * @return возвращает шашку на поле
     */
    private Checker getCheckerByCell(Cell cell) throws EmptyCellException {
        var current = getCheckerByCellOrNull(cell);
        if (current == null) {
            throw new EmptyCellException();
        }
        return current;
    }

    /**
     * Функция проверяет, есть ли на поле шашка или нет
     *
     * @param cell --- поле для проверки
     * @return возвращает true, если на поле есть шашка, false иначе
     */
    private boolean isItEmptyCell(Cell cell) {
        var current = getCheckerByCellOrNull(cell);
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
     * @param current    --- шашка, которая перемещается
     * @param to         --- поле, куда шашка должна переместиться
     * @param isItAttack --- булева переменная, true если перемещение является атакой, false иначе
     */
    private void checkCorrectMove(Checker current, Cell to, boolean isItAttack) throws IncorrectMoveException {
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
     * Функция, чтобы узнать, может ли шашка атаковать или нет
     *
     * @param current    --- шашка, которую проверяем на атаку
     * @param victimCell --- поле, которое хотим атаковать
     * @param nextCell   --- поле, на которое встанем после атаки
     * @return возвращает
     * POSSIBLE_ATTACK, если атака возможна
     * IMPOSSIBLE_ATTACK, если атака невозможна в этом направлении
     * UNKNOWN, если атака невозможна сейчас, но возможна далее в этом направлении
     */
    private AttackStatus getAttackStatus(Checker current, Cell victimCell, Cell nextCell) throws GameException {
        if (!nextCell.isItValidCell()) {
            return AttackStatus.IMPOSSIBLE_ATTACK;
        }
        if (!isItEmptyCell(victimCell) && isItEmptyCell(nextCell)) {
            if (getCheckerByCell(victimCell).getColor() == current.getColor()) {
                return AttackStatus.IMPOSSIBLE_ATTACK;
            } else {
                return AttackStatus.POSSIBLE_ATTACK;
            }
        }
        return AttackStatus.UNKNOWN;
    }

    /**
     * Функция проверяет, что шашка не должна атаковать в направлении
     *
     * @param current   --- шашка, которую проверяем на атаку
     * @param direction --- направление, в котором проверяем на атаку,
     *                  это поле типа Cell с координатами, по модулю равными 1
     */
    private void checkNotNeedAttackInDirection(Checker current, Cell direction) throws GameException {
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
     * Функция проверяет, что шашка не должна атаковать
     *
     * @param current --- шашка, которую проверяем на атаку
     */
    private void checkNotNeedAttack(Checker current) throws GameException {
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
     * Функция проверяет, что атака корректна и возвращает убитую шашку
     *
     * @param current --- атакующая шашка
     * @param to      --- поле, на которое атакующая шашка переместится
     * @return возвращает убитую шашку или выбрасывает исключение
     */
    private Checker checkCorrectKillAndReturnVictim(Checker current, Cell to) throws GameException {
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
            if (getCheckerByCell(victimCell).getColor() == current.getColor()) {
                throw new KillFriendException();
            }
        } else {
            int countOfVictims = 0;
            for (int x = from.getX() + xStep, y = from.getY() + yStep;
                 x != to.getX() && y != to.getY(); x += xStep, y += yStep) {
                Cell nextCell = new Cell(x, y);
                if (!isItEmptyCell(nextCell)) {
                    Checker nextChecker = getCheckerByCell(nextCell);
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

        return getCheckerByCell(victimCell);
    }
    //----------------------------------------------------------------------------------
}
