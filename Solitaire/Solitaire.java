//Jonathan Swena
import java.util.*;
import java.lang.*;
import java.nio.file.*;
import java.io.*;
public class Solitaire extends Deck {
    Scanner bob = new Scanner(System.in);
    private ArrayList<ArrayList<Integer>> board = new ArrayList<ArrayList<Integer>>();  //Game board 2d ArrayList
    private ArrayList<Integer> hearts = new ArrayList<Integer>();           //Foundation Hearts array
    private ArrayList<Integer> clubs = new ArrayList<Integer>();            //Foundation Clubs array
    private ArrayList<Integer> spades = new ArrayList<Integer>();           //Foundation Spades array
    private ArrayList<Integer> diamonds = new ArrayList<Integer>();         //Foundation Diamons array
    private int[] viewable = {0, 1, 2, 3, 4, 5, 6};                   //Dictates which cards have been flipped over
    private ArrayList<String> rows = new ArrayList<String>();              //Converts Row letter to integer
                                                                      //using indexOf() search function
    private Player user = new Player();                               //Initializes file save name

    public Solitaire() {
        super();                                                //Initializes card deck
        for (int i = 0; i < 7; i++) {
            ArrayList<Integer> temp = new ArrayList<Integer>();       //Fills the columns of board with empty arrays
            board.add(temp);                                    //Building the foundation of the board
        }
        for (int j = 0; j < 7; j++) {
            for (int i = j; i < 7; i++) {
                board.get(i).add(0, drawCard());          //Add the cards to the correct column and the correct
            }                                                   //number of cards
        }
        game();                                                 //Starts the game
    }

    public Solitaire(boolean Load) {
        board.clear();//Makes sure board is empty
        ArrayList<String> input = new ArrayList<String>();          //Arrays needed to store information being read in
        ArrayList<Integer> draw = new ArrayList<Integer>();
        ArrayList<Integer> play = new ArrayList<Integer>();
        ArrayList<Integer> view = new ArrayList<Integer>();
        boolean goodToPlay = false;
        for(int i = 0; i < 7; i++){
            ArrayList<Integer> stack = new ArrayList<Integer>();     //Builds foundation of board
            board.add(stack);
        }

        try {                                                   //Tries to get file location and open file
            Path currentRelativePath = Paths.get("");       //get path for curent working directory
            String s = currentRelativePath.toAbsolutePath().toString(); //converts to string
            String file = s + "\\" + user.getName() + ".txt";    //adds the file name dictated by the user
            FileInputStream fis = new FileInputStream(file);
            Scanner sc = new Scanner(fis);
            while (sc.hasNextLine()) {                           //reads in every line adds it to inpute
                input.add(sc.nextLine());
            }
            sc.close();
            goodToPlay = true;
        }catch(IOException e){
            System.out.println("Error reading file");            //reports error if it could read it in
        }

        if(goodToPlay) {                                        //If read correctly rebuild board
            int start, end;
            for (int j = 0; j < input.size(); j++) {

                ArrayList<String> temp = new ArrayList<String>();
                start = 0;

                for (int i = 0; i < input.get(j).length(); i++) {   //converts line of file read in to integer array
                    if (input.get(j).charAt(i) == ',') {
                        end = i;
                        temp.add(input.get(j).substring(start, end));
                        start = i + 1;
                    }
                }

                if (j == 0) {                                       //checks if its 1st line of file
                    for (int x = 0; x < temp.size(); x++) {
                        draw.add(Integer.parseInt(temp.get(x)));    //adds integers to draw deck
                    }
                } else if (j == 1) {                                //checks if 2nd line
                    for (int x = 0; x < temp.size(); x++) {
                        play.add(Integer.parseInt(temp.get(x)));    //adds integers to discard
                    }
                } else if (j == 2) {                                //checks if 3rd line
                    view.clear();                                   //makes sure arraw is clear
                    for (int x = 0; x < temp.size(); x++) {
                        view.add(Integer.parseInt(temp.get(x)));    //adds integers to view array
                    }
                } else if (j == 3) {                                //checks if 4th line of file
                    clubs.clear();
                    for (int x = 0; x < temp.size(); x++) {
                        clubs.add(Integer.parseInt(temp.get(x)));   //adds integers to clubs array
                    }
                } else if (j == 4) {                                //checks if 5th line of file
                    hearts.clear();
                    for (int x = 0; x < temp.size(); x++) {
                        hearts.add(Integer.parseInt(temp.get(x))); //adds integers to hearts array
                    }
                } else if (j == 5) {                                //checks if 6th line
                    spades.clear();
                    for (int x = 0; x < temp.size(); x++) {
                        spades.add(Integer.parseInt(temp.get(x)));  //adds integers to spades array
                    }
                } else if (j == 6) {                                //checks if 7th line of file
                    diamonds.clear();
                    for (int x = 0; x < temp.size(); x++) {
                        diamonds.add(Integer.parseInt(temp.get(x)));//adds to diamond array
                    }
                } else {                                            //if any line after foundation,draw,and discard save line
                    for (int x = 0; x < temp.size(); x++) {
                        board.get(j - 7).add(Integer.parseInt(temp.get(x)));    //adds to correct column based on file line
                    }
                }
                temp.clear();
            }
            for (int i = 0; i < view.size(); i++) {
                viewable[i] = view.get(i);                      //converts view ArrayList to normal array. (easier to work with)
            }
            deck.clear();                                       //makes sure ArrayList is clear
            discard.clear();                                    //^^
            for (int i = 0; i < draw.size(); i++) {
                deck.add(draw.get(i));                          //adds integers to deck array
            }
            for (int i = 0; i < play.size(); i++) {
                discard.add(play.get(i));                       //adds integers to discard array
            }
            //loadDeck(draw, play);
            draw.clear();                                       //clear all array created in this function
            play.clear();
            view.clear();
            game();                                             //starts game
        }
    }

