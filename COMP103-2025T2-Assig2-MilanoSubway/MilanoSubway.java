// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2025T2, Assignment 2
 * Name: Simon Burrows
 * Username: Burrowsimo
 * ID: 300666122
 */

import ecs100.*;
import java.awt.Color;
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

    private int selectIndex = 0;

    int HIGHTLIGHTSIZE = 10; 

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
        UI.addButton("Select Station",       this::selectCurrentStation); 
        UI.addButton("Select Destination",   this::selectCurrentLine);
        UI.addTextField("Set Time (24hr)",   this::setTime);
        UI.addButton("Lines of Station",     this::listLinesOfStation);
        UI.addButton("Stations on Line",     this::listStationsOnLine);
        UI.addButton("On same line?",        this::onSameLine);
        UI.addButton("Next Services",        this::findNextServices);
        UI.addButton("Find Trip",            this::findTrip);

        UI.addButton("Quit", UI::quit);
        UI.setMouseListener(this::doMouse);
        UI.setMouseMotionListener(this::doMouse);
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

    // Loads the station data //
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

    // Loads the subway line data //
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

    // Loads the line services data //
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

    // Lists all the stations //
    private void listAllStations(){
        UI.clearText();
        UI.println("All Stations in Milan:");
        UI.println("----------------");
        for(String stationName: allStations.keySet()){
            UI.println(stationName);
        }
        UI.println();
    }

    // Lists all the stations by name //
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

    // Lists all the subway lines //
    private void listAllSubwayLines(){
        UI.clearText();
        UI.println("All Subway Lines in Milan:");
        UI.println("----------------");
        for(String subwayName: allSubwayLines.keySet()){
            UI.println(allSubwayLines.get(subwayName).toString());
        }
    }

    // Lists all the subway lines of a stations //
    private void listLinesOfStation(){
        UI.clearText();
        UI.println("Subway lines for " + currentStationName + ":");
        UI.println("----------------");

        Set<SubwayLine> lines = allStations.get(currentStationName).getSubwayLines();

        for(SubwayLine line: lines){
            UI.println(line.toString());
        }
    }

    // Selects the current station to be select by the mouse //
    private void selectCurrentStation(){
        selectIndex = 0;
    }

    // Selects the destination station to be select by the mouse //
    private void selectCurrentLine(){
        selectIndex = 1;
    }

    // Lists the stations on a line //
    private void listStationsOnLine(){
        UI.clearText();
        UI.println("Stations for " + currentLineName + ":");
        UI.println("----------------");

        List<Station> stations = allSubwayLines.get(currentLineName).getStations();
        for(Station station: stations){
            UI.println(station);
        }
    }

    // Lists all lines that go from the current station to the destintion station //
    private void onSameLine(){
        UI.clearText();

        Set<SubwayLine> intercestingLines = findIntercestingLines();

        if(intercestingLines.size() == 0){
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

    // Finds the next services from the current station //
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

    // Finds a trip from the current station to the destination station //
    private void findTrip() {
        UI.clearText();
        UI.println("Finding all paths from " + currentStationName + " to " + destinationName + "...");
        UI.println("----------------");

        Set<SubwayLine> intercestingLines = findIntercestingLines();

        List<List<List<String>>> paths = pathsBetweenStations(currentStationName, destinationName); 

        int shortestTime = Integer.MAX_VALUE;
        List<List<String>> shortestPath = new ArrayList<List<String>>();
        List<Integer> times = new ArrayList<Integer>();
        List<Integer> leavingTimes = new ArrayList<Integer>();
        for(List<List<String>> path: paths){
            int pathArvivalTime = startTime;
            List<Integer> pathArvivalTimes = new ArrayList<Integer>();
            List<Integer> pathLeavingTimes = new ArrayList<Integer>();
            for(List<String> segment: path){
                pathArvivalTime = segmentToTime(segment, pathArvivalTime);

                pathArvivalTimes.add(pathArvivalTime);
                pathLeavingTimes.add(segmentToTime(segment, pathArvivalTime, true));
            }

            if(pathArvivalTime < shortestTime){
                shortestTime = pathArvivalTime;
                shortestPath = path;
                times = pathArvivalTimes;
                leavingTimes = pathLeavingTimes;
            }
        }

        UI.println("Shortest path found from " + currentStationName + " to " + destinationName + ":");
        int i = 0;

        for(List<String> segment: shortestPath){
            UI.println("Take the " + segment.get(2) + " from " + segment.get(0) + " to " + segment.get(1) + " from: " + timeToString(leavingTimes.get(i)) + "-" + timeToString(times.get(i)));
            UI.println("Then");
            i++;
            
            hilightStation(allStations.get(segment.get(0)));
            hilightStation(allStations.get(segment.get(1)));
        }
        UI.println("You will arrive at the destination at: " + timeToString(times.get(i-1)));

    }

    // Has the mouse logic /
    public void doMouse(String action, double x, double y){

        if (action.equals("pressed")){
            boolean changed = false;

            switch(selectIndex){
                case 0:
                    for(String stationName: allStations.keySet()){
                        Station station = allStations.get(stationName);
                        if(Math.abs(station.getXCoord() - x) < HIGHTLIGHTSIZE && Math.abs(station.getYCoord() - y) < HIGHTLIGHTSIZE){
                            currentStationName = stationName;
                            changed = true;
                        }
                    }
                    if(changed){
                        UI.println("Current Station changed to: " + currentStationName);
                    }
                    break;
                case 1:
                    for(String stationName: allStations.keySet()){
                        Station station = allStations.get(stationName);
                        if(Math.abs(station.getXCoord() - x) < HIGHTLIGHTSIZE && Math.abs(station.getYCoord() - y) < HIGHTLIGHTSIZE){
                            destinationName = stationName;
                            changed = true;
                        }
                    }
                    if(changed){
                        UI.println("Current Destination changed to: " + destinationName);
                    }
                    break;

            }
        }
        else{
            switch(selectIndex){
                case 0:
                    for(String stationName: allStations.keySet()){
                        Station station = allStations.get(stationName);
                        if(Math.abs(station.getXCoord() - x) < HIGHTLIGHTSIZE && Math.abs(station.getYCoord() - y) < HIGHTLIGHTSIZE){
                            UI.drawImage("data/system-map.jpg", 0, 0, 1000, 704);
                            hilightStation(station);
                        }
                    }
                    break;
                case 1:
                    for(String stationName: allStations.keySet()){
                        Station station = allStations.get(stationName);
                        if(Math.abs(station.getXCoord() - x) < HIGHTLIGHTSIZE && Math.abs(station.getYCoord() - y) < HIGHTLIGHTSIZE){
                            UI.drawImage("data/system-map.jpg", 0, 0, 1000, 704);
                            hilightStation(station);
                        }
                    }
                    break;
            }
        }
    }

    // Highlights a specific Station on the map //
    private void hilightStation(Station station){
        UI.setColor(Color.black);
        UI.setLineWidth(5);
        UI.drawOval(station.getXCoord()-HIGHTLIGHTSIZE/2, station.getYCoord()-HIGHTLIGHTSIZE/2, HIGHTLIGHTSIZE, HIGHTLIGHTSIZE);
    }

    // Turns an integer into a string with a colon //
    private String timeToString(int time){
        String t = Integer.toString(time);

        int length = t.length();
        if(length  != 4){
            String zeros = "";
            for(int i = 0; i < 4-length; i++){
                zeros += "0";
            }
            t = zeros + t;
        }
        return t.substring(0, 2) + ":" + t.substring(2, 4);
    }

    // Finds intercenting lines between two stations //
    private Set<SubwayLine> findIntercestingLines(String stationA, String stationB){
        Set<SubwayLine> currentLines = allStations.get(stationA).getSubwayLines();

        Set<SubwayLine> destinationLines = allStations.get(stationB).getSubwayLines();

        Set<SubwayLine> intercestingLines = new HashSet<>(currentLines);
        intercestingLines.retainAll(destinationLines);

        return intercestingLines;
    }

    // Does the above for the current and destination station //
    private Set<SubwayLine> findIntercestingLines(){  
        return findIntercestingLines(currentStationName, destinationName);
    }

    // Rounds a number to the specified decimal place //
    private double round(double number, int dp){
        double result = number * Math.pow(10, dp);
        result = Math.round(result);
        result /= Math.pow(10, dp);
        return result;
    }

    // Finds all paths between two stations //
    private List<List<List<String>>> pathsBetweenStations(String stationA, String stationB){
        List<List<List<String>>> allPaths = new ArrayList<>();
        List<String> currentPath = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        dfsPaths(stationA, stationB, visited, currentPath, allPaths);

        return allPaths;
    }

    // The Depth-First Search algorythym finds all simple paths that are less than 50 stations long //
    private void dfsPaths(String current, String destination, Set<String> visited,
    List<String> path, List<List<List<String>>> allPaths) {
        // Stop exploring this path if it's too long
        if (path.size() > 50) {
            return;
        }
        visited.add(current);
        path.add(current);

        if (current.equals(destination)) {
            List<List<String>> segments = convertToSegments(new ArrayList<>(path));
            if (!segments.isEmpty()) {
                allPaths.add(segments);
            } 
            else {
                UI.println("Invalid segment chain: " + path);
            }
        }

        else {
            for (String neighbor : getNeighbours(current)) {
                if (!visited.contains(neighbor)) {
                    dfsPaths(neighbor, destination, visited, path, allPaths);
                }
            }
        }

        path.remove(path.size() - 1);
        visited.remove(current);
    }

    // Converts a list of Stations into segments. A segment is {StartOfSegmentStation, EndOfSegmentStation, LinesItsOn} //
    private List<List<String>> convertToSegments(List<String> stationPath){
        List<List<String>> segments = new ArrayList<>();

        if (stationPath.size() < 2) return segments;

        String from = stationPath.get(0);
        String currentLine = null;

        for (int i = 1; i < stationPath.size(); i++) {
            String to = stationPath.get(i);

            Set<SubwayLine> commonLines = findIntercestingLines(from, to);

            if (commonLines.isEmpty()) {
                // Should not happen if the path is valid
                return new ArrayList<>();
            }

            // Prefer currentLine if still valid
            String nextLine = currentLine;
            if (currentLine == null || !commonLines.contains(currentLine)) {
                for(SubwayLine line: commonLines){
                    double distance = line.getDistanceFromStart(allStations.get(to))
                        - line.getDistanceFromStart(allStations.get(from));
                    distance = round(distance, 1);
                    if(distance > 0)
                        nextLine = line.getName();
                }
            }

            // Merge into last segment if same line
            if (!segments.isEmpty() && segments.get(segments.size() - 1).get(2).equals(nextLine)) {
                segments.get(segments.size() - 1).set(1, to);  // extend the current segment's destination
            } else {
                List<String> segment = new ArrayList<>();
                segment.add(from);
                segment.add(to);
                segment.add(nextLine);
                segments.add(segment);
            }

            currentLine = nextLine;
            from = to;
        }

        return segments;
    }

    // Finds all the neibours of a station //
    private List<String> getNeighbours(String baseStationName){
        Set<String> result = new HashSet<String>();
        Station baseStation = allStations.get(baseStationName);

        for(String compareStationName: allStations.keySet()){
            Station compareStation = allStations.get(compareStationName);
            Set<SubwayLine> commonLines = findIntercestingLines(baseStationName, compareStationName);

            if(!(commonLines == null || commonLines.size() == 0)){
                for(SubwayLine line: commonLines){
                    List<Station> commonStations = line.getStations();
                    int startIndex = commonStations.indexOf(baseStation);
                    int compareIndex = commonStations.indexOf(compareStation);

                    if(Math.abs(startIndex - compareIndex) == 1){
                        result.add(compareStationName);
                    }
                }
            }
        }

        return new ArrayList<String>(result);
    }

    // Finds when you arive from a station along a segment //
    private int segmentToTime(List<String> segment, int newStartTime){
        SubwayLine line = allSubwayLines.get(segment.get(2));
        List<LineService> services = line.getLineServices();
        List<Station> stations = line.getStations();

        int boardingStationNo = stations.indexOf(allStations.get(segment.get(0)));
        int departingStationNo = stations.indexOf(allStations.get(segment.get(1)));
        int nextService = Integer.MAX_VALUE;
        int arrivleService = Integer.MAX_VALUE;
        for(LineService service: services){
            int boardingTime = service.getTimes().get(boardingStationNo);
            int departingTime = service.getTimes().get(departingStationNo);

            if(boardingTime < nextService && boardingTime > newStartTime){
                nextService = boardingTime;
                arrivleService = departingTime;
            }
        }
        return arrivleService;
    }

    // Finds when you leave from a station along a segment //
    private int segmentToTime(List<String> segment, int newStartTime, boolean firstValue){
        SubwayLine line = allSubwayLines.get(segment.get(2));
        List<LineService> services = line.getLineServices();
        List<Station> stations = line.getStations();

        int boardingStationNo = stations.indexOf(allStations.get(segment.get(0)));
        int departingStationNo = stations.indexOf(allStations.get(segment.get(1)));
        int nextService = Integer.MAX_VALUE;
        int arrivleService = Integer.MAX_VALUE;
        for(LineService service: services){
            int boardingTime = service.getTimes().get(boardingStationNo);
            int departingTime = service.getTimes().get(departingStationNo);

            if(boardingTime < nextService && boardingTime > newStartTime){
                nextService = boardingTime;
                arrivleService = departingTime;
            }
        }
        return nextService;
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