//Jonathan Swena
import java.util.*;
import java.lang.*;
public class Deck {
    public static final String BLACK_BACKGROUND = "\u001B[40m"; //variable to set back color of game board
    public static final String RED = "\u001B[31m";                //sets font color to red
    public static final String BLUE = "\u001B[34m";               //sets font color to blue
    public static final String WHITE = "\u001B[37m";              //sets font color to white
    protected ArrayList<Integer> deck = new ArrayList<Integer>();       //Contains the cards that can be drawn
    protected ArrayList<Integer> discard = new ArrayList<Integer>();    //Playable card stack
    private Random rand = new Random();                          //random used for shuffling the deck

    public Deck(){                                              //default constructor
        for(int i =0; i < 52; i++){
            deck.add(i);                                        //initialize a normal deck.
        }
        shuffle();                                              //Shuffles deck
    }

    protected void loadDeck(ArrayList<Integer> draw, ArrayList<Integer> play){  //loads deck and discard
        deck.clear();                                                           //from arraylist passed in
        discard.clear();                                                        //Some Unknown errors so not in use
        for(int i = 0; i < draw.size(); i++){
            deck.add(draw.get(i));
        }
        for(int i = 0; i < play.size(); i++){
            discard.add(play.get(i));
        }
    }

    public void shuffle(){                          //Shuffles deck
        for(int i = 0; i < deck.size(); i++){       //Cycles the full arraylist
            int loc = rand.nextInt(deck.size());    //picks a random location
            int temp = deck.get(loc);               //Swaps the variable stored
            deck.set(loc, deck.get(i));             //in current location with
            deck.set(i, temp);                      //variable stored in random location
        }
    }

    public void flipTopCard(){                      //Flips top card of deck
        discard.add(0, drawCard());           //adds it discards it and removes it from deck
    }

    public String viewCard(){                       //returns the string contain name of card
        String temp = "";                           //ONLY THE TOP CARD OF DISCARD PILE
        if(getPlay() == -1){                        //-1 represents no card stored
            temp = "N/A";
        } else {
            temp = viewCards(getPlay());            //add the card name to be returned
        }
        return temp;
    }

    public String viewCards(int num){           //return string containing name of card
        String temp = cardNum(num), out = "";   //2 part process, 1st gets the card value. IE A for Ace, K for King
        temp += cardType(num);                  //gets card suit and add it the card value
        if(cardType(num) == "H" || cardType(num) == "D"){   //determines if red card or blue card
            out += RED + temp + WHITE + BLACK_BACKGROUND;   //if red makes the lettering red
        } else {                                            //then sets back to default
            out += BLUE + temp + WHITE + BLACK_BACKGROUND;  //if not red makes lettering blue
        }                                                   //then sets back to default
        return out;     //returns string contain name
    }
    public int getPlay(){       //returns top card of discard pile
        int card = -1;
        if(discard.size() <= 0){    //checks if there is a card
            card = -1;
        } else {
            card = discard.get(0); //if card set value of card
        }
        return card;                //return value of card
    }

    public int playDiscard(){         //plays discard
        int temp = discard.get(0);    //sets top of discard to temp variable
        discard.remove(0);      //removes card from discard
        return temp;                  //returns card value
    }

    public int drawCard(){            //Draws card from deck
        int temp = -1;
        if(deck.size() <= 0 && discard.size() <= 0){    //If the deck and discard is empty it returns -1
            temp = -1;
        } else if(deck.size() <= 0 && discard.size() > 0){  //if the deck is empty and discard has more than 1
            refillDeck();                                   //it reshuffles the discard into the draw
            temp = deck.get(0);
            deck.remove(0);
            return temp;
        }else {
            temp = deck.get(0);                             //if discard has at least 1 card it returns the card
            deck.remove(0);                           //and it removes it from the discard
            return temp;
        }
        return temp;
    }

    private void refillDeck(){                              //reshuffles the discard into the draw deck
        for(int i = 0; i < discard.size(); i++){
            deck.add(discard.get(i));

        }
        discard.clear();                                    //empties the discard
        shuffle();                                          //shuffles the draw deck
    }

    public String cardType(int num){                        //returns the card type
        String temp= "";                                    //H = hearts, C = clubs
        if(0 <= num && num <= 12){                          //S = spades, D = diamonds
            temp = "H";                                     //if the number is between the values it returns the suit
        } else if(13 <= num && num <= 25){                  //0-12 = H, 13-25 = C, 26-38 = S, 39-51 = D
            temp = "C";
        } else if(26 <= num && num <= 38){
            temp = "S";
        } else if(39 <= num && num <= 51){
            temp = "D";
        }
        return temp;                                        //returns the Cardtype as a string
    }

    public String cardNum(int num){                         //Returns the card value based on the number
        String temp = "";                                   //every 13th card is the value, based on a std deck
        int numCheck1 = 0, numCheck2 = 13, numCheck3 = 26, numCheck4 = 39;
        if(num == numCheck1++ || num == numCheck2++ || num == numCheck3++ || num == numCheck4++){
            temp = "A";
        } else if(num == numCheck1++ || num == numCheck2++ || num == numCheck3++ || num == numCheck4++){
            temp = "2";
        } else if(num == numCheck1++ || num == numCheck2++ || num == numCheck3++ || num == numCheck4++){
            temp = "3";
        } else if(num == numCheck1++ || num == numCheck2++ || num == numCheck3++ || num == numCheck4++){
            temp = "4";
        } else if(num == numCheck1++ || num == numCheck2++ || num == numCheck3++ || num == numCheck4++){
            temp = "5";
        } else if(num == numCheck1++ || num == numCheck2++ || num == numCheck3++ || num == numCheck4++){
            temp = "6";
        } else if(num == numCheck1++ || num == numCheck2++ || num == numCheck3++ || num == numCheck4++){
            temp = "7";
        } else if(num == numCheck1++ || num == numCheck2++ || num == numCheck3++ || num == numCheck4++){
            temp = "8";
        } else if(num == numCheck1++ || num == numCheck2++ || num == numCheck3++ || num == numCheck4++){
            temp = "9";
        } else if(num == numCheck1++ || num == numCheck2++ || num == numCheck3++ || num == numCheck4++){
            temp = "10";
        } else if(num == numCheck1++ || num == numCheck2++ || num == numCheck3++ || num == numCheck4++){
            temp = "J";
        } else if(num == numCheck1++ || num == numCheck2++ || num == numCheck3++ || num == numCheck4++){
            temp = "Q";
        } else if(num == numCheck1++ || num == numCheck2++ || num == numCheck3++ || num == numCheck4++){
            temp = "K";
        }
        return temp;                                    //returns the value has a string
    }

    public int getSize(){                               //Unneeded, Deck was originally a private, changed to protected
        return deck.size();
    }
}