    private void game() {
        initialize();                   //sets up row array
        do {
            System.out.println(this);   //prints board
            //checkAce();
            playerChoice();             //asks user where and what they want to move

            save();                     //saves board
        } while (true);
    }

    private void playerChoice() {
        boolean flipCard = false, accept = false;
        String input = "";
        int row = -1;
        int ans = -1;
        do {
            do {
                System.out.println("Please type the number correspond to the card you would like to move:\nPlease type '8' if you would like to move the card in the Talon:" +
                        "\nPlease type 9 to flip a new card over in the talon" +
                        "\nPlease type 10, 11, 12, 13 (left to right) corresponding to foundation you would like to place the card from");
                input = bob.nextLine();                 //gets user input on what they want to
                try {                                   //checks if user enter an integet
                    ans = Integer.parseInt(input);      //converts user input to integer
                } catch (NumberFormatException ex) {    //Catches error thrown if user input anything other than integer
                    ans = -1;                           //sets to default value to fail next evaluation
                }
                if (ans < 1 || ans > 13) {              //checks if user entered value within acceptable range
                    ans = -1;
                }
            } while (ans == -1);                        //runs continuously until user enter value required
            if (ans == 9) {                             //checks if user want to flip card
                flipTopCard();                          //flips top card
                flipCard = true;                        //card flip set to true
                accept = true;                          //accept is the required value to exit loop
            } else if (ans > 0 && ans < 8) {            //checks if user selected a column
                do {
                    System.out.println("Please enter the letter corresponding to the card you would like to move:");
                    input = bob.nextLine();             //gets user input
                    input = input.toUpperCase();        //converts to upper case
                    row = rows.indexOf(input);          //searches for int value related to letter
                    if (row > board.get(ans - 1).size() - 1) {  //if row in the possible value acceptable
                        row = -1;                       //acceptable values are length of column minus 1.
                    }                                   //Stop index out of bounds
                } while (row == -1);                    //continues until value is acceptable
            }
            if (!flipCard) {                            //if they didnt flip a card asks where they want to move it to
                int col = -1;
                do {
                    System.out.println("Please type the number correspond to the column you would like to move the card(s) to:" +
                            "Please type 10, 11, 12, 13 (left to right) corresponding to foundation you would like to place the card in");
                    input = bob.nextLine();             //gets user input
                    try {                               //tries to convert user input
                        col = Integer.parseInt(input);
                    } catch (NumberFormatException ex) {
                        col = -1;
                    }
                    if ((col < 1 || col > 7) && col != 10 && col != 11 && col != 12 && col != 13) {
                        col = -1;                       //If they moved to foundation the col needs decreased by 1
                    }
                } while (col == -1);                    //forces it to repeat until acceptable value
                if (legalMove(ans - 1, row, col - 1)) {
                    accept = true;                              //exit value set to true
                    makeMove(ans - 1, row, col - 1);    //makes move
                }
            }
        } while (!accept);                              //allows to exit if correct value is achieved
    }

