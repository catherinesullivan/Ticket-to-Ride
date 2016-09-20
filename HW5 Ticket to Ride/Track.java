
import java.awt.*;
import java.util.*;

/**
 * An individual track in the game. Holds the cities of the 
 * tracks and the length as well as the polygons to draw the 
 * track and the types trains needed to buy the track.
 * Holds a list of the players who bought the trak and if 
 * they visited the track or not. As well as if the track is 
 * a ferry path and if it is a double track the track it 
 * lies next to.
 * 
 * @author (Brian Knapp, Catherine Sullivan,
 *          Jessica Juan-Aquino, Uros Antic, Benjamin Costello) 
 * @version (1.0)
 */

public class Track{
    // 2 cities that the Track connects
    protected String city1, city2;
    // number of values in Track
    protected int squares;
    //all of the graphic points
    protected ArrayList<Polygon> graphicPoints = 
        new ArrayList<Polygon>();
    //trains needed to buy the track
    protected ArrayList<EColor> trainsNeeded = 
        new ArrayList<EColor>();
    //players bought arraylist of their color
    protected ArrayList<Color> playersBought = new ArrayList<Color>();
    //marks visited if the track was visited for dfs
    protected boolean visited;
    //if it is a ferrypath or not
    protected boolean isFerryPath;
    //if it is a double path, then the track
    //that it shares cities with
    protected Track otherPath;

    /*
     * makes sure the cities class gets initialized
     */
    static{
        new Cities();
    }

    /**
     * Constructs the individual Track
     * 
     * @param c1 city name
     * @param c2 city name
     * @param g list of points in the train
     * @param e ArrayList of colors need to purchase Track
     */
    public Track(String c1, String c2, ArrayList<Polygon> g,
    ArrayList<EColor> e){
        squares = g.size();
        city1=c1;
        city2=c2;
        graphicPoints.addAll(g);
        trainsNeeded.addAll(e);
        visited=false;
        isFerryPath=false;
        otherPath=null;
    }

    /**
     * Returns the cost in the form of a string
     * @return String of what to draw for the player
     */
    public String getCost(){
        //if you need a locomotive
        if(trainsNeeded.contains(EColor.LOCOMOTIVE)){
            //set number of locos and grays to 0
            int loco = 0;
            int gray = 0;
            for(int i = 0; i < trainsNeeded.size(); i++){
                if(trainsNeeded.get(i) == EColor.LOCOMOTIVE) loco++;
                if(trainsNeeded.get(i) == EColor.GRAY) gray++;
            }
            //return string of how many locos and grays you have
            return gray == 0 ? "COST: " + loco + 
                plural(loco, " LOCOMOTIVE", " LOCOMOTIVES") :
            "COST: " + loco + 
            plural(loco, " LOCOMOTIVE", " LOCOMOTIVES") + 
            "  AND " + gray + " GRAY " + 
            plural(gray, " TRAIN", " TRAINS");
        }
        else{
            //return cost plus number of trains
            //and plural based on number
            return "COST: " + trainsNeeded.size() + 
            " " + trainsNeeded.get(0) + 
            plural(trainsNeeded.size()," TRAIN", " TRAINS");
        }
    }

    /**
     * Returns the singular or plural word depending on the number
     * of trains
     * @param count the number of trains
     * @param singular the singular version of the word
     * @param plural the plural version of the word
     * @return the singular or plural version of the word
     */
    public String plural(int count, String singular, String plural){
        return count == 1 ? singular : plural;
    }

    /**
     * Determines if the player owns the track
     * @param p - player in question
     * @return true if player bought track or false if
     * player did not buy the track
     */
    public boolean playerOwn(Player p){
        Color playerColor = p.getColor();
        //go through and see if player owns this track
        for(Color c : playersBought){
            if(c.equals(playerColor)) return true;
        }
        return false;
    }

