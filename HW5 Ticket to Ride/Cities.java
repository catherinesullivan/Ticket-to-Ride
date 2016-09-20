import java.util.ArrayList;
import java.awt.*;
import static java.lang.Math.pow;

/**
 * The city class holds all relevant data points for city labels 
 * and locations. A method is also included to help determine if 
 * a given (x,y) coordinate is near a city's location
 * on the graphic map.
 * 
 * @author (Brian Knapp, Catherine Sullivan,
 *          Jessica Juan-Aquino, Uros Antic, Benjamin Costello) 
 * @version (42.0)
 */
class City
{
    // Each city contains a protected name, country 
    //location, and (x,y) location

    //name of the city
    protected String name;  
    //location of the city
    protected Country location;
    // (x,y) coordinate of the city's location on the graphic map
    protected Point boardLocation; 

    /**
     * Constructor for city object that initializes the city name,
     * location, and (x,y) coordinate on the graphic map
     * 
     * @param name - name of the city
     * @param location - location of the city (aka country)
     * @param boardLocation - (x,y) location of the city 
     * on the graphic map
     */
    public City(String name, Country location , 
    Point boardLocation ){
        this.name = name;
        this.location = location;
        this.boardLocation = boardLocation;
    }    

    /**
     * This getter method returns the city's (x,y) 
     * location on the graphical map
     * 
     * @return boardLocation the current city's (x,y) location 
     */

    public Point getBoardLocation(){
        return boardLocation;
    }

    /**
     * This getter method returns the city's name
     * 
     * @return name the current city's name
     */
    public String getCityName(){
        return name;
    }

    /**
     * This getter method returns the city's location/country
     * 
     * @return enum type of country that the city is in
     */
    public Country getCountry(){
        return location;
    }

}

/**
 * The cities class holds an arrayList of all cities in play. 
 * Each city will have its area and centerpoint stored.
 * 
 * @author (Brian Knapp, Catherine Sullivan,
 *          Jessica Juan-Aquino, Uros Antic, Benjamin Costello) 
 * @version (42.0)
 */

public class Cities{
    protected static ArrayList<City> listOfCities;

