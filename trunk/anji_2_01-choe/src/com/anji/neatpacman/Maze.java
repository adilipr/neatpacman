package com.anji.neatpacman;

public interface Maze
{

  // query game state (for neural network inputs)
  GameState getPacManState();

  GameState getGhostState(int id);

  // tick all
  void tick();

  // move (and thus change game state)
  void setPacManDirection(byte[] directions);

  void setGhostDirection(int id, byte direction);

  boolean isGameOver();
  
  int getGameResult();

  // query final scores (only used after game over, as fitness functions)
  int getPacManScore();

  int getGhostScore();

}