    /**
     * Determines if the player has enough cards
     * to buy the track. This only returns true if you 
     * have exactly the right amount of cards. 
     * @param p - Player wanting to buy the track
     * @param playerTrains - integer array of number of train cards
     * in the order of:
     * WHITE, BLUE, YELLOW, BLACK, RED, GREEN, ORANGE, PINK,
     * LOCOMOTIVE
     * @return true if the player can buy this track or false 
     * otherwise
     */
    public boolean playerHasEnoughCardsToBuy(Player p, 
    int[] playerTrains){  
        //Make copies of both
        //We don't want to modify the array so we will make a copy
        int[] trainCards = Arrays.copyOf(playerTrains, 
                playerTrains.length);
        ArrayList<EColor>  trains = new ArrayList<EColor>();
        trains.addAll(trainsNeeded );
        //If you have the special technology card
        //then go through and remove 1
        if(p.hasDieselPower()){
            //You must play at least 1 card
            if(trains.size()>1){
                for(int i=0; i<trains.size(); i++){
                    if(trains.get(i)!=EColor.LOCOMOTIVE){
                        trains.remove(i);
                        //end loop
                        i=trains.size()+42;
                    }
                }
            }
        }

        // 3 cards count as 1 locomotive,
        // if player has Booster card. 4 otherwise
        int locoCards = (p.haveBooster())? 3: 4;

        // 2 Cases:
        //1 : you have gray cards
        //2: you do not have gray cards
        if(trains.contains(EColor.GRAY)){
            //Loop through and pretend they are different colors
            for(int i=0; i<9; i++){
                //Write
                if(checkCards(locoCards, trains, trainCards, i)){
                    return true;
                }
            }
        }
        else{
            //Just call the method regularly
            return checkCards(locoCards,trains,trainCards,-1);
        }

        return false;
    }

    /**
     * Checks if player has enough cards
     * @param locoCards - either 3 or 4 cards count as a locomotive
     * @param t - ArrayList<Ecolor> list of colors to buy train
     * @param playCards - int[] number of each card player
     * has in order:
     * WHITE, BLUE, YELLOW, BLACK, RED, GREEN, ORANGE, PINK, 
     * LOCOMOTIVE
     * @param grayIndex - if there is a gray space, what color 
     * to count
     * the graytile as
     * @return true if the player has the exact amount of cards 
     * to buy the space or false otherwise
     */
    public boolean checkCards(int locoCards, ArrayList<EColor> t,
    int[] playCards, int grayIndex){
        //Make copies again
        int[] trainCards = Arrays.copyOf(playCards,playCards.length);
        ArrayList<EColor> trains = new ArrayList<EColor>();
        trains.addAll(t);

        //Go through and match card with card
        //if it is a gray card, treat it like 
        //the grayIndex color
        for(int b=0; b<trains.size(); b++){
            EColor color = trains.get(b);
            int colorIndex=grayIndex;
            if(color!=EColor.GRAY){
                colorIndex = EColor.getIndex(color);
            }
            if(trainCards[colorIndex]>0){
                trainCards[colorIndex]--;
                trains.remove(b);
                b--;
            }
        }

        int trainSizeLeft = trains.size();
        //Bought with appropriate cards
        //make sure used all trains too
        int[] blank = {0,0,0,0,0,0,0,0,0};
        if(trainSizeLeft==0 
        && Arrays.equals(blank, trainCards )) return true;

        int numPlayerHasLeft =0;
        int playersLoco = trainCards[8];
        trainCards[8]=0;
        for(int i : trainCards){
            numPlayerHasLeft+=i;        
        }

        //Rest of the Cards are converted into locomotives
        // Total number of spots needed times the number of cards that
        // a locomotive counts for has to each the number of cards the
        // player has left plus the number of players non-loco cards
        // plus locomotive cards times how many cards those count as
        if(trainSizeLeft*locoCards ==
        numPlayerHasLeft + playersLoco*locoCards )
            return true;

        //If you do not meet any of the criteria,
        // you cannot buy the track :(
        return false;
    }

    /**
     * Returns all the players who own the track
     * @return arraylist of all the player colors who 
     * own the track
     */
    public ArrayList<Color> getColors(){
        if(playersBought.size()>0){
            return playersBought;
        }
        else return null;
    }

    /**
     * Add owner to the track
     * @param c - Color of player who bought the track
     */
    public void addPlayer(Color c){
        playersBought.add(c);
    }

