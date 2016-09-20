import javax.swing.JFrame;
import java.awt.event.*;
import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.io.*;
import javax.imageio.*;
import javax.swing.Timer;
import java.awt.geom.AffineTransform;

/**
 * Directions panel, it creates separate panel with instructions 
 * of the game it simulates fliping page animation that depends
 * of the page that was clicked on
 * 
 * @authors Brian Knapp, Jessica Juan-aquino, Catherine Sullivan,
 * Benny Costello, Uros Antic
 * @version 42.0
 */
public class DirectionsPanel extends JPanel 
implements MouseListener, ActionListener
{
    Timer time;     //timer object 
    int frameNum;   // frame number used for transformation
    Image left;     // image on the left side
    Image right;    // image on the right side 
    // image on that shows up on left side after the page is flipped
    Image backLeft; 
    // image on that shows up on right side after the page is flipped
    Image backRight;    
    //var that records if the right side of the panel is clicked
    boolean rightSideClick;
    //var that records if the left side of the panel is clicked
    boolean leftSideClick;
    static ArrayList<Image> pictures = new ArrayList<Image>();
    // array list of images

    /**
     * Constructor for the direction panel
     */
    DirectionsPanel()   {
        addMouseListener(this);       
        time = new Timer(10,this);
        frameNum=1;
        left=right=null;
        rightSideClick=leftSideClick=false;
    }

    /**
     * Method that creates JFrame and JPanel that are displayed 
     * to the screen,
     * it setcontent to the JPanel
     * Both JFrame and Jpanel are set to visible state
     */ 
    public static void callPanel()  {
        try{
            for(int i=0; i<8; i++){
                pictures.add(ImageIO.read(
                        new File("photo/HowToPlay/page" 
                            + (i)+".jpg" ))); 
            }
        }
        catch(Exception e){
            //showStatus(e.toString());
        }
        JFrame j = new JFrame("Options");
        JPanel p = new DirectionsPanel();
        j.setContentPane(p);
        j.setSize(1280,938);
        j.setVisible(true);
        p.setVisible(true);
    }

    /**
     * methods that keeps track if the mouse is clicked
     * if mouse is clicked on the right side of 
     * the panel flips the images
     * in specific order for the right click;
     * if mouse is clicked on the left side of the panel 
     * flips the images
     * in specific order for the left click;
     * 
     * @param e - MouseEvent
     */
    public void mouseClicked(MouseEvent e){
        if(rightSideClick || leftSideClick){
            e.consume();
            return;
        }
        if(e.getX() > 644){
            backLeft = pictures.get(0);
            right = pictures.get(1);
            left = pictures.get(2);
            Collections.rotate(pictures,-2);
            time.start();
            rightSideClick=true;
        }
        if( e.getX()<= 644){
            left = pictures.get(0);
            right=pictures.get(1);
            backLeft = pictures.get(6);
            backRight = pictures.get(7);
            Collections.rotate(pictures,2);
            time.start();
            leftSideClick=true;
        }
        e.consume();
    }

    /**
     * paintComponent method, this method does the 
     * transformation of the images
     * it depends if the right or left side is clicked
     * 
     * @param g - Graphics g
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(0,0,getWidth(),getHeight());

        if(frameNum==1){        
            g.drawImage(pictures.get(0),0,0,this);
            g.drawImage(pictures.get(1),634,0,this);
        }
        else{
            //sets up graphics 2d
            Graphics2D g2 = (Graphics2D) g;
            //saves transformation
            AffineTransform affine = g2.getTransform();
            if(rightSideClick){
                //right page flip
                if(frameNum<60){
                    //draw old left
                    g2.drawImage(backLeft,0,0,this);
                    //draw next page
                    g2.drawImage(pictures.get(1),634,0,this);
                    g2.translate(634,0);
                    //right image is the one being shrunk
                    g2.scale( (60-frameNum)/60.0 ,1 );    
                    //draw scaled image
                    g2.drawImage(right, 0,0,this);
                }
                else{
                    //draws the 2 background images
                    g.drawImage(backLeft,0,0,this);
                    //new Right
                    g.drawImage(pictures.get(1),634,0,this);
                    g2.translate(633 - 633 * (frameNum-60)/60.0,0);
                    //we need to start with left 
                    g2.scale( (frameNum-60)/60.0,1);
                    //draw scaled image
                    g2.drawImage(left,0,0,this);
                }
            }
            if(leftSideClick){
                //left page flip
                if(frameNum<60){

                    //Draw new left
                    g2.drawImage(pictures.get(0),0,0,this);
                    //flip old right - right
                    g2.drawImage(right,634,0,this);
                    //flip old left
                    //coordinates to draw on
                    g2.translate(633 * (frameNum)/60.0,0);
                    //scale picture
                    g2.scale( (60-frameNum)/60.0 ,1 );    
                    //draw scaled image
                    g2.drawImage(left, 0,0,this);
                }
                else{
                    //draw new left
                    g2.drawImage(pictures.get(0),0,0,this);
                    //draw old right
                    g2.drawImage(right,634,0,this);
                    //flip in new right 
                    g2.translate(634,0);
                    //we need to start with left 
                    g2.scale( (frameNum-60)/60.0,1);
                    //draw scaled image
                    g2.drawImage(backRight,0,0,this);
                }

            }
            //sets transformation back
            g2.setTransform(affine);
        }
    }

    public void mouseDragged(MouseEvent e){
    }

    public void mouseEntered(MouseEvent e){
    }

    public void mouseExited(MouseEvent e){
    }

    public void mousePressed(MouseEvent e){
    }

    public void mouseReleased(MouseEvent e){
    }

    /**
     * Method that makes sure if the frame number is 120, timer stops;
     * if not it keeps update the frameNum and repaints the screen
     * 
     * @param e - ActionEvent
     */
    public void actionPerformed(ActionEvent e){
        frameNum++;        
        //120 frames
        if(frameNum==120){
            //reset
            frameNum=1;
            //stop time
            time.stop();
            //reset which side click
            leftSideClick=false;
            rightSideClick=false;
        }
        repaint();
    }
}