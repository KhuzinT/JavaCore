package ru.khuzint;

import java.util.ArrayList;

public final class Tower {
    private final ArrayList<Checker> checkers = new ArrayList<>();

    Tower(String towerDescription) {
        String[] posAndColors = towerDescription.split("_");
        String position = posAndColors[0];
        String checkersColors = posAndColors[1];

        for (int idx = 0; idx < checkersColors.length(); ++idx) {
            var currentColor = checkersColors.substring(idx, idx + 1);
            placeCheckerOnPosition(currentColor, position);
        }
    }

    private void placeCheckerOnPosition(String currentColor, String position) {
        Color color = currentColor.matches("[wW]") ? Color.WHITE : Color.BLACK;
        Checker currentChecker = new Checker(position, color);

        if (currentColor.matches("[WB]")) {
            currentChecker.makeQueen();
        }

        checkers.add(currentChecker);
    }

    private void pushChecker(Checker checker) {
        checker.setCell(topChecker().getCell());
        checkers.add(checker);
    }

    private Checker topChecker() {
        return checkers.get(0);
    }

    private Checker popChecker() {
        Checker top = topChecker();
        checkers.remove(0);
        return top;
    }

    public void shiftChecker(Tower other) {
        this.pushChecker(other.popChecker());
    }

    public boolean isItEmptyTower() {
        return checkers.size() == 0;
    }

    public void setCell(Cell cell) {
        for (var current : checkers) {
            current.setCell(cell);
        }
    }

    public Cell getCell() {
        return topChecker().getCell();
    }

    public void makeQueen() {
        topChecker().makeQueen();
    }

    public boolean isItQueen() {
        return topChecker().isItQueen();
    }

    public boolean isItOnLastRow() {
        return topChecker().isItOnLastRow();
    }

    public Color getColor() {
        return topChecker().getColor();
    }

    public String getStringPos() {
        return topChecker().getStringPos();
    }

    public String getTowerDescription() {
        String towerPos = getStringPos();

        char[] colors = new char[checkers.size()];
        for (int idx = 0; idx < checkers.size(); ++idx) {
            var current = checkers.get(idx);

            if (current.getColor() == Color.WHITE) {
                colors[idx] = current.isItQueen() ? 'W' : 'w';
            } else {
                colors[idx] = current.isItQueen() ? 'B' : 'b';
            }
        }

        return towerPos.toLowerCase() + "_" + new String(colors);
    }
}
