package ru.khuzint;

import java.io.File;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;

public final class App {
    private App() {
    }

    public static String[] getTokensOfNextLine(Scanner in) {
        String currentLine = in.nextLine();
        return currentLine.split(" ");
    }

    public static void printTokensSeparatedBySpace(String[] tokens) {
        for (String token : tokens) {
            System.out.print(token + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Game game = new Game();

        try (Scanner in = new Scanner(new File("input.txt"), StandardCharsets.UTF_8)) {
            String[] whiteCheckersPos = getTokensOfNextLine(in);
            String[] blackCheckersPos = getTokensOfNextLine(in);

            game.placeCheckers(whiteCheckersPos, Color.WHITE);
            game.placeCheckers(blackCheckersPos, Color.BLACK);

            while (in.hasNext()) {
                String[] whiteBlackMoves = getTokensOfNextLine(in);
                String whiteMove = whiteBlackMoves[0];
                String blackMove = whiteBlackMoves[1];

                game.makeMove(whiteMove);
                game.makeMove(blackMove);
            }

            String[] newWhiteCheckersPos = game.getCheckersPosByColor(Color.WHITE);
            String[] newBlackCheckersPos = game.getCheckersPosByColor(Color.BLACK);

            printTokensSeparatedBySpace(newWhiteCheckersPos);
            printTokensSeparatedBySpace(newBlackCheckersPos);

        } catch (BusyCellException | WhiteCellException | NeedAttackException exp) {
            System.out.println(exp.getMessage());
        } catch (GameException exp) {
            System.out.println("general error");
        } catch (Exception exp) {
            System.out.println("something goes wrong");
        }
    }
}