    /**
     * Determines if a player can buy a track.
     * Does not check if the player has the right
     * amount of cards. 
     * @param p - player wanting to buy the track
     * @return true if the player can buy it or false 
     * otherwise
     */
    public boolean playerCanBuy(Player p){
        //If the player owns it, they cannot buy it
        if(playerOwns(p.getColor())) return false;
        
        if(p.numOfTrains<squares) return false;
        
        //If it is already owned and you do not have the 
        //right of way card
        if(playersBought.size()>0 && !p.hasRightOfWay()){
            return false;
        }

        if(Players.twoPlayers()){
            //Check if it is a double path and if there are 2 players 
            //and if one of the tracks is owned
            //just the otherPath mainly because we do not care
            if(otherPath!=null  
            && otherPath.playersBought.size()>0
            && !p.hasRightOfWay()){
                return false;
            }
            //if you own the other path then you cannot buy it
            if(otherPath!=null && otherPath.playerOwns(p.getColor())){
                return false;
            }
        }

        //Get the cities that the 2 tracks connect
        City c1 = Cities.getCity(city1);
        City c2 = Cities.getCity(city2);    
        // New York is not listed as a city
        //You can buy new york whenever you want
        if(c1==null || c2==null) return true;

        //Check if track is a ferry and player has
        //a ferry card
        if (isFerryPath && !p.canBuildFerry() )
            return false;
        //Check if the player can build path length
        if(!p.canBuildLength(squares))
            return false;

        //Check Countries first
        Country country1 = c1.getCountry();
        Country country2 = c2.getCountry();
        //See if player can buy in these countries
        if(!p.canBuildInArea(country1, country2)){
            return false;
        }
        return true;
    }

    /**
     * Get graphics points
     * @return ArrayList<Polygon> all of the graphics
     * polygons for the track
     */
    public ArrayList<Polygon> getGraphicsPoints(){
        return graphicPoints;
    }

    /**
     * Determines if it is a single path or not
     * @return true if it is a single path or 
     * false if it is a double path
     */
    public boolean isSinglePath(){
        return (otherPath==null);
    }

    /**
     * Sets the track if it is a ferry track
     */
    public void setFerryPath(){
        isFerryPath=true;
    }

    /**
     * Returns true if it is a ferry path and false otherwise
     * @return true if it is a ferry path or false otherwise
     */
    public boolean isFerry(){
        return isFerryPath;
    }

    /**
     * Connects one track to the other
     * @param t - Other Track that you are setting a double path to
     */
    public void setDoublePath(Track t){
        this.setOtherPath(t);
        t.setOtherPath(this);
    }

    /**
     * Sets the otherPath Track variable to t
     * @param t - Track to set the OtherPath to
     */
    public void setOtherPath(Track t){
        this.otherPath=t;
    }

    /**
     * Adds the player who bought this track
     * @param playerColor - player who just bought track
     */
    public void addPlayerToTrack(Color playerColor){
        playersBought.add(playerColor);
    }

    /**
     * Returns true if the player owns this track and
     * false otherwise.
     * @param playerColor - the color of the player
     * @return true if the player owns this color or
     * false otherwise
     */
    public boolean playerOwns(Color playerColor){
        return playersBought.contains(playerColor);
    }

    /**
     * Get the value the Track is worth based off 
     * of the length of the Track and the technology
     * cards the player has
     * @param p - Player who bought the track
     * @return int value of points earned
     */
    public int getValue(Player p){
        int toreturn=0;
        switch(squares){
            case 1:
            toreturn+= 1;
            break;
            case 2:
            toreturn+= 2;
            break;
            case 3:
            toreturn+= 4;
            break;
            case 4:
            toreturn+= 7;
            break;            
            case 5:
            toreturn+= 10;
            break;            
            case 6:
            toreturn+= 15;
            break;
            case 10: 
            toreturn+= 40;
            break;
        }
        //extra poitns for boilerlagging
        if(p.hasBoilerLagging()){
            toreturn++;
        }
        //if it is a ferry path and the person
        //has the technology card for extra points
        if(isFerryPath && p.hasSteamTurbines()){
            toreturn+=2;
        }
        return toreturn;
    }

    /**
     * Returns true if the Track is occupied or false otherwise
     * @return true if the Track is occupied or false otherwise
     */
    public boolean occupied(){
        return (playersBought.size()>0)? true : false;
    }

    /**
     * Returns the first city name
     * @return name of first city for the Track
     */
    public String getCity1(){
        return city1;
    }

    /**
     * Returns the second city name for the Track
     * @return name of the secnod city for the Track
     */
    public String getCity2(){
        return city2;
    }

    /**
     * Returns the length of the path
     * 
     * @return the length of the path
     */
    public int getLength(){
        return squares;
    }

    /**
     * If this path has been visited, mark it as being visited
     */
    public void markVisited(){
        visited=true;
    }

    /**
     * If the path was not visited, mark it as not being visited
     */
    public void markUnvisited(){
        visited=false;
    }

    /**
     * Returns the boolean value of if the track has been visited 
     * or not.
     * @return true if the track was visited and false if it
     * was not visited
     */
    public boolean isVisited(){
        return visited;
    }
}