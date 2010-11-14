/*
 * Copyright (C) 2009 Yoonsuck Choe
 * 
 * This file is an add-on to ANJI (Another NEAT Java Implementation).
 * This add-on has the same terms and conditions as ANJI (see below).
 * 
 * Fri Nov 27 23:19:12 CST 2009
 *----------------------------------------------------------------------------
 * ANJI is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 *
 */
//package com.anji.navi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import java.util.Random;

public class NaviDisplay extends JPanel{
    private int    width  = 500;
    private int    height = 500;
    private double start_x = 0.0;
    private double start_y = 0.0;
    private double goal_x = 0.0;
    private double goal_y = 0.0;
    private double cur_x = 0.0;
    private double cur_y = 0.0;
    private double gran_x = 0.0;
    private double gran_y = 0.0;
    private double[][] motionSteps;
    private Graphics naviG;
    private Color[] pallett;
    private Color goalColor;
    private Color startColor;

    NaviDisplay (int w, int h, double sx, double sy, double gx, double gy, 
	double[][] motionSteps) {

	width = w;
	height = h; 

	gran_x = width;
	gran_y = height;

	start_x = cur_x = sx;
	start_y = cur_y = sy;
	goal_x = gx;
	goal_y = gy;

	this.motionSteps = motionSteps;

	// set up pallett for trajectory
	pallett = new Color[256*3];
	for (int i=0; i<255; ++i) {
		pallett[i] = new Color(i,0,0);
		pallett[i+255] = new Color(255,i,0);
		pallett[i+255*2] = new Color(255,255,i);
	}
	
	goalColor = new Color(0,255,0);
	startColor = new Color(255,0,0);
    }

    public void setCurrent(double x, double y) {
  
	cur_x = rangeCheck(x);
	cur_y = rangeCheck(y);
    }

    public void moveTo(double x, double y) {
	double next_x = rangeCheck(cur_x + x);
	double next_y = rangeCheck(cur_y + y);
	naviG.drawLine((int)(cur_x*gran_x),(int)(cur_y*gran_y),
		(int)(next_x*gran_x),(int)(next_y*gran_y));
	cur_x = next_x;
	cur_y = next_y;
    }

    private void plotLoc(double x, double y, Color c) {
	double offset = 3.0/gran_x;
	naviG.setColor(c);
	naviG.fillOval(
		(int)(rangeCheck(x)* gran_x),
		(int)(rangeCheck(y)*gran_y),
		(int)(gran_x/40.0),(int)(gran_y/40.0));
    }

    public void plotStart(double x, double y) {
	plotLoc(x,y,startColor);
    }

    public void plotGoal(double x, double y) {
	plotLoc(x,y,goalColor);
    }

    public double rangeCheck(double x) {
	if (x < 0.0) return 0.0;
	if (x >= 1.0) return 1.0;
	return x;
    }
   
    public void paintComponent (Graphics g) {
	super.paintComponent(g);
	naviG = g;
	randLine();
    }

    public void randLine () {
	for (int i = 0; i<motionSteps.length; ++i) {
		naviG.setColor(pallett[i%(256*3)]);
		moveTo(motionSteps[i][0],motionSteps[i][1]);
	}	
	plotStart(start_x,start_y);
	plotGoal(cur_x,cur_y);
    }

    public static void main (String args[]) {

	// set up frame
	int w=400, h=400;

	// set up motion steps array
	double[][] motion = new double[256*3][2];
	Random r = new Random(1234);
	for (int i=0; i<motion.length; ++i) {
		motion[i][0]=(r.nextDouble()-0.5)/20.0;
		motion[i][1]=(r.nextDouble()-0.5)/20.0;
	}
	
	// rest of the code
	JFrame frame = new JFrame("NaviDisplay");
	NaviDisplay navi = new NaviDisplay(w,h,r.nextDouble(),r.nextDouble(),
		r.nextDouble(), r.nextDouble(),motion);
	frame.setContentPane(navi);
	Container con = frame.getContentPane();
	con.setBackground(Color.black);
	frame.setSize(w,h);
	frame.setLocationRelativeTo(null);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
	//navi.randLine();

    }
}
       
class Closer extends WindowAdapter {
    public void windowClosing (WindowEvent event) {
        System.exit (0);
    }
}
