package com.anji.neatpacman;

import java.util.ArrayList;
import java.util.List;

/**
 * this class is not thread safe.
 * 
 * should be re-used in the same game playing to reduce unnecessary object creation.
 * 
 * @author quyin
 * 
 */
public class GameState
{
  
  // total number of inputs: 17 + 4G + 2J + 2P
  // G = # ghosts, J = # junctions, P = # power pills
  
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

  // distances to nearest wall / junction / dot / pill, in 4 directions
  public double[] distsNearestWall;

  public double[] distsNearestJunction;

  public double[] distsNearestDot;

  // is a ghost affected by power pill? yes - 1.0, no - 0.0
  public double[] isGhostAffected;
  
  /**
   * no cache / lazy evaluation, since this object may be re-used to reduce memory consumption.
   * 
   * @return
   */
  public double[] getValues()
  {
    List<Double> vs = getVs();
    double[] values = new double[vs.size()];
    for (int i = 0; i < vs.size(); ++i)
    {
      values[i] = vs.get(i);
    }
    return values;
  }
  
  public double[] getValues(double bias)
  {
    List<Double> vs = getVs();
    vs.add(bias);
    double[] values = new double[vs.size()];
    for (int i = 0; i < vs.size(); ++i)
    {
      values[i] = vs.get(i);
    }
    return values;
  }

  private List<Double> getVs()
  {
    List<Double> vs = new ArrayList<Double>(256);
    vs.add(distPacMan);
    vs.addAll(asList(distGhosts));
    vs.addAll(asList(distJunctions));
    vs.addAll(asList(distPowerPills));
    vs.add(deltaDistPacMan);
    vs.addAll(asList(deltaDistGhosts));
    vs.addAll(asList(deltaDistJunctions));
    vs.addAll(asList(deltaDistPowerPills));
    vs.add(xPacMan);
    vs.add(yPacMan);
    vs.addAll(asList(xGhosts));
    vs.addAll(asList(yGhosts));
    vs.addAll(asList(distsNearestWall));
    vs.addAll(asList(distsNearestJunction));
    vs.addAll(asList(distsNearestDot));
    vs.addAll(asList(isGhostAffected));
    return vs;
  }

  public static List<Double> asList(double[] vs)
  {
    List<Double> rst = new ArrayList<Double>(vs.length);
    for (int i = 0; i < vs.length; ++i)
    {
      rst.add(vs[i]);
    }
    return rst;
  }

}
