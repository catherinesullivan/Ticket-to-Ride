import java.util.*;
import java.awt.*;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.*;
import javax.sound.sampled.*;

/**
 * StatsPanel displays a window of statistics for the final
 * results of a game. Each player is designated a column (of their
 * chosen color) where their final recorded statistics are displayed.
 * This runs at the end of every game.
 * 
 * @author (Brian Knapp, Catherine Sullivan,
 *          Jessica Juan-Aquino, Uros Antic, Benjamin Costello) 
 * @version (1.0)
 */
public class StatsPanel extends JPanel 
{
    /**
     * PaintComponent collects all statistics for each player and
     * organizes this data into a graphic, comprehensive, column.
     * 
     * @param g graphics of the component
     */    
    @Override
    public void paintComponent(Graphics g)
    {
        // Initialize data needed to display the 
        //correct number of columns
        // and the correct column colors
        int numPlayers = Players.getNumPlayers();
        g.setColor(Players.players.get(0).getColor());
        g.fillRect(0,0,getWidth(),getHeight());

        for(int i=1; i<numPlayers; i++){
            // Sets the column's background color equal
            //to the player's color
            g.setColor(Players.players.get(i).getColor());

            // Store dimensions for column width 
            //then paint accordingly
            int x =200*i+2;
            int y = 0 ;
            g.fillRect(x,0,200*i+5,getHeight()  );
        }

        // Use black for the text color
        g.setColor(Color.BLACK);

        for(int i =0; i < numPlayers; i++){
            // Store dimensions for text location
            int x = 200 * i +5;
            int y = 20;

            // Set the current player to print their stats
            Player p = Players.players.get(i);

            // Print each statistic for the given player
            for(int j=1; j<=p.textToPrint.size(); j++){
                g.drawString(p.textToPrint.get(j-1),
                    x,y*j);                
            }
        }
    }

    /**
     * Formally display the JFrame for the stats pannel
     */
    public static void display(){
        //create frame and pannel
        JFrame j = new JFrame("Ending Stats");
        JPanel p = new StatsPanel();
        j.setContentPane(p);
        //set right column size
        int cols = Players.getNumPlayers();
        int rowLength=0;
        //set up proper length
        for(Player pl : Players.players){
            if(rowLength<pl.textToPrint.size()){
                rowLength =pl.textToPrint.size();
            }
        }
        j.setSize(200*cols+5,rowLength*20+100);
        j.setVisible(true);
        p.setVisible(true);
    }

}