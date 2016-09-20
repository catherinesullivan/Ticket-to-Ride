import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.*;
import javax.swing.JOptionPane;
import javax.sound.sampled.*;

/**
 * Creates a JPanel that displays the 
 * winning player's name in flashing
 * colors and shoots fireworks until the window is closed.
 *
 * @author (Brian Knapp, Catherine Sullivan,
 *          Jessica Juan-Aquino, Uros Antic, Benjamin Costello)
 * @version (42.0)
 */
public class WinningPanel extends JPanel implements ActionListener{
    //starting x and y vaules of the fireworks
    int firework1X = 195, firework1Y = 130;
    int firework2X = 205, firework2Y = 130;
    int firework3X = 205, firework3Y = 130;
    int firework4X = 205, firework4Y = 130;
    int firework5X = 203, firework5Y = 130;
    int firework6X = 196, firework6Y = 130;
    int firework7X = 196, firework7Y = 130;
    int firework8X = 188, firework8Y = 130;

    //counter that keeps track of the colors of the name displayed
    int i = 0;
    //stores the amount the x and y values 
    //of the fireworks will change
    //after each tick of the clock
    int change = 10;
    //stores the name of the winning player
    static String name;
    //instantiates the timer that will 
    //cause the fireworks to repeatedly
    //change positions
    Timer clock = new Timer(30,this);
    //creates the clip variable that will hold a song
    static Clip clip;

    /**
     * Constructor for the JPanel
     * @param name the variable holding the name of the winning player
     */
    WinningPanel(String name){
        try{
            //inserts the song Celebration into the clip variable
            clip = AudioSystem.getClip();
            AudioInputStream audioStream = 
                AudioSystem.getAudioInputStream(
                    new File("sounds/StartUp/Celebration.wav"));
            clip.open(audioStream);
        }
        catch(Exception e){

        }
        //sets the size of the JPanel
        setSize(400,300);
        //sets the global name equal to the name entered in the
        //constructor
        this.name=name;
    }

    /**
     * Paints the JPanel
     * @param g - graphics of the component
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        //starts the timer
        clock.start();

        //creates an array of colors that will be cycle through for
        //the String
        Color[] colors = {Color.BLUE, Color.YELLOW, Color.MAGENTA,
                Color.GREEN, Color.ORANGE};

        //sets the font properties and creates the
        //string that will
        //display "(Winning Players Name) Wins!"
        g.setFont(new Font("Verdana", Font.BOLD, 20));
        FontMetrics fm = g.getFontMetrics();
        String message = name + " Wins!";

        //sets the background color to black
        setBackground(Color.BLACK);
        //sets the color of the String to one of the ones in the color
        //array
        g.setColor(colors[i]);
        //draws the String
        g.drawString(message,
            200-fm.stringWidth(message)/2,
            150-(fm.getAscent()+fm.getDescent())/2);

        //calls the method that will draw the fireworks
        fireworks(g);

        //changes the color of the string to the 
        //next color in the array
        i++;
        //if the end of the array is reached, 
        //the first color is chosen
        //again
        if (i == colors.length-1){
            i = 0;
        }
    }

    /**
     * This method draws the fireworks
     * @param g graphics component
     */
    private void fireworks(Graphics g){
        //sets the color of the first firework
        g.setColor(Color.RED);
        //draws the line representing the first firework
        g.drawLine(firework1X, firework1Y, 
            firework1X-5, firework1Y-5);

        //sets the color of the second firework
        g.setColor(Color.CYAN);
        //draws the line representing the second firework
        g.drawLine(firework2X-5, firework2Y+5, 
            firework2X-9, firework2Y+1);

        //sets the color of the third firework
        g.setColor(new Color(255,255,0));
        //draws the line representing the third firework
        g.drawLine(firework3X-5, firework3Y-5, 
            firework3X-10, firework3Y);

        //sets the color of the fourth firework
        g.setColor(Color.GREEN);
        //draws the line representing the fourth firework
        g.drawLine(firework4X-10, firework4Y, 
            firework4X-15, firework4Y+5);

        //sets the color of the fifth firework
        g.setColor(new Color(255,165,0));
        //draws the line representing the fifth firework
        g.drawLine(firework5X, firework5Y, firework5X-6, firework5Y);

        //sets the color of the sixth firework
        g.setColor(Color.MAGENTA);
        //draws the line representing the sixth firework
        g.drawLine(firework6X, firework6Y, firework6X, firework6Y+6);

        //sets the color of the seventh firework
        g.setColor(new Color(160,32,240));
        //draws the line representing the seventh firework
        g.drawLine(firework7X, firework7Y-6, firework7X, firework7Y);

        //sets the color of the eighth firework
        g.setColor(Color.BLUE);
        //draws the line representing the eighth firework
        g.drawLine(firework8X, firework8Y, firework8X+6, firework8Y);
    }

    /**
     * This method changes the value of the x and y coordinates of the
     * fireworks by the
     * amount stored in the variable change.
     */
    private void updateVectors() {
        //increments/decrements the x and y value of the fireworks
        //depending on where it is on the panel
        firework1X -= change;
        firework1Y -= change;
        firework2X += change;
        firework2Y += change;
        firework3X += change;
        firework3Y -= change;
        firework4X -= change;
        firework4Y += change;
        firework5X += change;
        firework6Y += change;
        firework7Y -= change;
        firework8X -= change;
        //if the 5th firework exceeds the limits of the JFrame, all of
        //the fireworks are reset to their initial values
        if (firework5X > 400){
            firework1X = 195;
            firework1Y = 130;
            firework2X = 205;
            firework2Y = 130;
            firework3X = 205;
            firework3Y = 130;
            firework4X = 205;
            firework4Y = 130;
            firework5X = 203;
            firework6Y = 130;
            firework7Y = 130;
            firework8X = 188;
        }
    }

    /**
     * This method repaints the panel and call the updateVectors
     * method every time the timer "goes off".
     */
    @Override
    public void actionPerformed(ActionEvent arg0) {
        //calls the paint component to repaint the panel
        repaint();
        //calls the method that updates the position of the fireworks
        updateVectors();
    }

    /**
     * Creates the frame
     */
    public static void main() {
        //creates the JFrame
        JFrame j = new JFrame("Winner Winner Chicken Dinner!");
        //creates the WinningPanel JPanel
        JPanel p = new WinningPanel(name);
        //endlessly loops through the song until the program is closed
        //or restarted
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        //sets the content pane to the WinningPanel
        j.setContentPane(p);
        //sets the size of the JFrame
        j.setSize(400,300);
        //makes the JFrame visible
        j.setVisible(true);
        //makes the JFrame not resizable
        j.setResizable(false);
        //makes the WinningPanel visible
        p.setVisible(true);
        //adds a border to the WinningPanel
        p.setBorder(new MatteBorder(5, 5, 5, 5, Color.WHITE));
    }
}