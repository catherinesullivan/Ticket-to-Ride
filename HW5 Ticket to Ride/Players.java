import javax.swing.*;
import java.util.*;
import java.awt.Color;

/**
 * The players class sets up the underlying systems for 
 * players as a group. This group
 * collects all individual players into a unified arraylist.
 * 
 * @author (Brian Knapp, Catherine Sullivan,
 *          Jessica Juan-Aquino, Uros Antic, Benjamin Costello) 
 * @version (42.0)
 */

public class Players{
    //arraylist of all of the players
    protected static ArrayList<Player> players;
    // current player 
    protected static Player currentPlayer;
    // list of tech cards to buy
    protected static ArrayList<Technology> cardsToBuy;
    // list of all tech cards
    protected static ArrayList<Technology> allTechCards;
    // determines last turn 
    protected static boolean lastTurn;

    /**
     * Sets up all of the player information for the 
     * game at the start of the program
     */
    public Players(){
        // Set last turn to false and declare a cardsToBuy arrayList
        lastTurn = false;
        //arraylist of all the tech cards to buy
        cardsToBuy = new ArrayList<Technology>();

        try{

            players = new ArrayList<Player>();
            int numPlayers = -1;
            String[] options = {"2","3","4"};

            // If no players exist yet, 
            //ask how many will play (2 or 3 or 4)
            while(numPlayers == -1){
                numPlayers = JOptionPane.showOptionDialog(null,
                    "How many players?",
                    "Number of Players",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,     //do not use a custom Icon
                    options,  //the titles of buttons
                    options[0]); //default button title
            }

            ArrayList<String> playerNames = new ArrayList<String>();
            ArrayList<Color> playerColors = new ArrayList<Color>();

            // For each player, initialize a name and color
            for(int i=0; i<=numPlayers+1; i++){
                String name="";
                name= (String)JOptionPane.showInputDialog(
                    null,
                    "Player "+ (i+1) + ", what is your name?"
                    +"\n(Max 13 Chars)", 
                    "Player Name", 
                    JOptionPane.QUESTION_MESSAGE);

                // If the name field is empty, 
                //initialize the name to Player
                if(name==null || name.trim().length()==0){
                    name="Player " + (i+1);
                }

                // If the entered name already exists, 
                //add a number to the end
                if(playerNames.contains(name)){
                    int k=2;
                    while(playerNames.contains(name+k)){
                        k++;
                    }
                    playerNames.add(name+" "+k);
                    name=name+" "+k;
                }
                else{
                    playerNames.add(name);
                }

                Color chosen;

                // Open the color picker and let the 
                //user choose a color
                do{
                    JColorChooser colorChooser = new JColorChooser();
                    JDialog dialog = JColorChooser.createDialog(null,
                            "Pick your Color", true, colorChooser,
                            null, null);
                    dialog.setVisible(true);
                    Color c = colorChooser.getColor();
                    chosen = new Color(c.getRed(),c.getGreen(), 
                        c.getBlue() );
                }
                while(playerColors.contains(chosen));

                playerColors.add(chosen);           
                players.add(new Player(name, chosen));   
                int extraLoco=0;

                // A super duper fun easter egg used to 
                //test certain cases
                // Each player gets an extra locomotive 
                //and Cathy gets 42 :-)
                switch(name){
                    case "Catherine": case "Brian":
                    extraLoco=40;
                    case "Jess":   case "Benny": 
                    case "Uros":
                    extraLoco++;
                    for(int d=0; d<extraLoco; d++){
                        players.get(players.size()-1).
                        addTrainCard(EColor.LOCOMOTIVE);
                    }
                }
            }
            currentPlayer=players.get(0);
        }
        catch(Exception e){
            System.err.println(e.toString());
        }
    }

    // Adds all technology cards to the allTechCards arrayList
    static{
        allTechCards= new ArrayList<Technology>();
        allTechCards.add( new Technology( 2,4, 
                ETech.boiler_lagging)); 
        allTechCards.add( new Technology(2,4, ETech.booster)); 
        allTechCards.add( new Technology(3,1, ETech.diesel_power)); 
        allTechCards.add( new Technology(4,4, ETech.doubleheading)); 
        allTechCards.add( new Technology(2,1, ETech.equalising_beam));
        allTechCards.get(allTechCards.size()-1).setBuyBeforeShuffle();
        allTechCards.add( new Technology(1,4, 
                ETech.ireland_france_concession));
        allTechCards.add( new Technology(1,4, 
                ETech.mechanical_stoker));
        allTechCards.add( new Technology(2,4, ETech.propellers));
        allTechCards.add( new Technology(4,1, ETech.right_of_way));
        allTechCards.add( new Technology(2,1, ETech.risky_contracts));
        allTechCards.get(allTechCards.size()-1).setBuyBeforeShuffle();
        allTechCards.add( new Technology(1,4, 
                ETech.scotland_concession));
        allTechCards.add( new Technology(2,4, 
                ETech.steam_turbine));
        allTechCards.add( new Technology(2,4, 
                ETech.superheated_steam_boiler));
        allTechCards.add( new Technology(1,1, 
                ETech.thermo_compressor));
        allTechCards.add( new Technology(1,4, 
                ETech.wales_concession));
        allTechCards.add( new Technology(2,2, ETech.water_tenders));
    }

