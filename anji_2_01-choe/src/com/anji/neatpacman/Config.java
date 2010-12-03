package com.anji.neatpacman;

import com.anji.util.Properties;

public class Config
{

  private static Config the = null;

  public static Config get()
  {
    synchronized (Config.class)
    {
      return the;
    }
  }

  public static void init(Properties prop)
  {
    synchronized (Config.class)
    {
      the = new Config(prop);
    }
  }

  private Config(Properties prop)
  {
    playerMaxScore = prop.getIntProperty("neatpacman.player.max_score", 470);
    playerMinScore = prop.getIntProperty("neatpacman.player.min_score", 0);
    infDistance = prop.getIntProperty("neatpacman.infinite_distance", 40);
    maxTick = prop.getIntProperty("neatpacman.max_tick", 1000);
    numOfThreads = prop.getIntProperty("neatpacman.num_of_threads", 1);
    numOfGhosts = prop.getIntProperty("neatpacman.num_of_ghosts", 1);
    isGuiEnabled = prop.getBooleanProperty("neatpacman.gui_enabled", false);
    milliSecBetweenTicks = prop.getIntProperty("neatpacman.ms_between_ticks", 0);
  }

  private int playerMaxScore;

  public int getPlayerMaxScore()
  {
    return playerMaxScore;
  }

  private int playerMinScore;

  public int getPlayerMinScore()
  {
    return playerMinScore;
  }

  public int getGhostsMaxScore()
  {
    return -playerMinScore;
  }

  public int getGhostsMinScore()
  {
    return -playerMaxScore;
  }

  private int infDistance;

  public int getInfDistance()
  {
    return infDistance;
  }

  private int maxTick;

  public int getMaxTick()
  {
    return maxTick;
  }

  private int numOfThreads;

  public int getNumOfThreads()
  {
    return numOfThreads;
  }

  private int numOfGhosts;

  public int getNumOfGhosts()
  {
    return numOfGhosts;
  }
  
  private boolean isGuiEnabled;
  
  public boolean isGuiEnabled()
  {
    return isGuiEnabled;
  }
  
  private int milliSecBetweenTicks;
  
  public int getMilliSecBetweenTicks()
  {
    return milliSecBetweenTicks;
  }

}
