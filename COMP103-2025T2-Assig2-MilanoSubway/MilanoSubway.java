// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2025T2, Assignment 2
 * Name: Simon Burrows
 * Username: Burrowsimo
 * ID: 300666122
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
        String fname = "data/stations.data";
        if (!Files.exists(Path.of(fname))){
            UI.println("Data missing");
        }

        try{
            Scanner scan = new Scanner(Path.of(fname));

            while(scan.hasNext()){
                String stationName = scan.next();
                int stationX = scan.nextInt();
                int stationY = scan.nextInt();
                allStations.put(stationName, new Station(stationName, stationX, stationY));
            }   
        }
        catch (IOException e) { UI.println("Error reading stations"); }

    }

    private void loadSubwayLineData(){
        String fname = "data/subway-lines.data";
        if (!Files.exists(Path.of(fname))){
            UI.println("Data missing");
        }

        try{
            List<String> subwayNames = Files.readAllLines(Path.of(fname));

            for(String subwayName: subwayNames){
                String lineName = subwayName;

                SubwayLine nextSubwayLine = new SubwayLine(subwayName);

                Scanner scan = new Scanner(Path.of("data/"+subwayName+"-stations.data"));

                while(scan.hasNext()){
                    String StationName = scan.next();
                    double distance = scan.nextDouble();
                    nextSubwayLine.addStation(allStations.get(StationName), distance);
                    allStations.get(StationName).addSubwayLine(nextSubwayLine);
                }
                allSubwayLines.put(subwayName, nextSubwayLine);
            }   
        }
        catch (IOException e) { UI.println("Error reading subwayline"); }
    }

    private void loadLineServicesData(){
        for(String lineName : allSubwayLines.keySet()){

            try{
                List<String> subwayLineTimes = Files.readAllLines(Path.of("data/"+lineName+"-services.data"));

                for(String subwayLineTime: subwayLineTimes){
                    LineService lineToAdd = new LineService(allSubwayLines.get(lineName));

                    String[] times = subwayLineTime.split(" ");

                    for(String t: times){
                        lineToAdd.addTime(Integer.parseInt(t));
                    }
                    allSubwayLines.get(lineName).addLineService(lineToAdd);
                }
            }
            catch (IOException e) { UI.println("Error reading line services"); }
        }   
    }

    // Methods for answering the queries
    // The setupGUI method suggests the methods you need to write.

    /*# YOUR CODE HERE */

    private void listAllStations(){
        UI.clearText();
        UI.println("All Stations in Milan:");
        UI.println("----------------");
        for(String stationName: allStations.keySet()){
            UI.println(stationName);
        }
        UI.println();
    }

    private void listStationsByName(){
        UI.clearText();
        UI.println("All Stations in Milan sorted by name:");
        UI.println("----------------");
        List<String> sortedList = new ArrayList<>(allStations.keySet());

        Collections.sort(sortedList);

        for(String stationName: sortedList){
            UI.println(stationName);
        }
    }

    private void listAllSubwayLines(){
        UI.clearText();
        UI.println("All Subway Lines in Milan:");
        UI.println("----------------");
        for(String subwayName: allSubwayLines.keySet()){
            UI.println(allSubwayLines.get(subwayName).toString());
        }
    }

    private void listLinesOfStation(){
        UI.clearText();
        UI.println("Subway lines for " + currentStationName + ":");
        UI.println("----------------");

        Set<SubwayLine> lines = allStations.get(currentStationName).getSubwayLines();

        for(SubwayLine line: lines){
            UI.println(line.toString());
        }
    }

    private void listStationsOnLine(){
        UI.clearText();
        UI.println("Stations for " + currentLineName + ":");
        UI.println("----------------");
        
        List<Station> stations = allSubwayLines.get(currentLineName).getStations();
        for(Station station: stations){
            UI.println(station);
        }
    }

    private void onSameLine(){
        UI.clearText();
        
        Set<SubwayLine> intercestingLines = findIntercestingLines();
        
        if(intercestingLines == null || intercestingLines.size() == 0){
            UI.println("No subway line found from " + currentStationName + " to " + destinationName);
        }
        else{
            UI.println("Subway lines found from " + currentStationName + " to " + destinationName + ":");
            for(SubwayLine line: intercestingLines){
                double distance = line.getDistanceFromStart(allStations.get(destinationName))
                                  - line.getDistanceFromStart(allStations.get(currentStationName));
                distance = round(distance, 1);
                if(distance > 0)
                    UI.println(line.toString() + " Distance: " + distance + "km");
            }
        }
    }

    private void findNextServices(){
        UI.clearText();
        UI.println("Next services from " + currentStationName + " after " + timeToString(startTime) + ":");
        UI.println("----------------");

        Set<SubwayLine> lines = allStations.get(currentStationName).getSubwayLines();

        for(SubwayLine line: lines){
            List<LineService> services = line.getLineServices();
            List<Station> stations = line.getStations();
            
            int stationNo = stations.indexOf(allStations.get(currentStationName));
            int nextService = Integer.MAX_VALUE;
            for(LineService service: services){
                int time = service.getTimes().get(stationNo);
    
                if(time < nextService && time > startTime){
                    nextService = time;
                }
            }
            
            if(nextService == Integer.MAX_VALUE){
                UI.println("There are no services after " + timeToString(startTime) + " on " + line.toString());
            }
            else{
                UI.println("Next service on " + line.toString() + " is: " + timeToString(nextService));
            }
            
        }
    }

    private void findTrip() {
        UI.clearText();
        UI.println("Not implemented");
    }

    private String timeToString(int time){
        String t = Integer.toString(time);
        
        if(t.length() == 4){
            return t.substring(0, 2) + ":" + t.substring(2, 4);    
        }
        else if(t.length() == 3){
            return t.substring(0, 1) + ":" + t.substring(1, 3);    
        }
        else{
            return t;
        }
    }
    
    private Set<SubwayLine> findIntercestingLines(String stationA, String stationB){
        Set<SubwayLine> currentLines = allStations.get(stationA).getSubwayLines();
        
        Set<SubwayLine> destinationLines = allStations.get(stationB).getSubwayLines();
        
        Set<SubwayLine> intercestingLines = new HashSet<>(currentLines);
        intercestingLines.retainAll(destinationLines);
        
        return intercestingLines;
    }
    
    private Set<SubwayLine> findIntercestingLines(){  
        return findIntercestingLines(currentStationName, destinationName);
    }
    
    private double round(double number, int dp){
        double result = number * Math.pow(10, dp);
        result = Math.round(result);
        result /= Math.pow(10, dp);
        return result;
    }
    
    private List<List<Station>> getEdges(){
        Set<List<Station>> result = new HashSet<List<Station>>();
        for(String startStationName: allStations.keySet()){
            Station startStation = allStations.get(startStationName);
            
            for(String compareStationName: allStations.keySet()){
                Station compareStation = allStations.get(compareStationName);
                Set<SubwayLine> commonLines = findIntercestingLines(startStationName, compareStationName);
                
                if(!(commonLines == null || commonLines.size() == 0)){
                    for(SubwayLine line: commonLines){
                        List<Station> commonStations = line.getStations();
                        int startIndex = commonStations.indexOf(startStation);
                        int compareIndex = commonStations.indexOf(compareStation);
                        
                        if(Math.abs(startIndex - compareIndex) == 1){
                            List<Station> newEdge = new ArrayList<Station>();
                            newEdge.add(startStation);
                            newEdge.add(compareStation);
                            
                            result.add(newEdge);
                        }
                    }
                }
            }
        }
        
        return new ArrayList<List<Station>>(result);
    }
    
    private List<List<String>> pathBetweenStations(String stationA, String stationB){
        
        Set<SubwayLine> intercestingLines = findIntercestingLines(stationA, stationB);
        
        if(intercestingLines == null || intercestingLines.size() == 0){
            
        }
        else{
            List<List<String>> result = new ArrayList<List<String>>();
            for(SubwayLine line: intercestingLines)
                return new ArrayList<String>(stationA, stationB, allSubwayLines.keyOf(line));
        }
        return null;
    }
    
    private double pathToTime(List<String> path){
        return 0;
    }
    
    private String reverseMapSearch(Map<String, ?> map, value object){
        
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
                UI.println("Time set to: " + newTime);
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
