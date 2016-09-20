import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import javax.imageio.*;
import java.awt.event.*;
import java.applet.*;
/**
 * This is where the magic of playing Ticket To Ride comes
 * alive. This class draws all of the images and pieces to 
 * the screen as well as keeps track of where the player's
 * mouse is and all of the play logic. 
 * @author (Brian Knapp, Catherine Sullivan,
 *          Jessica Juan-Aquino, Uros Antic, Benjamin Costello) 
 * @version 42.0
 */
public class Board extends JApplet
implements MouseListener, MouseMotionListener, 
MouseWheelListener, AppletStub
{
    //board, current destination card of player
    // background images
    Image board,dest,background;
    // The graphics for the screen before it is printed
    // buffer graphics
    Graphics bg; 
    Image backScreen;
    //The train back and destination
    //card backs
    Image trainBack, destBack;
    //Image for the done button
    Image doneButtonTrack;
    //Ticket image for player name and trains left
    Image ticket;
    // Coordinates of a mouse click
    int mouseClickX, mouseClickY;
    // Coordinates of a mouse hover
    int mouseHoverX, mouseHoverY;
    // determines if scroll up or down
    boolean wheelScrollUp, wheelScrollDown;
    //Right or left click
    boolean rightClick, leftClick;
    //All the images for the train cards
    Image[] trainCards;
    //tells you the number of trains drawn
    //in order to draw the exact amount
    int numOfTrainsDrawn;
    //the current players train card array
    int[] trains;
    //the players ecolor arraylist
    ArrayList<EColor> playersHand;
    //the ecolor of the train cards
    //the player wants to buy
    ArrayList<EColor> buyingWithTrains;
    //Depending the mode whether it is the
    //first turn or the destination mode
    boolean firstTurn,destMode;
    //the indices of the destination cards picked
    ArrayList<Integer> destCardsPicked;
    //the destination cards to lay out
    ArrayList<Destination> destCardsLayedOut;
    // to be able to use the cities class and 
    //access all of the cities
    Cities cities;
    //the current city name that you are hovering
    //over
    String cityName;
    //if you are currently displaying a city because
    //you are hovering over it
    boolean displayCity,
        //display dest is the boolean for if you are
        //currently showing city destination highlites
    displaydest;
    //index of destination card in ArrayList
    //that you are hovering over
    int destCardHover;
    //array of the trains that the player
    //is using to buy
    int[] playerUsesToBuy;
    //if the mode is spendingTrains
    boolean spendingTrains;
    //determines if you need to draw the track
    boolean drawTrack;
    //the polygons of the track hovering over
    ArrayList<Polygon> polygons;
    //default font
    Font defaultFont;
    //font for the cities
    Font cityFont;
    //Fontmetrics object for drawing cities
    //nicely
    FontMetrics fm;
    // current player
    Player curPlayer;
    //if you are buying a tech card
    //or a track route
    boolean buyTechCard, buyTrackPath;
    //pick up five cards from the deck
    ArrayList<EColor> fiveCardsToPickUp;
    //If a player clicked on a track
    Track trackClicked;
    //Current technology card to buy
    Technology curTech;
    //Current destination card being displayed
    Destination curDest;
    //Determines if a player moved or not
    boolean moved, boughtTechnology;
    //counts number of cards taken from deck
    int fromDeck=0;
    //counts number of cards taken from table
    int fromTable=0;
    //counts the number of tracks bought
    int tracksBought=0;
    //signal that it is the last turn
    boolean lastTurn;
    //cost banner
    Image costBanner, banner;
    //Ticket to Ride Font
    Font ticketFont;
    //Font for the directions button
    Font dirFont,pointFont;
    //Images for buttons
    Image back, switchUser, buy, selectButton;

    /**
     * Initializes all variables and reads in some 
     * photos for the game.
     */
    public void init()
    {
        try{
            // set bought technology to false
            boughtTechnology=false;
            //set number of cards taken
            //from the deck or from the table
            //to 0
            fromDeck=0;
            fromTable=0;
            //set players moved to false
            moved=false;
            //initialize the destCardsLayedout arraylist
            destCardsLayedOut=new ArrayList<Destination>();
            //initialize the five cards to pick up arraylist
            fiveCardsToPickUp = new ArrayList<EColor>();
            //set buytechcard and buytechpath to false
            //initially
            buyTechCard=false;
            buyTrackPath=false;
            //make a new arraylist for the
            //tracks polygons to draw
            polygons = new ArrayList<Polygon>();
            //set draw track to false since no
            //track highlites are done yet
            drawTrack=false;
            //make sure all cities get intialized
            cities = new Cities();
            //set hover city name to empty string
            cityName = "";
            //set destcard hovered  to -1
            //meaning none of them have been hovered over yet
            destCardHover=-1;
            //set display city text to false
            displayCity=false;
            //set first turn to true
            firstTurn=true;
            //set dest mode to true
            //since we are currently 
            destMode=true;
            //intialize these arraylists described earlier
            destCardsPicked=new ArrayList<Integer>();
            playersHand=new ArrayList<EColor>();
            buyingWithTrains = new ArrayList<EColor>();
            //intialize these arrays
            //purpose described earlier
            playerUsesToBuy= new int[9];
            trains = new int[9];
            //set size of the japplet
            setSize(new Dimension(1267,900) ); 
            //add mouse/mousemotion/mousewheel listeners
            addMouseListener(this);
            addMouseMotionListener(this);
            addMouseWheelListener(this);
            //set spending trains mode to false
            spendingTrains=false;
            //set numOfTrainsDrawn to 0
            numOfTrainsDrawn=0;
            // This is the image we draw on to avoid
            // flickering by drawing directly to the screen
            backScreen = createImage(1267,900);
            // Set the buffer graphics
            bg = backScreen.getGraphics();
            //set glocal mouse click/hover variables 
            //to 0
            mouseClickX=mouseClickY=0;
            mouseHoverX=mouseHoverY=0;
            //set initial wheel scroll up/down
            wheelScrollUp =wheelScrollDown=false;
            //set trainCards array
            trainCards = new Image[9];
            //intialize tracks
            Tracks t = new Tracks();
            //set default font
            defaultFont = this.getFont();
            //set different fonts
            ticketFont = Font.createFont(Font.TRUETYPE_FONT, 
                new File("font/ticketFont.ttf") ).deriveFont(15f);
            cityFont = Font.createFont(Font.TRUETYPE_FONT, 
                new File("font/cityFont.ttf") ).deriveFont(12f);
            pointFont = Font.createFont(Font.TRUETYPE_FONT, 
                new File("font/cityFont.ttf") ).deriveFont(10f);
            dirFont = Font.createFont(Font.TRUETYPE_FONT,
                new File("font/ticketFont.ttf") ).deriveFont(20f);
            //set fontmetrics
            fm = bg.getFontMetrics(cityFont);

            //Reads in all photos needed
            try{
                board = ImageIO.read(new
                    File("photo/map7.jpg" ));
                //ticket images
                ticket = ImageIO.read(new 
                    File("photo/PlayerTicket.png"));
                background=ImageIO.read(new 
                    File("photo/background.jpg"));
                destBack = ImageIO.read(new
                    File("photo/Dest/destBack.png"));
                //banner images
                costBanner = ImageIO.read(new 
                    File("photo/banner.png"));
                banner = ImageIO.read(new 
                    File("photo/directionsBanner.png"));
                //button images
                switchUser = ImageIO.read(new 
                    File("photo/switchUser.png"));
                back = ImageIO.read(new
                    File("photo/back.png"));
                buy =ImageIO.read(new
                    File("photo/buy.png"));
                selectButton =ImageIO.read(new
                    File("photo/selectButton.png"));

                //Reads in train cards images
                trainCards[3]=ImageIO.read( new 
                    File("photo/train/black.png"));
                trainCards[1]=ImageIO.read( new 
                    File("photo/train/blue.png"));
                trainCards[5]=ImageIO.read( new 
                    File("photo/train/green.png"));
                trainCards[6]=ImageIO.read( new 
                    File("photo/train/orange.png"));
                trainCards[7]=ImageIO.read( new 
                    File("photo/train/pink.png"));
                trainCards[4]=ImageIO.read( new 
                    File("photo/train/red.png"));
                trainCards[0]=ImageIO.read( new 
                    File("photo/train/white.png"));
                trainCards[2]=ImageIO.read( new 
                    File("photo/train/yellow.png"));
                trainCards[8]=ImageIO.read( new 
                    File("photo/train/locomotive.png"));
                trainBack = ImageIO.read( new 
                    File("photo/train/trainCard.png"));
                doneButtonTrack = ImageIO.read( new
                    File("photo/buyTrackDone.png"));
            }
            catch(Exception e){
                showStatus(e.toString() + "Read in photo error");
            }
        }
        catch(Exception e){
            showStatus(e.toString() 
                + "init - non photo related error");
        }
    }

    /**
     * This method asks for the number of players and then
     * for each player asks for their name and color
     * and makes sure none of the names or colors are the 
     * exact same. 
     */
    public void start(){
        Players p= new Players();
        curPlayer = Players.getPlayer();
    }

    /**
     *****************************************************************
     *****************************************************************
     *****************************************************************
     *****************************************************************
     **************** Mouse/Wheel Listeners **************************
     *****************************************************************
     *****************************************************************
     *****************************************************************
     *****************************************************************
     */

    /**
     * Gets the coordinates of a mouse click and 
     * saves if it was the left or right click
     * @param e - MouseEvent
     */
    public void mouseClicked(MouseEvent e){
        //get x and y clicks
        mouseClickX = e.getX();
        mouseClickY = e.getY();
        //set left or right clicks
        leftClick= SwingUtilities.isLeftMouseButton(e);
        rightClick = SwingUtilities.isRightMouseButton(e);

        //click on directions button
        if(mouseClickX <= 1085+175 && mouseClickX>=1085
        && mouseClickY <=738+44 && mouseClickY>= 738){
            DirectionsPanel d = new DirectionsPanel();
            DirectionsPanel.callPanel();
        }

        //update destCardHover on a click
        destCardHover=hoverDestCards();
        //if a track is not already selected
        if(trackClicked==null){
            //Only allow it to be clicked if the player 
            //can buy the track
            trackClicked = Tracks.trackClicked
            (new Point(mouseClickX,mouseClickY ));

            //Click through for right of way and
            //multiple people own track
            if(trackClicked!=null
            && trackClicked.playersBought.size()>1 ){
                Collections.rotate( trackClicked.playersBought,-1);
            }
            //if you can't buy it set it to null
            if(trackClicked==null ||
            !trackClicked.playerCanBuy(curPlayer)){
                trackClicked=null;
            }
        }

        //Player clicked on track to buy
        //only if it is not the first turn
        // and they have not moved
        if(!firstTurn && !spendingTrains 
        && trackClicked!=null){
            //Either you moved or you're allowed to buy
            // a second track if you have the thermocompressor   
            if(!moved 
            || (tracksBought==1 
                && curPlayer.hasThermocompressor())){
                spendingTrains=true;
                buyTrackPath=true;
            }
        }

        // Player wants to pick up a destination card because
        // they clicked the top destination card
        if(!firstTurn && !spendingTrains &&
        !moved && clickTopDestinationCard()){
            //if there are enought dest cards left
            if(Deck.destCardsLeft()){
                destMode=true;
                moved=true;
            }
        }

        //Player wants to click through their Destination cards
        if(!firstTurn 
        && clickBottomDestinationCard()
        && curPlayer!=null){
            if(rightClick){
                //get new destImage to show
                curPlayer.shiftDest(1);
            }
            else{
                //get new destImage to show
                curPlayer.shiftDest(-1);
            }       
            curDest = curPlayer.getCurDest(); 
        }
        //consume event and repaint
        e.consume();
        repaint();
    }

    /**
     * Repaints if the mouse reenters the applet
     * @param e - MouseEvent that calls the function
     */
    public void mouseEntered(MouseEvent e){
        e.consume();
        repaint();
    }

    /**
     * Mouse dragged overrided method that
     * does nothing except consume the MouseEvent
     * 
     * @param e - MouseEvent that calls the function
     */
    public void mouseDragged(MouseEvent e){
        e.consume();
    }

    /**
     * Saves the coordinates of where the mouse
     * is hovering
     * 
     * @param e - MouseEvent
     */
    public void mouseMoved(MouseEvent e){
        //gets the x and y coordinates of the
        //mouse hover
        mouseHoverX = e.getX();
        mouseHoverY = e.getY();      
        //consume the event
        e.consume();

        //deal with city hovering
        cityName = cities.cityHoveringOver(
            new Point(mouseHoverX,mouseHoverY));
        //if there was a city hover
        if(!cityName.equals("")){
            repaint();
            displayCity=true;
            return;
        }
        //if there no longer is a city hover
        else if(displayCity){
            displayCity=false;
            repaint();
        }

        //get track being hovered over
        Track hoverOver =
            Tracks.trackClicked(new Point(mouseHoverX,mouseHoverY ));
        //if you are hovering over at track
        //that you can buy then highlite the track
        if(hoverOver!=null && hoverOver.playerCanBuy(curPlayer)){
            drawTrack=true;
            polygons = new ArrayList<Polygon>();
            polygons = hoverOver.getGraphicsPoints();
            repaint();
        }
        //if you are no longer hovering over a track
        //then set drawTrack to false and repaint
        else if(drawTrack){
            drawTrack=false;
            repaint();
        }

        //Destination card hovering
        destCardHover=hoverDestCards();
        //if you are not currently hovering then hover
        if(destCardHover>=0){
            displaydest=true;
            repaint();
        }
        else if(displaydest){
            displaydest=false;
            repaint();
        }
    }

    /**
     * If the mouse is exited. Overrided method that only 
     * consumes the mouse event e
     * @param e - MouseEvent that triggers the call of this method
     */
    public void mouseExited(MouseEvent e){
        e.consume();
    }

    /**
     * If the mouse is pressed. Overrided method that 
     * only consumes the mouse event e
     * @param e - MouseEvent that triggers the call of this method
     */
    public void mousePressed(MouseEvent e){
        e.consume();
    }

    /**
     * If the mouse is released. Overrided method that only 
     * consumes the mouse event e
     * @param e - MouseEvent that triggers the call of this method
     */
    public void mouseReleased(MouseEvent e){
        e.consume();
    }

    /**
     * Determines if you scroll up or down
     * @param e - MouseWheelEvent
     */
    public void mouseWheelMoved(MouseWheelEvent e){
        //amount scrolled up or down by
        int up = e.getWheelRotation();
        //consume event
        e.consume();
        // Mouse wheel scrolled down
        if(up<0){
            wheelScrollUp=true;
            wheelScrollDown=false;
        }
        //Mouse wheel scrolled down
        if(up>0){
            wheelScrollUp=false;
            wheelScrollDown=true;
        }
        //repaint the picture
        repaint();
    }

    /**
     *****************************************************************
     *****************************************************************
     *****************************************************************
     *****************************************************************
     **************** Left Side Detection   **************************
     *****************************************************************
     *****************************************************************
     *****************************************************************
     *****************************************************************
     */

    /**
     * Signals if the player wants to buy destination
     * cards.
     * @return true if they clicked the destination card
     * or false if they didnt
     */
    public boolean clickTopDestinationCard(){
        //if it is within the coordinates of the top destination card
        if(mouseClickX>=48 && mouseClickX<= 198
        && mouseClickY>=15 && mouseClickY<=244){
            //if there is a card to be clicked play the sound effect
            //and return true
            if(Deck.destinationDeck.size()>0){
                play(getDocumentBase(),
                    "sounds/StartUp/card-flip.wav");
                return true;              
            }
        }
        return false;
    }

    /**
     * If you click the bottom destination card
     * then it returns true.
     * @return true if you click the destination 
     * card or false if you don't 
     */
    public boolean clickBottomDestinationCard(){        
        //if it is within the coordinates of the top destination card
        if(mouseClickX>=48 && mouseClickX<= 198
        && mouseClickY>=650 && mouseClickY<=879){
            //play song of clicking through destination cards
            play(getDocumentBase(),"sounds/StartUp/card-flip.wav");
            return true;
        }
        return false;
    }

    /**
     * Scrolls through the tech cards to buy
     * @return true if you are hovering 
     * over the tech cards, false otherwise
     */
    public boolean scrollTechCardsToBuy(){
        //if it is within the coordinates of hovering
        //technology cards to buy
        if(mouseHoverX>=9 && mouseHoverX<= 237
        && mouseHoverY>=275 && mouseHoverY<=425){
            return true;
        }
        return false;
    }

    /**
     * Scrolls through the players tech cards
     * @return true if you are hovering over
     * the player's technology cards and false otherwise
     */
    public boolean scrollPlayerTechCards(){
        //coordinates of scrolling player cards
        if(mouseHoverX>=9 && mouseHoverX<= 237
        && mouseHoverY>=475 && mouseHoverY<=625){
            return true;
        }
        return false;
    }

    /**
     *****************************************************************
     *****************************************************************
     *****************************************************************
     *****************************************************************
     ******* Board - City and Path Highlite **************************
     *****************************************************************
     *****************************************************************
     *****************************************************************
     *****************************************************************
     */

    /**
     * If you are hovering over a city then it draws the
     * city's name next to the city.
     */
    public void drawCityName(){
        //get city width from fontmetrics
        double cityWidth = fm.getStringBounds("  "+cityName+" ", 
                bg).getWidth();
        //get city height from fontmetrics
        double cityHeight = fm.getMaxAscent() + 
            fm.getMaxDescent();
        bg.setColor(new Color(169,169,169,200));
        bg.fillOval(mouseHoverX, mouseHoverY - 15, 
            (int)cityWidth + 5, (int)cityHeight + 12);
        bg.setColor(Color.white);
        bg.setFont(cityFont);
        bg.drawString("  "+cityName, mouseHoverX + 1, 
            mouseHoverY + 6);
        bg.setFont(defaultFont);
    }

    /**
     * Draws the 2 (or 3 if France) cities of the destination card
     * that the player is hovering over
     */
    public void drawCityHighlite(){
        //Get Destination card :0
        String city1="";
        String city2="";
        Destination d=null;
        //Player's top current destination card
        if( destCardHover==5){
            city1 = curDest.getCity1();
            city2 = curDest.getCity2();
            d = curDest;
        }
        //One of the other destination cards in question
        if(destCardHover>=0 
        && destCardHover<destCardsLayedOut.size()){
            d = destCardsLayedOut.get(destCardHover);
            city1 = d.getCity1();
            city2 = d.getCity2();
        } 

        //Either green or yellow if the path is complete
        if(city1.length()>0 && city2.length()>0 
        && d!=null && d.playerComplete()){
            //Set Green for complete
            bg.setColor(new Color(152,251,152,150));
        }
        else{
            //Set yellow
            bg.setColor(new Color(255,255,102,150));
        }

        //Draws the city
        if(city1.length()>0)
            drawCity(city1);
        if(city2.length()>0)
            drawCity(city2);
    }

    /**
     * Draws the city's name
     * @param name - name of city to draw
     */
    public void drawCity(String name){
        if(name.equals("France")){
            //Get city point
            Point p = Cities.listOfCities.get(
                    Cities.listOfCities.size()-2).boardLocation;
            int x =(int) p.getX()-8+245;
            int y = (int) p.getY()-8;
            bg.fillOval(x,y,16,16);
            //Get city point
            p = Cities.listOfCities.get(
                Cities.listOfCities.size()-1).boardLocation;
            x =(int) p.getX()-8+245;
            y = (int) p.getY()-8;
            bg.fillOval(x,y,16,16);            
        }
        else{
            //Get city point
            Point p = Cities.getPoint(name);
            int x =(int) p.getX()-11+245;
            int y = (int) p.getY()-11;
            bg.fillOval(x,y,22,22);
        }    
    }

    /**
     * Draws on the highlite for when you
     * are drawing a track
     */
    public void drawTrackHighlite(){
        //Set to semi-transparent yellow
        bg.setColor(new Color(255,255,102,150));
        //Goes through each polygon for each track
        for(Polygon p : polygons){
            bg.fillPolygon(p);
        }
    }

    /**
     * Draws the Points Around the board
     */
    public void drawPoints(){
        //goes through all players
        for(int i=0; i<Players.getNumPlayers(); i++){
            Player p = Players.getPlayerAt(i);
            int points=p.getNumOfPoints();
            while(points>=100){
                points-=100;
            }
            //draw players paint dot
            bg.setColor(p.getColor());
            //math based on location of number
            if(points>=0 && points<=30){
                points = 30-points;
                bg.fillOval(245+10,11 +(points*29) ,10,10 );
            }
            else if( points<50){
                points= points-30;
                bg.fillOval(245+15 + (points*28)  ,11,10,10);
            }
            else if(points<=80){
                points = points-50;
                bg.fillOval(825, 11 + (points*29) ,10,10);
            }
            else{            
                points = 100-points;
                bg.fillOval(245+15+(points*28) ,881,10,10);
            }
        }
    }
    /**
     *****************************************************************
     *****************************************************************
     *****************************************************************
     *******      Draw Right Middle Left    **************************
     *****************************************************************
     *****************************************************************
     *****************************************************************
     *****************************************************************
     */

    /**
     * Draws the board onto the buffer
     */
    public void drawBoard(){
        //draws the background then the baord
        bg.drawImage(background,0,0,this);
        bg.drawImage(board, 245,0,this );
        //draws the players points
        drawPoints();
        // Draw Players Tracks
        Tracks.drawPlayerTracks(bg);
        // Track Highlite
        if(drawTrack){
            drawTrackHighlite();
        }        
        //Highlite City
        if(destCardHover>=0){
            drawCityHighlite();
            destCardHover=-1;
        }
        //City Name appears
        if(cityName.length()>0){
            drawCityName();
            cityName="";
        }
    }

    /**
     * Draws the stuff to the left side of the board
     */
    public void drawLeftSide(){
        //When you are hovering or click over Tech cards to buy
        if(scrollTechCardsToBuy() && curPlayer!=null ){
            if(wheelScrollUp && !spendingTrains){
                //Scroll one way through
                Players.shiftTech(1);
            }
            else if(wheelScrollDown && !spendingTrains){
                //Scroll other way through
                Players.shiftTech(-1);
            }
            //if you have not moved and not 
            //bought technology cards then you can
            //buy a technology card
            //and if you are not currently buying a track
            else if(!moved 
            && !spendingTrains && !boughtTechnology 
            && !destMode && leftClick){
                //Click to buy and enter buying mode
                buyTechCard=true;
                spendingTrains=true;
                play(getDocumentBase(),
                    "sounds/StartUp/card-flip.wav");
                repaint();
            }
            //scrolling through tech cards to buy to make sound
            //to make the sound
            if(!spendingTrains 
            && Players.allTechCards.size()>1
            && (wheelScrollUp || wheelScrollDown)){
                play(getDocumentBase(),
                    "sounds/StartUp/card-flip.wav");
            }
        }

        //When you are hovering and scroll over your own tech cards
        if(scrollPlayerTechCards()&& curPlayer!=null){
            if(wheelScrollUp){
                // scroll one way through
                curPlayer.shiftTech(-1);
            }
            else{
                // scroll other way through
                curPlayer.shiftTech(1);
            }
            //make sound if the player is scrolling
            //through their own tech cards
            if(curPlayer!=null &&
            curPlayer.techCardsOwned.size()>1){
                play(getDocumentBase(),
                    "sounds/StartUp/card-flip.wav");
            }
        }

        //draw player separation banner
        if(curPlayer!=null){
            bg.drawImage(banner,32,425, this);
            bg.setFont(dirFont.deriveFont(35) );
            bg.setColor(new Color(194,59,37));
            bg.drawString("YOUR CARDS", 60, 455);
            bg.setFont(new Font("Times New Roman", Font.BOLD, 15));
        }

        // Top Destination Card
        // Want back side
        if(curPlayer!=null 
        && Deck.destinationDeck.size()>0){
            bg.drawImage(destBack, 48,15, this );
        }

        // Player's current destination card on top
        if(curPlayer!=null && !firstTurn){
            curDest = curPlayer.getCurDest();
            dest = curDest.getImage();
            bg.drawImage(dest,48,650, this);
        }

        //display the current players tech cards
        if(curPlayer!=null){
            curTech = Players.getCurrentTech();
            if(curTech!=null){
                // All Technology cards to shuffle through
                bg.drawImage(curTech.getImage(),9, 275, this);
            }
        }

        //display current players tech cards
        if(curPlayer!=null){
            Image playerTech = curPlayer.getCurTechCard();
            if(playerTech!=null){
                // Player's technology cards
                bg.drawImage(playerTech,9, 475, this);
            }
        }
    }

    /**
     * Draws stuff to the right side of the board
     */
    public void drawRightSide(){
        try{
            //Draws the ticket along with the player name and
            //number of trains left
            if(curPlayer!=null){
                //draw players color in a round box
                bg.setColor(curPlayer.getColor());
                bg.fillRoundRect(1085,790,175,99,15,15);
                //draws ticket
                bg.drawImage(ticket,1085,790, this);
                //draws players name
                bg.setColor(Color.BLACK);
                bg.setFont(new Font("Times New Roman", 
                        Font.BOLD, 15));
                //centers the players name
                double strWidth = bg.getFontMetrics().stringWidth(
                        curPlayer.getName());
                //in case players name is more than 13 characters
                if(curPlayer.getName().length() > 13){
                    bg.drawString(curPlayer.getName().substring(0,13),
                        1100, 815);
                }
                else{
                    bg.drawString(curPlayer.getName(), 1100 + 72 -
                        ((int)strWidth / 2 ), 815);
                }
                //draws players trains
                bg.drawString(String.valueOf(
                        curPlayer.getNumOfTrains()), 1205, 860);
                //draws the number of points the player has
                bg.drawString("Points: " +curPlayer.getNumOfPoints()
                ,1143 ,832 );
            }

            //if this is the first turn
            if(firstTurn){
                //draw 5 destination cards
                if(curPlayer!=null){
                    draw5DestCards();
                    draw5DestCardsPicked();
                }
            }
            //if this is the destination mode for 3 cards
            else if(destMode){
                draw3DestCards();
                draw3DestCardsPicked();
            }
            //if you are in the spending trains mode
            else if(spendingTrains){
                //draw banner
                bg.drawImage(costBanner,854,5,this);
                //Draw number of locomotives to buy
                bg.setFont(ticketFont.deriveFont(12) );
                bg.setColor(new Color(194,59,37));
                if(buyTechCard){
                    //Tech card cost
                    String loco = curTech.getCost() == 1 ? 
                            " Locomotive" : " Locomotives";
                    String cost = "Cost: " + curTech.getCost() + loco;
                    double strWidth = 
                        bg.getFontMetrics().stringWidth(cost);
                    bg.drawString(cost, 895 + 158 - 
                        ((int)strWidth / 2 ), 36);
                }
                else{
                    //buying a track
                    String cost = trackClicked.getCost();
                    double strWidth = bg.getFontMetrics().
                        stringWidth(cost);
                    bg.drawString(cost, 895 + 158 - 
                        ((int)strWidth / 2 ), 36);
                }
                //set font back
                bg.setFont(new Font("Times New Roman", 
                        Font.BOLD, 15));

                //Draw spending trains cards
                spendingTrainsCardClicked();
                // If you moved the correct amount of cards 
                //then add a buy button
                // Once they click the buy button then set 
                //spendingTrains to false
                if(buyTechCard){
                    //Check if you have enough cards to buy tech card
                    if(curTech.canBuy(curPlayer,playerUsesToBuy)){
                        //If you have enough cards then the done 
                        //button
                        //appears
                        drawDoneButton();
                        // if you click the done button switch out 
                        //of buy mode
                        if(clickedDoneButton()){
                            //set buyTechCard and spendingTrains 
                            //back to 
                            //false
                            buyTechCard=false;
                            spendingTrains=false;
                            //add trains back to deck that were spent
                            Deck.addBackTrainCards(playerUsesToBuy);
                            //reset array player is using to buy cards
                            playerUsesToBuy = new int[9];
                            //add current tech to players hand
                            curPlayer.addTechCard(curTech.getETech());
                            //add player to that tech card
                            Players.addPlayerToTech(
                                curTech.getETech());
                            //set chosen tech card to null
                            curTech=null;

                            Players.updateCardsToBuy();
                            boughtTechnology=true;
                            repaint();
                        }
                    }
                }
                //if you are in the buying a track path 
                //mode
                else if (buyTrackPath){
                    //Check if the player has enough cards 
                    //to buy
                    if(trackClicked.
                    playerHasEnoughCardsToBuy(curPlayer, 
                        playerUsesToBuy) ){
                        //If you have enough cards then 
                        //the done button 
                        //appears
                        drawDoneButton();
                        // if you click the done button 
                        //switch out of buy
                        //mode
                        if(clickedDoneButton()){
                            //set phases back to false
                            buyTrackPath=false;
                            spendingTrains=false;
                            //add cards spent back to deck
                            Deck.addBackTrainCards(playerUsesToBuy);
                            //reset player array for cards used to buy
                            playerUsesToBuy = new int[9];
                            //add player to owning track clicked
                            trackClicked.addPlayer(
                                curPlayer.getColor());
                            //increase number of points 
                            //to the player 
                            int point = trackClicked.
                                getValue(curPlayer);
                            //show amount of points earned
                            showStatus(point+ " " +
                                "points earned for track");
                            //increment number of players points
                            curPlayer.incNumOfPoints(point);
                            //Decrease number of trains used by the
                            //curPlayer Signals last turn here
                            lastTurn = curPlayer.decreaseNumOfTrains(
                                trackClicked.getLength());
                            //increment tracks bought by 1
                            tracksBought++;
                            //track clicked is null
                            trackClicked=null;
                            //buying a track is your turn
                            moved=true;
                            //reset mouse globals
                            resetMouseGlobals();
                            //repaint
                            repaint();
                        }
                    }
                }
                //Add a back button
                bg.drawImage(back, 850,790,this);
                //if you click back button
                if(clickedBackButton()){
                    buyTrackPath=false;
                    buyTechCard=false;
                    spendingTrains=false;
                    //Add cards back to hand
                    curPlayer.addTrainCards(playerUsesToBuy);
                    //Reset cards used to buy
                    playerUsesToBuy = new int[9];
                    trackClicked=null;
                    repaint();
                }
                //Draw the trains they are using to 
                //purchase the track
                //or technology card
                drawTrainsUsedToBuy();            
            }
            else{
                //if current player is null then 
                //return and do not draw                
                if(curPlayer==null) return;
                //Get Deck then Draw Deck
                while(fiveCardsToPickUp.size()<5 && 
                Deck.trainCardDeck.size()>0){
                    fiveCardsToPickUp.add(Deck.drawTrainCard());
                }
                //Draw 5 train cards
                //Check to make sure there is a card to 
                //draw first!
                //In case all of the players magically 
                //pick up all of the cards
                switch(fiveCardsToPickUp.size()){
                    case 5:
                    bg.drawImage(trainCards[EColor.
                            getIndex(fiveCardsToPickUp.get(4))],
                        854,265, this);
                    case 4:
                    bg.drawImage(trainCards[EColor.
                            getIndex(fiveCardsToPickUp.get(3))], 
                        1060,135,this);
                    case 3:
                    bg.drawImage(trainCards[EColor.
                            getIndex(fiveCardsToPickUp.get(2))],
                        854,135, this);
                    case 2:
                    bg.drawImage(trainCards[EColor.
                            getIndex(fiveCardsToPickUp.get(1))], 1060,5,this);
                    case 1:
                    bg.drawImage(trainCards[EColor.
                            getIndex(fiveCardsToPickUp.get(0))],854,5, this);
                }
                //Back of deck
                if(Deck.trainCardDeck.size()>0){
                    bg.drawImage(trainBack, 1060,265,this);
                }
                //If you clicked on one of the train cards
                clickTrainCards();
            }
            //Always draw the players hand
            drawPlayerHand();
            //draw directions button
            if(curPlayer!=null){
                //draws a banner
                bg.drawImage(banner,1085,738, this);
                //Sets the direction font
                bg.setFont(dirFont.deriveFont(35));
                bg.setColor(new Color(194,59,37));
                //draws the string for the directions button
                bg.drawString("DIRECTIONS", 1113, 768);
                //resets the font back to default
                bg.setFont(new Font("Times New Roman", 
                        Font.BOLD, 15));
            }
        }
        catch(Exception e){
            showStatus(e.toString() + " right side");
        }
    }

    /**
     *****************************************************************
     *****************************************************************
     *****************************************************************
     *******              Done Button       **************************
     *****************************************************************
     *****************************************************************
     *****************************************************************
     *****************************************************************
     */

    /**
     * Determines if you clicked the back button.
     * @return true if you clicked the done button, false otherwise
     */
    public boolean clickedBackButton(){
        if(mouseClickX>=850 && mouseClickX<=950
        && mouseClickY>=790 && mouseClickY<=890){
            return true;
        }
        return false;
    }

    /**
     * Determines if you clicked the done button and makes a 
     * sound if you did depending on the phase of the game
     * @return true if you clicked the done button, false otherwise
     */
    public boolean clickedDoneButton(){
        if(mouseClickX>=960 && mouseClickX<=1060
        && mouseClickY>=790 && mouseClickY<=890){
            if(spendingTrains || destMode){
                if(!buyTrackPath)
                    play(getDocumentBase(),
                        "sounds/StartUp/card-flip.wav");
                else
                    play(getDocumentBase(),
                        "sounds/StartUp/Train.wav");
            }            
            return true;
        }
        return false;
    }

    /**
     * Draws the done button based off which phase of 
     * game play you are in 
     */
    public void drawDoneButton(){
        //if no dest cards are picked, don't draw button
        if(destMode && destCardsPicked.size()==0) return;
        //goes through all possible phases
        if(buyTrackPath){
            bg.drawImage(doneButtonTrack, 960,790,this);
        }
        else if( destMode){
            bg.drawImage(selectButton, 960,790,this);
        }
        else if(spendingTrains){
            bg.drawImage(buy, 960,790,this);
        }
        else{
            bg.drawImage(switchUser, 960,790,this);
        }
    }

    /**
     *****************************************************************
     *****************************************************************
     *****************************************************************
     *******      Picking Destination Cards **************************
     *****************************************************************
     *****************************************************************
     *****************************************************************
     *****************************************************************
     */

    /**
     * Picking 3 destination cards mode
     */
    public void draw3DestCardsPicked(){
        try{
            int destCard = click3DestCards();
            //Add to list to draw
            if(destCard>=0 && !destCardsPicked.contains(destCard)){
                destCardsPicked.add(destCard);
            }
            // Remove from selected list
            else if(destCard>=0 && destCardsPicked.
            contains(destCard)){
                destCardsPicked.remove(destCardsPicked.
                    indexOf(destCard));
            }
            //if there are cards to draw
            if(destCardsPicked.size()>0){
                for(int i : destCardsPicked){
                    draw3DestCardChoosen(i);
                }
            }
            //if you picked more than 1 dest card
            if(destCardsPicked.size()>0){
                //draw done button
                drawDoneButton();
                //if you clicked done button
                if(clickedDoneButton()){
                    //exit destination mode
                    destMode=false;
                    //loop through 3 des cards
                    for(int i=0; i<3; i++){
                        if(destCardsPicked.contains(i)){
                            //Add destCardsPicked to 
                            //Player's dest cards
                            curPlayer.addDesCard(
                                destCardsLayedOut.get(i));
                        }
                        else{
                            //send other dest cards back to deck
                            if(destCardsLayedOut.size()>i){
                                Deck.discardDest(
                                    destCardsLayedOut.get(i));
                            }
                        }
                    }
                    //clear dest cards picked and dest cards layed out
                    destCardsPicked.clear();
                    destCardsLayedOut.clear();
                    //repaint
                    repaint();
                }
            }
        }
        catch(Exception e){
            showStatus("draw3destCardsPickin"+ e.toString());
        }
    }

    /**
     * Drawing 5 destination cards picked
     */
    public void draw5DestCardsPicked(){
        int destCard = click5DestCards();
        //Add to list to draw
        if(destCard>=0 && !destCardsPicked.contains(destCard)){
            destCardsPicked.add(destCard);
        }
        // Remove from selected list
        else if(destCard>=0 && destCardsPicked.contains(destCard)){
            destCardsPicked.remove(destCardsPicked.indexOf(destCard));
        }
        //go through and draw cards chosen
        if(destCardsPicked.size()>0){
            for(int i : destCardsPicked){
                draw5DestCard5Choosen(i);
            }
        }
        //if you selected more than 3 cards
        if(destCardsPicked.size()>=3){
            //draw done button
            drawDoneButton();
            //if you clicked the done button
            if(clickedDoneButton()){
                //go through and add cards to your hand or
                //back to deck
                for(int i=0; i<5; i++){
                    if(destCardsPicked.contains(i)){
                        //Add destCardsPicked to Player's dest cards
                        curPlayer.addDesCard(
                            destCardsLayedOut.get(i));
                    }
                    else{
                        //send other dest cards back to deck
                        Deck.discardDest(destCardsLayedOut.get(i));
                    }
                }
                //clear destcards picked and destcardslayed out
                destCardsPicked.clear();
                destCardsLayedOut.clear();
                //  Switch players
                curPlayer = Players.getNextPlayer();
                //if the new curPlayer has dest cards then
                if(curPlayer.destCards.size()>0){
                    destMode=false;
                    firstTurn=false;
                }
                repaint();
            }
        }
    }

    /**
     * Draws 5 destination cards for the first round
     */
    public void draw5DestCards(){
        try{
            //add cards to the arraylist to lay out
            while( destCardsLayedOut.size()<5){
                destCardsLayedOut.
                add(Deck.drawDest());
            }
            // Get 5 cards to draw
            // Draw in order 0 1 2 3 4
            bg.drawImage(destCardsLayedOut.
                get(0).getImage(),830,5 , this);
            bg.drawImage(destCardsLayedOut.
                get(1).getImage(),972,5 , this);
            bg.drawImage(destCardsLayedOut.
                get(2).getImage(),1117, 5, this);
            bg.drawImage(destCardsLayedOut.
                get(3).getImage(),900 ,240 , this);
            bg.drawImage(destCardsLayedOut.
                get(4).getImage(), 1050,240 , this);
        }
        catch(Exception e){
            showStatus("draw5destcards " + e.toString());
        }
    }

    /**
     * Returns order of cards top row:
     * 0 1 2
     * bottom row:
     *  3 4
     *  from left to right.
     *  @return int number of card clicked or
     *  -1 if you did not click a card
     */
    public int click5DestCards(){
        if(mouseClickY>=5 && mouseClickY<=234){
            if(mouseClickX>= 830 && mouseClickX<=972){
                return 0;
            }
            if(mouseClickX> 972 && mouseClickX<=1117){
                return 1;
            }
            if(mouseClickX>1117 && mouseClickX<=1346){
                return 2;
            }
        }
        if(mouseClickY>=240 && mouseClickY<=469){
            if(mouseClickX>= 900&& mouseClickX<=1050){
                return 3;
            }
            if(mouseClickX>1050 && mouseClickX<=1200){
                return 4;
            }
        }
        return -1;
    }

    /**
     * Hovers and returns the number of destination
     * card you are hovering over
     * Order for first turn:
     * 0 1 2 
     * 3 4
     * Order for regular turn:
     * 0 1 2
     * If you are hovering over your
     * own stack of dest cards then 5
     * @return the number of the dest card as 
     * illustrated above
     */
    public int hoverDestCards(){
        //if first turn
        if(firstTurn){
            if(mouseHoverY>=5 && mouseHoverY<=234){
                if(mouseHoverX>= 830 && mouseHoverX<=972){
                    return 0;
                }
                if(mouseHoverX> 972 && mouseHoverX<=1117){
                    return 1;
                }
                if(mouseHoverX>1117 && mouseHoverX<=1346){
                    return 2;
                }
            }
            if(mouseHoverY>=240 && mouseHoverY<=469){
                if(mouseHoverX>= 900&& mouseHoverX<=1050){
                    return 3;
                }
                if(mouseHoverX>1050 && mouseHoverX<=1200){
                    return 4;
                }
            }
        }
        //if dest mode
        else { 
            if(destMode){
                if(mouseHoverY>=75 && mouseHoverY<=304){
                    if(mouseHoverX>= 830 && mouseHoverX<=972
                    && destCardsLayedOut.size()>0){
                        return 0;
                    }
                    if(mouseHoverX> 972 && mouseHoverX<=1117
                    && destCardsLayedOut.size()>1){
                        return 1;
                    }
                    if(mouseHoverX>1117 && mouseHoverX<=1346
                    && destCardsLayedOut.size()>2){
                        return 2;
                    }
                }
            }
            if((mouseHoverX>=48 && mouseHoverX<= 198
                && mouseHoverY>=650 && mouseHoverY<=879) || (
                mouseClickX>=48 && mouseClickX<= 198
                && mouseClickY>=650 && mouseClickY<=879
            )){
                return 5;
            }
        }
        return -1;
    }

    /**
     *  Draws the destination card selected based on the card
     *  @param i - number of card chosen
     */
    public void draw5DestCard5Choosen(int i){
        //set color to light gray
        bg.setColor(new Color(169,169,169,100 ));
        //based on number draw round rect
        switch(i){
            case 0:
            bg.fillRoundRect(830,5,150,229,5,5);
            break;
            case 1:
            bg.fillRoundRect(972,5,150,229,5,5);
            break;            
            case 2:
            bg.fillRoundRect(1117,5,150,229,5,5);
            break;            
            case 3:
            bg.fillRoundRect(900,240,150,229,5,5);
            break;            
            case 4:
            bg.fillRoundRect(1050,240,150,229,5,5);
            break;
        }
    }

    /**
     * Draws 3 destination cards or fewer based on size of destination
     * card deck.
     */
    public void draw3DestCards(){
        //adds cards to draw
        while( destCardsLayedOut.size()<3){
            Destination d = Deck.drawDest();
            if(d!=null){
                destCardsLayedOut.add(d);
            }
            else{
                break;
            }
        }
        //draws based on number of cards
        switch(destCardsLayedOut.size()){
            case 3:
            bg.drawImage(destCardsLayedOut.get(2).
                getImage(),
                1117,75 , this);
            case 2:
            bg.drawImage(destCardsLayedOut.get(1).
                getImage(),972,75 , this);
            case 1:
            bg.drawImage(destCardsLayedOut.get(0).
                getImage(),830 ,75, this);
        }
    }

    /**
     * Returns order of card clicked based off of:
     * 0 1 2 from right to left
     * -1 if no card was clicked
     * @return int of card number clicked
     */
    public int click3DestCards(){
        //areas of cards
        if(mouseClickY>=75 && mouseClickY<=304){
            if(mouseClickX>= 830 && mouseClickX<=972 
            && destCardsLayedOut.size()>0){
                return 0;
            }
            if(mouseClickX> 972 && mouseClickX<=1117
            && destCardsLayedOut.size()>1){
                return 1;
            }
            if(mouseClickX>1117 && mouseClickX<=1346
            && destCardsLayedOut.size()>2){
                return 2;
            }
        }
        return -1;
    }

    /**
     *  Draws the destination card selected based on the card number
     *  0 1 2 from left to right.
     *  @param i - number of card chosen
     */
    public void draw3DestCardChoosen(int i){
        //set color to light gray
        bg.setColor(new Color(169,169,169,100 ));
        //switch for coordinates to draw
        switch(i){
            case 0:
            bg.fillRoundRect(830,75,150,229,5,5);
            break;
            case 1:
            bg.fillRoundRect(972,75,150,229,5,5);
            break;            
            case 2:
            bg.fillRoundRect(1117,75,150,229,5,5);
            break;            
        }
    }

    /**
     *****************************************************************
     *****************************************************************
     *****************************************************************
     *******      Buying with Trains        **************************
     *****************************************************************
     *****************************************************************
     *****************************************************************
     *****************************************************************
     */

    /**
     * Draws the cards you want to use to buy a train
     * and 
     */
    public void spendingTrainsCardClicked(){
        //Set number of trains to draw
        setNumOfTrainsDrawn(trains);
        // get the card clicked on the board
        int cardClickedFromHand= playerCardClicked(0);
        // if there was a card clicked on the board
        if(cardClickedFromHand>=0 ){
            //Get the color of the card picked
            EColor cardColor = playersHand.get(cardClickedFromHand);  
            //Decrease 1 from trains[] increase 1 to playerUsesToBuy
            int index = EColor.getIndex(cardColor);
            if(index!=-1){
                //Decrease that card from the player
                curPlayer.getTrainCard(EColor.getColor(index));
                //Increase that card from the player
                //uses to buy hand
                playerUsesToBuy[index]++;
                //repaint the board
                repaint();
            }
        }            
        //Set the number of trains the player uses to buy
        setNumOfTrainsDrawn(playerUsesToBuy);
        //get the train the player clicked on
        int cardClickedFromPay = playerCardClicked(400);
        //if the player clicked a card that they wanted to use to buy
        if( cardClickedFromPay>=0){                
            //Get the color of the card clicked
            EColor cardColor = buyingWithTrains.
                get(cardClickedFromPay);  
            //Decrease 1 from trains[] increase 1 to playerUsesToBuy
            int index = EColor.getIndex(cardColor);
            if(index!=-1){
                //Increase that card back to the player
                curPlayer.addTrainCard(EColor.getColor(index));
                //Decrease that card from the player
                //uses to buy
                playerUsesToBuy[index]--;
                //repaint the board
                repaint();
            }
        }
    }

    /**
     * Shows the cards using to purchase
     */
    public void drawTrainsUsedToBuy(){
        //New playersHand each Turn
        buyingWithTrains.clear();
        //x and y coordinates of all 7 possibilities
        int[] xCoord = {854,1060};
        int[] yCoord = {85,125,165,205,245};
        //train image to draw
        Image cur;
        //count of trains drawn
        numOfTrainsDrawn=0;
        //go through all train colors
        for(int i=0; i<9; i++){
            if(playerUsesToBuy[i]>0){
                //get train card image
                cur = trainCards[i];
                //get x and y values
                int x = numOfTrainsDrawn%2;
                int y = numOfTrainsDrawn/2;
                //draw train
                bg.drawImage(cur,xCoord[x],yCoord[y],this);
                //draw the number of cards the player has
                drawNumberOfTrains(playerUsesToBuy[i],
                    xCoord[x],yCoord[y]);
                //increment number of cards drawn
                numOfTrainsDrawn++;
                //update arraylist
                buyingWithTrains.add(EColor.getColor(i));
            }
        }
    }

    /**
     * Set number of trains drawn based on the number
     * of trains in the method
     * @param trains-int array of number of cards for each color
     * where index corresponds to color listed in EColor.
     */
    public void setNumOfTrainsDrawn(int[] trains){
        //set num of trains to 0
        numOfTrainsDrawn=0;
        for(int i: trains){
            //count number of cards drawn based on array
            if(i>0){
                numOfTrainsDrawn++;
            }
        }
    }

    /**
     *****************************************************************
     *****************************************************************
     *****************************************************************
     *******     Player's train cards       **************************
     *****************************************************************
     *****************************************************************
     *****************************************************************
     *****************************************************************
     */

    /**
     * Returns the index of the card the player clicked
     * or -1 if a player did not click the card in order:
     * 0 1
     * 2 3
     * 4 5
     * 6 7
     * 8 
     * @param yOffset - offset based on if it is the players hand
     * or the cards on top indicating the cards being used to buy
     * @return int corresponding to card as shown above
     */
    public int playerCardClicked(int yOffset){
        //left side
        if(mouseClickX>=854 && mouseClickX<=1053){
            if( numOfTrainsDrawn>0 
            && (mouseClickY>=485-yOffset &&
                (mouseClickY<=525-yOffset || 
                    ( numOfTrainsDrawn<=2 && 
                        mouseClickY<=485+125-yOffset)))){
                return 0;
            }
            if( numOfTrainsDrawn>2
            && (mouseClickY>525-yOffset &&
                (mouseClickY<=565-yOffset || 
                    ( numOfTrainsDrawn<=4 && 
                        mouseClickY<=525+125-yOffset)))){
                return 2;
            }
            if( numOfTrainsDrawn>4
            && (mouseClickY>565-yOffset &&
                (mouseClickY<=605-yOffset || 
                    ( numOfTrainsDrawn<=6 && 
                        mouseClickY<=565+125-yOffset)))){
                return 4;
            }
            if( numOfTrainsDrawn>6
            && (mouseClickY>605-yOffset &&
                (mouseClickY<=645-yOffset || 
                    ( numOfTrainsDrawn<=8 && 
                        mouseClickY<=605+125-yOffset)))){
                return 6;
            }
            if( numOfTrainsDrawn>8
            && (mouseClickY>645-yOffset &&
                (mouseClickY<=770-yOffset))){
                return 8;
            }
        }
        // On the Right Side
        if(mouseClickX>=1060 && mouseClickX<=1257){
            if( numOfTrainsDrawn>0 
            && (mouseClickY>=485-yOffset &&
                (mouseClickY<=525-yOffset || 
                    ( numOfTrainsDrawn<=3 && 
                        mouseClickY<=485+125-yOffset)))){
                return 1;
            }
            if( numOfTrainsDrawn>2
            && (mouseClickY>525-yOffset &&
                (mouseClickY<=565-yOffset || 
                    ( numOfTrainsDrawn<=5 && 
                        mouseClickY<=525+125-yOffset)))){
                return 3;
            }
            if( numOfTrainsDrawn>4
            && (mouseClickY>565-yOffset &&
                (mouseClickY<=605-yOffset || 
                    ( numOfTrainsDrawn<=7 && 
                        mouseClickY<=565+125-yOffset)))){
                return 5;
            }
            if(mouseClickY>605-yOffset && mouseClickY<=730-yOffset ){
                return 7;
            }
        }        
        return -1;    
    }

    /**
     * Draws the players hand based on the number of cards
     * of each color they have.
     */
    public void drawPlayerHand(){
        //New PlayersHand each Turn
        playersHand.clear();
        //if there hasn't been an established player
        //return null
        if(curPlayer==null) return;
        // Get Number of each Color Here from Player Class
        trains = curPlayer.getColorArray();
        // Order: WHITE, BLUE, YELLOW, BLACK, RED, 
        //GREEN, ORANGE, PINK, LOCOMOTIVE
        //x and y coordinates of image placement
        int[] xCoord = {854,1060};
        int[] yCoord = {485,525,565,605,645};
        //current image to draw
        Image cur;
        //set numOfTrainsDrawn to 0
        numOfTrainsDrawn=0;
        //go through all 9 trains
        for(int i=0; i<9; i++){
            if(trains[i]>0){
                //get current image
                cur = trainCards[i];
                //get x and y coordinates
                int x = numOfTrainsDrawn%2;
                int y = numOfTrainsDrawn/2;
                //draw iamge
                bg.drawImage(cur,xCoord[x],yCoord[y],this);
                //draw circle with number on it
                drawNumberOfTrains(trains[i],xCoord[x],yCoord[y]);
                //increment number of trains drawn
                numOfTrainsDrawn++;
                //add to players hand card drawn
                playersHand.add(EColor.getColor(i));
            }
        }
    }

    /**
     * Draws the number of trains you have on the card
     * 
     * @param num - number of the type of train
     * @param x - x coordinate to draw oval
     * @param y - y coordinate to draw oval 
     */
    public void drawNumberOfTrains(int num, int x, int y){
        // 40 between cards... upper right
        //Don't want to draw a number if it is 4
        if(num==1)return;
        //set color to black
        bg.setColor(Color.black);
        //Oval size based on number and draw it
        if(num<10){
            bg.fillOval(x+5,y+5,15,15);
        }
        else{
            bg.fillOval(x+5,y+5,23,15);
        }
        //draw number in white
        bg.setColor(Color.white);
        bg.drawString(num+"",x+10,y+17 );

    }    

    /**
     *****************************************************************
     *****************************************************************
     *****************************************************************
     *******     Train Cards Pile to Draw   **************************
     *****************************************************************
     *****************************************************************
     *****************************************************************
     *****************************************************************
     */

    /**
     * Returns 0 through 5 depending on 
     * which card you clicked. labeled as:
     * top row: 0 1
     * middle row: 2 3
     * bottom row: 4 5
     * or -1 if you did not click any of the cards
     * @return int - card chosen based from label above
     * or -1 if you did not click a card
     */
    public int chooseTrainFromDeck(){
        if(mouseClickX>=854 && mouseClickX<=1044){
            if(mouseClickY>=5 && mouseClickY<= 125){
                return 0;
            }
            if(mouseClickY>=135 && mouseClickY<=255){
                return 2;
            }
            if(mouseClickY>=265 && mouseClickY<=385){
                return 4;
            }
        }
        if(mouseClickX>=1060 && mouseClickX<=1250){
            if(mouseClickY>=5 && mouseClickY<= 125){
                return 1;
            }
            if(mouseClickY>=135 && mouseClickY<=255){
                return 3;
            }
            if(mouseClickY>=265 && mouseClickY<=385){
                return 5;
            }
        }        
        return -1;
    }

    /**
     * When you click Train Cards to purhcase, it determines which 
     * card you click.
     */
    public void clickTrainCards(){
        //If you clicked on a train card or on the deck
        int trainIndex=chooseTrainFromDeck();
        //If you already picked 2 up from the table
        if(fromTable>=2)return;
        //if you picked 1 up  from the table 
        //and 1 up from the deck
        if(fromTable>0 && fromTable+fromDeck==2)return;

        //if you made another move, return
        if(fromTable==0 && fromDeck==0 && moved)return;

        //if you have picked up 2 from the deck
        //and you do not have watertenders
        if(!curPlayer.hasWaterTenders()){
            if(fromTable+fromDeck==2)
                return;
        }
        else{
            if(fromDeck>=3) return;
        }

        //From fiveCardsToPickUp
        if(trainIndex>-1 && trainIndex<5 
        && trainIndex< fiveCardsToPickUp.size()){
            //If you pick up a locomotive
            //Then your turn is over
            if( (fromTable>0 || fromDeck>0)
            && fiveCardsToPickUp.get(trainIndex)==EColor.LOCOMOTIVE){
                return;
            }
            if( fiveCardsToPickUp.get(trainIndex)==EColor.LOCOMOTIVE){
                fromTable=2;
                fromDeck=2;
            }
            //Add to players hand
            curPlayer.addTrainCard(fiveCardsToPickUp.
                get(trainIndex ));
            //Remove from pile
            fiveCardsToPickUp.remove(trainIndex);
            //Increment number of cards from deck
            // and table
            fromTable++;
            moved=true;
            //play sound
            play(getDocumentBase(),"sounds/StartUp/card-flip.wav");
            repaint();
        }

        //From Deck
        if(trainIndex==5 && Deck.trainCardDeck.size()>0 ){
            //update count from deck
            fromDeck++;
            //add card picked up to current player
            curPlayer.addTrainCard(Deck.drawTrainCard());
            //set moved to true
            moved=true;
            //play sound
            play(getDocumentBase(),"sounds/StartUp/card-flip.wav");
            repaint();
        }
    }

    /**
     *****************************************************************
     *****************************************************************
     *****************************************************************
     *******            Paint               **************************
     *****************************************************************
     *****************************************************************
     *****************************************************************
     *****************************************************************
     *\

    /**
     * Paint method for applet.
     * Draws left/right/board sections.
     * As well as handles the switching of a player
     * and ending of the game and calling the end game applet.
     * 
     * @param  g   the Graphics object for the screen
     */
    public void paint(Graphics g){
        try{
            bg.clearRect(0,0,1267,900);            
            drawBoard();
            //Switch players
            if(moved && !spendingTrains){
                //if you've taken your turn draw done button
                drawDoneButton();
                //if you're not in dest mode and you clicked
                //done button
                if(!destMode && clickedDoneButton()){
                    //if you get the last turn signal
                    if(lastTurn){
                        //signal last turn to future players
                        Players.signalLastTurn();
                    }
                    //update player
                    curPlayer = Players.getNextPlayer();
                    //reset play variables
                    boughtTechnology=false;
                    moved=false;
                    //reset mouse globals
                    resetMouseGlobals();
                    fromDeck=0;
                    fromTable=0;
                    trackClicked=null;
                    tracksBought=0;

                    if(curPlayer==null){
                        //Game's Over
                        //call the game over method to calculate final
                        //points
                        Players.gameOver();
                        Class applet2 = Class.forName("EndGame");
                        Applet appletToLoad = (Applet)
                            applet2.newInstance();
                        appletToLoad.setStub(this);
                        setLayout( new GridLayout(1,0));
                        add(appletToLoad);
                        appletToLoad.init();
                        appletToLoad.start();
                    }
                    else{
                        repaint();
                    }
                }
            }

            //Try and reshuffle 
            if(fiveCardsToPickUp.size()<5){
                Deck.shuffleTrain();
            }

            //draws left and right sides of board
            drawRightSide();
            drawLeftSide();

            //Draw backbuffer on screen
            g.drawImage(backScreen,0,0,this);
            //Reset all mouse globals
            resetMouseGlobals();

        }
        catch(Exception e){
            showStatus("paint" + e.toString());
        }
    }

    /**
     * Resets the mouse global variables after a redraw. 
     * This is called
     * at the very end of the paint method
     */
    public void resetMouseGlobals(){
        wheelScrollUp=wheelScrollDown=false;
        mouseClickX=mouseClickY=0;
        rightClick=leftClick=false;
        if(destMode){
            trackClicked=null;
        }
    }

    /**
     * Makes board an applet sub and resizes it
     * @param width - width to resize it
     * @param height - height to resize it to
     */
    public void appletResize( int width, int height ){
        resize( 1267, 900 );
    }
}