    private boolean legalMove(int col, int row, int col2) { //col is the column of card to be remove,
        if (col == 7) {                             //7 is the value of draw pile,  col2 is column to be moved to
            if (10 <= col2 + 1 && col2 + 1 <= 13) { //location is foundation
                col2++;                             //required for foundation.
                int cardCheck = getPlay();
                if (cardType(cardCheck) == "C" && col2 == 10) { //checks if cardtype matches foundation
                    int cardCheck2 = -1;                        //default value in case array is empty
                    if (clubs.size() > 0) {
                        cardCheck2 = clubs.get(0);              //returns top card of array
                    }
                    if (cardNum(cardCheck) == "A" && clubs.size() == 0) {//if its an ace automatically passes
                        return true;
                    } else {
                        return moveFoundation(cardCheck, cardCheck2);   //does the check for all other cases
                    }
                } else if (cardType(cardCheck) == "H" && col2 == 11) {  //same for all foundation checks
                    int cardCheck2 = -1;
                    if (hearts.size() > 0) {
                        cardCheck2 = hearts.get(0);
                    }
                    if (cardNum(cardCheck) == "A" && hearts.size() == 0) {
                        return true;
                    } else {
                        return moveFoundation(cardCheck, cardCheck2);
                    }
                } else if (cardType(cardCheck) == "S" && col2 == 12) { //foundation check
                    int cardCheck2 = -1;
                    if (spades.size() > 0) {
                        cardCheck2 = spades.get(0);
                    }
                    if (cardNum(cardCheck) == "A" && spades.size() == 0) {
                        return true;
                    } else {
                        return moveFoundation(cardCheck, cardCheck2);
                    }
                } else if (cardType(cardCheck) == "D" && col2 == 13) { //foundation check
                    int cardCheck2 = -1;
                    if (diamonds.size() > 0) {
                        cardCheck2 = diamonds.get(0);
                    }
                    if (cardNum(cardCheck) == "A" && diamonds.size() == 0) {
                        return true;
                    } else {
                        return moveFoundation(cardCheck, cardCheck2);
                    }
                } else {
                    return false;
                }

            } else {

                if (board.get(col2).size() == 0) {      //checks cases for all columns that has no cards
                    if (cardNum(getPlay()) == "K") {    //if its a king it passes all other fail
                        return true;
                    } else {
                        return false;
                    }
                } else {                                //checks for all other columns and cases
                    int cardCheck = getPlay();
                    int cardCheck2 = board.get(col2).get(board.get(col2).size() - 1);
                    return regularCheck(cardCheck, cardCheck2);
                }
            }
        } else if (10 <= col+1 && col+1 <= 13) {    //checks if moving from foundation
            col++;                                  //not working not sure why

            if (col == 10) {
                int cardCheck = clubs.get(0);
                int cardCheck2 = board.get(col2).size()-1;
                return regularCheck(cardCheck, cardCheck2);
            } else if (col == 11) {
                int cardCheck = hearts.get(0);
                int cardCheck2 = board.get(col2).size()-1;
                return regularCheck(cardCheck, cardCheck2);
            } else if (col == 12) {
                int cardCheck = spades.get(0);
                int cardCheck2 = board.get(col2).size()-1;
                return regularCheck(cardCheck, cardCheck2);
            } else {
                int cardCheck = diamonds.get(0);
                int cardCheck2 = board.get(col2).size()-1;
                return regularCheck(cardCheck, cardCheck2);
            }

        } else {                                    //checks if moving from a normal column
            if ((10 <= col2 + 1 && col2 + 1 <= 13) && row == board.get(col).size() - 1) { //checks if destination
                col2++;                                                                   //is foundation and is last
                int cardCheck = board.get(col).get(row);                                  //card in column
                if (cardType(cardCheck) == "C" && col2 == 10) {          //foundation check
                    int cardCheck2 = -1;
                    if (clubs.size() > 0) {
                        cardCheck2 = clubs.get(0);
                    }
                    if (cardNum(cardCheck) == "A" && clubs.size() == 0) {
                        return true;
                    } else {
                        return moveFoundation(cardCheck, cardCheck2);
                    }
                } else if (cardType(cardCheck) == "H" && col2 == 11) {  //foundation check
                    int cardCheck2 = -1;
                    if (hearts.size() > 0) {
                        cardCheck2 = hearts.get(0);
                    }
                    if (cardNum(cardCheck) == "A" && hearts.size() == 0) {
                        return true;
                    } else {
                        return moveFoundation(cardCheck, cardCheck2);
                    }
                } else if (cardType(cardCheck) == "S" && col2 == 12) {  //foundation check
                    int cardCheck2 = -1;
                    if (spades.size() > 0) {
                        cardCheck2 = spades.get(0);
                    }
                    if (cardNum(cardCheck) == "A" && spades.size() == 0) {
                        return true;
                    } else {
                        return moveFoundation(cardCheck, cardCheck2);
                    }
                } else if (cardType(cardCheck) == "D" && col2 == 13) {  //foundation check
                    int cardCheck2 = -1;
                    if (diamonds.size() > 0) {
                        cardCheck2 = diamonds.get(0);
                    }
                    if (cardNum(cardCheck) == "A" && diamonds.size() == 0) {
                        return true;
                    } else {
                        return moveFoundation(cardCheck, cardCheck2);
                    }
                } else {
                    return false;
                }
            } else {
                if (board.get(col2).size() == 0) {                  //checks if destination column is empty
                    if (cardNum(board.get(col).get(row)) == "K") {  //if king pass otherwise fail
                        return true;
                    } else {
                        return false;
                    }
                } else {                                            //all other cases end up here
                    int cardCheck = board.get(col).get(row);
                    int cardCheck2 = board.get(col2).get(board.get(col2).size() - 1);
                    return regularCheck(cardCheck, cardCheck2);     //does a normal check

                }
            }
        }


    }

