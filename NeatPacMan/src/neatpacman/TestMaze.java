package neatpacman;

import pacman.GameModel;
import pacman.Player;
import pacman.Ghost;


public class TestMaze implements Maze
{
  
  GameModel gameModel;
  Player player;
  Ghost[] ghosts;
  

  public TestMaze()
  {
    // start the game
  }
  
  @Override
  public GameState getPacManState()
  {
    GameState gs = new GameState();
    gs.deltaDistGhosts = new double[] { 0, 0, 0, 0};
    
    return gs;
  }

  @Override
  public GameState getGhostState(int id)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isGameOver()
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public int getPacManScore()
  {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getGhostScore()
  {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void tick()
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void setPacManDirection(int direction)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void setGhostDirection(int id, int direction)
  {
    // TODO Auto-generated method stub
    
  }

}
