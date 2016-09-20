import java.util.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;

/**
 * Contains all the methods to handle deck operations.
 * 
 * @author Brian Knapp, Jessica Juan-aquino,
 * Catherine Sullivan, Benny Costello, Uros Antic
 * @version 5/2/2016
 */
public class Deck
{
    //the train cards in the deck
    protected static ArrayList<EColor> trainCardDeck; 
    //train card discard deck
    protected static ArrayList<EColor> trainCardDiscard;
    //destination card deck
    protected static ArrayList<Destination> destinationDeck;
    //if the train deck has been shuffled or not
    private static boolean shuffle;
    //Add all of the cards to the deck here
    static{
        shuffle=false;
        //Create list of train cards and then shuffle
        trainCardDeck = new ArrayList<>();
        for(int i = 0; i < 12; i++){
            trainCardDeck.add((EColor.WHITE));
            trainCardDeck.add((EColor.BLUE));
            trainCardDeck.add((EColor.YELLOW));
            trainCardDeck.add((EColor.BLACK));
            trainCardDeck.add((EColor.RED));
            trainCardDeck.add((EColor.GREEN));
            trainCardDeck.add((EColor.ORANGE));
            trainCardDeck.add((EColor.PINK));
        }
        for(int i = 0; i < 20; i++){
            trainCardDeck.add((EColor.LOCOMOTIVE));
        }
        // Shuffle the train Card Deck
        Collections.shuffle(trainCardDeck);
        //Discard pile starts empty
        trainCardDiscard = new ArrayList<EColor>();

        //Create list of destination tickets and then 
        //shuffle deck
        destinationDeck = new ArrayList<>();
        destinationDeck.add(new Destination("Aberdeen",
                "Glasgow", 5));
        destinationDeck.add(new Destination("Aberystwyth",
                "Cardiff", 2));
        destinationDeck.add(new Destination("Belfast",
                "Dublin", 4));
        destinationDeck.add(new Destination("Belfast", 
                "Manchester", 9));
        destinationDeck.add(new Destination("Birmingham",
                "Cambridge", 2));
        destinationDeck.add(new Destination("Birmingham",
                "London", 4));
        destinationDeck.add(new Destination("Bristol",
                "Southampton", 2));
        destinationDeck.add(new Destination("Cambridge", 
                "London", 3));
        destinationDeck.add(new Destination("Cardiff",
                "London", 8));
        destinationDeck.add(new Destination("Cardiff",
                "Reading", 4));

        destinationDeck.add(new Destination("Cork",
                "Leeds", 13));
        destinationDeck.add(new Destination("Dublin",
                "Cork", 6));
        destinationDeck.add(new Destination("Dublin", 
                "London", 15));
        destinationDeck.add(new Destination("Dundalk", 
                "Carlisle", 7));
        destinationDeck.add(new Destination("Edinburgh",
                "Birmingham", 12));
        destinationDeck.add(new Destination("Edinburgh",
                "London", 15));
        destinationDeck.add(new Destination("Fort William",
                "Edinburgh", 3));
        destinationDeck.add(new Destination("Galway", 
                "Barrow", 12));
        destinationDeck.add(new Destination("Galway",
                "Dublin", 5));
        destinationDeck.add(new Destination("Glasgow", 
                "Dublin", 9));

        destinationDeck.add(new Destination("Glasgow",
                "France", 19));
        destinationDeck.add(new Destination("Glasgow",
                "Manchester", 11));
        destinationDeck.add(new Destination("Holyhead",
                "Cardiff", 4));
        destinationDeck.add(new Destination("Inverness",
                "Belfast", 10));
        destinationDeck.add(new Destination("Inverness",
                "Leeds", 13));
        destinationDeck.add(new Destination("Leeds", 
                "France", 10));
        destinationDeck.add(new Destination("Leeds",
                "London", 6));
        destinationDeck.add(new Destination("Leeds",
                "Manchester", 1));
        destinationDeck.add(new Destination("Limerick",
                "Cardiff", 12));
        destinationDeck.add(new Destination("Liverpool",
                "Hull", 3));

        destinationDeck.add(new Destination("Liverpool",
                "Llandrindod Wells", 6));
        destinationDeck.add(new Destination("Liverpool",
                "Southampton", 6));
        destinationDeck.add(new Destination("London", 
                "Brighton", 3));
        destinationDeck.add(new Destination("London",
                "France", 7));
        destinationDeck.add(new Destination("Londonderry", 
                "Birmingham", 15));
        destinationDeck.add(new Destination("Londonderry",
                "Dublin", 6));
        destinationDeck.add(new Destination("Londonderry", 
                "Stranraer", 4));
        destinationDeck.add(new Destination("Manchester", 
                "London", 6));
        destinationDeck.add(new Destination("Manchester", 
                "Norwich", 6));
        destinationDeck.add(new Destination("Manchester", 
                "Plymouth", 8));

        destinationDeck.add(new Destination("Newcastle", 
                "Hull", 3));
        destinationDeck.add(new Destination("Newcastle", 
                "Southampton", 7));
        destinationDeck.add(new Destination("Northampton", 
                "Dover", 3));
        destinationDeck.add(new Destination("Norwich", 
                "Ipswich", 1));
        destinationDeck.add(new Destination("Nottingham",
                "Ipswich", 3));
        destinationDeck.add(new Destination("Penzance",
                "London", 10));
        destinationDeck.add(new Destination("Plymouth",
                "Reading", 5));
        destinationDeck.add(new Destination("Rosslare",
                "Aberystwyth", 4));
        destinationDeck.add(new Destination("Rosslare",
                "Carmarthen", 6));
        destinationDeck.add(new Destination("Sligo", 
                "Holyhead", 9));

        destinationDeck.add(new Destination("Southampton", 
                "London", 4));
        destinationDeck.add(new Destination("Stornoway", 
                "Aberdeen", 5));
        destinationDeck.add(new Destination("Stornoway", 
                "Glasgow", 7));
        destinationDeck.add(new Destination("Stranraer", 
                "Tullamore", 6));
        destinationDeck.add(new Destination("Ullapool", 
                "Dundee", 4));
        destinationDeck.add(new Destination("Wick", 
                "Dundee", 4));
        destinationDeck.add(new Destination("Wick", 
                "Edinburgh", 5));

        //reads in all the images for the destination cards
        try{
            for(int i=1; i<=57; i++){
                Image img =ImageIO.read(
                        new File("photo/Dest/dest" +i+".png" ));
                destinationDeck.get(i-1).addImage(img);
            }
        }
        catch(Exception e){
        }
        //shuffles the destination cards
        Collections.shuffle(destinationDeck);
        //set shuffle for the train cards to false
        shuffle=false;
    }

