import java.awt.*;
import javax.swing.*;
import java.util.*;

/**
 * The player class constructs the player object. 
 * Each player object contains name, color, number 
 * of trains , number of points
 * 
 * @author (Brian Knapp, Catherine Sullivan,
 *          Jessica Juan-Aquino, Uros Antic, Benjamin Costello) 
 * @version (42.0)
 */
public class Player
{
    // name of the player
    protected String name;  
    //color of the player 
    protected Color color;  
    // number of the trains possessed by the player
    protected int numOfTrains;  
    // number of player's points
    protected int numOfPoints;  

    // Declares arrayLists to hold trainCards and 
    //destinationCards
    protected ArrayList<EColor> trainCards = new 
        ArrayList<EColor>();
    protected ArrayList<Destination> destCards = 
        new ArrayList<Destination>();

    // Declares an arrayList to hold technologyCards that
    //the player owns
    protected ArrayList<ETech> techCardsOwned;

    // Records statistics for the player's last turn and 
    //which destinationCards were completed
    protected int destCardsComplete;
    protected int destCardIndex;
    protected boolean tookLastTurn;

    // Arraylist to print all dest cards
    protected ArrayList<String> textToPrint = 
        new ArrayList<String>();

    /**
     * Each player is constructed with a name, #of trains, 
     * #of points, a color,
     * and other empty datapoints which help tally 
     * statistics at the end of a game.
     * 
     * @param name Player's name
     * @param color Player's color
     */
    public Player (String name, Color color){
        this.name = name;
        this.numOfTrains = 35;
        this.numOfPoints = 0;
        this.color = color;

        destCardsComplete=0;
        destCardIndex=0;
        tookLastTurn=false;

        techCardsOwned=new ArrayList<ETech>();

        //Set up players initial 5-card hand
        trainCards.add( Deck.drawLoco());
        trainCards.add(Deck.drawTrainCard());
        trainCards.add(Deck.drawTrainCard());
        trainCards.add(Deck.drawTrainCard());
        trainCards.add(Deck.drawTrainCard());
    }

    /**
     * Returns whether the player took their last turn
     * 
     * @return true if the player already took their 
     * last turn or false otherwise
     */
    public boolean takeLastTurn()
    {
        if(tookLastTurn) return true;
        tookLastTurn=true;
        return false;
    }

    /**
     * Returns the number of completed destination tickets
     * 
     * @return returns the number of completed destination tickets
     */
    public int getDestCardsComplete()
    {
        return destCardsComplete;
    }

    /**
     * This method sets the name of the player
     * 
     * @param name - name of the player
     */
    public void setName(String name)    
    {
        this.name = name;
    }

    /**
     * This method returns the name of the player
     * 
     * @return this.name name of the player
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * This method sets the color of the player
     * 
     * @param color color of the player
     */
    public void setColor(Color color)   
    {
        this.color = color;
    }

    /**
     * This method returns the color of the player
     * 
     * @return this.color color of the player
     */
    public Color getColor()  
    {
        return this.color;
    }

    /**
     * This method returns the number of the player's trains
     * 
     * @return numOfTrains number of trains 
     */
    public int getNumOfTrains() 
    {
        return numOfTrains;
    }

    /**
     * This method decrease the number of the player's train
     * 
     * @param pathMade length of the path made 
     * @return true True if player has 0, 1 and 2 trains, 
     * false otherwise 
     */
    public boolean decreaseNumOfTrains(int pathMadeLen)   
    {
        numOfTrains -= pathMadeLen;
        for(Destination d : destCards){
            d.updatePath(color);
        }

        if ( numOfTrains < 3)   {
            return true;
        }

        return false;
    }

    /**
     * This method returns the number of player's points
     * 
     * @return numOfPoints number of player's points 
     */
    public int getNumOfPoints() {
        return numOfPoints;
    }

    /**
     * This method increases the number of points
     * 
     * @param points number of points added 
     */
    public void incNumOfPoints(int points)    
    {
        numOfPoints += points;
    }

    /**
     * Get new Hand, asks the deck class for 4 new train cards
     * and sets the players trainCard hand
     */
    public void setHand()
    {
        for(int i = 0; i < 4; i++)
        {
            trainCards.add(Deck.drawTrainCard());
        }
    }

    /**
     * Get a new card, the player gets a new card to their hand
     * 
     * @param EColor color to add to the pile
     */
    public void addTrainCard(EColor color)
    {
        trainCards.add(color);
    }

    /**
     * Adds back the train cards to the players hand if they clicked
     * on the back button
     * 
     * @param train - int array of train cards 
     * to add back in order of: WHITE, BLUE, YELLOW, 
     * BLACK, RED, GREEN, ORANGE, PINK, LOCOMOTIVE
     */
    public void addTrainCards(int[] train)
    {
        //Go through all colors
        for(int i=0;i<train.length; i++)
        {
            if(train[i]>0)
            {
                //Add back card for each card taken out
                for(int j=0; j<train[i]; j++)
                {
                    trainCards.add(EColor.getColor(i));
                }
            }
        }
    }

