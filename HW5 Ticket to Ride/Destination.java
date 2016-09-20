import java.awt.*;
/**
 * Class contains the methods required to handle operations
 * dealing with the destination cards
 * 
 * @author Brian Knapp, Jessica Juan-aquino,
 * Catherine Sullivan, Benny Costello, Uros Antic
 * @version 5/2/2016
 */
public class Destination 
{
    //name of city 1
    protected String city1;
    //name of city 2
    protected String city2;
    //points if you get destination card
    protected int points;
    //image of destination card
    protected Image front;
    //if player completes it or not
    protected boolean complete;

    /**
     * Constructs the destination card
     * @param city1 the first city of the destination card
     * @param city2 the second city of the destination card
     * @param points the number of points the destination
     * card is worth
     */
    public Destination(String city1, String city2, int points )
    {
        this.city1 = city1;
        this.city2 = city2;
        this.points = points;
        front=null;
        complete=false;
    }

    /**
     * Adds the image to the card
     * @param front the front image of the card
     */
    public void addImage(Image front){
        this.front=front;    
    }

    /**
     * Returns the image
     * @return the front image of the card
     */
    public Image getImage(){
        return front;
    }

    /**
     * Get first city on ticket
     * @return name of the city
     */
    public String getCity1(){
        return city1;
    }

    /**
     * Get first city on ticket
     * @return name of the city
     */
    public String getCity2(){
        return city2;
    }

    /**
     * Returns the points value for the destination card
     * @return int
     */
    public int getPoints(){
        return points;
    }

    /**
     * Updates if the player completed the destination card
     * this is done when a player buys a track. 
     * @param color - player's color in question
     * @return returns if the destination card is complete 
     * or not
     */
    public boolean updatePath(Color color){
        //Only check again if it has not
        //been completed
        if(!complete && 
        Tracks.isPath(city1,city2, color)){
            complete=true;
        }
        //return the value of complete
        return complete;
    }

    /**
     * Determines if a player completed this destination 
     * card or not
     * @return true if the player completed the destination
     * card or false otherwise
     */
    public boolean playerComplete(){
        return complete;
    }
}