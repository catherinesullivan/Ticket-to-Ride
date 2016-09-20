import java.awt.*;
import java.util.*;

/**
 * Contains all the methods to handle operations dealing
 * with the technology cards
 * 
 * @author Brian Knapp, Jessica Juan-aquino, 
 * Catherine Sullivan, Benny Costello, Uros Antic
 * @version 5/2/2016
 */
public class Technology 
{
    //Cost to buy card
    int cost;    
    // An array list if the player owns it
    ArrayList<Color> playersOwn;
    //maximum number of players that can
    //buy the card
    int maxNumPlayersToBuy;
    //tech card
    ETech tech;
    //if you have to buy the card before the shuffle
    boolean beforeShuffle;

    /**
     * Constructs a technology card by adding the cost,
     * max num of players
     * that can buy, and the tech card
     * @param cost - cost to buy the card
     * @param maxNumPlayersToBuy the max num of players that
     * can buy the tech card
     * @param tech the tech card
     */
    public Technology(int cost, int maxNumPlayersToBuy,
    ETech tech){
        this.cost=cost;
        this.tech=tech;
        this.maxNumPlayersToBuy=maxNumPlayersToBuy;
        playersOwn=new ArrayList<Color>();
        beforeShuffle=false;
    }

    /**
     * Adds a player to the playersOwn list
     * @param player
     */
    public void addPlayer(Color player ){
        playersOwn.add(player);
    }

    /**
     * Clears all of the players who own this card
     */
    public void clearPlayersOwned(){
        playersOwn.clear();
    }

    /**
     * returns the ETech for the player to hold onto
     * @return the ETech for the player to hold onto
     */
    public ETech getETech(){
        return tech;
    }

    /**
     * If you have to buy the card before shuffling it
     */
    public void setBuyBeforeShuffle(){
        beforeShuffle=true;
    }

    /**
     * Returns true if you have to buy it before shuffling 
     * or false otherwise
     * @return true if you have to buy it before shuffling
     */
    public boolean buyBeforeShuffle(){
        return beforeShuffle;
    }

    /**
     * Gets the image for the technology card
     * @return Image - image of the technology card
     */
    public Image getImage(){
        return tech.image;
    }

    /**
     * If the player can buy the card
     * @param p - player who wants to buy the card
     * @return if a player can buy the tech card
     */
    public boolean canBuy(Player p){
        Color playerColor = p.getColor();
        //Player already owns card
        if(playersOwn.contains(playerColor)){
            return false;
        }
        if(playersOwn.size()>=maxNumPlayersToBuy){
            return false;
        }
        if(beforeShuffle && Deck.shuffled()){
            return false;
        }        
        return true;
    }

    /**
     * Returns back the cost of the technology card
     * @return the cost of the card
     */
    public int getCost(){
        return cost;
    }

    /**
     * Determines if the player can buy this card
     * @param p the player trying to buy the card
     * @param trains the trains the player is using
     * to buy the tech card with
     * @return if the player can buy the tech card
     */
    public boolean canBuy(Player p, int[] trains){
        //To avoid changing the trains array
        int[] trainCards = Arrays.copyOf(trains,
                trains.length);
        int playerLoco = trainCards[8];
        trainCards[8]=0;
        //If the players number of locomotive cards 
        // equals the cost
        //and they have no extra ints
        int[] blank = {0,0,0,0,0,0,0,0,0};
        if(playerLoco==cost && Arrays.
        equals(blank, trainCards )) return true;
        //Determine how many of each other card
        //is 1 locomotive
        int locoCards = (p.haveBooster())? 3: 4;
        int numPlayerCards=0;
        for(int i: trainCards){
            numPlayerCards+=i;
        }
        //Count rest of player cards
        // see if cost times number of random cards
        // is same as number of locmotoives times 
        // player loco plus the number of player cards
        // This is to avoid division and potential error
        if(cost*locoCards==
        playerLoco*locoCards+numPlayerCards){
            return true;
        }
        return false;
    }
}