import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;
import java.awt.Font;
import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.applet.*;

/**
 * Start game applet, it sets the background and creates 
 * "play game", "how to play" and "exit" buttons. By clicking 
 * on the play game button it takes user to a new applet and 
 * allows the user to plat the game. By clicking on the how to 
 * play button, new JPanel is opened and it allowes the user to 
 * read the instructions for the game. Clicking on the
 * exit button the applet is closed.
 * 
 * @author (Brian Knapp, Catherine Sullivan,
 *          Jessica Juan-Aquino, Uros Antic, Benjamin Costello) 
 * @version (42.0)
 */
public class StartGame extends JApplet implements MouseListener,
MouseMotionListener, AppletStub, AudioClip
{
    // images for the background
    Image front,howToPlay,exit;
    //default font
    Font myFont = new Font("TimesRoman", Font.BOLD, 26);

    Graphics bg;    
    //graphics background object necessary for double 
    //buffering effect

    //offscreen image for double buffering
    Image offScreen = createImage(1267,900); 

    //sound file location
    String soundfile;

    boolean entered = true; 
    //boolean that is changed to false if the mouse 
    //has cordinates of one of the buttons 

    int mouseClickX, mouseClickY; 
    // variables used to record at what position was mouse clicked
    int mouseHoverX,mouseHoverY;  
    // variables used to keep track of mouse position

    boolean mousePressed1,mousePressed2= false;  
    //variables used to record if the button was pressed or not

    int buttonHover; 
    // contains the number of the button that was hovered 
    //by the mouse
    boolean displayHoverB1,displayHoverB2;
    //variables used to record if the button was hovered or not

    //background sound
    AudioClip backSound;

    /**
     * Called by the browser or applet viewer to inform this 
     * JApplet that it has been loaded into the system. It is 
     * always called before the first 
     * time that the start method is called.
     */
    public void init(){
        try{
            // get the background sound
            backSound = getAudioClip(getCodeBase(),
                "sounds/StartUp/beatles.wav");
            //Start it on loop
            backSound.loop();

            offScreen = createImage(1267,900); // Creating offscreen
            bg = offScreen.getGraphics();
            // Creating bufferGraphics as the graphics object 
            //for offscreen

            soundfile = "sounds/StartUp/histicks.wav";
            setSize(new Dimension(1267,900)); 
            addMouseListener(this);
            addMouseMotionListener(this);
            mouseHoverX=mouseHoverY=0;
            front = ImageIO.
            read(new File("photo/StartUp/FrontPicture.jpg"));
            howToPlay = ImageIO.
            read(new File("photo/StartUp/howToPlay.jpg"));
            exit = ImageIO.read(new File("photo/StartUp/exit.jpg"));
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * Paints the whole start game applet
     * 
     * @param g - Graphics g
     */
    public void paint(Graphics g){
        bg.drawImage(front,0,0,this);
        bg.setFont(myFont); // seting the font of the string    

        //drawing Play Game button
        bg.setColor(new Color(185,0,0));    // red color
        //filled background rect 
        bg.fillRoundRect(483,505,300,50,30,30);
        bg.setColor(Color.black);
        bg.drawRoundRect(483,505,300,50,30,30);     
        // edge of the bg filled rect

        bg.setColor(new Color(245,21,21));  // color for top rect
        bg.fillRoundRect(483,500,300,50,30,30); // filled top rect

        if (displayHoverB1 == true)    {
            bg.setColor(new Color(150,0,0));
            bg.fillRoundRect(483,500,300,50,30,30);
            if (mousePressed1)  {
                bg.drawImage(front,0,0,this);

                //drawing Play Game button
                bg.setColor(new Color(245,21,21));    // red color
                bg.fillRoundRect(483,505,300,50,30,30); 
                //filled background rect 
                bg.setColor(Color.black);
                bg.drawRoundRect(483,505,300,50,30,30);

                bg.setColor(Color.yellow);
                bg.drawString("Play Game", 633 -
                    bg.getFontMetrics().stringWidth("Play Game")/2 + 
                    1/2, 536);
            }
        }

        if(!mousePressed1)   {
            // color black
            bg.setColor(Color.black);
            // edge for top rect
            bg.drawRoundRect(483,500,300,50,30,30);

            if (displayHoverB1 == true)    {
                bg.setColor(Color.yellow); //diferent
            }
            bg.drawString("Play Game", 633 -
                bg.getFontMetrics().stringWidth("Play Game")/2 +
                1/2,532);
        }

        //drawing How To Play button
        // red color for bg rect
        bg.setColor(new Color(185,0,0));
        // filled bg rect
        bg.fillRoundRect(483,580,300,50,30,30);

        bg.setColor(Color.black);
        // egde for bg rect
        bg.drawRoundRect(483,580,300,50,30,30);

        bg.setColor(new Color(245,21,21));
        bg.fillRoundRect(483,575,300,50,30,30);
        if (displayHoverB2 == true)    {
            bg.setColor(new Color(150,0,0));//different 
            bg.fillRoundRect(483,575,300,50,30,30);
            if (mousePressed2)  {
                bg.drawImage(howToPlay,483,575,this);

                //drawing Play Game button
                // red color
                bg.setColor(new Color(245,21,21));
                // filled bg rect
                bg.fillRoundRect(483,580,300,50,30,30);
                bg.setColor(Color.black);
                // egde for bg rect
                bg.drawRoundRect(483,580,300,50,30,30); 

                bg.setColor(Color.yellow);
                bg.drawString("How To Play", 633 - 
                    bg.getFontMetrics().stringWidth("How To Play")/2
                    + 1/2,611);
            }
        }

        if(!mousePressed2)  {
            bg.setColor(Color.black);
            bg.drawRoundRect(483,575,300,50,30,30);

            if (displayHoverB2 == true)    {
                bg.setColor(Color.yellow);
            }
            bg.drawString("How To Play", 633 - 
                bg.getFontMetrics().stringWidth("How To Play")/2 + 
                1/2,607);
        }

        g.drawImage(offScreen,0,0,this);
    }

    /**
     * Depending on the cordinates of mouse clicked, this method 
     * openes up
     * applet for playing the game, JPanel with directions or
     * setVisibility of the applet to false
     * @param e - MouseEvent
     */
    public void mouseClicked(MouseEvent e){
        mouseClickX = e.getX();
        mouseClickY = e.getY();
        // play game button
        if(mouseClickY >=500 && mouseClickY<=550 )   {
            if(mouseClickX >=483 && mouseClickX <=783 )   {
                try{
                    Class applet2 = Class.forName("Board");
                    Applet appletToLoad = 
                        (Applet)applet2.newInstance();
                    appletToLoad.setStub(this);
                    setLayout( new GridLayout(1,0));
                    add(appletToLoad);
                    appletToLoad.init();
                    appletToLoad.start();
                    backSound.stop();
                }
                catch(Exception eee){
                    showStatus(eee.toString()+ " startGame");
                }
            }
        }
        // how to play button
        else if(mouseClickY >=575 && mouseClickY<=625 )   {
            if(mouseClickX >=483 && mouseClickX <=783 )   {
                try{
                    DirectionsPanel d = new DirectionsPanel();
                    DirectionsPanel.callPanel();
                }
                catch(Exception eee){
                    showStatus(eee.toString());
                }
            }
        }
    }

    /**
     * It records x and y cordinates where mouse was pressed, and
     * if the cordinates match any of buttons cordinates it sets
     * mousePressed variable of that button to true
     * 
     * @param e - MouseEvent
     */
    public void mousePressed(MouseEvent e){
        mouseClickX = e.getX();
        mouseClickY = e.getY();
        if(mouseClickY >=500 && mouseClickY<=550 )   {
            if(mouseClickX >=483 && mouseClickX <=783 )   {
                mousePressed1 = true;
            }
        }
        else if(mouseClickY >=575 && mouseClickY<=625 )   {
            if(mouseClickX >=483 && mouseClickX <=783 )   {
                mousePressed2 = true;
            }
        }
        repaint();
    }

    /**
     * It sets all the variables that keep track if the button 
     * was clicked to the false 
     * 
     * @param e - MouseEvent 
     */
    public void mouseReleased(MouseEvent e) {
        mousePressed1 = false;
        mousePressed2 = false;
    }

    /**
     * This method calls the method hoverButton that returns an int
     * if the number returned from the hoverButton method equals any
     * number but 0, it set the variable that correspond to 
     * specific button and calls repaint method.
     * 
     * @param e - MouseEvent
     */
    public void mouseMoved(MouseEvent e)  {
        mouseHoverX = e.getX();
        mouseHoverY = e.getY();        
        e.consume();

        buttonHover = hoverButton();
        if (buttonHover == 0)   {
            entered = true;
        }
        if(buttonHover == 1)  {
            displayHoverB1 = true;
            if (entered)    {
                play(getDocumentBase(),soundfile);
                entered = false;
            }
            repaint();
        }
        else if (buttonHover == 2)  {
            displayHoverB2 = true;
            if (entered)    {
                play(getDocumentBase(),soundfile);
                entered = false;
            }
            repaint();
        }
        else {
            displayHoverB1 = false;
            displayHoverB2 = false;
            repaint();
        }
    }  

    /**
     * It returns a number from 0 to 3, each number correspond to
     * specific location on the applet
     * 1 - if the play game button is clicked
     * 2 - if the how to play button is clicked
     * 0 - for anyting else
     * 
     * @return 0,1, or 2 - number that correspond to button or 
     * background
     */
    public int hoverButton()    {
        if(mouseHoverY >=500 && mouseHoverY<=550 )   {
            if(mouseHoverX >=483 && mouseHoverX <=783 )   {
                return 1;
            }
        }
        else if(mouseHoverY >=575 && mouseHoverY<=625 )   {
            if(mouseHoverX >=483 && mouseHoverX <=783 )   {
                return 2;
            }
        }
        return 0;        
    }

    public void appletResize( int width, int height ){}

    public void mouseDragged(MouseEvent e)  {}

    public void mouseEntered(MouseEvent e){}

    public void mouseExited(MouseEvent e)  {}

    /**
     * Loops sound. Overriden method
     */
    public void loop(){};  

    /**
     * Plays song. Overriden method
     */
    public void play(){};

    /**
     * Stops the song. Overriden method
     */
    public void stop(){};

}