    // Cities, area, and center points are 
    //added for each individual city
    static 
    {
        listOfCities= new ArrayList<City>();

        Country area = Country.SCOTLAND;
        //Scotland
        listOfCities.add(new City("Stornoway", area, 
                new Point(392,42)));
        listOfCities.add(new City("Wick", area, 
                new Point(544,88)));
        listOfCities.add(new City("Ullapool", area, 
                new Point(427,87)));
        listOfCities.add(new City("Inverness", area, 
                new Point(458,133)));
        listOfCities.add(new City("Fort William", area, 
                new Point(383,178)));
        listOfCities.add(new City("Aberdeen", area, 
                new Point(536,214)));
        listOfCities.add(new City("Dundee", area, 
                new Point(482,248)));
        listOfCities.add(new City("Glasgow", area, 
                new Point(386,275)));
        listOfCities.add(new City("Edinburgh", area, 
                new Point(443,293)));
        listOfCities.add(new City("Stranraer", area, 
                new Point(319,334)));

        area = Country.IRELAND;
        //Ireland
        listOfCities.add(new City("Londonderry", area, 
                new Point(219,259)));
        listOfCities.add(new City("Sligo", area, 
                new Point(123,293)));
        listOfCities.add(new City("Galway", area, 
                new Point(55,363)));
        listOfCities.add(new City("Tullamore", area, 
                new Point(131,416)));
        listOfCities.add(new City("Limerick", area, 
                new Point(56,430)));
        listOfCities.add(new City("Cork", area, 
                new Point(44,498)));
        listOfCities.add(new City("Dublin", area, 
                new Point(187,432)));
        listOfCities.add(new City("Rosslare", area, 
                new Point(139,511)));
        listOfCities.add(new City("Dundalk", area, 
                new Point(210,376)));
        listOfCities.add(new City("Belfast", area, 
                new Point(259,340)));

        area = Country.WALES;
        //wales
        listOfCities.add(new City("Holyhead", area, 
                new Point(271,476)));
        listOfCities.add(new City("Aberystwyth", area, 
                new Point(251,562)));
        listOfCities.add(new City("Carmarthen", area, 
                new Point(217,611)));
        listOfCities.add(new City("Cardiff", area, 
                new Point(258,653)));
        listOfCities.add(new City("Llandrindod Wells", area, 
                new Point(291,602)));

        area = Country.ENGLAND;
        //England
        listOfCities.add(new City("Penzance", area, 
                new Point(88,708)));
        listOfCities.add(new City("Plymouth", area, 
                new Point(178,717)));
        listOfCities.add(new City("Bristol", area, 
                new Point(297,689)));
        listOfCities.add(new City("Southampton", area, 
                new Point(346,766)));
        listOfCities.add(new City("Reading", area, 
                new Point(378,710)));

        listOfCities.add(new City("Brighton", area, 
                new Point(408,792)));
        listOfCities.add(new City("Dover", area, 
                new Point(500,792)));
        listOfCities.add(new City("London", area, 
                new Point(435,730)));
        listOfCities.add(new City("Ipswich", area, 
                new Point(510,722)));
        listOfCities.add(new City("Northampton", area, 
                new Point(411,658)));

        listOfCities.add(new City("Cambridge", area, 
                new Point(466,676)));
        listOfCities.add(new City("Norwich", area, 
                new Point(553,671)));
        listOfCities.add(new City("Birmingham", area, 
                new Point(373,614)));
        listOfCities.add(new City("Nottingham", area, 
                new Point(424,588)));
        listOfCities.add(new City("Manchester", area, 
                new Point(390,521)));

        listOfCities.add(new City("Hull", area, new Point(492,531)));
        listOfCities.add(new City("Leeds", area, new Point(441,493)));
        listOfCities.add(new City("Liverpool", area, 
                new Point(344,489)));
        listOfCities.add(new City("Barrow", area, 
                new Point(369,436)));
        listOfCities.add(new City("Carlisle", area, 
                new Point(409,388)));

        listOfCities.add(new City("Newcastle", area, 
                new Point(476,407)));

        //France
        listOfCities.add(new City("France", 
                Country.FRANCE, new Point(298,866)));
        listOfCities.add(new City("France", 
                Country.FRANCE, new Point(569,834)));
    }

    /**
     * Returns the City that matches this string or null if it 
     * cannot be found
     * 
     * @param name name of the city you're looking for
     * @return city the city object that matches the parameter
     */
    public static City getCity(String name){

        for(City city : listOfCities)
        {    
            // If the city key matches a city that currently 
            //exists, return it
            if(city.getCityName().equals(name))
            {
                return city;
            }
        }

        // If not found, return null
        return null;
    }

    /**
     * Returns the center point for a city matching this string 
     * or null if it cannot be found
     * 
     * @param name name of the city you're looking for
     * @return point the (x,y) coordinate for this city's location
     */
    public static Point getPoint(String name){

        for(City city : listOfCities)
        {           
            // If the city key matches a city (x,y) point 
            //that currently exists, return it
            if(city.getCityName().equals(name))
            {
                return city.getBoardLocation();
            }
        }

        // If not found, return null
        return null;
    }

    /**
     * Uses the distance formula to determine
     * if the point that is given in as a parameter
     * is within 10 pixels of the center of one of the
     * cities and then returns the city that its close to.
     * Returns null if no such city was found.
     * 
     * @param p Point on board in consideration
     * @return String name of city hovering over
     */
    public String cityHoveringOver(Point p)
    {
        for(City city : listOfCities)
        {
            // Locally store the (x,y) coordinates of the 
            //currently-looped city
            double x = city.getBoardLocation().getX() +245 ;
            double y = city.getBoardLocation().getY();

            // If the considered point is <= 10 pixels 
            //away from a city, return the city name
            if(121 >= pow((x-p.getX()),2) + pow((y-p.getY()),2) )
            {
                return city.getCityName();
            }
        }

        // If not found, return an empty string
        return "";
    }
}