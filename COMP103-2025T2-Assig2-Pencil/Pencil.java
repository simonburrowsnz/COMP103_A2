// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2025T2, Assignment 2
 * Name: Simon Burrows
 * Username: burrowsimo
 * ID: 300666122
 */

import ecs100.*;
import java.util.*;

/** Pencil   */
public class Pencil{
    private double lastX;
    private double lastY;
    
    private ArrayList<double[]> currentStroke;
    
    private Stack<ArrayList<double[]>> strokes = new Stack<ArrayList<double[]>>();

    /**
     * Setup the GUI
     */
    public void setupGUI(){
        UI.setMouseMotionListener(this::doMouse);
        UI.addButton("Quit", UI::quit);
        UI.setLineWidth(3);
        UI.setDivider(0.0);
    }

    /**
     * Respond to mouse events
     */
    public void doMouse(String action, double x, double y) {
        if (action.equals("pressed")){
            currentStroke = new ArrayList<double[]>();
            double[] position = {x, y};
            currentStroke.add(position);
            lastX = x;
            lastY = y;
        }
        else if (action.equals("dragged")){
            UI.drawLine(lastX, lastY, x, y);
            double[] position = {x, y};
            currentStroke.add(position);
        }
        else if (action.equals("released")){
            UI.drawLine(lastX, lastY, x, y);
        }
    }
    
    public void undo(){
        
    }

    public static void main(String[] arguments){
        new Pencil().setupGUI();
    }

}
