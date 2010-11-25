package com.anji.neatpacman;

public class Utils
{
  
  public static int argmax(double[] values)
  {
    int maxp = -1;
    double maxv = Double.NEGATIVE_INFINITY;
    for (int i = 0; i < values.length; ++i)
    {
      if (maxv < values[i])
      {
        maxp = i;
        maxv = values[i];
      }
    }
    return maxp;
  }
  
  private static int[] directions = { Maze.UP, Maze.DOWN, Maze.LEFT, Maze.RIGHT };
  
  public static int outputsToDirection(double[] outputs)
  {
    if (outputs.length != directions.length)
    {
      System.err.println("error in converting outputs to direction!");
      return Maze.ERROR;
    }
    return directions[argmax(outputs)];
  }
  
}