    /**
     * Adds the EColors back to the discard deck
     * @param trains the trains to be added to the discard deck
     */
    public static void addBackTrainCards(int[] trains){
        for(int i=0; i<trains.length; i++){
            EColor toadd = EColor.getColor(i);
            for(int train=0; train<trains[i]; train++){
                addTrainCardBack(toadd);
            }
        }
    }

    /**
     * Draw 1 card off the top of the deck
     * @return train card from top of deck
     */
    public static EColor drawTrainCard(){
        if(trainCardDeck.size() == 0) {
            Deck.shuffleTrain();
            shuffle=true;
        }        
        return trainCardDeck.remove(0);
    }

    /**
     * If the train cards ahve been shuffled
     * @return true if the cards have been shuffled or 
     * false otherwise
     */
    public static boolean shuffled(){
        return shuffle;
    }

    /**
     * Draw a locomotive card
     * @return EColor a locomotive card
     */
    public static EColor drawLoco(){
        trainCardDeck.remove(EColor.LOCOMOTIVE);
        return EColor.LOCOMOTIVE;
    }

    /**
     * This method adds a train card back to the 
     * trainCardDiscard pile
     * @param t - Train card to be added back
     */
    public static void addTrainCardBack(EColor t){
        trainCardDiscard.add(t);
    }

    /**
     * Shuffles the train deck after adding in the cards 
     * that were discarded
     */
    public static void shuffleTrain(){
        if(trainCardDeck.size()==0){
            shuffle=true;
            trainCardDeck.addAll(trainCardDiscard);
            trainCardDiscard.clear();
            Collections.shuffle(trainCardDeck);
        }
    }

    /**
     * Shuffles the destination deck
     */
    public static void shuffleDest(){
        Collections.shuffle(destinationDeck);
        shuffle=true;
    }

    /**
     * Draws/removes a destination card from the deck
     * @return the destination card drawn from the deck
     */
    public static Destination drawDest(){
        if(destinationDeck.size()>0){
            return destinationDeck.remove(0);
        }
        return null;
    }

    /**
     * Adds discarded destination card to the bottom of 
     * the destination deck
     * @param dest the destination card being added back 
     * into the deck
     */
    public static void discardDest(Destination dest){
        destinationDeck.add(dest);
    }

    /**
     * Returns true if there are enough dest cards left 
     * or false otherwise
     * @return if there is enough cards in the deck
     */
    public static boolean destCardsLeft(){
        if(destinationDeck.size()>0){
            return true;
        }
        return false;
    }

}