package ru.khuzint;

public abstract class GameException extends Exception {
    public GameException(String errorMessage) {
        super(errorMessage);
    }
}

//-----------about input-----------------------------
class IncorrectInputException extends GameException {
    IncorrectInputException() {
        super("incorrect input");
    }
}
//---------------------------------------------------


//-----------about cell------------------------------
class BusyCellException extends GameException {
    BusyCellException() {
        super("busy cell");
    }
}

class EmptyCellException extends GameException {
    EmptyCellException() {
        super("empty cell");
    }
}

class WhiteCellException extends GameException {
    WhiteCellException() {
        super("white cell");
    }
}

class NotOnBoardException extends GameException {
    NotOnBoardException() {
        super("not on board");
    }
}
//---------------------------------------------------


//-----------about move------------------------------
class NeedAttackException extends GameException {
    NeedAttackException() {
        super("invalid move");
    }
}

class IncorrectMoveException extends GameException {
    IncorrectMoveException() {
        super("incorrect move");
    }
}

class KillFriendException extends GameException {
    KillFriendException() {
        super("kill friend");
    }
}
//---------------------------------------------------
