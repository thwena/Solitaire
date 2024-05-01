//Jonathan Swena
import java.util.*;
import java.lang.*;
public class Player {
    private String name;            //file name for saving game
    Scanner bob = new Scanner(System.in);
    public Player(){                //called player intended to user player name for saving.
        int ans;                    //player choice stored
        boolean correct = false;    //used for user to check if they meant to enter what they entered
        do{
            System.out.println("Please enter the name for the save file. Only One word please:");
            name = bob.nextLine(); //gets user input
            do{
                System.out.println("File Name: " + name);   //prints out name for user to confirm
                System.out.println("If the name is correct please type '1' if not type '2'");
                String input = bob.nextLine();              //gets users confirmation
                try {                                       //tries to convert string to int
                    ans = Integer.parseInt(input);
                } catch (NumberFormatException ex) {
                    ans = -1;                               //if failed applies default value to cause repeat
                }
            }while (ans != 1 && ans != 2);                  //checks if user entered acceptable value
            if (ans == 1) {                                 //checks user choice
                correct = true;                             //if user confirms file name correct set to true, allows
            } else{                                         //user to retype the file name
                correct =  false;                           //if user not confirmed causes repeat
            }
        }while(!correct);                                   //checks whether user confirm or not to force repeat of code

    }
    public String getName(){
        return name;
    }               //returns name of file
}
