package ru.khuzint;

enum Color {
    BLACK,
    WHITE
}

public final class Checker {

    private Cell cell;
    private boolean isItQueen;
    private final Color color;


    Checker(String checkerPos, Color color) {
        this.cell = getCellFromPos(checkerPos);
        this.color = color;
        this.isItQueen = false;
    }

    public static Cell getCellFromPos(String position) {
        String positionLower = position.toLowerCase();
        int x = positionLower.charAt(0) - 'a' + 1;
        int y = positionLower.charAt(1) - '0';

        return new Cell(x, y);
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
    }

    public void makeQueen() {
        this.isItQueen = true;
    }

    public boolean isItQueen() {
        return isItQueen;
    }

    public boolean isItOnLastRow() {
        if (color == Color.WHITE) {
            return cell.getY() == Game.K_BOARD_SIZE;
        } else {
            return cell.getY() == 1;
        }
    }

    public Color getColor() {
        return color;
    }

    public String getStringPos() {
        char startLetter = isItQueen ? 'A' : 'a';
        char letter = (char) (startLetter + cell.getX() - 1);
        char number = (char) ('0' + cell.getY());
        return letter + Character.toString(number);
    }
}
