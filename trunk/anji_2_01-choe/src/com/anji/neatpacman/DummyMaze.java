package com.anji.neatpacman;

import pacman.Thing;

/**
 * this implements a simple dummy maze for testing. the maze will be a 5x5
 * field. there is one pacman at (1,1), one ghost at (3,3) and one power pill at
 * (5,5). dots are at other places.
 * 
 * the pacman is supposed to learn to flee from ghost, collect dots, and perhaps
 * using power pill.
 * 
 * the ghost is supposed to learn to chase pacman, and perhaps flee from pacman
 * after power pill.
 * 
 * @author quyin
 * 
 */
public class DummyMaze extends Debug implements Maze
{

  private static int       DOT                 = 1;

  private static int       POWERPILL           = 2;

  private int[][]          map                 = {
                                               { 0, DOT, DOT, DOT, DOT },
                                               { DOT, DOT, DOT, DOT, DOT },
                                               { DOT, DOT, 0, DOT, DOT },
                                               { DOT, DOT, DOT, DOT, DOT },
                                               { DOT, DOT, DOT, DOT, POWERPILL },
                                               };

  private double           pacmanX             = 0;

  private double           pacmanY             = 0;

  private byte              pacmanDirection     = Thing.RIGHT;

  private double           ghostX              = 2;

  private double           ghostY              = 2;

  private byte              ghostDirection      = Thing.UP;

  private boolean          powerPillStillThere = true;

  private boolean          ghostAffected       = false;

  private int              powerCountDown;

  // highest score for pacman: 10 * 22 + 50 * 1 + 200 * 1 = 470
  private int              score               = 0;

  private GameState        pacmanState         = new GameState(1, 0, 1);

  private GameState        ghostState          = new GameState(1, 0, 1);

  private boolean          gameOver            = false;

  private int              tickCount           = 0;

  @Override
  public GameState getPacManState()
  {
    double newDistGhost = Math.abs(pacmanX - ghostX) + Math.abs(pacmanY - ghostY);
    double newDistPowerPill = powerPillStillThere ? Math.abs(pacmanX - 4) + Math.abs(pacmanY - 4)
        : Config.get().getInfDistance();

//    pacmanState.deltaDistPacMan = 0;
//    pacmanState.deltaDistGhosts[0] = newDistGhost - pacmanState.distGhosts[0];
//    pacmanState.deltaDistPowerPills[0] = newDistPowerPill - pacmanState.distPowerPills[0];

    pacmanState.distPacMan = 0;
    pacmanState.distGhosts[0] = newDistGhost;
    pacmanState.distPowerPills[0] = newDistPowerPill;

//    pacmanState.xPacMan = 0;
//    pacmanState.yPacMan = 0;
//    pacmanState.xGhosts[0] = ghostX - pacmanX;
//    pacmanState.yGhosts[0] = ghostY - pacmanY;
    
    pacmanState.pacmanDirection = pacmanDirection;
    pacmanState.ghostsDirection[0] = ghostDirection;

    pacmanState.distsNearestDot[0] = distNearest(pacmanX, pacmanY, Thing.UP, 1);
    pacmanState.distsNearestDot[1] = distNearest(pacmanX, pacmanY, Thing.DOWN, 1);
    pacmanState.distsNearestDot[2] = distNearest(pacmanX, pacmanY, Thing.LEFT, 1);
    pacmanState.distsNearestDot[3] = distNearest(pacmanX, pacmanY, Thing.RIGHT, 1);

    pacmanState.distsNearestJunction[0] = Config.get().getInfDistance();
    pacmanState.distsNearestJunction[1] = Config.get().getInfDistance();
    pacmanState.distsNearestJunction[2] = Config.get().getInfDistance();
    pacmanState.distsNearestJunction[3] = Config.get().getInfDistance();

    pacmanState.distsNearestWall[0] = pacmanY + 1;
    pacmanState.distsNearestWall[1] = 5 - pacmanY;
    pacmanState.distsNearestWall[2] = pacmanX + 1;
    pacmanState.distsNearestWall[3] = 5 - pacmanY;

    pacmanState.isGhostAffected[0] = ghostAffected ? 1.0 : 0.0;

    return pacmanState;
  }

  @Override
  public GameState getGhostState(int id)
  {
    double newDistPacman = Math.abs(pacmanX - ghostX) + Math.abs(pacmanY - ghostY);
    double newDistPowerPill = powerPillStillThere ? Math.abs(ghostX - 4) + Math.abs(ghostY - 4)
        : Config.get().getInfDistance();

//    ghostState.deltaDistPacMan = newDistPacman - ghostState.distPacMan;
//    ghostState.deltaDistGhosts[0] = 0;
//    ghostState.deltaDistPowerPills[0] = newDistPowerPill - ghostState.distPowerPills[0];

    ghostState.distPacMan = newDistPacman;
    ghostState.distGhosts[0] = 0;
    ghostState.distPowerPills[0] = newDistPowerPill;

//    ghostState.xPacMan = pacmanX - ghostX;
//    ghostState.yPacMan = pacmanY - ghostY;
//    ghostState.xGhosts[0] = 0;
//    ghostState.yGhosts[0] = 0;

    pacmanState.pacmanDirection = pacmanDirection;
    pacmanState.ghostsDirection[0] = ghostDirection;

    ghostState.distsNearestDot[0] = distNearest(ghostX, ghostY, Thing.UP, 1);
    ghostState.distsNearestDot[1] = distNearest(ghostX, ghostY, Thing.DOWN, 1);
    ghostState.distsNearestDot[2] = distNearest(ghostX, ghostY, Thing.LEFT, 1);
    ghostState.distsNearestDot[3] = distNearest(ghostX, ghostY, Thing.RIGHT, 1);

    ghostState.distsNearestJunction[0] = Config.get().getInfDistance();
    ghostState.distsNearestJunction[1] = Config.get().getInfDistance();
    ghostState.distsNearestJunction[2] = Config.get().getInfDistance();
    ghostState.distsNearestJunction[3] = Config.get().getInfDistance();

    ghostState.distsNearestWall[0] = ghostY + 1;
    ghostState.distsNearestWall[1] = 5 - ghostY;
    ghostState.distsNearestWall[2] = ghostX + 1;
    ghostState.distsNearestWall[3] = 5 - ghostX;

    ghostState.isGhostAffected[0] = ghostAffected ? 1.0 : 0.0;

    return ghostState;
  }

