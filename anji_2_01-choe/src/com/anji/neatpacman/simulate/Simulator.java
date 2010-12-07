package com.anji.neatpacman.simulate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import pacman.GameModel;
import pacman.Ghost;
import pacman.PacMan;

import com.anji.neatpacman.Config;
import com.anji.neatpacman.GameState;
import com.anji.neatpacman.Maze;
import com.anji.neatpacman.Utils;
import com.anji.tournament.PlayerStats;

public class Simulator implements Maze
{
	static HashMap<String,HashMap<String,Integer>> mapDistances;
//	static HashMap<String,Integer> mapStrInt;
	static ArrayList<String> listJunctions;
	static ArrayList<String> listPowerPills;
	static ArrayList<String> listHideOut;
	
	static
	{
//		mapStrInt=new HashMap<String,Integer>();
		mapDistances=new HashMap<String,HashMap<String,Integer>>();
		listJunctions=new ArrayList<String>();
		listPowerPills = new ArrayList<String>();
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
					if (!mapDistances.containsKey(splitString[0]))
					{
					  mapDistances.put(splitString[0], new HashMap<String, Integer>());
					}
					HashMap<String, Integer> mapStrInt = mapDistances.get(splitString[0]);
					mapStrInt.put(splitString[1], Integer.parseInt(splitString[2]));
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
	}
	
	private GameState pacmanGameState;
	private GameState[] ghostGameState;
	GameModel gamemodel;
	PacMan pacman;
	int [][] m_gamestate;
	private int nGhosts;
	
	public JFrame frame = null;
	