    private boolean moveFoundation(int cardCheck, int cardCheck2) {             //checks if card being placed is legal to place
        if (cardNum(cardCheck) == "K" && cardNum(cardCheck2) == "Q") {          //returns true or false based on if legal to place
            return true;
        } else if (cardNum(cardCheck) == "Q" && cardNum(cardCheck2) == "J") {
            return true;
        } else if (cardNum(cardCheck) == "J" && cardNum(cardCheck2) == "10") {
            return true;
        } else if (cardNum(cardCheck) == "10" && cardNum(cardCheck2) == "9") {
            return true;
        } else if (cardNum(cardCheck) == "9" && cardNum(cardCheck2) == "8") {
            return true;
        } else if (cardNum(cardCheck) == "8" && cardNum(cardCheck2) == "7") {
            return true;
        } else if (cardNum(cardCheck) == "7" && cardNum(cardCheck2) == "6") {
            return true;
        } else if (cardNum(cardCheck) == "6" && cardNum(cardCheck2) == "5") {
            return true;
        } else if (cardNum(cardCheck) == "5" && cardNum(cardCheck2) == "4") {
            return true;
        } else if (cardNum(cardCheck) == "4" && cardNum(cardCheck2) == "3") {
            return true;
        } else if (cardNum(cardCheck) == "3" && cardNum(cardCheck2) == "2") {
            return true;
        } else if (cardNum(cardCheck) == "2" && cardNum(cardCheck2) == "A") {
            return true;
        } else {
            return false;
        }
    }

