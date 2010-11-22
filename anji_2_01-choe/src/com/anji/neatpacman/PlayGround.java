package com.anji.neatpacman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.anji.neatpacman.ghosts.GhostsPlayer;
import com.anji.neatpacman.player.PacmanPlayer;
import com.anji.tournament.PlayerResults;

public class PlayGround
{

  public static int         numOfThreads = 3;

  public static int         numOfGhosts  = 4;

  private static PlayGround the          = null;

  public static PlayGround get()
  {
    if (the == null)
    {
      synchronized (PlayGround.class)
      {
        if (the == null)
          the = new PlayGround(numOfThreads);
      }
    }
    return the;
  }

  private int                 numThreads;

  private List<PlayerResults> availablePlayers = null;

  private Object              pendPlayers      = new Object();

  private List<PlayerResults> availableGhosts  = null;

  private Object              pendGhosts       = new Object();

  private List<MyPlayerPair> playerPairs      = null;

  private Object              lockPlayerPairs  = new Object();

  private int                 numOfPairs;

  private Executor            pool;

  private int                 numOfPairsDone   = 0;

  private Object              lockPairsDone    = new Object();

  private Object              pendAllDone      = new Object();

  private PlayGround(int numThreads)
  {
    this.numThreads = numThreads;
    pool = Executors.newFixedThreadPool(this.numThreads);
  }

  public void registerPlayers(List<PlayerResults> playerResults)
  {
    availablePlayers = playerResults;
    pendPlayers.notifyAll();
  }

  public void registerGhosts(List<PlayerResults> ghostsResults)
  {
    availableGhosts = ghostsResults;
    pendGhosts.notifyAll();
  }

  public List<PlayerResults> getPlayers()
  {
    while (availablePlayers == null)
    {
      try
      {
        pendPlayers.wait();
      }
      catch (InterruptedException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return availablePlayers;
  }

  public List<PlayerResults> getGhosts()
  {
    while (availableGhosts == null)
    {
      try
      {
        pendGhosts.wait();
      }
      catch (InterruptedException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return availableGhosts;
  }

  public List<MyPlayerPair> getPlayerPairs()
  {
    if (playerPairs == null)
    {
      synchronized (lockPlayerPairs)
      {
        if (playerPairs == null)
        {
          playerPairs = new ArrayList<MyPlayerPair>();
          
          List<PlayerResults> playersList = getPlayers();
          List<PlayerResults> ghostsList = getGhosts();
          Collections.shuffle(playersList);
          Collections.shuffle(ghostsList);
          
          for (PlayerResults player : playersList)
          {
            for (PlayerResults ghosts : ghostsList)
            {
              MyPlayerPair mpp = new MyPlayerPair();
              mpp.contestant = player;
              mpp.opponent = ghosts;
              playerPairs.add(mpp);
            }
          }
          numOfPairs = playerPairs.size();
        }
      }
    }
    return playerPairs;
  }

  public void play(final PlayerResults player, final PlayerResults ghosts)
  {
    pool.execute(new Runnable()
    {
      @Override
      public void run()
      {
        playTheGame(player, ghosts);
      }
    });
  }

  private void playTheGame(PlayerResults player, PlayerResults ghosts)
  {
    Maze maze = new DummyMaze(numOfGhosts);
    while (!maze.isGameOver())
    {
      GameState playerState = maze.getPacManState();
      GameState[] ghostsStates = new GameState[numOfGhosts];
      for (int i = 0; i < numOfGhosts; ++i)
        ghostsStates[i] = maze.getGhostState(i);

      // feed input to NN, and get responses
      PacmanPlayer pl = (PacmanPlayer) player.getPlayer();
      GhostsPlayer gh = (GhostsPlayer) ghosts.getPlayer();
      int playerMove = pl.move(playerState);
      int[] ghostsMove = gh.move(ghostsStates);

      maze.setPacManDirection(playerMove);
      for (int i = 0; i < numOfGhosts; ++i)
        maze.setGhostDirection(i, ghostsMove[i]);
      maze.tick();
    }

    player.getResults().incrementRawScore(maze.getPacManScore());
    ghosts.getResults().incrementRawScore(maze.getGhostScore());

    synchronized (lockPairsDone)
    {
      numOfPairsDone++;
      pendAllDone.notifyAll();
    }
  }

  public void waitForFinish()
  {
    while (numOfPairsDone < numOfPairs)
    {
      try
      {
        pendAllDone.wait();
      }
      catch (InterruptedException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    synchronized (PlayGround.class)
    {
      the = null;
    }
  }

}
