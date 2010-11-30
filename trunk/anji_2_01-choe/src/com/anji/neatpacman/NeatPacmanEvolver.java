package com.anji.neatpacman;

import com.anji.neat.Evolver;
import com.anji.util.Properties;

public class NeatPacmanEvolver
{
  
  static
  {
//    Debug.setEnable(DummyMaze.class, true);
  }

  private Evolver playerEvolver;

  private Evolver ghostEvolver;

  private boolean playerDone   = false;

  private boolean ghostDone    = false;

  private Object  pendWorkDone = new Object();

  public NeatPacmanEvolver(Properties playerProps, Properties ghostsProps) throws Exception
  {
    playerEvolver = new Evolver();
    playerEvolver.init(playerProps);
    ghostEvolver = new Evolver();
    ghostEvolver.init(ghostsProps);
  }

  public void evolve()
  {
    Thread t1 = new Thread(new Runnable()
    {
      @Override
      public void run()
      {
          try
          {
            playerEvolver.run();
          }
          catch (Exception e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          playerDone = true;
          synchronized (pendWorkDone)
          {
            pendWorkDone.notify();
          }
      }
    });
    Thread t2 = new Thread(new Runnable()
    {
      @Override
      public void run()
      {
          try
          {
            ghostEvolver.run();
          }
          catch (Exception e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          ghostDone = true;
          synchronized (pendWorkDone)
          {
            pendWorkDone.notifyAll();
          }
      }
    });

    t1.start();
    t2.start();

    while (!playerDone || !ghostDone)
    {
      synchronized (pendWorkDone)
      {
        try
        {
          pendWorkDone.wait();
        }
        catch (InterruptedException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }
  
  public static void main(String[] args) throws Exception
  {
//    Debug.setEnable(PlayGround.class, true);
    
    if (args.length != 2)
    {
      System.err.println("args: <pacman-properties-file-path> <ghost-properties-file-path>");
      System.exit(-1);
    }
    
    String pacmanPropsFp = args[0];
    String ghostPropsFp = args[1];
    
    Properties pacmanProps = new Properties(pacmanPropsFp);
    Properties ghostProps = new Properties(ghostPropsFp);
    
    NeatPacmanEvolver npe = new NeatPacmanEvolver(pacmanProps, ghostProps);
    npe.evolve();
  }

}