    /**
     * Returns the next player by shifting all of the
     * players by 1 then returning the player at index 0
     * 
     * @return Next player to go
     */
    public static Player getNextPlayer(){
        removeCards();
        Collections.rotate(players,-1);
        currentPlayer = players.get(0);

        if(lastTurn){
            if(currentPlayer.takeLastTurn()){
                //signal game's over
                return null;
            }

            JOptionPane.showMessageDialog(null,
                currentPlayer.getName() + " Last Turn" );
        }        

        updateCardsToBuy();
        return currentPlayer;
    }

    /**
     * When the game is over, this method parses through
     * all player destination cards to determine final statistics
     * like longestPath, riskyContracts, and Equalising Beams. This
     * also determines a winner and calls a winningPanel.
     */
    public static void gameOver(){
        ArrayList<Integer> completedDest = new ArrayList<Integer>();
        ArrayList<Integer> longestPath = new ArrayList<Integer>();

        //Go through and check all
        //of the playesr completed Destination tickets
        for(int i=0; i<players.size(); i++){
            Player p = players.get(i);
            completedDest.add(p.completedDestinationTickets());
            longestPath.add(Tracks.longestPath(p));
        }

        //Check for equalising beam
        //check for risky contracts
        for(int i=0; i<players.size(); i++){
            Player p = players.get(i);
            //check risky contracts
            if(p.hasRiskyContracts()){
                int index = getMax(completedDest,i);
                int pointInc = (index==i)?20:-20;
                p.incNumOfPoints(pointInc);
                p.textToPrint.add("Risky Contracts "+ 
                    ((pointInc>0)? "+"+pointInc:pointInc));
            }
            //check equalising beam
            if(p.hasEqualisingBeam()){
                int index = getMax(longestPath,i);
                int pointInc = (index==i)?15:-15;                
                p.incNumOfPoints(pointInc);
                p.textToPrint.add("Equalising Beam "+
                    ((pointInc>0)? "+"+pointInc:pointInc));
            }
        }
        //player who won
        Player pWon = players.get(0);
        for(Player p : players){
            p.textToPrint.add("Ending Points: " 
                + p.numOfPoints);
            // If the winning conditions are met, set the winner
            if(p.getNumOfPoints()>pWon.getNumOfPoints()){
                pWon=p;
            }
        }
        //add winning panel
        WinningPanel p = new WinningPanel(pWon.getName());
        p.main();
        //add stats panel
        StatsPanel sp = new StatsPanel();
        sp.display();
    }

    /**
     * Returns the index of the maximum value in the list
     * and if there is a tie, it returns the current player
     * if the current player is one of those people
     * 
     * @param list arraylist of integers
     * @param player index of current player in the array
     * @return index of max value
     */
    public static int getMax(ArrayList<Integer> list, int player){
        int index=0;
        int max =list.get(0);
        for(int i=1; i<list.size(); i++){
            if(list.get(i)>max){
                index=i;
                max=list.get(i);
            }
        }

        //if tie, make sure player gets their points
        if(max==list.get(player)) return player;
        return index;
    }

    /**
     * Takes out the right of way card and
     * thermocompressor from the current player
     * so that way they return it.
     */
    public static void removeCards(){
        //Remove owners from right of way and thermocompressor
        //then sends them back to the deck
        currentPlayer.removeTech();
        for(Technology card : allTechCards){
            if(card.getETech()==ETech.right_of_way
            || card.getETech()==ETech.thermo_compressor){
                card.clearPlayersOwned();
            }
        }
    }

    /**
     * Update the cards that the current player
     * is allowed to purchase.
     */
    public static void updateCardsToBuy(){
        cardsToBuy = new ArrayList<Technology>();
        cardsToBuy.addAll(allTechCards);

        for(Technology t : allTechCards){
            if(!t.canBuy(currentPlayer)){
                cardsToBuy.remove(t);
            }
        }
    }

    /**
     * If this is the last turn, set this variable to true.
     */
    public static void signalLastTurn(){
        lastTurn = true;
    }

    /**
     * If a player bough a technology card,
     * add it to their deck of technology cards.
     * 
     * @param card - technology card adding player to
     */
    public static void addPlayerToTech(ETech card){
        for(Technology t: allTechCards){
            if(t.getETech()==card){
                t.addPlayer(currentPlayer.getColor());
            }
        }
    }

    /**
     * Returns the current tech card to buy
     * 
     * @return current technology card
     */
    public static Technology getCurrentTech(){
        //May need to update current tech cards to buy
        if(cardsToBuy.size()==0) updateCardsToBuy();

        if(cardsToBuy.size()!=0){
            return cardsToBuy.get(0);
        }

        return null;
    }

    /**
     * Shifts the tech card to display by i
     * 
     * @param i amount to shift tech cards by
     */
    public static void shiftTech(int i){
        if(cardsToBuy.size()>0){
            Collections.rotate(cardsToBuy,i);
        }
    }

    /**
     * Returns the current player
     * 
     * @return returns the current player
     */
    public static Player getPlayer(){
        return currentPlayer;
    }

    /**
     * returns true if there are 2
     * players or false if there are more.
     * This is necessary because a 2 player game
     * is different than a 3 or 4 player game.
     * 
     * @return true if there are 2 players or
     * false otherwise
     */
    public static boolean twoPlayers(){
        if(players.size()==2) return true;
        return false;
    }

    /**
     * Returns the number of players
     * 
     * @return the number of players in the game
     */
    public static int getNumPlayers(){
        return players.size();
    }

    /**
     * Returns the player at the current index
     * 
     * @param index index in player arraylist to get
     * @return Player at that index
     */
    public static Player getPlayerAt(int index){
        return players.get(index);
    }
}