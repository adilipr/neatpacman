package com.anji.neatpacman.simulate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.anji.neatpacman.GameState;
import com.anji.neatpacman.Maze;

public class Simulator implements Maze
{
	HashMap<String,HashMap<String,Integer>> mapDistances;
	HashMap<String,Integer> mapstrInt;
	private GameState pacmanGameState = new GameState();
	private GameState ghostGameState = new GameState();
	public Simulator()
	{
		mapstrInt=new HashMap<String,Integer>();
		mapDistances=new HashMap<String,HashMap<String,Integer>>();
		File f=new File("distances.txt");
		String delimiter = " ";
		String[] splitString;
		try 
		{
			BufferedReader input =  new BufferedReader(new FileReader(f));
			String line=null;
			try
			{
				while (( line = input.readLine()) != null)
				{
					splitString=line.split(delimiter);
					mapstrInt.put(splitString[1], Integer.parseInt(splitString[2]));
					mapDistances.put(splitString[0],mapstrInt);
				}
				input.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		} 
		catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	    pacmanGameState.distGhosts = new double[1];
	    pacmanGameState.distJunctions = new double[0];
	    pacmanGameState.distPowerPills = new double[1];
	    pacmanGameState.deltaDistGhosts = new double[1];
	    pacmanGameState.deltaDistJunctions = new double[0];
	    pacmanGameState.deltaDistPowerPills = new double[1];
	    pacmanGameState.xGhosts = new double[1];
	    pacmanGameState.yGhosts = new double[1];
	    pacmanGameState.distsNearestDot = new double[4];
	    pacmanGameState.distsNearestJunction = new double[4];
	    pacmanGameState.distsNearestWall = new double[4];
	    //pacmanGameState.distsPowerPill = new double[4];
	    pacmanGameState.isGhostAffected = new double[1];

	    ghostGameState.distGhosts = new double[1];
	    ghostGameState.distJunctions = new double[0];
	    ghostGameState.distPowerPills = new double[1];
	    ghostGameState.deltaDistGhosts = new double[1];
	    ghostGameState.deltaDistJunctions = new double[0];
	    ghostGameState.deltaDistPowerPills = new double[1];
	    ghostGameState.xGhosts = new double[1];
	    ghostGameState.yGhosts = new double[1];
	    ghostGameState.distsNearestDot = new double[4];
	    ghostGameState.distsNearestJunction = new double[4];
	    ghostGameState.distsNearestWall = new double[4];
	    //ghostGameState.distsPowerPill = new double[4];
	    ghostGameState.isGhostAffected = new double[1];
	}
	@Override
	public GameState getPacManState() 
	{
		// TODO Auto-generated method stub
		
		
		return null;
	}

	@Override
	public GameState getGhostState(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPacManDirection(int direction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGhostDirection(int id, int direction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isGameOver() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getPacManScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getGhostScore() {
		// TODO Auto-generated method stub
		return 0;
	}
	public static void main(String args[])
	{
		Simulator s=new Simulator();
		int i=0;
	}
}
