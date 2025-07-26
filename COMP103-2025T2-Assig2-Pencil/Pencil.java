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
import java.awt.event.KeyEvent;
import java.awt.Color;
import javax.swing.JColorChooser;
import javax.swing.event.*;

/** Pencil   */
public class Pencil{
    private double lastX;
    private double lastY;

    private ArrayList<double[]> currentStroke;
    
    private Color currentColor = Color.black;
    
    private double currentWidth = 3;
    
    private Stack<Stroke> strokes = new Stack<Stroke>();
    private Stack<Stroke> undoneStrokes = new Stack<Stroke>();

    /**
     * Setup the GUI
     */
    public void setupGUI(){
        UI.setMouseMotionListener(this::doMouse);
        UI.setKeyListener(this::doKey);
        UI.addSlider("Pencil", 0.1, 10, 3, this::slide);
        UI.addButton("Pick Color", this::setColor);
        UI.addButton("Undo", this::undo);
        UI.addButton("Redo", this::redo);
        UI.addButton("Quit", UI::quit);
        UI.setLineWidth(3);
        UI.setDivider(0.0);
    }

    /**
     * Respond to mouse events
     */
    public void doMouse(String action, double x, double y) {
        if (action.equals("pressed")){
            UI.setLineWidth(currentWidth);
            UI.setColor(currentColor);  
            currentStroke = new ArrayList<double[]>();
            undoneStrokes = new Stack<Stroke>();
            double[] position = {x, y};
            currentStroke.add(position);
            
            lastX = x;
            lastY = y;
        }
        else if (action.equals("dragged")){
           UI.drawLine(lastX, lastY, x, y);
            lastX = x;
            lastY = y;
 
            double[] position = {x, y};
            currentStroke.add(position);
        }
        else if (action.equals("released")){
            UI.drawLine(lastX, lastY, x, y);
            strokes.push(new Stroke(currentStroke, currentColor, currentWidth));
        }
    }
    
    // Maps ctr+c & ctrl+v to undo and redo //
    public void doKey(String action){
        if(action.equals("")){
            undo();
        }
        else if(action.equals("")){
            redo();
        }
    }
    
    // Draws the frame //
    private void drawFrame(){
        UI.clearGraphics();
        for(Stroke stroke: strokes){
            stroke.drawStroke();
         }
    }
    
    // Undoes a stroke //
    public void undo(){
        if(!strokes.empty()){
            undoneStrokes.push(strokes.pop());
        }
        drawFrame();
    }
    
    // Redoes a stroke //
    public void redo(){
        if(!undoneStrokes.empty()){
            strokes.push(undoneStrokes.pop());
        }
        drawFrame();
    }
    
    // Sets the width of a stroke to the slider value //
    public void slide(double action){
        currentWidth = action;
    }
    
    // Lets the user pick the color //
    public void setColor(){
        currentColor = JColorChooser.showDialog(null, "Pick a Color", currentColor);
    }

    public static void main(String[] arguments){
        new Pencil().setupGUI();
    }

}