    /**
     * Get new destination card, selected from board
     * to add to the players hand.
     * 
     * @param d destination card being added to the
     * players hand
     */
    public void addDesCard(Destination d){
        destCards.add(d);

        //update paths for destination cards
        for(Destination dd : destCards)
        {
            dd.updatePath(color);
        }
    }

    /**
     * Gets the current players destination card
     * 
     * @return Destination card
     */
    public Destination getCurDest(){
        if(destCards.size()>0){
            return destCards.get(0);
        }
        return null;
    }

    /**
     * Shifts the current destination card
     * being shown by i
     * 
     * @param i - amount to be shifted by
     */
    public void shiftDest(int i){
        if(destCards.size()>0)
        {
            Collections.rotate(destCards,i);
        }
    }

    /**
     * Goes throught the players hand and returns a card
     * of the specified color
     * 
     * @param e - EColor color of train card
     * we are looking for
     * @return EColor so the train card of that color
     * or null if there is none
     */
    public EColor getTrainCard(EColor e){
        for(int i=0; i<trainCards.size(); i++){
            EColor train = trainCards.get(i);
            if(train==e){
                trainCards.remove(i);
                return train;
            }
        }
        return null;
    }

    /**
     * Gets the color cards for the player and puts in an array
     * where the indices follow the order:
     *  0-WHITE, 1-BLUE, 2-YELLOW, 3-BLACK,
     *  4-RED, 5-GREEN, 6-ORANGE, 7-PINK, 8-LOCOMOTIVE;
     * 
     * @return array of integer values for the number of 
     * colors in each
     * type
     */
    public int[] getColorArray()
    {
        int[] toreturn = new int[9];
        EColor color;

        for(EColor trainColor : trainCards)
        {
            switch(trainColor)
            {
                case WHITE:
                toreturn[0]++;
                break;

                case BLUE: 
                toreturn[1]++;
                break;

                case YELLOW: 
                toreturn[2]++;
                break;

                case BLACK:
                toreturn[3]++;
                break;

                case RED:
                toreturn[4]++;
                break;

                case GREEN:
                toreturn[5]++;
                break;

                case ORANGE: 
                toreturn[6]++;
                break;

                case PINK:
                toreturn[7]++;
                break;

                case LOCOMOTIVE:
                toreturn[8]++;
                break;
            }
        }
        return toreturn;
    }

    /**
     * Given the Enum Area of the country, 
     * it tells you if you can build there or not
     * 
     * @param city1 Enum of city1 wanting to build
     * @param city2 Enum of city2 wanting to build
     * @return true if you can build or false otherwise
     */
    public boolean canBuildInArea(Country city1, Country city2)
    {
        // build in wales
        if( !techCardsOwned.contains(ETech.wales_concession) && 
        (city1 == Country.WALES || city2 == Country.WALES))
        {
            return false;
        }

        // build france or ireland
        if( !techCardsOwned.contains(ETech.ireland_france_concession) 
        &&((city1 == Country.FRANCE || city2 == Country.FRANCE)
            || (city1 == Country.IRELAND || 
                city2 == Country.IRELAND)))
        {
            return false;
        }

        // build in Scotland
        if(!techCardsOwned.contains(ETech.scotland_concession) &&
        (city1 == Country.SCOTLAND || city2 == Country.SCOTLAND))
        {
            return false;
        }

        return true;
    }

    /**
     * Determines if you have the right technology cards
     * to build the track based on the length
     * 
     * @param length length of path you want to build
     * @return boolean true if you can build it or false otherwise
     */
    public boolean canBuildLength(int length)
    {
        // can you build length 3
        if(!techCardsOwned.contains(ETech.mechanical_stoker)
        && length==3)
        {
            return false;
        }

        // can you build length 4, 5, or 6
        if(!techCardsOwned.contains(ETech.superheated_steam_boiler)
        && length!=10 && length>=4)
        {
            return false;
        }

        return true;
    }

    /**
     * Determines if you can build on a track
     * whether it is a ferry track or not
     * 
     * @return true if you can build on the track or false otherwise
     */
    public boolean canBuildFerry(){      
        return techCardsOwned.contains(ETech.propellers);
    }

    /**
     * If you have the boost technology card or not.
     * Booster: you many now use any 3 cards as a locomotive
     * (instead of 4 cards)
     * 
     * @return true if you have it or false otherwise
     */
    public boolean haveBooster(){
        return techCardsOwned.contains(ETech.booster);
    }

