//Jonathan Swena

import java.util.*;
import java.lang.*;
public class Main {
    public static void main(String[] arg) {
        Scanner bob = new Scanner(System.in);
        int ans = -1;
        String input = "";
        do {                                    //Repeats until user enters an acceptable value
            System.out.println("If you would like to start a new game type 1:\nIf you would like to load a previous game type 2:");
            input = bob.nextLine();             //gets user input
            try {                               //tries to convert input to int
                ans = Integer.parseInt(input);
            } catch (NumberFormatException ex) {//if failed to convert applies default value that is not acceptable
                ans = -1;
            }
        } while (ans != 1 && ans != 2);         //makes sure code get an acceptable value will repeat if not acceptable
        System.out.println("Follow on screen instructions.\nIn the tableau you can only place a card on top of another card if the color does not match" +
                "\nOnly a kind can be placed in an empty stack. They stack starting with king down to ace" +
                "\nIn the Foundation suit must match. Ace goes on the bottom and goes upto 2 through King" +
                "\nA = ace, J = jack, Q = queen, K = king" +
                "\nC = clubs, H = hearts, S = spades, D = diamonds");
        if (ans == 2) {                         //Checks if user wanted to load a game or start a new game
            Solitaire game = new Solitaire(true); //loads a previous game
        } else {
            Solitaire game = new Solitaire();   //starts a new game
        }
    }
}
