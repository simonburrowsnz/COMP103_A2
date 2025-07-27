// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2025T2, Assignment 2
 * Name:
 * Username:
 * ID:
 */

import ecs100.*;
import java.util.*;
import java.util.Map.Entry;
import java.io.*;
import java.nio.file.*;

/**
 * MilanoSubway
 * A program to answer queries about Milan Metro subway lines and timetables for
 *  the subway services on those subway lines.
 *
 * See the assignment page for a description of the program and what you have to do.
 */

public class MilanoSubway{
    //Fields to store the collections of Stations and Lines
    private Map<String, Station> allStations = new HashMap<String, Station>(); // all stations, indexed by station name
    private Map<String, SubwayLine> allSubwayLines = new HashMap<String, SubwayLine>(); // all subway lines, indexed by name of the line

    // Fields for the GUI  (with default values)
    private String currentStationName = "Zara";     // station to get info about, or to start journey from
    private String currentLineName = "M1-north";    // subway line to get info about.
    private String destinationName = "Brenta";      // station to end journey at
    private int startTime = 1200;                   // time for enquiring about

    /**
     * main method:  set up the user interface and load the data
     */
    public static void main(String[] args){
        MilanoSubway milan = new MilanoSubway();
        milan.setupGUI();   // set up the interface
        milan.loadData();   // load all the data
    }

    /**
     * User interface has buttons for the queries and text fields to enter stations and subway line
     * You will need to implement the methods here, or comment out the button.
     */
    public void setupGUI(){
        UI.addButton("List all Stations",    this::listAllStations);
        UI.addButton("List Stations by name",this::listStationsByName);
        UI.addButton("List all Lines",       this::listAllSubwayLines);
        UI.addButton("Set Station",          this::setCurrentStation); 
        UI.addButton("Set Line",             this::setCurrentLine);
        UI.addButton("Set Destination",      this::setDestinationStation);
        UI.addTextField("Set Time (24hr)",   this::setTime);
        UI.addButton("Lines of Station",     this::listLinesOfStation);
        UI.addButton("Stations on Line",     this::listStationsOnLine);
        UI.addButton("On same line?",        this::onSameLine);
        UI.addButton("Next Services",        this::findNextServices);
        UI.addButton("Find Trip",            this::findTrip);

        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(1500, 750);
        UI.setDivider(0.2);

        UI.drawImage("data/system-map.jpg", 0, 0, 1000, 704);

    }

    /**
     * Load data files
     */
    public void loadData(){
        loadStationData();
        UI.println("Loaded Stations");
        loadSubwayLineData();
        UI.println("Loaded Subway Lines");
        // The following is only needed for the Completion and Challenge
        loadLineServicesData();
        UI.println("Loaded Line Services");
    }

    // Methods for loading data 
    // The loadData method suggests the methods you need to write.

    /*# YOUR CODE HERE */

    private void loadStationData(){
        
    }

    private void loadSubwayLineData(){
        
    }
    
    private void loadLineServicesData(){
        
    }
    // Methods for answering the queries
    // The setupGUI method suggests the methods you need to write.

    /*# YOUR CODE HERE */

    private void listAllStations(){
        
    }
    
    private void listStationsByName(){
        
    }
    
    private void listAllSubwayLines(){
        
    }
    
    private void listLinesOfStation(){
        
    }
    
    private void listStationsOnLine(){
        
    }
    
    private void onSameLine(){
        
    }
    
    private void findNextServices(){
        
    }
    
    private void findTrip(){
        
    } 

    // ======= written for you ===============
    // Methods for asking the user for station names, line names, and time.
    
    /**
     * Set the startTime.
     * If user enters an invalid time, it reports an error
     */
    public void setTime (String time){
        int newTime = startTime; //default;
        try{
            newTime=Integer.parseInt(time);
            if (newTime >=0 && newTime<2400){
                startTime = newTime;
            }
            else {
                UI.println("Time must be between 0000 and 2359");
            }
        }catch(Exception e){UI.println("Enter time as a four digit integer");}
    }

    /**
     * Ask the user for a station name and assign it to the currentStationName field
     * Must pass a collection of the names of the stations to getOptionFromList
     */
    public void setCurrentStation(){
        String name = getOptionFromList("Choose current station", allStations.keySet());
        if (name==null ) {return;}
        UI.println("Setting current station to "+name);
        currentStationName = name;
    }

    /**
     * Ask the user for a destination station name and assign it to the destinationName field
     * Must pass a collection of the names of the stations to getOptionFromList
     */
    public void setDestinationStation(){
        String name = getOptionFromList("Choose destination station", allStations.keySet());
        if (name==null ) {return;}
        UI.println("Setting destination station to "+name);
        destinationName = name;
    }

    /**
     * Ask the user for a subway line and assign it to the currentLineName field
     * Must pass a collection of the names of the lines to getOptionFromList
     */
    public void setCurrentLine(){
        String name =  getOptionFromList("Choose current subway line", allSubwayLines.keySet());
        if (name==null ) {return;}
        UI.println("Setting current subway line to "+name);
        currentLineName = name;
    }


    // 
    /**
    * Method to get a string from a dialog box with a list of options
    */
    public String getOptionFromList(String question, Collection<String> options){
        Object[] possibilities = options.toArray();
        Arrays.sort(possibilities);
        return (String)javax.swing.JOptionPane.showInputDialog
            (UI.getFrame(),
             question, "",
             javax.swing.JOptionPane.PLAIN_MESSAGE,
             null,
             possibilities,
             possibilities[0].toString());
    }
}
