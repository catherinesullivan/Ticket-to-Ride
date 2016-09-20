import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import javax.imageio.*;
import java.awt.event.*;

/**
 * Class EndGame - write a description of the class here
 * 
 * @author (your name) 
 * @version (a version number)
 */
public class EndGame extends JApplet implements MouseListener,
MouseMotionListener
{
    Image board, background, ticket;
    Graphics bg;
    Image backScreen;
    Player p1,p2,p3,p4;
    //Current destination card hovering over
    Destination curDest;
    Image dest;
    Player curDestPlayer;

    /**
     * Called by the browser or applet viewer to inform this 
     * JApplet that it
     * has been loaded into the system. It is always called 
     * before the first 
     * time that the start method is called.
     */
    public void init()
    {
        curDest=null;
        try{
            setSize(new Dimension(1267,900) ); 
            addMouseListener(this);
            addMouseMotionListener(this);
            if(2<=Players.getNumPlayers()){
                p1 = Players.players.get(0);
                p2 = Players.players.get(1);
            }
            else{
                p1=null;
                p2=null;
            }
            if(3<=Players.getNumPlayers()){
                p3 = Players.players.get(2);
            }
            else{
                p3=null;
            }
            if(4<=Players.getNumPlayers()){
                p4 = Players.players.get(3);
            }
            else{
                p4=null;
            }
            Tracks.unAddOffSet();
        }
        catch(Exception e){
            showStatus(e.toString()+"first");
        }
        try{
            // This is the image we draw on to avoid
            // flickering by drawing directly to the screen
            backScreen = createImage(1267,900);
            // Set the buffer graphics
            bg = backScreen.getGraphics();
            ticket = ImageIO.read(new File("photo/PlayerTicket.png"));
            board = ImageIO.read(new File("photo/map7.jpg" ));
            background=ImageIO.read(new File("photo/background.jpg"));
        }
        catch(Exception e){
            showStatus(e.toString()+"second");
        }
    }

    /**
     * draws player1
     * 
     * 150 by 229
     */
    public void drawPlayer1(){
        if(p1==null){
            return;
        }

        int x = 662;
        int y = 17;

        drawPlayerInfo(x,y,p1);

        //Draws the current destination card
        Image dest = p1.getCurDest().getImage();

        bg.drawImage(dest,675,133,this );
    }

    public void drawPlayerInfo(int x,int y,Player p){
        //Draws the ticket
        bg.setColor(p.getColor());
        bg.fillRoundRect(x,y,175,99,15,15);

        bg.drawImage(ticket, x,y,this );
        //intially 1085, 790
        bg.setColor(Color.BLACK);
        bg.setFont(new Font("Times New Roman",
                Font.BOLD, 15));
        double strWidth = bg.getFontMetrics().
            stringWidth(p.getName());
        if(p.getName().length() > 13){
            bg.drawString(p.getName().substring(0,13), x+15, y+25);
        }
        else{
            bg.drawString(p.getName(),
                x+15 + 72 - ((int)strWidth / 2 ), y+25);
        }
        bg.drawString(String.valueOf(
                p.getNumOfPoints()), x+120, y+70);

    }

    /**
     * draws player2
     * 
     * 150 by 229
     */
    public void drawPlayer2(){
        if(p2==null){
            return;
        }

        //Draws the ticket
        int x =962 ;
        int y =17 ;
        drawPlayerInfo(x,y,p2);

        //Draws the current destination card
        Image dest = p2.getCurDest().getImage();
        bg.drawImage(dest,975,133,this );
    }

    /**
     * draws player3
     * 
     * 150 by 229
     */
    public void drawPlayer3(){
        if(p3==null){
            return;
        }

        //Draws the ticket
        int x =662 ;
        int y =467 ;
        drawPlayerInfo(x,y,p3);
        //Draws the current destination card
        Image dest = p3.getCurDest().getImage();
        bg.drawImage(dest,675,583,this );
    }

    /**
     * draws player4
     * 
     * 150 by 229
     */
    public void drawPlayer4(){
        if(p4==null){
            return;
        }

        //Draws the ticket
        int x =962 ;
        int y =467 ;
        drawPlayerInfo(x,y,p4);
        //Draws the current destination card
        Image dest = p4.getCurDest().getImage();
        bg.drawImage(dest,975,583,this );
    }

    /**
     * Paint method for applet.
     * 
     * @param  g   the Graphics object for this applet
     */
    public void paint(Graphics g){
        try{
            showStatus("Thanks for Playing!");
            bg.clearRect(0,0,1267,900);            
            drawBoard();
            drawPlayer1();
            drawPlayer2();
            drawPlayer3();
            drawPlayer4();

            if(curDest!=null){
                drawCityHighlite(curDest.getCity1(),
                    curDest.getCity2());
                curDest=null;
            }

            //Draw backbuffer on screen
            g.drawImage(backScreen,0,0,this);
        }
        catch(Exception e){
            showStatus(e.toString()+"paint");
        }
    }

    /**
     * Called by the browser or applet viewer to inform
     * this JApplet that it
     * is being reclaimed and that it should destroy 
     * any resources that it
     * has allocated. The stop method will always be
     * called before destroy. 
     */
    public void destroy(){
        // provide code to be run when JApplet 
        //is about to be destroyed.
    }

    /**
     * Draws the board onto the buffer
     */
    public void drawBoard(){
        bg.drawImage(background,0,0,this);
        bg.drawImage(board, 0,0,this );
        drawPoints();
        // Draw Players Tracks
        Tracks.drawPlayerTracks(bg);      
    }

    /**
     * Draws the 2 (or 3 if France) cities of the destination card
     * that the player is hovering over
     */
    public void drawCityHighlite(String city1, String city2){
        if(curDest.playerComplete()){
            //Set Green for complete
            bg.setColor(new Color(0,128,0,150));
        }
        else{
            //Set red
            bg.setColor(new Color(255,0,0,150));
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
            Point p = Cities.listOfCities.
                get(Cities.listOfCities.size()-2).boardLocation;
            int x =(int) p.getX()-8;
            int y = (int) p.getY()-8;
            bg.fillOval(x,y,16,16);
            //Get city point
            p = Cities.listOfCities.
            get(Cities.listOfCities.size()-1).boardLocation;
            x =(int) p.getX()-8;
            y = (int) p.getY()-8;
            bg.fillOval(x,y,16,16);            
        }
        else{
            //Get city point
            Point p = Cities.getPoint(name);
            int x =(int) p.getX()-11;
            int y = (int) p.getY()-11;
            bg.fillOval(x,y,22,22);
        }    
    }

    /**
     * Draws the Points Around the board
     */
    public void drawPoints(){
        //Loop through players and get number
        //of points and their color
        //Do points mod 100
        for(int i=0; i<Players.getNumPlayers(); i++){
            Player p = Players.getPlayerAt(i);
            bg.setColor(p.getColor());
            int points=p.getNumOfPoints();
            while(points>=100){
                points-=100;
            }
            //show negative values
            while(points<0){
                points+=100;
            }
            if(points>=0 && points<=30){
                points = 30-points;
                bg.fillOval(10,11 +(points*29) ,10,10 );
            }
            else if( points<50){
                points= points-30;
                bg.fillOval(15 + (points*28)  ,11,10,10);
            }
            else if(points<=80){
                points = points-50;
                bg.fillOval(580, 11 + (points*29) ,10,10);
            }
            else{            
                points = 100-points;
                bg.fillOval(15+(points*28) ,881,10,10);
            }
        }
    }

    /**
     * Determines where you clicked
     */
    public void mouseClicked(MouseEvent e){
        int x = e.getX();
        int y = e.getY();
        boolean leftClick= SwingUtilities.isLeftMouseButton(e);
        boolean rightClick = SwingUtilities.isRightMouseButton(e);

        changeDestCard(x,y,p1,675,133, leftClick);

        changeDestCard(x,y,p2,975,133, leftClick);

        changeDestCard(x,y,p3,675,583, leftClick);

        changeDestCard(x,y,p4,975,583, leftClick);

        e.consume();
    }

    /**
     * Changes current dest card
     */
    public void changeDestCard(int x, int y, 
    Player p, int playerCardX, int playerCardY, boolean leftClick){
        if(p==null) return;
        if(x>=playerCardX && x<=playerCardX+150){
            if(y>=playerCardY & y<=playerCardY+229){
                if(leftClick){
                    p.shiftDest(1);
                    curDest=p.getCurDest();
                }
                else{
                    p.shiftDest(-1);
                    curDest=p.getCurDest();
                }
            }
        }
        curDestPlayer=p;
        repaint();
    }

    /**
     * Repaints if the mouse reenters the applet
     * @param e - MouseEvent
     */
    public void mouseEntered(MouseEvent e){
        e.consume();
        repaint();
    }

    public void mouseDragged(MouseEvent e){
        e.consume();
    }

    /**
     * Saves the coordinates of where the mouse
     * is hovering
     * @param e - MouseEvent
     */
    public void mouseMoved(MouseEvent e){

        int x = e.getX();
        int y = e.getY();        
        //4 players, see if one is either one
        //of their dest cards
        highLiteCity(x,y,p1,675,133);
        highLiteCity(x,y,p2,975,133);
        highLiteCity(x,y,p3,675,583);
        highLiteCity(x,y,p4,975,583);

        repaint();
        e.consume();
    }

    public void highLiteCity(int x, int y, 
    Player p, int playerCardX, int playerCardY){
        if(p==null) return;
        if(x>=playerCardX && x<=playerCardX+150){
            if(y>=playerCardY & y<=playerCardY+229){
                curDest = p.getCurDest();
            }
        }
        curDestPlayer=p;
    }

    public void mouseExited(MouseEvent e){
        e.consume();
    }

    public void mousePressed(MouseEvent e){
        e.consume();
    }

    public void mouseReleased(MouseEvent e){
        e.consume();
    }

}