	public Simulator()
	{
	  this.nGhosts = Config.get().getNumOfGhosts();
		boolean guiEnabled = Config.get().isGuiEnabled();
		
    pacman = new PacMan(guiEnabled, nGhosts);
		pacman.init(); // init() will create the game model for you
		if (guiEnabled)
		{
		  frame = new JFrame("NeatPacMan");
		  frame.add("Center", pacman);
		  frame.pack();
		  frame.setVisible(true);
		}
		
		gamemodel = pacman.getGameModel(); // you don't need to create a new game model
		//gamestate
		m_gamestate = gamemodel.getGameState();
		
		pacmanGameState = new GameState(nGhosts, listJunctions.size(), listPowerPills.size());
		ghostGameState = new GameState[nGhosts];
		for (int i = 0; i < nGhosts; ++i)
  		ghostGameState[i] = new GameState(nGhosts, listJunctions.size(), listPowerPills.size());
	}
	//order UP,DOWN,LEFT,RIGHT
	public double[] distNearestWall(int x,int y)
	{
		double[] distances = new double[] {1,1,1,1};
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
		double[] distances = new double[] {1,1,1,1};
		String ptString;
		//UP
		distances[0] = 0;
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
				distances[0] = Config.get().getInfDistance();
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
				distances[1] = Config.get().getInfDistance();
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
				distances[2] = Config.get().getInfDistance();
				break;
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
				distances[3] = Config.get().getInfDistance();
				break;
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
		double[] distances = new double[] {1,1,1,1};
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
				distances[0] = Config.get().getInfDistance();
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
				distances[1] = Config.get().getInfDistance();
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
				distances[2] = Config.get().getInfDistance();
				break;
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
				distances[3] = Config.get().getInfDistance();
				break;
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
		double[] affected = new double[nGhosts];
		for(int i=0;i<nGhosts;i++)
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
		int infDistance = Config.get().getInfDistance();
		pacmanGameState.distPacMan=0;
		String locPacman=gamemodel.getPlayerloc();
		Map<String, Integer> mapStrInt = mapDistances.get(locPacman);
		
		//distance to ghosts
		String locGhost;
		for(int i=0;i<nGhosts;i++)
		{
			locGhost=gamemodel.getGhostloc(i);
			int newDist = Utils.mapUse(mapStrInt, locGhost, infDistance);
			pacmanGameState.deltaDistGhosts[i] = newDist - pacmanGameState.distGhosts[i];
      pacmanGameState.distGhosts[i] = newDist;
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
		pacmanGameState.deltaDistPacMan = 0;
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
		for(int i=0;i<nGhosts;i++)
		{
			pacmanGameState.xGhosts[i] = gamemodel.getGhostX(i) - pacmanX;
			pacmanGameState.yGhosts[i] = gamemodel.getGhostY(i) - pacmanY;
		}
		
		// directions
		pacmanGameState.pacmanDirection = gamemodel.getPacmanDirection();
		for (int i = 0; i < nGhosts; i++)
		{
		  pacmanGameState.ghostsDirection[i] = gamemodel.getGhostDirection(i);
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
		int infDistance = Config.get().getInfDistance();
		String currGhostloc = gamemodel.getGhostloc(id);
		Map<String, Integer> mapStrInt = mapDistances.get(currGhostloc);
		
		//PacMan distance
		String locPacman=gamemodel.getPlayerloc();
		int newPacmanDist = mapStrInt == null ? infDistance : mapStrInt.get(locPacman);
		ghostGameState[id].deltaDistPacMan = newPacmanDist - ghostGameState[id].distPacMan;
		ghostGameState[id].distPacMan = newPacmanDist;
		
		//ghost distances
		for(int i=0;i<nGhosts;i++)
		{
      int newDist = mapStrInt == null ? infDistance :
          Utils.mapUse(mapStrInt, gamemodel.getGhostloc(i), infDistance);
      ghostGameState[id].deltaDistGhosts[i] = newDist - ghostGameState[id].distGhosts[i];
      ghostGameState[id].distGhosts[i] = newDist;
		}
		//distances to junctions
		for(int i=0;i<listJunctions.size();i++)
		{
			ghostGameState[id].distJunctions[i]=mapStrInt == null ? infDistance : mapStrInt.get(listJunctions.get(i));
		}
		//distances to power pills
		for(int i=0;i<listPowerPills.size();i++)
		{
			ghostGameState[id].distPowerPills[i]=mapStrInt == null ? infDistance : mapStrInt.get(listPowerPills.get(i));
		}
		//delta distances
		for(int i=0;i<listJunctions.size();i++)
		{
			ghostGameState[id].deltaDistJunctions[i]=mapStrInt == null ? infDistance : mapStrInt.get(listJunctions.get(i));
		}
		for(int i=0;i<listPowerPills.size();i++)
		{
			ghostGameState[id].deltaDistPowerPills[i]=mapStrInt == null ? infDistance : mapStrInt.get(listPowerPills.get(i));
		}
		//relative coordinations
		int ghostX = gamemodel.getGhostX(id);
		int ghostY = gamemodel.getGhostY(id);
		ghostGameState[id].xPacMan = gamemodel.getPlayerX() - ghostX;
		ghostGameState[id].yPacMan = gamemodel.getPlayerY() - ghostY;
		
		ghostGameState[id].pacmanDirection = gamemodel.getPacmanDirection();
		for (int i = 0; i < nGhosts; i++)
		{
		  ghostGameState[id].ghostsDirection[i] = gamemodel.getGhostDirection(i);
		}
		
		for(int i=0;i<nGhosts;i++)
		{
			ghostGameState[id].xGhosts[i] = gamemodel.getGhostX(i) - ghostX;
			ghostGameState[id].yGhosts[i] = gamemodel.getGhostY(i) - ghostY;
		}
		//Nearest distances
		ghostGameState[id].distsNearestWall = distNearestWall(ghostX,ghostY);
		ghostGameState[id].distsNearestJunction = distNearestJunction(ghostX,ghostY);
		ghostGameState[id].distsNearestDot = distNearestDot(ghostX,ghostY);
		ghostGameState[id].isGhostAffected = GhostAffected();
		return ghostGameState[id];
	}

	@Override
	public void tick() 
	{
		pacman.tick();
	}

	@Override
	public void setPacManDirection(byte[] directions) 
	{
		gamemodel.setPacmanDirection(directions, Utils.min(pacmanGameState.distGhosts) <= 3);
	}

	@Override
	public void setGhostDirection(int id, byte direction) 
	{
		gamemodel.setGhostDirection(id, direction);
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
//		GameState g = new GameState();
//		g=s.getPacManState();
//		g=s.getGhostState(0);
		
	}
  @Override
  public int getGameResult()
  {
    return pacman.getGameResult();
  }
  
  public void setGhostUseExternalController(boolean externalController)
  {
    for (Ghost ghost : gamemodel.getGhosts())
    {
      ghost.useExternalController(externalController);
    }
  }
}
