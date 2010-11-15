package neatpacman;

public class GameState
{

  // distances to pacman / ghosts / junctions / pills
  public double   distPacMan;

  public double[] distGhosts;

  public double[] distJunctions;

  public double[] distPowerPills;

  // delta distances, measuring movement
  public double   deltaDistPacMan;

  public double[] deltaDistGhosts;

  public double[] deltaDistJunctions;

  public double[] deltaDistPowerPills;

  // relative coordinations
  public double   xPacMan;

  public double   yPacMan;

  public double[] xGhosts;

  public double[] yGhosts;

  // distances to nearest wall / junction / dot / pill
  public double[] distsNearestWall;

  public double[] distsNearestJunction;

  public double[] distsNearestDot;

  public double[] distsPowerPill;

  // is pacman in power pill state?
  public double   isPacManInPower;

}
