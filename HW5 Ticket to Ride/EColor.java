/**
 * Enumeration class EColors - names of all colors 
 * that are used in the game 
 * 
 * @author (Brian Knapp, Catherine Sullivan,
 *          Jessica Juan-Aquino, Uros Antic, Benjamin Costello) 
 * @version (42.0)
 */
public enum EColor
{
    // Each color corresponds to a pre-set path or train card
    WHITE, BLUE, YELLOW, BLACK, RED, GREEN, ORANGE, PINK, 
    GRAY, LOCOMOTIVE;

    /**
     * Gives the index of the EColor in the fixed order of:
     * WHITE, BLUE, YELLOW, BLACK, RED, GREEN, 
     * ORANGE, PINK, LOCOMOTIVE
     * 
     * @param color requested color to find index
     * @return index of the EColor
     */
    public static int getIndex(EColor color){
        switch(color){
            case WHITE:
            return 0;

            case BLUE: 
            return 1;

            case YELLOW: 
            return 2;

            case BLACK:
            return 3;

            case RED:
            return 4;

            case GREEN:
            return 5;

            case ORANGE: 
            return 6;

            case PINK:
            return 7;

            case LOCOMOTIVE:
            return 8;
        }

        // If an invalid color, return -1
        return -1;
    }

    /**
     * Gets the color based on the index
     * 
     * @param index index of color you're searching for
     * @return EColor the corresponding color
     */
    public static EColor getColor(int index){
        switch(index)
        {
            case 0:
            return EColor.WHITE;

            case 1:
            return EColor.BLUE; 

            case 2:
            return EColor.YELLOW; 

            case 3:
            return EColor.BLACK;

            case 4:
            return EColor.RED;

            case 5:
            return EColor.GREEN;

            case 6:
            return EColor.ORANGE; 

            case 7:
            return EColor.PINK;

            default:
            return EColor.LOCOMOTIVE;
        }
    }
}