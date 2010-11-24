package com.anji.neatpacman.test;

import org.junit.Test;

import com.anji.neatpacman.Debug;
import com.anji.neatpacman.DummyMaze;

public class TestDummyMaze
{

  @Test
  public void testNoAction()
  {
    Debug.setEnable(true);
    
    DummyMaze dm = new DummyMaze();
    while (!dm.isGameOver())
      dm.tick();
    System.out.println(dm.getPacManScore());
  }
  
}
