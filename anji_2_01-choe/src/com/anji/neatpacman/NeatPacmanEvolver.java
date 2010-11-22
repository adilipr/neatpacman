package com.anji.neatpacman;

import com.anji.neat.Evolver;
import com.anji.util.Properties;

public class NeatPacmanEvolver
{

  private Evolver playerEvolver;

  private Evolver ghostEvolver;

  private boolean playerDone   = false;

  private boolean ghostDone    = false;

  private Object  pendWorkDone = new Object();

  public void init(Properties playerProps, Properties ghostsProps) throws Exception
  {
    playerEvolver = new Evolver();
    playerEvolver.init(playerProps);
    ghostEvolver = new Evolver();
    ghostEvolver.init(ghostsProps);
  }

  public void evolve() throws InterruptedException
  {
    Thread t1 = new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          playerEvolver.run();
          playerDone = true;
          pendWorkDone.notifyAll();
        }
        catch (Exception e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
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
          ghostDone = true;
          pendWorkDone.notifyAll();
        }
        catch (Exception e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    });

    t1.start();
    t2.start();

    while (!playerDone || !ghostDone)
    {
      pendWorkDone.wait();
    }
  }

}
