import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * Class contains an arraylist of all of the tracks.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Tracks{
    // list of all the Tracks
    protected static ArrayList<Track> allTracks;
    private static boolean addedOffset;

    /**
     * Constructor for all Tracks.
     * Adds the offset to all of the points which are added
     * in a static intializer block.
     */
    public Tracks(){
        if(!addedOffset){
            for(Track t : allTracks){
                ArrayList<Polygon> graphics = t.getGraphicsPoints();
                for(Polygon p : graphics){
                    p.translate(245,0);
                }
            }
            addedOffset=true;
        }
    }

    /**
     * Goes through and unadds the offset to all of the tracks.
     */
    public static void unAddOffSet(){
        for(Track t : allTracks){
            ArrayList<Polygon> graphics = t.getGraphicsPoints();
            for(Polygon p : graphics){
                p.translate(-245,0);
            }
        }
    }

    /**
     * Goes through and draws all of the player owned tracks
     * @param bg - Buffered Graphics to draw tracks
     */
    public static void drawPlayerTracks(Graphics bg){
        for(Track t : allTracks){
            ArrayList<Color> colors = t.getColors();
            if(colors!=null){
                for(int i=0; i<colors.size(); i++){
                    Color c = colors.get(i);
                    ArrayList<Polygon> polygons = 
                        t.getGraphicsPoints();
                    bg.setColor(c);
                    for(Polygon p : polygons){
                        p.translate(i,i);
                        bg.fillPolygon(p);
                        p.translate(-i,-i);
                    }
                }
            }
        }
    }

    /**
     * Determines what Track you clicked on or null if 
     * none was clicked.
     * @param p the point clicked
     * @return boolean true if you clicked this Track and false
     * otherwise
     */
    public static Track trackClicked(Point p){
        for(Track track : allTracks){
            for(Polygon box : track.graphicPoints){
                if(box.contains(p)) 
                    return track;
            }
        }
        return null;
    }

    /**
     * Returns the track only if the player can purchase it
     * and if the track is occupied then the player has the
     * right of way card and the mouse is hovering over it.
     * 
     * @param hover- the coordinates of the point in question
     * @param player - the player whose turn it is
     * @return Track - the track object that was being hovered
     * over or null if one doesn't exist or the player cannot
     * purchase it
     */
    public static Track trackOver(Point hover, Player player){
        //If the player has the right of way card
        boolean rightOfWay = player.hasRightOfWay();
        for(Track track : allTracks){
            //If the track is unowned and
            //the player has the right cards to buy it
            //then the track will be returned if you
            //hover over the track
            if(track.playerCanBuy(player) &&  
            (!track.occupied() || rightOfWay) ){
                for(Polygon box : track.graphicPoints){
                    if(box.contains(hover)) 
                        return track;
                }
            }
        }
        return null;
    }

    /**
     * Determines if there is a path from city 1 to city 2.
     * 
     * @param city1 - name of second city
     * @param city2 - name of first city
     * @param playerColor color of player
     * @return true if the player has a path from city1 to city 2
     * false otherwise
     */
    public static boolean isPath(String city1, String city2, 
    Color playerColor){
        //Go through all possible tracks from one city
        ArrayList<Track> tracks = getTracks(city1, playerColor);
        for(Track starting : tracks){    
            //Set used path to false
            markUnvisited();
            //if there is a connection to city2 then return true
            if(dfs(starting, city2, playerColor)){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the list of tracks from the starting city that the
     * player has.
     * @param city - city name of Track we are looking for
     * @return ArrayList<Track> - the Track that contains the city
     */
    public static ArrayList<Track> getTracks(String city, 
    Color playerColor){
        //gets a list of all the cities that the player
        //owns and is connected to
        ArrayList<Track> toreturn = new ArrayList<Track>();
        for(Track t : allTracks){
            if((t.getCity1().equals(city) ||
                t.getCity2().equals(city))
            && t.playerOwns(playerColor)){
                toreturn.add(t);
            }
        }
        return toreturn;
    }

    /**
     * Marks all Tracks as not being visited
     */
    public static void markUnvisited(){
        //goes through and marks all of the 
        //tracks as unvisited
        for(Track t : allTracks){
            t.markUnvisited();
        }
    }

    /**
     * Performs a depth first search on the current Track
     * and returns true if it connects to the second city
     * @param t - current track looking at
     * @param city - city's name you're looking for
     * @param playerColor - the color of the player
     * @return true if there is a path to that city 
     * or false otherwise
     */
    public static boolean dfs(Track t, String city, 
    Color playerColor ){
        //mark t as visited
        t.markVisited();
        //if the current track has the city being searched for
        if(t.getCity1().equals(city) || 
        t.getCity2().equals(city)){
            return true;
        }
        // for each Track adj in V adjacent to t do
        // such that it is the players color
        // if it is france or new york then it returns an empty
        // arraylist
        ArrayList<Track> adjTracks = 
            getAdjTrack(t,playerColor,t.getCity1());
        //add from other city...this is to protect France issues
        adjTracks.addAll(getAdjTrack(t,playerColor,t.getCity2()));
        //check all paths from one side
        for(Track adj : adjTracks){
            //if adj is unvisited
            if(!adj.isVisited()){
                if(adj.getCity1().equals(city) ||
                adj.getCity2().equals(city)){
                    return true;
                }
                // dfs(adj)
                if(dfs(adj,city, playerColor)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines the longest path length
     */
    public static int longestPath(Player p){
        //Set the initial path length
        int max=0;
        //Call a modified dfs on all tracks
        //Go through and mark each track as visited and record 
        //length of path traveled and return the max along the way
        for(Track t : allTracks){
            // If the track is owned by the player
            if(t.playerOwn(p)){
                //Mark all as unvisited
                markUnvisited();
                //get longest path from current track
                int compare1 = 
                    longestPathFromTrack(t, p.getColor() , t.getCity1());
                int compare2 = 
                    longestPathFromTrack(t, p.getColor() , t.getCity2());
                if(compare1>max) max=compare1;
                if(compare2>max) max=compare2;
            }
        }
        return max;
    }

    /**
     * Algorithm for finding the longest path from
     * the particular track t
     * @param t - current track in question
     * @param playerColor - the players color
     * @param city - the city that the player came from
     * @return length of the longest 
     * path from the particular track
     */
    public static int longestPathFromTrack(Track t, 
    Color playerColor, String city){
        //Mark current track as visited
        t.markVisited();
        //Get adjacent tracks from the current track
        // that the player owns
        ArrayList<Track> adjTracks = 
            getAdjTrack(t, playerColor, city);
        //No tracks to look for
        if(adjTracks.size()==0) {
            //if player owns current track return size
            if(t.playerOwns(playerColor))
                return t.getLength();
            //else return 0
            else
                return 0;
        }
        // max path length
        int max =0;
        for(Track adj : adjTracks){
            //if adj track is unvisited
            //take it!
            if(!adj.isVisited()){
                //Get next city
                String nextCity  = (adj.getCity1().equals(city))? 
                        adj.getCity2() : adj.getCity1();
                //Get length of that path to compare
                int compare = longestPathFromTrack(adj, 
                        playerColor, nextCity);
                //Reset maximum
                if(compare>max){
                    max=compare;
                }
            }
        }        
        //Mark as unvisited
        t.markUnvisited();
        //Add on the length of the current track
        return max + t.getLength();
    }

    /**
     * Returns an ArrayList of all of the Tracks 
     * linked to the particular city if
     * that player holds that particular Track
     * 
     * @param t - Track to find adjacent Tracks to
     * @param playerColor - the color of the player
     * @param city - City the person is coming from
     * @return all Tracks connecting to that city
     */
    public static ArrayList<Track> getAdjTrack(Track track, 
    Color playerColor, String city){
        ArrayList<Track> toReturn = new ArrayList<Track>();
        //if the track player owns
        if(!track.playerOwns(playerColor)){
            return new ArrayList<Track>();
        }
        //if it is france then dead end
        //if it is new york then dead end also
        if(city.equals("France") ||
        city.equals("New York")) return new ArrayList<Track>();

        //Go through all of the tracks
        for(Track t : allTracks){
            if((t.getCity1().equals(city) || 
                t.getCity2().equals(city))               
            && t.playerOwns(playerColor)){
                toReturn.add(t);
            }
        }
        return toReturn;
    }

    /*
     * static intializer block for all of the tracks
     */
    static {
        addedOffset=false;
        allTracks = new ArrayList<Track>();
        ArrayList<EColor> c = new ArrayList<EColor>();

        // ---------- Track from Ullapool to Fort William ----------

        ArrayList<Polygon> spots = new ArrayList<Polygon>();

        // Draw & Add Path 1
        int[] xCoord = new int[]{385,393,406,398};
        int[] yCoord = new int[]{153,157,131,126};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 2
        xCoord = new int[]{399,407,419,412};
        yCoord = new int[]{125,128,103, 99};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.PINK);
        c.add(EColor.PINK);

        // Add Track
        allTracks.add(new Track("Ullapool",
                "Fort William", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Fort William to Inverness ----------

        // Draw & Add Path 1
        xCoord = new int[]{394,399,422,419   };
        yCoord = new int[]{169,174,160,152  };
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{421,444,449, 425};
        yCoord = new int[]{151,136, 143, 158};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.BLACK);
        c.add(EColor.BLACK);

        // Add Track
        allTracks.add(new Track("Fort William",
                "Inverness", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Ullapool to Wick ----------

        // Draw & Add Path 1
        xCoord = new int[]{441,466,470,445};
        yCoord = new int[]{79,67,74,86};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{472,501,501, 472};
        yCoord = new int[]{65,65,75,75};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{506,533,531,503};
        yCoord = new int[]{66,74,82,75};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.YELLOW);
        c.add(EColor.YELLOW);
        c.add(EColor.YELLOW);

        // Add Track
        allTracks.add(new Track("Ullapool","Wick", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Ullapool to Inverness----------

        // Draw & Add Path 1
        xCoord = new int[]{434,442,456,448};
        yCoord = new int[]{99,95,120,124};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.ORANGE);

        // Add Track
        allTracks.add(new Track("Ullapool","Inverness", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Inverness to Wick ----------

        // Draw & Add Path 1
        xCoord = new int[]{471,496,501,476};
        yCoord = new int[]{118,105,112,125};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{499,524,528,503};
        yCoord = new int[]{103,90,97,110};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.RED);
        c.add(EColor.RED);

        // Add Track
        allTracks.add(new Track("Inverness","Wick", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Inverness to Aberdeen ----------

        // Draw & Add Path 1
        xCoord = new int[]{467,470,498,494 };
        yCoord = new int[]{141,133,142, 150 };
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{497, 503, 521, 515};
        yCoord = new int[]{152,146,168,  173  };
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{517, 525, 533,526};
        yCoord = new int[]{176, 173,200, 202};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.PINK);
        c.add(EColor.PINK);
        c.add(EColor.PINK);

        // Add Track
        allTracks.add(new Track("Inverness","Aberdeen", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Inverness to Dundee ----------

        // Draw & Add Path 1
        xCoord = new int[]{457,465,471,462};
        yCoord = new int[]{146,145,172,173};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{463,471,478,469};
        yCoord = new int[]{177,175,202,204};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{470,478,484,475};
        yCoord = new int[]{207,206,231,234};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.BLUE);
        c.add(EColor.BLUE);
        c.add(EColor.BLUE);

        // Add Track
        allTracks.add(new Track("Inverness","Dundee", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Fort William to Dundee ----------

        // Draw & Add Path 1
        xCoord = new int[]{393,399,422,416};
        yCoord = new int[]{190,183,200,206};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{418,423,447,441};
        yCoord = new int[]{207,201,217,224};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{448,471,466,443};
        yCoord = new int[]{219,236,242,225};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GREEN);
        c.add(EColor.GREEN);
        c.add(EColor.GREEN);

        // Add Track
        allTracks.add(new Track("Fort William","Dundee", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Fort William to Glasgow ----------

        // Draw & Add Path 1
        xCoord = new int[]{382,391,391,382};
        yCoord = new int[]{200,200,227,227};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{382,391,392,384};
        yCoord = new int[]{231,231,258,258};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.ORANGE);
        c.add(EColor.ORANGE);

        // Add Track
        allTracks.add(new Track("Fort William","Glasgow", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Dundee to Edinburgh ----------

        // Draw & Add Path 
        xCoord = new int[]{462,469,452,446};
        yCoord = new int[]{251,256,278,272};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.YELLOW);
        allTracks.add(new Track("Dundee","Edinburgh", spots, c ));

        spots.clear();
        c.clear();

        // Draw & Add Path 
        xCoord = new int[]{472,478,460,454};
        yCoord = new int[]{259,263,285,280};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        c.add(EColor.RED);

        // Add Track
        allTracks.add(new Track("Dundee","Edinburgh", spots, c ));
        allTracks.get(allTracks.size()-1).setDoublePath(
            allTracks.get(allTracks.size()-2));
        spots.clear();
        c.clear();

        // ---------- Track from Dundee to Aberdeen ----------

        // Draw & Add Path 1
        xCoord = new int[]{498,493,517,522};
        yCoord = new int[]{241,234,219,226};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.WHITE);       

        // Add Track
        allTracks.add(new Track("Dundee","Aberdeen", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Edinburgh to Newcastle ----------

        // Draw & Add Path 1
        xCoord = new int[]{464,456,463,472};
        yCoord = new int[]{394,366,364,391};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{455,448,455,463};
        yCoord = new int[]{363,338,335,361};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{447,439,447,454};
        yCoord = new int[]{333,307,305,331};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GREEN);
        c.add(EColor.GREEN);
        c.add(EColor.GREEN);

        // Add Track
        allTracks.add(new Track("Edinburgh","Newcastle", spots, c ));

        spots.clear();
        c.clear();

        // Draw & Add Path 1
        xCoord = new int[]{475,467,474,482};
        yCoord = new int[]{390,364,361,388};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{466,459,466,474};
        yCoord = new int[]{360,334,331,358};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{458,450,458,465};
        yCoord = new int[]{331,304,302,328};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );
        c.add(EColor.PINK);
        c.add(EColor.PINK);
        c.add(EColor.PINK);

        // Add Track
        allTracks.add(new Track("Edinburgh","Newcastle", spots, c ));

        spots.clear();
        c.clear();
        //Double Path
        allTracks.get(allTracks.size()-1).setDoublePath(
            allTracks.get(allTracks.size()-2));

        // ---------- Track from Carlisle to Newcastle ----------

        // Draw & Add Path 1
        xCoord = new int[]{453,427,428,456};
        yCoord = new int[]{405,397,389,397};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.YELLOW);

        // Add Track
        allTracks.add(new Track("Carlisle","Newcastle", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Carlisle to Edinburgh ----------

        // Draw & Add Path 1
        xCoord = new int[]{410,418,426,418};
        yCoord = new int[]{369,343,346,372};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{419,427,436,427};
        yCoord = new int[]{340,314,316,342};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.ORANGE);
        c.add(EColor.ORANGE);

        // Add Track
        allTracks.add(new Track("Carlisle","Edinburgh", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Glasgow to Edinburgh ----------

        // Draw & Add Path 1
        xCoord = new int[]{399,401,428,426};
        yCoord = new int[]{287,279,287,294};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.BLACK);

        // Add Track
        allTracks.add(new Track("Glasgow","Edinburgh", spots, c ));

        spots.clear();
        c.clear();

        // Draw & Add Path 1
        xCoord = new int[]{429,402,405,431};
        yCoord = new int[]{283,276,268,276};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        c.add(EColor.BLUE);

        // Add Track
        allTracks.add(new Track("Glasgow","Edinburgh", spots, c ));

        spots.clear();
        c.clear();
        allTracks.get(allTracks.size()-1).setDoublePath(
            allTracks.get(allTracks.size()-2));
        // ---------- Track from Stranraer to Edinburgh ----------
        // Draw & Add Path 1
        xCoord = new int[]{338,366,368,341};
        yCoord = new int[]{328,320,327,335};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{371,368,395,398};
        yCoord = new int[]{326,319,310,318};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{398,425,428,401};
        yCoord = new int[]{310,302,309,318};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.WHITE);
        c.add(EColor.WHITE);
        c.add(EColor.WHITE);

        // Add Track
        allTracks.add(new Track("Stranraer","Edinburgh", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Glasgow to Stranraer ----------

        // Draw & Add Path 1
        xCoord = new int[]{327,348,354,332};
        yCoord = new int[]{321,303,309,327};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{351,372,378,356};
        yCoord = new int[]{301,283,290,308};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.RED);
        c.add(EColor.RED);

        // Add Track
        allTracks.add(new Track("Glasgow","Stranraer", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Leeds to Newcastle ----------
        // Draw & Add Path 1
        xCoord = new int[]{462,470,460,451};
        yCoord = new int[]{419,422,448,445};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{450,458,448,440};
        yCoord = new int[]{448,451,477,474};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.WHITE);
        c.add(EColor.WHITE);

        // Add Track
        allTracks.add(new Track("Leeds","Newcastle", spots, c ));

        spots.clear();
        c.clear();

        // Draw & Add Path 3
        xCoord = new int[]{472,480,470,463};
        yCoord = new int[]{423,426,452,449};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{462,469,459,451};
        yCoord = new int[]{453,455,481,478};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        c.add(EColor.ORANGE);
        c.add(EColor.ORANGE);

        // Add Track
        allTracks.add(new Track("Leeds","Newcastle", spots, c ));

        spots.clear();
        c.clear();

        //Set a double Track
        allTracks.get(allTracks.size()-1).setDoublePath(
            allTracks.get(allTracks.size()-2));

        // ---------- Track from Leeds to Hull ----------

        // Draw & Add Path 1
        xCoord = new int[]{458,482,476,453};
        yCoord = new int[]{500,516,524,507};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.YELLOW);

        // Add Track
        allTracks.add(new Track("Leeds","Hull", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Nottingham to Hull ----------
        // Draw & Add Path 1
        xCoord = new int[]{479,485,463,458};
        yCoord = new int[]{538,544,562,555};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{455,461,439,434};
        yCoord = new int[]{557,563,581,575};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.BLACK);
        c.add(EColor.BLACK);

        // Add Track
        allTracks.add(new Track("Nottingham","Hull", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Barrow to Leeds ----------

        // Draw & Add Path 1
        xCoord = new int[]{386,411,408,380};
        yCoord = new int[]{436,448,455,444};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{417,433,427,410};
        yCoord = new int[]{452,475,480,457};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GREEN);
        c.add(EColor.GREEN);

        // Add Track
        allTracks.add(new Track("Barrow","Leeds", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Carlisle to Barrow ----------

        // Draw & Add Path 1
        xCoord = new int[]{394,402,386,379};
        yCoord = new int[]{398,403,427,421};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.RED);

        // Add Track
        allTracks.add(new Track("Carlisle","Barrow", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Nottingham to Leeds ----------

        // Draw & Add Path 1
        xCoord = new int[]{423,429,437,431};
        yCoord = new int[]{568,540,542,570};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{429,434,443,437};
        yCoord = new int[]{538,511,513,540};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.PINK);
        c.add(EColor.PINK);

        // Add Track
        allTracks.add(new Track("Nottingham","Leeds", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Manchester to Leeds ----------

        // Draw & Add Red Path 1
        xCoord = new int[]{421,425,402,397};
        yCoord = new int[]{492,499,513,507};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.RED);

        // Add Track
        allTracks.add(new Track("Manchester","Leeds", spots, c ));

        spots.clear();
        c.clear();

        // Draw & Add Blue Path 1
        xCoord = new int[]{427,432,408,403};
        yCoord = new int[]{501,508,523,517};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.BLUE);
        // Add Track
        allTracks.add(new Track("Manchester","Leeds", spots, c ));

        spots.clear();
        c.clear();
        //Set as double Track
        allTracks.get(allTracks.size()-1).setDoublePath(
            allTracks.get(allTracks.size()-2));

        // ---------- Track from Liverpool to Leeds ----------

        // Draw & Add Path 1
        xCoord = new int[]{360,388,388,360};
        yCoord = new int[]{478,478,486,486};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{391,420,420,391};
        yCoord = new int[]{478,478,486,486};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.BLACK);
        c.add(EColor.BLACK);

        // Add Track
        allTracks.add(new Track("Liverpool","Leeds", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Liverpool to Manchester ----------

        // Draw & Add Orange Path 1
        xCoord = new int[]{358,383,379,354};
        yCoord = new int[]{489,503,511,496};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.ORANGE);

        // Add Track
        allTracks.add(new Track("Liverpool","Manchester", spots, c ));

        spots.clear();
        c.clear();

        // Write what each block needs
        c.add(EColor.PINK);

        // Draw & Add Pink Path 2
        xCoord = new int[]{353,377,373,348};
        yCoord = new int[]{498,512,520,506};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Add Track
        allTracks.add(new Track("Liverpool","Manchester", spots, c ));

        spots.clear();
        c.clear();

        //Set Double Tracks
        allTracks.get(allTracks.size()-1).setDoublePath(
            allTracks.get(allTracks.size()-2));
        // ---------- Track from Belfast to Londonderry ----------

        // Draw & Add Path 1
        xCoord = new int[]{220,229,241,234};
        yCoord = new int[]{278,274,299,303};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{235,243,256,248};
        yCoord = new int[]{306,302,326,330};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.ORANGE);
        c.add(EColor.ORANGE);

        // Add Track
        allTracks.add(new Track("Londonderry","Belfast", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Belfast to Dundalk ----------

        // Draw & Add White Path 1
        xCoord = new int[]{218,240,246,225};
        yCoord = new int[]{361,342,348,368};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.WHITE);

        // Add Track
        allTracks.add(new Track("Belfast","Dundalk", spots, c ));

        spots.clear();
        c.clear();

        // Draw & Add Red Path 2
        xCoord = new int[]{227,232,254,248};
        yCoord = new int[]{370,376,357,351};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        c.add(EColor.RED);

        // Add Track
        allTracks.add(new Track("Belfast","Dundalk", spots, c ));

        spots.clear();
        c.clear();

        //Set as double Track
        allTracks.get(allTracks.size()-1).setDoublePath(
            allTracks.get(allTracks.size()-2));

        // ---------- Track from Dundalk to Londonderry ----------
        // Draw & Add Path 1
        xCoord = new int[]{205,213,205,196};
        yCoord = new int[]{364,361,334,336};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{196,205,207,198};
        yCoord = new int[]{330,331,303,302};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{199,207,217,208};
        yCoord = new int[]{298,300,273,271};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.PINK);
        c.add(EColor.PINK);
        c.add(EColor.PINK);

        // Add Track
        allTracks.add(new Track("Dundalk","Londonderry", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Hull to Norwich ----------

        // Draw & Add Path 1
        xCoord = new int[]{488,498,495,486};
        yCoord = new int[]{547,547,576,577};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{485,495,502,493};
        yCoord = new int[]{581,579,607,609};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{497,503,522,517};
        yCoord = new int[]{615,610,629,635};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{520,525,549,544};
        yCoord = new int[]{637,631,646,653};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);

        // Add Track
        allTracks.add(new Track("Hull","Norwich", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Norwich to Nottingham ----------

        // Draw & Add Path 1
        xCoord = new int[]{436,441,465,460};
        yCoord = new int[]{598,590,605,613};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{462,467,491,486};
        yCoord = new int[]{614,607,623,630};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{488,512,518,493};
        yCoord = new int[]{631,646,639,624};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 4
        xCoord = new int[]{514,520,543,538};
        yCoord = new int[]{647,641,655,663};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.WHITE);
        c.add(EColor.WHITE);
        c.add(EColor.WHITE);
        c.add(EColor.WHITE);

        // Add Track
        allTracks.add(new Track("Norwich","Nottingham", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Nottingham to Cambridge ----------

        // Draw & Add Path 1
        xCoord = new int[]{432,440,453,445};
        yCoord = new int[]{608,604,629,633};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{446,454,466,458};
        yCoord = new int[]{636,632,658,662};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);

        // Add Track
        allTracks.add(new Track("Nottingham","Cambridge", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Cambridge to Norwich ----------

        // Draw & Add Path 1
        xCoord = new int[]{480,480,508,509};
        yCoord = new int[]{672,680,685,676};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{512,538,541,514};
        yCoord = new int[]{677,668,676,685};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.RED);
        c.add(EColor.RED);

        // Add Track
        allTracks.add(new Track("Cambridge","Norwich", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Cambridge to Ipswich ----------
        // Draw & Add Path 1
        xCoord = new int[]{475,482,502,496};
        yCoord = new int[]{693,686,705,711};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.BLACK);

        // Add Track
        allTracks.add(new Track("Cambridge","Ipswich", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Norwich to Ipswich ----------
        // Draw & Add Path 1
        xCoord = new int[]{536,544,528,521};
        yCoord = new int[]{683,688,712,707};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.GREEN);

        // Add Track
        allTracks.add(new Track("Norwich","Ipswich", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from London to Ibswich ----------
        // Draw & Add Path 1
        xCoord = new int[]{460,460,489,489};
        yCoord = new int[]{723,733,729,720};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.WHITE);

        // Add Track
        allTracks.add(new Track("London","Ipswich", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from London to Cambridge ----------
        // Draw & Add Orange Path 1
        xCoord = new int[]{438,450,459,446};
        yCoord = new int[]{710,684,688,714};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.ORANGE);

        // Add Track
        allTracks.add(new Track("London",
                "Cambridge", spots, c ));

        spots.clear();
        c.clear();

        // Write what each block needs
        c.add(EColor.YELLOW);

        // Draw & Add Yellow Path 2
        xCoord = new int[]{461,469,456,448};
        yCoord = new int[]{689,693,719,714};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Add Track
        allTracks.add(new Track("London",
                "Cambridge", spots, c ));

        spots.clear();
        c.clear();

        //Set as double Track
        allTracks.get(allTracks.size()-1).setDoublePath(
            allTracks.get(allTracks.size()-2));

        //Track from London to Northampton ----------
        // Draw & Add Pink Path 1
        xCoord = new int[]{416,409,417,424};
        yCoord = new int[]{704,676,675,702};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.PINK);

        // Add Track
        allTracks.add(new Track("London",
                "Northampton", spots, c ));

        spots.clear();
        c.clear();

        // Draw & Add Blue Path 1
        xCoord = new int[]{420,428,436,427};
        yCoord = new int[]{674,672,699,701};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.BLUE);

        // Add Track
        allTracks.add(new Track("London",
                "Northampton", spots, c ));

        spots.clear();
        c.clear();

        //Set as double Track
        allTracks.get(allTracks.size()-1).setDoublePath(
            allTracks.get(allTracks.size()-2));

        //Track from Northampton to Cambridge ----------

        // Draw & Add Path 1
        xCoord = new int[]{429,426,453,456};
        yCoord = new int[]{654,663,673,664};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.GRAY);

        // Add Track
        allTracks.add(new Track("Northampton",
                "Cambridge", spots, c ));

        spots.clear();
        c.clear();

        //Track from Northampton to Nottingham ----------
        // Draw & Add Path 1
        xCoord = new int[]{417,426,423,414};
        yCoord = new int[]{607,608,637,635};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.ORANGE);

        // Add Track
        allTracks.add(new Track("Northampton",
                "Nottingham", spots, c ));

        spots.clear();
        c.clear();

        //Track from Birmingham to Nottingham ----------

        // Draw & Add Path 1
        xCoord = new int[]{383,409,413,387};
        yCoord = new int[]{600,588,596,608};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.GRAY);

        // Add Track
        allTracks.add(new Track("Birmingham",
                "Nottingham", spots, c ));

        spots.clear();
        c.clear();

        //Track from Birmingham to Manchester ----------
        // Draw & Add Black Path 1
        xCoord = new int[]{379,387,382,373};
        yCoord = new int[]{533,534,562,560};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Black Path 2
        xCoord = new int[]{372,381,375,366};
        yCoord = new int[]{563,565,593,591};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.BLACK);
        c.add(EColor.BLACK);

        // Add Track
        allTracks.add(new Track("Birmingham",
                "Manchester", spots, c ));

        spots.clear();
        c.clear();

        // Draw & Add Yellow Path 1
        xCoord = new int[]{390,399,393,384};
        yCoord = new int[]{536,537,565,563};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Yellow Path 2
        xCoord = new int[]{383,392,386,377};
        yCoord = new int[]{566,568,595,593};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.YELLOW);
        c.add(EColor.YELLOW);

        // Add Track
        allTracks.add(new Track("Birmingham",
                "Manchester", spots, c ));

        spots.clear();
        c.clear();

        //Set as double Track
        allTracks.get(allTracks.size()-1).setDoublePath(
            allTracks.get(allTracks.size()-2));

        // ---------- Track from Birmingham to Northampton ----------
        // Draw & Add White Path 1
        xCoord = new int[]{373,379,399,394};
        yCoord = new int[]{634,627,648,655};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.WHITE);

        // Add Track
        allTracks.add(new Track("Birmingham",
                "Northampton", spots, c ));

        spots.clear();
        c.clear();

        // Draw & Add Green Path 1
        xCoord = new int[]{382,388,407,401};
        yCoord = new int[]{626,621,640,646};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        c.add(EColor.GREEN);

        // Add Track
        allTracks.add(new Track("Birmingham",
                "Northampton", spots, c ));

        spots.clear();
        c.clear();

        //Set as double Track
        allTracks.get(allTracks.size()-1).setDoublePath(
            allTracks.get(allTracks.size()-2));

        // ---------- Track from Northampton to Reading ----------

        // Draw & Add Path 1
        xCoord = new int[]{395,403,389,381};
        yCoord = new int[]{670,675,700,696};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.RED);

        // Add Track
        allTracks.add(new Track("Northampton","Reading", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Reading to Southampton ----------
        // Draw & Add Path 1
        xCoord = new int[]{365,372,355,348};
        yCoord = new int[]{721,727,749,744};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.ORANGE);

        // Add Track
        allTracks.add(new Track("Reading","Southampton", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Reading to London ----------
        // Draw & Add Path 1
        xCoord = new int[]{392,390,417,419};
        yCoord = new int[]{709,718,727,719};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.GREEN);

        // Add Track
        allTracks.add(new Track("Reading","London", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from London to Dover ----------
        // Draw & Add Path 1
        xCoord = new int[]{442,449,469,463};
        yCoord = new int[]{744,738,757,763};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{465,471,493,487};
        yCoord = new int[]{765,759,778,784};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);

        // Add Track
        allTracks.add(new Track("London","Dover", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Brighton to London ----------
        // Draw & Add Path 1
        xCoord = new int[]{427,435,423,415};
        yCoord = new int[]{751,755,781,776};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.GRAY);

        // Add Track
        allTracks.add(new Track("Brighton","London", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Brighton to Dover ----------
        // Draw & Add Path 1
        xCoord = new int[]{424,454,454,425};
        yCoord = new int[]{788,788,797,797};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{456,456,485,485};
        yCoord = new int[]{788,797,797,788};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.PINK);
        c.add(EColor.PINK);

        // Add Track
        allTracks.add(new Track("Brighton","Dover", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Brighton to Southampton ----------
        // Draw & Add Path 1
        xCoord = new int[]{365,393,391,363};
        yCoord = new int[]{775,783,792,784};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.BLUE);

        // Add Track
        allTracks.add(new Track("Brighton","Southampton", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Southampton to Bristol ----------
        // Draw & Add Path 1
        xCoord = new int[]{330,315,323,337};
        yCoord = new int[]{753,727,723,748};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{314,322,308,300};
        yCoord = new int[]{724,720,697,701};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GREEN);
        c.add(EColor.GREEN);

        // Add Track
        allTracks.add(new Track("Southampton","Bristol", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Bristol to Reading ----------
        // Draw & Add Path 1
        xCoord = new int[]{309,312,339,336};
        yCoord = new int[]{691,682,692,701};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{338,341,368,365};
        yCoord = new int[]{702,693,702,711};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.WHITE);
        c.add(EColor.WHITE);

        // Add Track
        allTracks.add(new Track("Bristol","Reading", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Reading to Birmingham ----------
        // Draw & Add Path 1
        xCoord = new int[]{368,377,368,359};
        yCoord = new int[]{698,695,668,670};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{359,368,370,361};
        yCoord = new int[]{664,665,637,636};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);

        // Add Track
        allTracks.add(new Track("Reading","Birmingham", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Southampton to London ----------
        // Draw & Add Red Path 1
        xCoord = new int[]{357,384,387,361};
        yCoord = new int[]{753,743,751,760};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Red Path 2
        xCoord = new int[]{387,390,416,413};
        yCoord = new int[]{742,750,739,732};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.RED);
        c.add(EColor.RED);

        // Add Track
        allTracks.add(new Track("Southampton","London", spots, c ));

        spots.clear();
        c.clear();

        // Draw & Add Black Path 1
        xCoord = new int[]{362,388,391,365};
        yCoord = new int[]{763,753,761,771};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Black Path 2
        xCoord = new int[]{391,394,420,417};
        yCoord = new int[]{752,760,750,742};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );
        // Write what each block needs
        c.add(EColor.BLACK);
        c.add(EColor.BLACK);

        // Add Track
        allTracks.add(new Track("Southampton","London", spots, c ));

        spots.clear();
        c.clear();

        //Set as double Track
        allTracks.get(allTracks.size()-1).setDoublePath(
            allTracks.get(allTracks.size()-2));

        //Track from Cardiff to Birmingham Wells ----------

        // Draw & Add Blue Path 1
        xCoord = new int[]{269,296,299,272};
        yCoord = new int[]{640,630,638,648};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Blue Path 2
        xCoord = new int[]{298,324,327,301};
        yCoord = new int[]{629,619,626,637};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Blue Path 3
        xCoord = new int[]{327,353,356,330};
        yCoord = new int[]{618,608,616,626};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.BLUE);
        c.add(EColor.BLUE);
        c.add(EColor.BLUE);

        // Add Track
        allTracks.add(new Track("Cardiff","Birmingham", spots, c ));

        spots.clear();
        c.clear();

        // Draw & Add Orange Path 1
        xCoord = new int[]{273,299,303,276};
        yCoord = new int[]{651,640,648,658};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Orange Path 2
        xCoord = new int[]{302,328,331,305};
        yCoord = new int[]{639,629,637,647};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Orange Path 3
        xCoord = new int[]{331,357,360,334};
        yCoord = new int[]{628,618,626,636};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.ORANGE);
        c.add(EColor.ORANGE);
        c.add(EColor.ORANGE);

        // Add Track
        allTracks.add(new Track("Cardiff","Birmingham", spots, c ));

        spots.clear();
        c.clear();

        //Set as double Track
        allTracks.get(allTracks.size()-1).setDoublePath(
            allTracks.get(allTracks.size()-2));

        //Track from Llandrindod Wells to Birmingham ----------
        // Draw & Add Path 1
        xCoord = new int[]{303,330,333,306};
        yCoord = new int[]{592,583,591,600};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{339,335,360,365};
        yCoord = new int[]{583,591,605,597};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.RED);
        c.add(EColor.RED);

        // Add Track
        allTracks.add(new Track("Llandrindod Wells",
                "Birmingham", spots, c ));

        spots.clear();
        c.clear();

        //Track from Llandrindod Wells to Manchester ----------

        // Draw & Add Path 1
        xCoord = new int[]{300,320,327,307};
        yCoord = new int[]{580,560,566,586};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{323,346,352,329};
        yCoord = new int[]{558,540,548,565};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{349,372,377,354};
        yCoord = new int[]{539,523,530,546};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GREEN);
        c.add(EColor.GREEN);
        c.add(EColor.GREEN);

        // Add Track
        allTracks.add(new Track("Llandrindod Wells",
                "Manchester", spots, c ));

        spots.clear();
        c.clear();

        //Track from Llandrindod Wells to Holyhead ----------
        // Draw & Add Path 1
        xCoord = new int[]{288,283,291,296};
        yCoord = new int[]{581,553,552,580};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{282,278,286,291};
        yCoord = new int[]{550,523,522,548};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{277,272,280,286};
        yCoord = new int[]{519,492,491,518};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.BLUE);
        c.add(EColor.BLUE);
        c.add(EColor.BLUE);

        // Add Track
        allTracks.add(new Track("Llandrindod Wells",
                "Holyhead", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Dundalk to Sligo ----------
        // Draw & Add Path 1
        xCoord = new int[]{195,201,181,175};
        yCoord = new int[]{369,362,342,348};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{173,153,160,179};
        yCoord = new int[]{346,327,320,340};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{157,138,132,151};
        yCoord = new int[]{318,299,305,325};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.BLACK);
        c.add(EColor.BLACK);
        c.add(EColor.BLACK);

        // Add Track
        allTracks.add(new Track("Dundalk","Sligo", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Plymouth to Bristol ----------
        // Draw & Add Path 1
        xCoord = new int[]{194,197,224,223};
        yCoord = new int[]{711,721,715,706};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{226,227,254,253};
        yCoord = new int[]{705,713,708,700};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{256,257,285,283};
        yCoord = new int[]{699,707,701,693};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.YELLOW);
        c.add(EColor.YELLOW);
        c.add(EColor.YELLOW);

        // Add Track
        allTracks.add(new Track("Plymouth","Bristol", spots, c ));

        spots.clear();
        c.clear();

        //Track from Cardiff to Llandrindod Wells ----------
        // Draw & Add Path 1
        xCoord = new int[]{259,266,282,275};
        yCoord = new int[]{632,638,615,609};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.PINK);

        // Add Track
        allTracks.add(new Track("Llandrindod Wells",
                "Cardiff", spots, c ));

        spots.clear();
        c.clear();

        //Track from Llandrindod Wells to Aberystwyth ----------

        // Draw & Add Path 1
        xCoord = new int[]{258,264,285,279};
        yCoord = new int[]{575,569,588,594};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.WHITE);

        // Add Track
        allTracks.add(new Track("Llandrindod Wells",
                "Aberystwyth", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Aberystwyth to Carmarthen ----------
        // Draw & Add Path 1
        xCoord = new int[]{221,229,246,238};
        yCoord = new int[]{594,599,576,571};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.YELLOW);

        // Add Track
        allTracks.add(new Track("Aberystwyth",
                "Carmarthen", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Carmarthen to Cardiff ----------

        // Draw & Add Path 1
        xCoord = new int[]{225,231,251,243};
        yCoord = new int[]{624,617,637,643};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.RED);

        // Add Track
        allTracks.add(new Track("Carmarthen","Cardiff", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Rosslare to Dublin ----------
        // Draw & Add White Path 1
        xCoord = new int[]{142,156,164,149};
        yCoord = new int[]{492,468,472,496};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add White Path 2
        xCoord = new int[]{158,165,180,173};
        yCoord = new int[]{465,469,446,441};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.WHITE);
        c.add(EColor.WHITE);

        // Add Track
        allTracks.add(new Track("Rosslare","Dublin", spots, c ));

        spots.clear();
        c.clear();

        // Draw & Add Black Path 3
        xCoord = new int[]{183,189,175,168};
        yCoord = new int[]{447,451,475,471};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Black Path 3
        xCoord = new int[]{166,173,159,151};
        yCoord = new int[]{473,478,502,497};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.BLACK);
        c.add(EColor.BLACK);

        // Add Track
        allTracks.add(new Track("Rosslare","Dublin", spots, c ));

        spots.clear();
        c.clear();

        //Set as double Track
        allTracks.get(allTracks.size()-1).setDoublePath(
            allTracks.get(allTracks.size()-2));

        // ---------- Track from Tullamore to Dublin ----------
        // Draw & Add Green Path 1
        xCoord = new int[]{148,146,174,176};
        yCoord = new int[]{410,418,424,416};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.GREEN);

        // Add Track
        allTracks.add(new Track("Tullamore","Dublin", spots, c ));

        spots.clear();
        c.clear();

        // Draw & Add Orange Path 2
        xCoord = new int[]{145,144,171,173};
        yCoord = new int[]{421,429,435,428};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.ORANGE);

        // Add Track
        allTracks.add(new Track("Tullamore","Dublin", spots, c ));

        spots.clear();
        c.clear();

        //Set as double Track
        allTracks.get(allTracks.size()-1).setDoublePath(
            allTracks.get(allTracks.size()-2));

        // ---------- Track from Dublin to Dundalk ----------
        // Draw & Add Yellow Path 1
        xCoord = new int[]{183,192,201,193};
        yCoord = new int[]{413,416,389,386};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.YELLOW);        

        // Add Track
        allTracks.add(new Track("Dublin","Dundalk", spots, c ));

        spots.clear();
        c.clear();        

        // Draw & Add Blue Path 2
        xCoord = new int[]{195,202,212,204};
        yCoord = new int[]{417,419,393,390};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        c.add(EColor.BLUE);

        // Add Track
        allTracks.add(new Track("Dublin","Dundalk", spots, c ));

        spots.clear();
        c.clear();

        //Set as double Track
        allTracks.get(allTracks.size()-1).setDoublePath(
            allTracks.get(allTracks.size()-2));

        // ---------- Track from Rosslare to Tullamore ----------

        // Draw & Add Path 1
        xCoord = new int[]{128,137,134,125};
        yCoord = new int[]{494,493,465,466};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{125,134,135,126};
        yCoord = new int[]{462,462,434,434};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.RED);
        c.add(EColor.RED);

        // Add Track
        allTracks.add(new Track("Rosslare","Tullamore", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Rosslare to Cork ----------
        // Draw & Add Path 1
        xCoord = new int[]{122,120,93,94};
        yCoord = new int[]{503,512,507,499};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{91,90,62,63};
        yCoord = new int[]{498,506,501,493};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.BLUE);
        c.add(EColor.BLUE);

        // Add Track
        allTracks.add(new Track("Rosslare","Cork", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Cork to Tullamore ----------
        // Draw & Add Path 1
        xCoord = new int[]{52,58,79,72};
        yCoord = new int[]{483,490,470,464};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{74,81,102,95};
        yCoord = new int[]{462,469,449,443};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{98,104,125,118};
        yCoord = new int[]{442,448,429,422};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.YELLOW);
        c.add(EColor.YELLOW);
        c.add(EColor.YELLOW);

        // Add Track
        allTracks.add(new Track("Cork","Tullamore", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Cork to Limerick ----------
        // Draw & Add Path 1
        xCoord = new int[]{42,48,57,51};
        yCoord = new int[]{474,446,448,476};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.PINK);

        // Add Track
        allTracks.add(new Track("Cork","Limerick", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Limerick to Tullamore ----------

        // Draw & Add Path 1
        xCoord = new int[]{75,102,104,76};
        yCoord = new int[]{422,418,427,431};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.GRAY);

        // Add Track
        allTracks.add(new Track("Limerick","Tullamore", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Limerick to Galway ----------

        // Draw & Add Path 1
        xCoord = new int[]{50,59,59,50};
        yCoord = new int[]{382,382,411,411};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.YELLOW);

        // Add Track
        allTracks.add(new Track("Limerick","Galway", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Tullamore to Sligo ----------
        // Draw & Add Path 1
        xCoord = new int[]{126,135,133,124};
        yCoord = new int[]{399,399,370,371};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{124,133,131,123};
        yCoord = new int[]{368,367,340,340};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{122,131,130,121};
        yCoord = new int[]{337,337,309,309};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.BLUE);
        c.add(EColor.BLUE);
        c.add(EColor.BLUE);

        // Add Track
        allTracks.add(new Track("Tullamore","Sligo", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Tullamore to Galway ----------
        // Draw & Add Path 1
        xCoord = new int[]{63,86,91,69};
        yCoord = new int[]{380,397,391,373};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{93,88,111,116};
        yCoord = new int[]{392,399,416,409};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);

        // Add Track
        allTracks.add(new Track("Tullamore","Galway", spots, c ));

        spots.clear();
        c.clear();

        // Draw & Add Path 3
        xCoord = new int[]{70,92,98,76};
        yCoord = new int[]{372,388,381,364};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{100,94,117,122};
        yCoord = new int[]{383,390,407,401};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);

        // Add Track
        allTracks.add(new Track("Tullamore","Galway", spots, c ));

        spots.clear();
        c.clear();

        //Set as double Track
        allTracks.get(allTracks.size()-1).setDoublePath(
            allTracks.get(allTracks.size()-2));

        // ---------- Track from Galway to Sligo ----------

        // Draw & Add Path 1
        xCoord = new int[]{63,69,90,84};
        yCoord = new int[]{343,350,330,324};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{86,92,112,106};
        yCoord = new int[]{322,328,309,302};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.ORANGE);
        c.add(EColor.ORANGE);

        // Add Track
        allTracks.add(new Track("Galway","Sligo", spots, c ));

        spots.clear();
        c.clear();

        // ---------- Track from Sligo to Londonderry ----------

        // Draw & Add Path 1
        xCoord = new int[]{140,143,170,167};
        yCoord = new int[]{280,287,277,269};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{169,172,198,195};
        yCoord = new int[]{269,277,266,259};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GREEN);
        c.add(EColor.GREEN);

        // Add Track
        allTracks.add(new Track("Sligo","Londonderry", spots, c ));

        spots.clear();
        c.clear();

        /**
         *Everything below this line contains a locomotive Track
         * *****************************************************
         */

        //Locomotive Track from Wick to Aberdeen ----------
        // Draw & Add Path 1
        xCoord = new int[]{539,539, 541, 547, 548, 548};
        yCoord = new int[]{131, 105, 103,103,105,130};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{539,539,548,548};
        yCoord = new int[]{161,132,132,161};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{539,539,547,547,545,540};
        yCoord = new int[]{189,164,164,189,191,191};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Wick","Aberdeen", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        //Locomotive Track from Stornoway to Wick ----------

        // Draw & Add Path 1
        xCoord = new int[]{436,411,408,408,411,435};
        yCoord = new int[]{45,45,43,38,36,36};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{438,467,467,438};
        yCoord = new int[]{36,36,45,45};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{470,497,497,470};
        yCoord = new int[]{36,36,45,44};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 4
        xCoord = new int[]{500,529,529,500};
        yCoord = new int[]{36,36,44,44};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 5
        xCoord = new int[]{530,538,547,547,542,538};
        yCoord = new int[]{46,42,63,69,71,67};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.LOCOMOTIVE);
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Stornoway","Wick", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Stornoway to Fort William ----------

        // Draw & Add Path 1
        xCoord = new int[]{357,352,372,377,379,377};
        yCoord = new int[]{59,52,40,39,43,46};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{348,354,339,332};
        yCoord = new int[]{57,61,84,80};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{329,338,337,328};
        yCoord = new int[]{87,88,116,115};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 4
        xCoord = new int[]{337,330,342,349};
        yCoord = new int[]{121,124,148,144};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 5
        xCoord = new int[]{352,368,370,368,364,346};
        yCoord = new int[]{147,160,165,168,168,153};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Stornoway",
                "Fort William", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        //Track from Londonderry to Fort William ----------

        // Draw & Add Path 1
        xCoord = new int[]{343,362,368,371,369,347};
        yCoord = new int[]{193,183,182,185,190,201};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{345,320,316,340};
        yCoord = new int[]{202,215,208,195};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{313,317,292,288};
        yCoord = new int[]{210,217,229,222};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 4
        xCoord = new int[]{285,289,265,262};
        yCoord = new int[]{223,230,243,236};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 5
        xCoord = new int[]{258,262,241,238,235,236};
        yCoord = new int[]{237,245,255,256,252,248};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Londonderry",
                "Fort William", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Stornoway to Ullapool ----------

        // Draw & Add Path 1
        xCoord = new int[]{409,420,421,417,413,401,402,405};
        yCoord = new int[]{52,68,73,76,74,57,53,51};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Stornoway","Ullapool", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Edinburgh to Aberdeen ----------

        // Draw & Add Path 1
        xCoord = new int[]{466,488,484,462,460,462};
        yCoord = new int[]{292,304,311,300,296,293};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{491,512,518,497};
        yCoord = new int[]{303,285,292,310};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{513,525,533,521};
        yCoord = new int[]{283,257,261,286};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 4
        xCoord = new int[]{525,525,527,532,534,534};
        yCoord = new int[]{255,230,227,227,230,255};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Edinburgh","Aberdeen", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Newcastle to Aberdeen ----------
        // Draw & Add Path 1
        xCoord = new int[]{546,546,536,536,538,543};
        yCoord = new int[]{230,255,255,230,227,227};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{536,545,546,536};
        yCoord = new int[]{257,257,285,285};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{536,545,543,534};
        yCoord = new int[]{288,289,316,315};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 4
        xCoord = new int[]{534,542,535,526};
        yCoord = new int[]{318,321,348,345};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 5
        xCoord = new int[]{526,534,519,512};
        yCoord = new int[]{348,353,377,372};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 6
        xCoord = new int[]{509,516,498,495,492,492};
        yCoord = new int[]{375,381,398,398,396,392};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Newcastle","Aberdeen", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Glasgow to Londonderry ----------

        // Draw & Add Path 1
        xCoord = new int[]{278,277,252,249,250,253};
        yCoord = new int[]{260,269,268,265,261,259};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{280,308,308,280};
        yCoord = new int[]{261,262,272,270};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{311,338,338,310};
        yCoord = new int[]{263,265,274,272};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 4
        xCoord = new int[]{342,365,369,369,366,341};
        yCoord = new int[]{266,267,270,275,277,275};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Glasgow","Londonderry", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Hull to Newcastle ----------
        // Draw & Add Path 1
        xCoord = new int[]{521,500,495,493,495,517};
        yCoord = new int[]{417,407,408,411,415,424};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{526,519,536,543};
        yCoord = new int[]{422,427,449,444};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{537,545,548,540};
        yCoord = new int[]{453,452,479,480};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 2
        xCoord = new int[]{540,547,537,529};
        yCoord = new int[]{483,487,512,508};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{527,532,514,509,506,507};
        yCoord = new int[]{511,518,532,533,530,525};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Hull","Newcastle", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Liverpool to Barrow ----------

        // Draw & Add Path 1
        xCoord = new int[]{357,362,366,367,358,353,350,349};
        yCoord = new int[]{452,450,452,456,473,476,475,469};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Liverpool","Barrow", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Carlisle to Stranraer ----------
        // Draw & Add Path 1
        xCoord = new int[]{389,366,363,387,392,393};
        yCoord = new int[]{391,396,387,382,383,387};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{362,357,333,337};
        yCoord = new int[]{386,395,380,372};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{334,326,318,320,322,326};
        yCoord = new int[]{371,374,352,347,346,349};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Carlisle","Stranraer", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Londonderry to Stranraer ----------

        // Draw & Add Path 1
        xCoord = new int[]{256,235,235,237,241,261};
        yCoord = new int[]{290,275,271,269,269,283};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{263,258,281,286};
        yCoord = new int[]{284,291,307,300};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{289,284,303,308,310,308};
        yCoord = new int[]{302,309,324,323,320,315};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Londonderry",
                "Stranraer", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Belfast to Stranraer ----------

        // Draw & Add Path 1
        xCoord = new int[]{280,298,304,304,300,280,276,275};
        yCoord = new int[]{332,332,334,338,341,342,339,335};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Belfast","Stranraer", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Barrow to Belfast ----------

        // Draw & Add Path 1
        xCoord = new int[]{287,269,268,270,274,293};
        yCoord = new int[]{371,355,350,348,349,364};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{295,316,311,289};
        yCoord = new int[]{366,384,391,372};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{313,319,340,334};
        yCoord = new int[]{393,387,404,410};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{337,343,360,362,359,354};
        yCoord = new int[]{413,406,421,426,429,428};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Barrow","Belfast", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Dover to France ----------

        // Draw & Add Path 1
        xCoord = new int[]{534,539,519,515,512,514};
        yCoord = new int[]{817,810,796,796,801,805};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{541,536,557,561,563,562};
        yCoord = new int[]{812,819,831,831,829,824};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.LOCOMOTIVE);
        c.add(EColor.GRAY);

        // Add Track
        allTracks.add(new Track("Dover","France", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Hull to Newcastle ----------
        // Draw & Add Path 1
        xCoord = new int[]{521,500,495,493,495,517};
        yCoord = new int[]{417,407,408,411,415,424};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{526,519,536,543};
        yCoord = new int[]{422,427,449,444};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{537,545,548,540};
        yCoord = new int[]{453,452,479,480};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 2
        xCoord = new int[]{540,547,537,529};
        yCoord = new int[]{483,487,512,508};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{527,532,514,509,506,507};
        yCoord = new int[]{511,518,532,533,530,525};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Hull","Newcastle", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Liverpool to Barrow ----------

        // Draw & Add Path 1
        xCoord = new int[]{357,362,366,367,358,353,350,349};
        yCoord = new int[]{452,450,452,456,473,476,475,469};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Liverpool","Barrow", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();
        spots.clear();
        c.clear();

        // ---------- Track from Carlisle to Stranraer ----------
        // Draw & Add Path 1
        xCoord = new int[]{389,366,363,387,392,393};
        yCoord = new int[]{391,396,387,382,383,387};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{362,357,333,337};
        yCoord = new int[]{386,395,380,372};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{334,326,318,320,322,326};
        yCoord = new int[]{371,374,352,347,346,349};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Carlisle","Stranraer", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Londonderry to Stranraer ----------

        // Draw & Add Path 1
        xCoord = new int[]{256,235,235,237,241,261};
        yCoord = new int[]{290,275,271,269,269,283};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{263,258,281,286};
        yCoord = new int[]{284,291,307,300};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{289,284,303,308,310,308};
        yCoord = new int[]{302,309,324,323,320,315};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Londonderry","Stranuer", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Belfast to Stranraer ----------

        // Draw & Add Path 1
        xCoord = new int[]{280,298,304,304,300,280,276,275};
        yCoord = new int[]{332,332,334,338,341,342,339,335};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Belfast","Stranraer", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Barrow to Belfast ----------

        // Draw & Add Path 1
        xCoord = new int[]{287,269,268,270,274,293};
        yCoord = new int[]{371,355,350,348,349,364};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{295,316,311,289};
        yCoord = new int[]{366,384,391,372};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{313,319,340,334};
        yCoord = new int[]{393,387,404,410};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{337,343,360,362,359,354};
        yCoord = new int[]{413,406,421,426,429,428};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Barrow","Belfast", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Dover to France ----------

        // Draw & Add Path 1
        xCoord = new int[]{534,539,519,515,512,514};
        yCoord = new int[]{817,810,796,796,801,805};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{541,536,557,561,563,562};
        yCoord = new int[]{812,819,831,831,829,824};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.LOCOMOTIVE);
        c.add(EColor.GRAY);

        // Add Track
        allTracks.add(new Track("Dover","France", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Bristol to Cardiff ----------
        // Draw & Add Path 1
        xCoord = new int[]{283,267,266,267,272,288,290,287};
        yCoord = new int[]{680,669,664,662,661,672,677,681};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Write what each block needs
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Bristol","Cardiff", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Holyhead to Liverpool ----------

        // Draw & Add Path 1
        xCoord = new int[]{311,289,284,282,286,309};
        yCoord = new int[]{460,468,467,464,459,451};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{320,314,331,335,338,338};
        yCoord = new int[]{455,461,478,479,476,472};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.LOCOMOTIVE);
        c.add(EColor.GRAY);

        // Add Track
        allTracks.add(new Track("Holyhead","Liverpool", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Holyhead to Dundalk ----------
        // Draw & Add Path 1
        xCoord = new int[]{254,247,258,263,267,267};
        yCoord = new int[]{436,440,461,463,461,457};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{253,239,231,246};
        yCoord = new int[]{433,409,413,437};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{230,237,226,222,218,218};
        yCoord = new int[]{410,406,386,384,386,390};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.LOCOMOTIVE);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);

        // Add Track
        allTracks.add(new Track("Holyhead","Dundalk", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from FranceLeft to Southampton ----------
        // Draw & Add Locomotive Path 1
        xCoord = new int[]{328,337,340,344,345,335};
        yCoord = new int[]{804,781,779,780,783,807};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{326,334,320,312};
        yCoord = new int[]{806,810,834,830};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{311,317,302,298,295,296};
        yCoord = new int[]{832,838,857,858,856,851};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.LOCOMOTIVE);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);

        // Add Track
        allTracks.add(new Track("France","Southampton", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // Draw & Add Locomotive Path 4
        xCoord = new int[]{338,346,350,354,355,345};
        yCoord = new int[]{808,787,784,785,790,813};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 5
        xCoord = new int[]{336,343,329,322};
        yCoord = new int[]{812,816,840,836};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 6
        xCoord = new int[]{320,327,312,308,305,305};
        yCoord = new int[]{839,844,863,864,862,858};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.LOCOMOTIVE);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);

        // Add Track
        allTracks.add(new Track("France","Southampton", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        //Set as double Track
        allTracks.get(allTracks.size()-1).setDoublePath(
            allTracks.get(allTracks.size()-2));

        // ---------- Track from Southampton to New York ----------
        // Draw & Add Path 1
        xCoord = new int[]{326,330,333,331,310,305};
        yCoord = new int[]{772,772,775,779,793,784};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{303,307,283,278};
        yCoord = new int[]{786,794,808,799};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{276,250,253,279};
        yCoord = new int[]{801,811,820,809};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{247,249,222,220};
        yCoord = new int[]{813,821,828,819};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 1
        xCoord = new int[]{217,219,191,190};
        yCoord = new int[]{820,829,834,825};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{187,188,160,159};
        yCoord = new int[]{826,835,839,830};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{156,156,129,128};
        yCoord = new int[]{830,839,841,832};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{125,125,97,97};
        yCoord = new int[]{832,841,840,831};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{94,93,65,67};
        yCoord = new int[]{831,839,833,825};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{64,61,38,36,37,42};
        yCoord = new int[]{825,833,824,820,817,816};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.LOCOMOTIVE);
        c.add(EColor.LOCOMOTIVE);
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Southampton","New York", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Plymouth to Southampton ----------

        // Draw & Add Path 1
        xCoord = new int[]{325,328,328,325,300,300};
        yCoord = new int[]{759,762,767,768,768,759};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{298,297,268,270};
        yCoord = new int[]{759,767,764,755};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{267,265,237,239};
        yCoord = new int[]{754,763,756,748};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{237,234,207,210};
        yCoord = new int[]{747,755,746,738};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{208,204,182,180,182,186};
        yCoord = new int[]{737,745,734,730,727,726};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Plymouth","Southampton", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Penzance to Plymouth ----------
        // Draw & Add Path 1
        xCoord = new int[]{143,149,168,168,166,161};
        yCoord = new int[]{748,756,738,734,732,732};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{140,140,111,111};
        yCoord = new int[]{749,758,756,748};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{109,101,90,91,94,98};
        yCoord = new int[]{746,750,729,725,723,725};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.LOCOMOTIVE);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);

        // Add Track
        allTracks.add(new Track("Penzance","Plymouth", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        /*
         * Note: This is not a ferry Track, just need it for
         * a double Track in this section
         */

        // ---------- Track from Plymouth to Penzance ----------
        // Draw & Add Path 1
        xCoord = new int[]{102,131,129,102};
        yCoord = new int[]{706,710,718,715};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{133,161,161,132};
        yCoord = new int[]{710,714,722,719};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.BLACK);
        c.add(EColor.BLACK);

        // Add Track
        allTracks.add(new Track("Plymouth","Penzance", spots, c ));

        //Set as double Track
        allTracks.get(allTracks.size()-1).setDoublePath(
            allTracks.get(allTracks.size()-2));

        spots.clear();
        c.clear();

        // ---------- Track from Penzance to Cardiff ----------

        // Draw & Add Path 1
        xCoord = new int[]{118,124,106,102,99,100};
        yCoord = new int[]{676,683,698,699,697,692};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{124,127,153,151};
        yCoord = new int[]{674,681,673,664};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{155,156,184,183};
        yCoord = new int[]{664,672,668,658};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{185,187,214,216};
        yCoord = new int[]{658,666,662,653};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{215,239,243,244,241,217};
        yCoord = new int[]{653,649,650,654,658,662};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Penzance","Cardiff", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Penzance to Cork ----------
        // Draw & Add Path 1
        xCoord = new int[]{88,87,83,79,74,83};
        yCoord = new int[]{688,691,692,689,666,664};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{82,74,68,76};
        yCoord = new int[]{661,663,636,635};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{75,67,61,70};
        yCoord = new int[]{631,632,606,604};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{61,68,63,54};
        yCoord = new int[]{602,601,573,575};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{62,54,48,56};
        yCoord = new int[]{570,571,546,544};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{47,55,50,47,44,41};
        yCoord = new int[]{542,540,517,514,515,518};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.LOCOMOTIVE);
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Penzance","Cork", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Rosslare to Carmarthen ----------
        // Draw & Add Path 1
        xCoord = new int[]{128,137,137,135,132,129};
        yCoord = new int[]{554,554,529,526,526,529};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{130,137,149,141};
        yCoord = new int[]{560,557,583,586};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{151,146,169,174};
        yCoord = new int[]{585,592,608,601};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{176,174,199,202,203,200};
        yCoord = new int[]{603,611,616,614,609,607};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Rosslare","Carmarthen", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Rosslare to Aberystwyth ----------
        // Draw & Add Path 1
        xCoord = new int[]{181,176,155,153,155,159};
        yCoord = new int[]{529,537,527,523,521,519};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{184,181,206,209};
        yCoord = new int[]{530,538,548,540};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{213,209,231,236,237,234};
        yCoord = new int[]{541,549,559,558,554,550};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Rosslare","Aberystwyth", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Rosslare to Holyhead ----------
        // Draw & Add Path 1
        xCoord = new int[]{188,190,167,163,162,164};
        yCoord = new int[]{496,505,511,510,506,503};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{190,193,220,218};
        yCoord = new int[]{495,503,496,488};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Draw & Add Path 3
        xCoord = new int[]{221,223,247,249,247,243};
        yCoord = new int[]{487,495,489,485,481,480};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.GRAY);
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Rosslare","Holyhead", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Dublin to Holyhead ----------
        // Draw & Add Path 1
        xCoord = new int[]{227,222,201,200,203,207};
        yCoord = new int[]{451,459,446,441,439,439};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{230,225,246,250,252,249};
        yCoord = new int[]{453,461,473,472,469,465};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Dublin","Holyhead", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();

        // ---------- Track from Holyhead to Aberystwyth ----------
        // Draw & Add Path 1
        xCoord = new int[]{252,259,267,265,262,258};
        yCoord = new int[]{514,517,494,489,488,491};
        spots.add( new Polygon( xCoord,yCoord, xCoord.length));

        // Draw & Add Path 2
        xCoord = new int[]{251,260,260,258,254,251};
        yCoord = new int[]{521,521,544,547,548,544};
        spots.add( new Polygon(xCoord, yCoord , xCoord.length) );

        // Write what each block needs
        c.add(EColor.GRAY);
        c.add(EColor.LOCOMOTIVE);

        // Add Track
        allTracks.add(new Track("Holyhead","Aberystwyth", spots, c ));
        allTracks.get(allTracks.size()-1).setFerryPath();

        spots.clear();
        c.clear();
    }

}
