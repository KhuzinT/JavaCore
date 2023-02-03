package ru.khuzint;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

public class GameTest {
    @Test
    void firstTestFromCondition() {
        Game game = new Game();

        try {
            String[] whiteTowers = {"a1_w", "c1_w", "e1_w", "f2_ww", "h2_w", "g5_wbb"};
            String[] blackTowers = {"a3_b", "e3_b", "a5_bww", "c5_bwww", "e7_b", "g7_b", "b8_b", "d8_b", "f8_b", "h8_b"};

            game.placeTowers(whiteTowers);
            game.placeTowers(blackTowers);

            String[] moves = {"f2_ww:d4_wwb:b6_wwbb", "g7_b-f6_b",
                    "h2_w-g3_w", "f6_b:h4_bw:f2_bww",
                    "e1_w:g3_wb", "g5_bb-h4_bb"};

            for (var move : moves) {
                game.makeMove(move);
            }

        } catch (Exception exp) {
            Assertions.assertThat(exp.getMessage()).isEqualTo("invalid move");
        }

        System.out.println("(ﾉ◕ヮ◕)ﾉ*:･ﾟ✧ \n");
    }

    @Test
    void secondTestFromCondition() {
        Game game = new Game();

        try {
            String[] whiteTowers = {"a7_wbb", "b2_ww", "c1_w", "e1_w", "f2_w", "g1_w"};
            String[] blackTowers = {"b4_bwww", "b8_b", "c3_b", "c7_b", "e5_bww", "e7_b", "f8_b", "g5_b", "g7_b", "h8_b"};

            game.placeTowers(whiteTowers);
            game.placeTowers(blackTowers);

            String[] moves = {"b2_ww:d4_wwb:f6_wwbb:d8_wwbbb:b6_wwbbbb", "b4_bwww-a3_bwww"};

            for (var move : moves) {
                game.makeMove(move);
            }

            String[] newWhite = game.getTowersDescriptionByColor(Color.WHITE);
            String[] newBlack = game.getTowersDescriptionByColor(Color.BLACK);

            String[] whiteOutput = {"a7_wbb", "b6_Wwbbbb", "c1_w", "e1_w", "e5_ww", "f2_w", "g1_w"};
            String[] blackOutput = {"a3_bwww", "b8_b", "f8_b", "g5_b", "g7_b", "h8_b"};

            Assertions.assertThat(newWhite).isEqualTo(whiteOutput);
            Assertions.assertThat(newBlack).isEqualTo(blackOutput);

        } catch (Exception ignored) {
        }

        System.out.println("＼(＾▽＾)／ \n");
    }

    @Test
    void makeQueenTest() {
        Game game = new Game();

        try {
            String[] whiteTowers = {"g7_wwbbBw"};
            String[] blackTowers = {"b2_bwwwwB"};

            game.placeTowers(whiteTowers);
            game.placeTowers(blackTowers);

            String[] moves = {"g7_wwbbBw-h8_wwbbBw", "b2_bwwwwB-a1_bwwwwB"};

            for (var move : moves) {
                game.makeMove(move);
            }

            String[] newWhite = game.getTowersDescriptionByColor(Color.WHITE);
            String[] newBlack = game.getTowersDescriptionByColor(Color.BLACK);

            String[] whiteOutput = {"h8_WwbbBw"};
            String[] blackOutput = {"a1_BwwwwB"};

            Assertions.assertThat(newWhite).isEqualTo(whiteOutput);
            Assertions.assertThat(newBlack).isEqualTo(blackOutput);

        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        }

        System.out.println("(((o(*°▽°*)o)))\n");
    }

    @Test
    void busyCellTest() {
        Game game = new Game();

        try {
            String[] whiteTowers = {"g7_w", "h8_W"};
            String[] blackTowers = {"b2_b", "a1_B"};

            game.placeTowers(whiteTowers);
            game.placeTowers(blackTowers);

            String[] moves = {"g7_w-h8_w", "b2_b-a1_b"};

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
            String[] whiteTowers = {"h1_w"};
            String[] blackTowers = {"a8_b"};

            game.placeTowers(whiteTowers);
            game.placeTowers(blackTowers);

        } catch (Exception exp) {
            Assertions.assertThat(exp.getMessage()).isEqualTo("white cell");
        }

        System.out.println("(╯✧▽✧)╯\n");
    }

    @Test
    void incorrectInputTest() {
        Game game = new Game();

        try {
            String[] whiteTowers = {"h8"};

            game.placeTowers(whiteTowers);

        } catch (Exception exp) {
            Assertions.assertThat(exp.getMessage()).isEqualTo("incorrect input");
        }

        try {
            String[] whiteTowers = {"42"};

            game.placeTowers(whiteTowers);

        } catch (Exception exp) {
            Assertions.assertThat(exp.getMessage()).isEqualTo("incorrect input");
        }

        System.out.println("o(≧▽≦)o\n");
    }

    @Test
    void emptyCellTest() {
        Game game = new Game();

        try {
            String[] whiteTowers = {"g7_wB", "H8_Wb"};
            String[] blackTowers = {"b2_bW", "A1_Bw"};

            game.placeTowers(whiteTowers);
            game.placeTowers(blackTowers);

            String[] moves = {"d4_W-e5_w", "e5_b-d4_b"};

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
            String[] whiteTowers = {"a1_WwwBb", "b2_wWWW"};

            game.placeTowers(whiteTowers);

            String[] moves = {"a1_WwwBb:c3_wWWW"};

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
            String[] whiteTowers = {"B2_WB"};
            String[] blackTowers = {"G7_BW"};

            game.placeTowers(whiteTowers);
            game.placeTowers(blackTowers);

            String[] moves = {"B2_WB-C3_WB", "G7_BW-F6_BW"};

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
            String[] whiteTowers = {"b2_w"};
            String[] blackTowers = {"g7_b"};

            game.placeTowers(whiteTowers);
            game.placeTowers(blackTowers);

            String[] moves = {"b2_w-d4_w", "g7_b-e5_b"};

            for (var move : moves) {
                game.makeMove(move);
            }

        } catch (Exception exp) {
            Assertions.assertThat(exp.getMessage()).isEqualTo("incorrect move");
        }

        try {
            String[] whiteTowers = {"B2_W"};
            String[] blackTowers = {"c3_b", "d4_b"};

            game.placeTowers(whiteTowers);
            game.placeTowers(blackTowers);

            String[] moves = {"B2_W:E5_W"};

            for (var move : moves) {
                game.makeMove(move);
            }

        } catch (Exception exp) {
            Assertions.assertThat(exp.getMessage()).isEqualTo("incorrect move");
        }

        System.out.println("¯\\_(ツ)_/¯\n");
    }

}