    private boolean regularCheck(int cardCheck, int cardCheck2) {
        boolean card1Red, card2Red;
        if (cardType(cardCheck) == "H" || cardType(cardCheck) == "D") {     //checks if its a hearts or diamonds to
            card1Red = true;                                                //determins if red or blue card
        } else {
            card1Red = false;
        }
        if (cardType(cardCheck2) == "H" || cardType(cardCheck2) == "D") {   //same as above
            card2Red = true;
        } else {
            card2Red = false;
        }
        if ((card1Red && !card2Red) || (!card1Red && card2Red)) {           //checks if card are opposite colors
            if (cardNum(cardCheck) == "A" && cardNum(cardCheck2) == "2") {  //inverse legal placement for foundation check
                return true;
            } else if (cardNum(cardCheck) == "2" && cardNum(cardCheck2) == "3") {
                return true;
            } else if (cardNum(cardCheck) == "3" && cardNum(cardCheck2) == "4") {
                return true;
            } else if (cardNum(cardCheck) == "4" && cardNum(cardCheck2) == "5") {
                return true;
            } else if (cardNum(cardCheck) == "5" && cardNum(cardCheck2) == "6") {
                return true;
            } else if (cardNum(cardCheck) == "6" && cardNum(cardCheck2) == "7") {
                return true;
            } else if (cardNum(cardCheck) == "7" && cardNum(cardCheck2) == "8") {
                return true;
            } else if (cardNum(cardCheck) == "8" && cardNum(cardCheck2) == "9") {
                return true;
            } else if (cardNum(cardCheck) == "9" && cardNum(cardCheck2) == "10") {
                return true;
            } else if (cardNum(cardCheck) == "10" && cardNum(cardCheck2) == "J") {
                return true;
            } else if (cardNum(cardCheck) == "J" && cardNum(cardCheck2) == "Q") {
                return true;
            } else if (cardNum(cardCheck) == "Q" && cardNum(cardCheck2) == "K") {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    private void makeMove(int col, int row, int col2) { //makes move
        if (col == 7) {                                 //if moving from discard pile
            int temp = playDiscard();                   //removes from discard
            if (10 <= col2 + 1 && col2 + 1 <= 13) {     //to foundation makes move
                col2++;
                if (col2 == 10) {
                    clubs.add(0, temp);
                } else if (col2 == 11) {
                    hearts.add(0, temp);
                } else if (col2 == 12) {
                    spades.add(0, temp);
                } else {
                    diamonds.add(0, temp);
                }
            } else {
                board.get(col2).add(temp);            //moves to correct column
            }
        } else if (10 <= col2 + 1 && col2 + 1 <= 13) {  //moves to foundation
            col2++;
            int temp = board.get(col).get(row);
            board.get(col).remove(row);                 //removes from current location
            if (col2 == 10) {                           //then adds it to correct foundation
                clubs.add(0, temp);
            } else if (col2 == 11) {
                hearts.add(0, temp);
            } else if (col2 == 12) {
                spades.add(0, temp);
            } else {
                diamonds.add(0, temp);
            }
            if(viewable[col] > 0) {
                viewable[col]--;
            }
        } else if (10 <= col + 1 && col + 1 <= 13) {  //moves from foundation
            col++;                                    //not working not sure why
            int temp;
            if (col == 10) {
                temp = clubs.get(0);
            } else if (col == 11) {
                temp = hearts.get(0);
            } else if (col == 12) {
                temp = spades.get(0);
            } else {
                temp = diamonds.get(0);
            }
            board.get(col2).add(temp);
        } else {                                                //moves from column to column
            ArrayList<Integer> temp = new ArrayList<Integer>();
            for (int i = row; i < board.get(col).size(); i++) { //add all cards to be moved to temp list
                temp.add(board.get(col).get(i));
            }
            int size = board.get(col).size();                   //adds to correct column
            for (int i = row; i < size; i++) {
                board.get(col).remove(row);
            }
            for (int i = 0; i < temp.size(); i++) {
                board.get(col2).add(temp.get(i));
            }
            if (viewable[col] > 0) {
                viewable[col]--;
            }
            temp.clear();
        }
    }

    private void initialize(){ //fills array for getting int from letter
        rows.add("A");
        rows.add("B");
        rows.add("C");
        rows.add("D");
        rows.add("E");
        rows.add("F");
        rows.add("G");
        rows.add("H");
        rows.add("I");
        rows.add("J");
        rows.add("K");
        rows.add("L");
        rows.add("M");
        rows.add("N");
        rows.add("O");
        rows.add("P");
        rows.add("Q");
        rows.add("R");
        rows.add("S");
        rows.add("T");
        rows.add("U");
        flipTopCard();      //flips card from draw from discard

    }
/*
    private void checkAce(){
        for(int i = 0; i < board.size(); i++){
            int lastCard = board.get(i).get(board.get(i).size()-1);
            if(lastCard == 0 || lastCard == 13 || lastCard == 26 || lastCard == 39){
                if(cardType(lastCard) == "H"){
                    hearts.add(0,lastCard);
                } else if(cardType(lastCard) == "D"){
                    diamonds.add(0,lastCard);
                } else if(cardType(lastCard) == "S"){
                    spades.add(0,lastCard);
                } else{
                    clubs.add(0,lastCard);
                }
                if(viewable[i]>0) {
                    viewable[i]--;
                }
                board.get(i).remove(board.get(i).size()-1);
                System.out.println(this);
            }
        }
        int lastCard = getPlay();
        if(lastCard == 0 || lastCard == 13 || lastCard == 26 || lastCard == 39){
            if(cardType(lastCard) == "H"){
                hearts.add(0,playDiscard());
            } else if(cardType(lastCard) == "D"){
                diamonds.add(0,playDiscard());
            } else if(cardType(lastCard) == "S"){
                spades.add(0,playDiscard());
            } else{
                clubs.add(0,playDiscard());
            }
            System.out.println(this);
        }

    }
*/

    private void save() {   //saves
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String file = s + "\\" + user.getName() + ".txt";               //gets file name and path
        System.out.println(file);                                       //creates strings to use to save
        String deckStack = "", discardStack = "", views = "", club = "", heart = "", spade = "", diamond = "", stack1 = "", stack2 = "", stack3 = "", stack4 = "", stack5 = "", stack6 = "", stack7 = "";
        if(deck.size() == 0){                                           //adds arrays contents to string for savine
            deckStack += "-";                                           //check if array is empty
        } else {                                                        //if empty special value used
            for (int i = 0; i < deck.size(); i++) {                     // ^^
                deckStack += deck.get(i) + ",";
            }
        }
        if(discard.size() == 0){                                        // ^^
            discardStack += "-";
        } else {
            for (int i = 0; i < discard.size(); i++) {
                discardStack += discard.get(i) + ",";
            }
        }
        for (int i = 0; i < 7; i++) {                                   // ^^
            views += viewable[i] + ",";
        }
        if (clubs.size() == 0) {                                        // ^^
            club += "-";
        } else {
            for (int i = 0; i < clubs.size(); i++) {
                club += clubs.get(i) + ",";
            }
        }
        if (hearts.size() == 0) {                                       // ^^
            heart += "-";
        } else{
            for (int i = 0; i < hearts.size(); i++) {
                heart += hearts.get(i) + ",";
            }
        }
        if(spades.size() == 0){                                         // ^^
            spade += "-";
        } else {
            for (int i = 0; i < spades.size(); i++) {
                spade += spades.get(i) + ",";
            }
        }
        if(diamonds.size() == 0){                                       // ^^
            diamond += "-";
        } else {
            for (int i = 0; i < diamonds.size(); i++) {
                diamond += diamonds.get(i) + ",";
            }
        }
        if(board.get(0).size() == 0){                                   // ^^
            stack1 += "-";
        } else {
            for (int i = 0; i < board.get(0).size(); i++) {
                stack1 += board.get(0).get(i) + ",";
            }
        }
        if(board.get(1).size() == 0){                                   // ^^
            stack2 += "-";
        } else {
            for (int i = 0; i < board.get(1).size(); i++) {
                stack2 += board.get(1).get(i) + ",";
            }
        }
        if(board.get(2).size() == 0){                                   // ^^
            stack3 += "-";
        } else {
            for (int i = 0; i < board.get(2).size(); i++) {
                stack3 += board.get(2).get(i) + ",";
            }
        }if(board.get(3).size() == 0){                                  // ^^
            stack4 += "-";
        } else {
            for(int i = 0; i < board.get(3).size(); i++){
                stack4 += board.get(3).get(i) + ",";
            }
        }if(board.get(4).size() == 0){                                  // ^^
            stack5 += "-";
        } else{
            for(int i = 0; i < board.get(4).size(); i++){
                stack5 += board.get(4).get(i) + ",";
            }
        }
        if(board.get(5).size() == 0){                                   // ^^
            stack6 += "-";
        } else {
            for(int i = 0; i < board.get(5).size(); i++){
                stack6 += board.get(5).get(i) + ",";
            }
        }if(board.get(6).size() == 0){                                  // ^^
            stack7 += "-";
        }else {
            for(int i = 0; i < board.get(6).size(); i++){
                stack7 += board.get(6).get(i) + ",";
            }
        }



        try {
            FileWriter myWriter = new FileWriter(file);             //opens file for writing and then writes to file
            myWriter.write(deckStack+"\n"+discardStack+"\n"+views+"\n"+club+"\n"+heart+"\n"+spade+"\n"+diamond+"\n"+stack1+"\n"+stack2+"\n"+stack3+"\n"+stack4+"\n"+stack5+"\n"+stack6+"\n"+stack7);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


    }

    public String toString(){           //sets up string to print with
        int longestStack = 0;
        for(int i = 0; i < board.size(); i++){
            if (board.get(i).size() > longestStack){
                longestStack = board.get(i).size();
            }
        }

        String output = BLACK_BACKGROUND + WHITE + "Stock: "+ getSize()+
                        "\nTalon: "+ viewCard()+"\t"+discard.size()+
                        "\nFoundations:\t" + getFoundationC() + "\t" + getFoundationH() +
                        "\t" + getFoundationS() + "\t" + getFoundationD() +
                        "\nTableau:\t1\t2\t3\t4\t5\t6\t7";
        for(int i = 0; i < longestStack; i++){
            int stackNum = 0;
            output+= "\n\t\t"+ rows.get(i) +":";
            output += getCard(stackNum++, i);
            output += getCard(stackNum++, i);
            output += getCard(stackNum++, i);
            output += getCard(stackNum++, i);
            output += getCard(stackNum++, i);
            output += getCard(stackNum++, i);
            output += getCard(stackNum++, i);

        }

        return output;

    }

    private String getCard(int stackNum, int index){        //gets name of card based on number for printing
        String output = "";
        if(index+1 > board.get(stackNum).size()){
            output+= "\t ";
        }else if(index < viewable[stackNum]){
            output+= "\t-";
        }else {
            output += "\t" + viewCards(board.get(stackNum).get(index));
        }
        return output;
    }

    private String getFoundationH(){                //gets foundation top card for printing
        String card = "Hearts";
        if(hearts.size() > 0){
            card = viewCards(hearts.get(0));
        }
        return card;
    }
    private String getFoundationC(){                // ^^
        String card = "Clubs";
        if(clubs.size() > 0){
            card = viewCards(clubs.get(0));
        }
        return card;
    }
    private String getFoundationS(){                // ^^
        String card = "Spades";
        if(spades.size() > 0){
            card = viewCards(spades.get(0));
        }
        return card;
    }
    private String getFoundationD(){                    // ^^
        String card = "Diamonds";
        if(diamonds.size() > 0){
            card = viewCards(diamonds.get(0));
        }
        return card;
    }

}
