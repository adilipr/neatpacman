package com.anji.neatpacman;

public class DummyMaze implements Maze
{
  
  private int numOfGhosts;
  
  public DummyMaze(int numOfGhosts)
  {
    this.numOfGhosts = numOfGhosts;
  }

  @Override
  public GameState getPacManState()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public GameState getGhostState(int id)
  {
    // TODO Auto-generated method stub
    return null;
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

}
