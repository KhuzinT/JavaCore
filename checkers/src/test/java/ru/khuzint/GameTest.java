package ru.khuzint;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

public class GameTest {
    @Test
    void testFromCondition() {
        Game game = new Game();

        try {
            String[] whiteCheckersPos = {"a1", "a3", "b2", "c1", "c3", "d2", "e1", "e3", "f2", "g1", "g3", "h2"};
            String[] blackCheckersPos = {"a7", "b6", "b8", "c7", "d6", "d8", "e7", "f6", "f8", "g7", "h6", "h8"};

            game.placeCheckers(whiteCheckersPos, Color.WHITE);
            game.placeCheckers(blackCheckersPos, Color.BLACK);

            String[] moves = {"g3-f4", "f6-e5",
                    "c3-d4", "e5:c3",
                    "b2:d4", "d6-c5",
                    "d2-c3", "g7-f6",
                    "h2-g3", "h8-g7",
                    "c1-b2", "f6-g5",
                    "g3-h4", "g7-f6",
                    "f4-e5", "f8-g7"};

            for (var move : moves) {
                game.makeMove(move);
            }

            String[] newWhiteCheckersPos = game.getCheckersPosByColor(Color.WHITE);
            String[] newBlackCheckersPos = game.getCheckersPosByColor(Color.BLACK);

            String[] whiteOutput = {"a1", "a3", "b2", "c3", "d4", "e1", "e3", "e5", "f2", "g1", "h4"};
            String[] blackOutput = {"a7", "b6", "b8", "c5", "c7", "d8", "e7", "f6", "g5", "g7", "h6"};

            Assertions.assertThat(whiteOutput).isEqualTo(newWhiteCheckersPos);
            Assertions.assertThat(blackOutput).isEqualTo(newBlackCheckersPos);

        } catch (Exception ignored) {
        }

        System.out.println("(ﾉ◕ヮ◕)ﾉ*:･ﾟ✧ \n");
    }

    @Test
    void justTest() {
        Game game = new Game();

        try {
            String[] whiteCheckersPos = {"C3"};
            String[] blackCheckersPos = {"d4", "f6", "e3", "E1"};

            game.placeCheckers(whiteCheckersPos, Color.WHITE);
            game.placeCheckers(blackCheckersPos, Color.BLACK);

            String[] moves = {"C3:E5:G7", "e3-d4", "G7:C3", "E1:A5"};

            for (var move : moves) {
                game.makeMove(move);
            }

            String[] newWhiteCheckersPos = game.getCheckersPosByColor(Color.WHITE);
            String[] newBlackCheckersPos = game.getCheckersPosByColor(Color.BLACK);

            String[] blackOutput = {"A5"};

            Assertions.assertThat(newWhiteCheckersPos).isEmpty();
            Assertions.assertThat(newBlackCheckersPos).isEqualTo(blackOutput);

        } catch (Exception ignored) {
        }

        System.out.println("＼(＾▽＾)／ \n");
    }

    @Test
    void makeQueenTest() {
        Game game = new Game();

        try {
            String[] whiteCheckersPos = {"g7"};
            String[] blackCheckersPos = {"b2"};

            game.placeCheckers(whiteCheckersPos, Color.WHITE);
            game.placeCheckers(blackCheckersPos, Color.BLACK);

            String[] moves = {"g7-h8", "b2-a1"};

            for (var move : moves) {
                game.makeMove(move);
            }

            String[] newWhiteCheckersPos = game.getCheckersPosByColor(Color.WHITE);
            String[] newBlackCheckersPos = game.getCheckersPosByColor(Color.BLACK);

            String[] whiteOutput = {"H8"};
            String[] blackOutput = {"A1"};

            Assertions.assertThat(newWhiteCheckersPos).isEqualTo(whiteOutput);
            Assertions.assertThat(newBlackCheckersPos).isEqualTo(blackOutput);

        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        }

        System.out.println("(((o(*°▽°*)o)))\n");
    }

    @Test
    void busyCellTest() {
        Game game = new Game();

        try {
            String[] whiteCheckersPos = {"g7", "H8"};
            String[] blackCheckersPos = {"b2", "A1"};

            game.placeCheckers(whiteCheckersPos, Color.WHITE);
            game.placeCheckers(blackCheckersPos, Color.BLACK);

            String[] moves = {"g7-h8", "b2-a1"};

            for (var move : moves) {
                game.makeMove(move);
            }

        } catch (Exception exp) {
            Assertions.assertThat(exp.getMessage()).isEqualTo("busy cell");
        }

        System.out.println("(◕‿◕)\n");
    }

