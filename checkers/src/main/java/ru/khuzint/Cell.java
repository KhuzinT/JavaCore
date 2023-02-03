package ru.khuzint;

public final class Cell {
    private final int x;
    private final int y;

    Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isItBlackCell() {
        return x % 2 == y % 2;
    }

    public boolean isItCellInBoard() {
        if (1 <= x && x <= Game.K_BOARD_SIZE) {
            return 1 <= y && y <= Game.K_BOARD_SIZE;
        }
        return false;
    }

    public boolean isItValidCell() {
        return isItBlackCell() && isItCellInBoard();
    }

    public static Cell makeStepInDirectionAndReturnNew(Cell current, Cell direction) {
        return new Cell(current.x + direction.x, current.y + direction.y);
    }

    public static boolean equals(Cell first, Cell second) {
        return first.x == second.x && first.y == second.y;
    }
}