  @Override
  public void tick()
  {
    if (tickCount >= Config.get().getMaxTick())
    {
      gameOver = true;
      return;
    }
    tickCount++;

    // power state
    if (powerCountDown == 0)
      ghostAffected = false;
    if (ghostAffected)
      powerCountDown--;

    // move pacman, eating dot or power pill
    double nextPacmanX = nextX(pacmanX, pacmanDirection, 1);
    double nextPacmanY = nextY(pacmanY, pacmanDirection, 1);
    pacmanX = nextPacmanX;
    pacmanY = nextPacmanY;
    // debug("pacman moves to (%.1f, %.1f).", pacmanX, pacmanY);
    int i = (int) Math.round(pacmanX), j = (int) Math.round(pacmanY);
    if (map[i][j] == DOT)
    {
      score += 10;
      map[i][j] = 0;
      debug("pacman eats dot at (%d, %d).", i, j);
    }
    if (map[i][j] == POWERPILL)
    {
      score += 50;
      map[i][j] = 0;
      powerPillStillThere = false;
      ghostAffected = true;
      powerCountDown = 10;
      debug("pacman eats power pill at (%d, %d).", i, j);
    }

    // move ghost
    double nextGhostX = nextX(ghostX, ghostDirection, ghostAffected ? 0.5 : 1);
    double nextGhostY = nextY(ghostY, ghostDirection, ghostAffected ? 0.5 : 1);
    ghostX = nextGhostX;
    ghostY = nextGhostY;
    // debug("ghost moves to (%.1f, %.1f).", ghostX, ghostY);

    // when pacman meets ghost
    if (pacmanX == ghostX && pacmanY == ghostY)
    {
      if (ghostAffected)
      {
        debug("pacman eats ghost at (%.1f, %.1f).", pacmanX, pacmanY);
        score += 200;
        ghostX = 2;
        ghostY = 2;
        ghostAffected = false;
      }
      else
      {
        debug("ghost eats pacman at (%.1f, %.1f) -- game over.", pacmanX, pacmanY);
        gameOver = true;
      }
    }
  }

  @Override
  public void setPacManDirection(byte[] directions)
  {
    pacmanDirection = directions[0];
    debug("pacman direction: " + pacmanDirection);
  }

  @Override
  public void setGhostDirection(int id, byte direction)
  {
    ghostDirection = direction;
    debug("ghost direction: " + direction);
  }

  @Override
  public boolean isGameOver()
  {
    return gameOver;
  }

  @Override
  public int getPacManScore()
  {
    return score;
  }

  @Override
  public int getGhostScore()
  {
    return -score;
  }

  double distNearest(double x, double y, int direction, int what)
  {
    int i, j;

    switch (direction)
    {
    case Thing.UP:
      i = (int) Math.round(x);
      j = (int) Math.floor(y);
      while (j > 0)
      {
        if (map[i][j] == what)
          return y - j;
        j--;
      }
      break;
    case Thing.DOWN:
      i = (int) Math.round(x);
      j = (int) Math.ceil(y);
      while (j < 5)
      {
        if (map[i][j] == what)
          return j - y;
        j++;
      }
      break;
    case Thing.LEFT:
      i = (int) Math.floor(x);
      j = (int) Math.round(y);
      while (i >= 0)
      {
        if (map[i][j] == what)
          return y - i;
        i--;
      }
      break;
    case Thing.RIGHT:
      i = (int) Math.ceil(x);
      j = (int) Math.round(y);
      while (i < 5)
      {
        if (map[i][j] == what)
          return i - y;
        i++;
      }
      break;
    }

    return Config.get().getInfDistance();
  }

  private double nextX(double x, int direction, double step)
  {
    switch (direction)
    {
    case Thing.LEFT:
      if (x - step >= 0)
        return x - step;
      break;
    case Thing.RIGHT:
      if (x + step <= 4)
        return x + step;
      break;
    }
    return x;
  }

  private double nextY(double y, int direction, double step)
  {
    switch (direction)
    {
    case Thing.UP:
      if (y - step >= 0)
        return y - step;
      break;
    case Thing.DOWN:
      if (y + step <= 4)
        return y + step;
      break;
    }
    return y;
  }

  @Override
  public int getGameResult()
  {
    // TODO Auto-generated method stub
    return 0;
  }

}
