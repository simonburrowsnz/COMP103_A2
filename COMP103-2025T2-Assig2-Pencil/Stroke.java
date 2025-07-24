
// This program is copyright ME.
// You are NOT!!!! granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.
// HYEYAAAAAAAAAAA

/* Code for COMP103 - 2025T2, Assignment 2
 * Name: Simon Burrows
 * Username: burrowsimo
 * ID: 300666122
 */

import ecs100.*;
import java.util.*;
import java.awt.Color;

public class Stroke
{
    private ArrayList<double[]> points;
    
    private Color color;
    
    private double width;

    public Stroke(ArrayList<double[]> p, Color c, double w)
    {
        this.points = p;
        this.color = c;
        this.width = w;
    }

    public void drawStroke(){
        UI.setColor(color);
        UI.setLineWidth(width);
        for(int i = 1; i < points.size(); i++){
            double[] currentPoint = points.get(i-1);
            double[] lastPoint = points.get(i);
        
            UI.drawLine(currentPoint[0], currentPoint[1], lastPoint[0], lastPoint[1]);
        }
    }
}