    /**
     * If you have the boiler lagging technology card or not.
     * You score 1 extra point for each Track that you claim
     * 
     * @return true if you have it or false otherwise
     */
    public boolean hasBoilerLagging(){
        return techCardsOwned.contains(ETech.boiler_lagging);
    }

    /**
     * If you have the steamturbines technology card or not.
     * You score 2 extra points for each ferry Track that you 
     * claim.
     * 
     * @return true if you have it or false otherwise
     */
    public boolean hasSteamTurbines()
    {       
        return techCardsOwned.contains(ETech.steam_turbine);
    }

    /**
     * Determines if the player has the right of way card.
     * You may play this card to claim a route that was already
     * claimed by another player. When playing this card, you
     * must still play the correct number of cards to claim the
     * route. Simply place your trains on it next to the other
     * player's trains. You must immediately claim the route
     * when you take this card, and after you have claimed the
     * route, you return the card to the table so it can be taken
     * by another player.
     * 
     * @return true if the player has it or false otherwise
     */
    public boolean hasRightOfWay()
    {        
        return techCardsOwned.contains(ETech.right_of_way);
    }

    /**
     * Determines if the player has the thermocompressor card.
     * Claim 2 Routes this turn, then return this card.
     * 
     * @return true if the player has it
     * or false otherwise
     */
    public boolean hasThermocompressor(){
        return techCardsOwned.contains(ETech.thermo_compressor);
    }

    /**
     * Removes the right of way card and the thermocompressor
     */
    public void removeTech()
    {
        if(hasThermocompressor())
        {
            techCardsOwned.remove(ETech.thermo_compressor);
        }

        if(hasRightOfWay())
        {
            techCardsOwned.remove(ETech.right_of_way);
        }
    }

    /**
     * Determines if the player has the water_tenders card.
     * When drawing Train Cards, you can decide to draw 3 
     * blind cards instead of the regular 2. 
     * 
     * @return true if the player has it or false otherwise
     */
    public boolean hasWaterTenders(){
        return techCardsOwned.contains(ETech.water_tenders);
    }

    /**
     * Returns true if the player has risky contracts or
     * false otherwise
     *
     *@return true if the player has risky contracts 
     * or false otherwise
     */
    public boolean hasRiskyContracts(){
        return techCardsOwned.contains(ETech.risky_contracts);
    }

    /**
     * Returns true if the player has equalising beam 
     * or false otherwise
     * 
     * @return true if the player has equalising beam 
     * or false otherwise
     */
    public boolean hasEqualisingBeam(){
        return techCardsOwned.contains(ETech.equalising_beam);
    }

    /**
     * Determines if the player has the double
     * heading card
     * At the end of the game, you score 2 points
     * for each Ticket that you completed.
     * 
     * @return boolean true if the player has it or false otherwise
     */
    public boolean hasDoubleHeading(){        
        return techCardsOwned.contains(ETech.doubleheading);
    }

    /**
     * Determines if you have the Diesel Power technology
     * card.
     * 
     * @return true if you have the diesel power card
     * or false otherwise
     */
    public boolean hasDieselPower(){        
        return techCardsOwned.contains(ETech.diesel_power);
    }

    /**
     * gets the current image to show
     * 
     * @return image current player tech card image
     */
    public Image getCurTechCard(){
        if(techCardsOwned.size()==0) return null;
        return techCardsOwned.get(0).image;
    }

    /**
     * Adds tech card to players hand
     * 
     * @param tech adds tech card to players hand
     */
    public void addTechCard(ETech tech){
        techCardsOwned.add(tech);
    }

    /**
     * Shift the tech cards to display
     * 
     * @param i - amount to shift by
     */
    public void shiftTech(int i){
        if(techCardsOwned.size()>0)
        {
            Collections.rotate(techCardsOwned,i);
        }
    }

    /**
     * Determines how many complete destination tickets
     * the player has done. Adds to the count.
     *
     * If you completed the destination card then you get
     * the points (and if you have Double Heading then you 
     * get more points)
     * 
     * @return int - number of dest cards complete
     */
    public int completedDestinationTickets(){
        destCardsComplete=0;
        int extraBonus = (hasDoubleHeading())? 2: 0;

        textToPrint.add(name);
        textToPrint.add("Current Points: " + numOfPoints);

        for(Destination d : destCards){
            //if already completed
            boolean complete = d.updatePath(color);

            //get city names
            String city1 = d.getCity1();   
            String city2 = d.getCity2();  

            //If complete you gain the points
            if(complete){
                numOfPoints+=d.getPoints();
                numOfPoints+=extraBonus;
                destCardsComplete++;
                textToPrint.add(city1+"-"+
                    city2+" +"+d.getPoints());
            }

            //Not complete you lose the points
            else{
                numOfPoints-=d.getPoints();
                textToPrint.add(city1+"-"+
                    city2+" -"+d.getPoints());
            }
        }
        return destCardsComplete;
    }
}