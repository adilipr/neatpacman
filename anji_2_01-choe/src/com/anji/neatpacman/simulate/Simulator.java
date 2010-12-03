package com.anji.neatpacman.simulate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import pacman.GameModel;
import pacman.PacMan;

import com.anji.neatpacman.GameState;
import com.anji.neatpacman.Maze;

public class Simulator implements Maze
{
	HashMap<String,HashMap<String,Integer>> mapDistances;
	HashMap<String,Integer> mapStrInt;
	ArrayList<String> listJunctions;
	ArrayList<String> listPowerPills;
	private GameState pacmanGameState;
	private GameState ghostGameState;
	GameModel gamemodel;
	private PacMan pacman;
	int [][] m_gamestate;
	ArrayList<String> listHideOut;
	public static int UnreachableDist = 9999; 
	public Simulator()
	{
		mapStrInt=new HashMap<String,Integer>();
		mapDistances=new HashMap<String,HashMap<String,Integer>>();
		listJunctions=new ArrayList<String>();
		listPowerPills = new ArrayList<String>();
		pacmanGameState = new GameState();
		ghostGameState = new GameState();
		pacman = new PacMan();
		gamemodel =new GameModel(pacman);
		listHideOut = new ArrayList<String>();
		//read all pair shortest distances
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
					mapStrInt.put(splitString[1], Integer.parseInt(splitString[2]));
					mapDistances.put(splitString[0],mapStrInt);
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
		//read the list of junctions
		f=new File("junctions.txt");
		try 
		{
			BufferedReader input =  new BufferedReader(new FileReader(f));
			String line=null;
			try
			{
				while (( line = input.readLine()) != null)
				{
					listJunctions.add(line);
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
		//add list of powerpill locations
		listPowerPills.add("(1,3)");
		listPowerPills.add("(1,23)");
		listPowerPills.add("(26,3)");
		listPowerPills.add("(26,23)");
		//list of HideOut locations
		listHideOut.add("(12,14)");
		listHideOut.add("(13,14)");
		listHideOut.add("(14,14)");
		listHideOut.add("(15,14)");
		//gamestate
		gamemodel.loadPacManMaze();
		m_gamestate = gamemodel.getGameState();
		
	    pacmanGameState.distGhosts = new double[4];
	    pacmanGameState.distJunctions = new double[listJunctions.size()];
	    pacmanGameState.distPowerPills = new double[listPowerPills.size()];
	    pacmanGameState.deltaDistGhosts = new double[4];
	    pacmanGameState.deltaDistJunctions = new double[listJunctions.size()];
	    pacmanGameState.deltaDistPowerPills = new double[listPowerPills.size()];
	    pacmanGameState.xGhosts = new double[4];
	    pacmanGameState.yGhosts = new double[4];
	    pacmanGameState.distsNearestDot = new double[4];
	    pacmanGameState.distsNearestJunction = new double[4];
	    pacmanGameState.distsNearestWall = new double[4];
	    //pacmanGameState.distsPowerPill = new double[4];
	    pacmanGameState.isGhostAffected = new double[4];

	    ghostGameState.distGhosts = new double[4];
	    ghostGameState.distJunctions = new double[listJunctions.size()];
	    ghostGameState.distPowerPills = new double[listPowerPills.size()];
	    ghostGameState.deltaDistGhosts = new double[4];
	    ghostGameState.deltaDistJunctions = new double[listJunctions.size()];
	    ghostGameState.deltaDistPowerPills = new double[listPowerPills.size()];
	    ghostGameState.xGhosts = new double[4];
	    ghostGameState.yGhosts = new double[4];
	    ghostGameState.distsNearestDot = new double[4];
	    ghostGameState.distsNearestJunction = new double[4];
	    ghostGameState.distsNearestWall = new double[4];
	    //ghostGameState.distsPowerPill = new double[4];
	    ghostGameState.isGhostAffected = new double[4];
	}
	//order UP,DOWN,LEFT,RIGHT
	public double[] distNearestWall(int x,int y)
	{
		//double[] distances = new double[4];
		double[] distances = {1,1,1,1};
		//UP
		for(int j=y;j>=0;j--)
		{
			if(!GameModel.hasNorthWall(m_gamestate[x][j]))
			{
				distances[0]++;
			}
			else
				break;
		}
		//DOWN
		for(int j=y;j<31;j++)
		{
			if(!GameModel.hasSouthWall(m_gamestate[x][j]))
			{
				distances[1]++;
			}
			else
				break;
		}
		//LEFT
		for(int i=x;i>=0;i--)
		{
			if(!GameModel.hasWestWall(m_gamestate[i][y]))
			{
				distances[2]++;
			}
			else
				break;
			if(i==0 && y==14)
			{
				i=28;
			}
		}
		//RIGHT
		for(int i=x;i<28;i++)
		{
			if(!GameModel.hasEastWall(m_gamestate[i][y]))
			{
				distances[3]++;
			}
			else
				break;
			//to handle special case- opening in maze
			if(i==27 && y==14)
			{
				i=-1;
			}
		}
		return distances;
	}
	//order UP,DOWN,LEFT,RIGHT
	public double[] distNearestJunction(int x,int y)
	{
		double[] distances = new double[4];
		String ptString;
		//UP
		for(int j=y;j>=0;j--)
		{
			ptString = "("+Integer.toString(x)+","+Integer.toString(j)+")";
			if(listJunctions.contains(ptString))
			{
				break;
			}
			if(!GameModel.hasNorthWall(m_gamestate[x][j]))
			{
				distances[0]++;
			}
			else
			{
				distances[0] = -1;
				break;
			}
		}
		//DOWN
		for(int j=y;j<31;j++)
		{
			ptString = "("+Integer.toString(x)+","+Integer.toString(j)+")";
			if(listJunctions.contains(ptString))
			{
				break;
			}
			if(!GameModel.hasSouthWall(m_gamestate[x][j]))
			{
				distances[1]++;
			}
			else
			{
				distances[1] = -1;
				break;
			}
		}
		//LEFT
		for(int i=x;i>=0;i--)
		{
			ptString = "("+Integer.toString(i)+","+Integer.toString(y)+")";
			if(listJunctions.contains(ptString))
			{
				break;
			}
			if(!GameModel.hasWestWall(m_gamestate[i][y]))
			{
				distances[2]++;
			}
			else
			{
				distances[2] = -1;
			}
			if(i==0 && y==14)
			{
				i = 28;
			}
		}
		//RIGHT
		for(int i=x;i<28;i++)
		{
			ptString = "("+Integer.toString(i)+","+Integer.toString(y)+")";
			if(listJunctions.contains(ptString))
			{
				break;
			}
			if(!GameModel.hasEastWall(m_gamestate[i][y]))
			{
				distances[3]++;
			}
			else
			{
				distances[3] = -1;
			}
			if(i==27 && y==14)
			{
				i = -1;
			}
		}
		return distances;
	}
	public double[] distNearestDot(int x,int y)
	{
		double[] distances = new double[4];
		//UP
		for(int j=y;j>=0;j--)
		{
			if(GameModel.hasFood(m_gamestate[x][j]))
			{
				break;
			}
			if(!GameModel.hasNorthWall(m_gamestate[x][j]))
			{
				distances[0]++;
			}
			else
			{
				distances[0] = -1;
				break;
			}
		}
		//DOWN
		for(int j=y;j<31;j++)
		{
			if(GameModel.hasFood(m_gamestate[x][j]))
			{
				break;
			}
			if(!GameModel.hasSouthWall(m_gamestate[x][j]))
			{
				distances[1]++;
			}
			else
			{
				distances[1] = -1;
				break;
			}
		}
		//LEFT
		for(int i=x;i>=0;i--)
		{
			if(GameModel.hasFood(m_gamestate[i][y]))
			{
				break;
			}
			if(!GameModel.hasWestWall(m_gamestate[i][y]))
			{
				distances[2]++;
			}
			else
			{
				distances[2] = -1;
			}
			if(i==0 && y==14)
			{
				i = 28;
			}
		}
		//RIGHT
		for(int i=x;i<28;i++)
		{
			if(GameModel.hasFood(m_gamestate[i][y]))
			{
				break;
			}
			if(!GameModel.hasEastWall(m_gamestate[i][y]))
			{
				distances[3]++;
			}
			else
			{
				distances[3] = -1;
			}
			if(i==27 && y==14)
			{
				i = -1;
			}
		}
		return distances;
	}//Ghost Affected
	public double[] GhostAffected()
	{
		double[] affected = new double[4];
		for(int i=0;i<4;i++)
		{
			if(gamemodel.isGhostAffected(i))
			{
				affected[i]=1;
			}
		}
		return affected;
	}
	@Override
	public GameState getPacManState() 
	{
		pacmanGameState.distPacMan=0;
		String locPacman=gamemodel.getPlayerloc();
		mapStrInt = mapDistances.get(locPacman);
		//distance to ghosts
		String locGhost;
		for(int i=0;i<4;i++)
		{
			locGhost=gamemodel.getGhostloc(i);
			if(!listHideOut.contains(locGhost))
				pacmanGameState.distGhosts[i]=mapStrInt.get(locGhost);
			else
				pacmanGameState.distGhosts[i] = UnreachableDist;
		}
		//distances to junctions
		for(int i=0;i<listJunctions.size();i++)
		{
			pacmanGameState.distJunctions[i]=mapStrInt.get(listJunctions.get(i));
		}
		//distances to PowerPills
		for(int i=0;i<listPowerPills.size();i++)
		{
			pacmanGameState.distPowerPills[i]=mapStrInt.get(listPowerPills.get(i));
		}
		//delta distances
		String lastlocPacman =gamemodel.getPlayerlastloc();
		mapStrInt=mapDistances.get(lastlocPacman);
		pacmanGameState.deltaDistPacMan = mapStrInt.get(locPacman);
		for(int i=0;i<4;i++)
		{
			String lastlocGhost=gamemodel.getGhostlastloc(i);
			if(!listHideOut.contains(lastlocGhost))
				pacmanGameState.deltaDistGhosts[i]=mapStrInt.get(lastlocGhost);
			else
				pacmanGameState.deltaDistGhosts[i] = UnreachableDist;
		}
		for(int i=0;i<listJunctions.size();i++)
		{
			pacmanGameState.deltaDistJunctions[i]=mapStrInt.get(listJunctions.get(i));
		}
		for(int i=0;i<listPowerPills.size();i++)
		{
			pacmanGameState.deltaDistPowerPills[i]=mapStrInt.get(listPowerPills.get(i));
		}
		//relative coordinations
		pacmanGameState.xPacMan = 0;
		pacmanGameState.yPacMan = 0;
		int pacmanX = gamemodel.getPlayerX();
		int pacmanY = gamemodel.getPlayerY();
		for(int i=0;i<4;i++)
		{
			pacmanGameState.xGhosts[i] = gamemodel.getGhostX(i) - pacmanX;
			pacmanGameState.yGhosts[i] = gamemodel.getGhostY(i) - pacmanY;
		}
		//Nearest distances
		pacmanGameState.distsNearestWall = distNearestWall(pacmanX,pacmanY);
		pacmanGameState.distsNearestJunction = distNearestJunction(pacmanX,pacmanY);
		pacmanGameState.distsNearestDot = distNearestDot(pacmanX,pacmanY);
		pacmanGameState.isGhostAffected = GhostAffected();
		return pacmanGameState;
	}
	
	@Override
	public GameState getGhostState(int id) 
	{
		String currGhostloc = gamemodel.getGhostloc(id);
		mapStrInt = mapDistances.get(currGhostloc);
		//PacMan distance
		String locPacman=gamemodel.getPlayerloc();
		ghostGameState.distPacMan = mapStrInt.get(locPacman);
		//ghost distances
		for(int i=0;i<4;i++)
		{
			ghostGameState.distGhosts[i]= mapStrInt.get(gamemodel.getGhostloc(i));
		}
		//distances to junctions
		for(int i=0;i<listJunctions.size();i++)
		{
			ghostGameState.distJunctions[i]=mapStrInt.get(listJunctions.get(i));
		}
		//distances to power pills
		for(int i=0;i<listPowerPills.size();i++)
		{
			ghostGameState.distPowerPills[i]=mapStrInt.get(listPowerPills.get(i));
		}
		//delta distances
		String lastGhostloc = gamemodel.getGhostlastloc(id);
		mapStrInt = mapDistances.get(lastGhostloc);
		String lastPacmanloc = gamemodel.getPlayerlastloc();
		ghostGameState.deltaDistPacMan = mapStrInt.get(lastPacmanloc);
		for(int i=0;i<4;i++)
		{
			ghostGameState.deltaDistGhosts[i] = mapStrInt.get(gamemodel.getGhostlastloc(i));
		}
		for(int i=0;i<listJunctions.size();i++)
		{
			ghostGameState.deltaDistJunctions[i]=mapStrInt.get(listJunctions.get(i));
		}
		for(int i=0;i<listPowerPills.size();i++)
		{
			ghostGameState.deltaDistPowerPills[i]=mapStrInt.get(listPowerPills.get(i));
		}
		//relative coordinations
		int ghostX = gamemodel.getGhostX(id);
		int ghostY = gamemodel.getGhostY(id);
		ghostGameState.xPacMan = gamemodel.getPlayerX() - ghostX;
		ghostGameState.yPacMan = gamemodel.getPlayerY() - ghostY;
		for(int i=0;i<4;i++)
		{
			ghostGameState.xGhosts[i] = gamemodel.getGhostX(i) - ghostX;
			ghostGameState.yGhosts[i] = gamemodel.getGhostY(i) - ghostY;
		}
		//Nearest distances
		ghostGameState.distsNearestWall = distNearestWall(ghostX,ghostY);
		ghostGameState.distsNearestJunction = distNearestJunction(ghostX,ghostY);
		ghostGameState.distsNearestDot = distNearestDot(ghostX,ghostY);
		ghostGameState.isGhostAffected = GhostAffected();
		return ghostGameState;
	}

	@Override
	public void tick() 
	{
		pacman.tick();
	}

	@Override
	public void setPacManDirection(int direction) 
	{
		gamemodel.setPacmanDirection((byte) direction);
	}

	@Override
	public void setGhostDirection(int id, int direction) 
	{
		gamemodel.setGhostDirection(id, (byte) direction);
	}

	@Override
	public boolean isGameOver() 
	{
		return gamemodel.isGameOver();
	}

	@Override
	public int getPacManScore() 
	{
		return gamemodel.getPacmanScore();
	}

	@Override
	public int getGhostScore() 
	{
		return gamemodel.getGhostScore();
	}
	public static void main(String args[])
	{
		Simulator s=new Simulator();
		int i=0;
		GameState g = new GameState();
		g=s.getPacManState();
		g=s.getGhostState(0);
		
	}
}
