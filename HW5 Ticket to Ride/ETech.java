import java.io.*;
import java.awt.*;
import javax.imageio.*;

/**
 * Enumeration class ETech - names of all technology cards 
 * that are used in the game
 * 
 * @author (Brian Knapp, Catherine Sullivan,
 *          Jessica Juan-Aquino, Uros Antic, Benjamin Costello) 
 * @version (42.0)
 */
public enum ETech
{
    // Each technology card in the game
    boiler_lagging,
    booster,
    diesel_power,
    doubleheading,
    equalising_beam,
    ireland_france_concession,
    mechanical_stoker,
    propellers,
    right_of_way,
    risky_contracts,
    scotland_concession,
    steam_turbine,
    superheated_steam_boiler,
    thermo_compressor,
    wales_concession,
    water_tenders;

    // Initialize image
    Image image;

    ETech(){
        try{
            // Try importing each image according to its filename
            image = 
            ImageIO.read(new File("photo/Technology/" +this+".png" ));
        }
        catch(Exception e){
        }
    }
}