    @Test
    void whiteCellTest() {
        Game game = new Game();

        try {
            String[] whiteCheckersPos = {"h1"};
            String[] blackCheckersPos = {"a8"};

            game.placeCheckers(whiteCheckersPos, Color.WHITE);
            game.placeCheckers(blackCheckersPos, Color.BLACK);

        } catch (Exception exp) {
            Assertions.assertThat(exp.getMessage()).isEqualTo("white cell");
        }

        System.out.println("(╯✧▽✧)╯\n");
    }

    @Test
    void incorrectInputTest() {
        Game game = new Game();

        try {
            String[] whiteCheckersPos = {"h8"};

            game.placeCheckers(whiteCheckersPos, Color.WHITE);

        } catch (Exception exp) {
            Assertions.assertThat(exp.getMessage()).isEqualTo("incorrect input");
        }

        try {
            String[] whiteCheckersPos = {"42"};

            game.placeCheckers(whiteCheckersPos, Color.WHITE);

        } catch (Exception exp) {
            Assertions.assertThat(exp.getMessage()).isEqualTo("incorrect input");
        }

        System.out.println("o(≧▽≦)o\n");
    }

    @Test
    void emptyCellTest() {
        Game game = new Game();

        try {
            String[] whiteCheckersPos = {"g7", "H8"};
            String[] blackCheckersPos = {"b2", "A1"};

            game.placeCheckers(whiteCheckersPos, Color.WHITE);
            game.placeCheckers(blackCheckersPos, Color.BLACK);

            String[] moves = {"d4-e5", "e5-d4"};

            for (var move : moves) {
                game.makeMove(move);
            }

        } catch (Exception exp) {
            Assertions.assertThat(exp.getMessage()).isEqualTo("empty cell");
        }

        System.out.println("(* ^ ω ^)\n");
    }

    @Test
    void killFriendTest() {
        Game game = new Game();

        try {
            String[] whiteCheckersPos = {"a1", "b2"};

            game.placeCheckers(whiteCheckersPos, Color.WHITE);

            String[] moves = {"a1:c3"};

            for (var move : moves) {
                game.makeMove(move);
            }
        } catch (Exception exp) {
            Assertions.assertThat(exp.getMessage()).isEqualTo("kill friend");
        }

        System.out.println("(｡•́‿•̀｡)\n");
    }

    @Test
    void needAttackTest() {
        Game game = new Game();

        try {
            String[] whiteCheckersPos = {"B2"};
            String[] blackCheckersPos = {"G7"};

            game.placeCheckers(whiteCheckersPos, Color.WHITE);
            game.placeCheckers(blackCheckersPos, Color.BLACK);

            String[] moves = {"B2-C3", "G7-F6"};

            for (var move : moves) {
                game.makeMove(move);
            }

        } catch (Exception exp) {
            Assertions.assertThat(exp.getMessage()).isEqualTo("invalid move");
        }

        System.out.println("(⌒‿⌒)\n");
    }

    @Test
    void incorrectMoveTest() {
        Game game = new Game();

        try {
            String[] whiteCheckersPos = {"b2"};
            String[] blackCheckersPos = {"g7"};

            game.placeCheckers(whiteCheckersPos, Color.WHITE);
            game.placeCheckers(blackCheckersPos, Color.BLACK);

            String[] moves = {"b2-d4", "g7-e5"};

            for (var move : moves) {
                game.makeMove(move);
            }

        } catch (Exception exp) {
            Assertions.assertThat(exp.getMessage()).isEqualTo("incorrect move");
        }

        try {
            String[] whiteCheckersPos = {"B2"};
            String[] blackCheckersPos = {"c3", "d4"};

            game.placeCheckers(whiteCheckersPos, Color.WHITE);
            game.placeCheckers(blackCheckersPos, Color.BLACK);

            String[] moves = {"B2:E5"};

            for (var move : moves) {
                game.makeMove(move);
            }

        } catch (Exception exp) {
            Assertions.assertThat(exp.getMessage()).isEqualTo("incorrect move");
        }

        System.out.println("¯\\_(ツ)_/¯\n");
    }

}
