package com.anji.neatpacman;

import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.anji.neatpacman.ghosts.GhostsPlayer;
import com.anji.neatpacman.player.PacmanPlayer;
import com.anji.tournament.PlayerResults;

public class PlayGround extends Debug implements Runnable
{

  public static int         numOfThreads = 1;

  public static int         numOfGhosts  = 1;

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

  private Queue<MyPlayerPair> playerPairs      = null;

  private Object              lockPlayerPairs  = new Object();

  private Executor            pool;

  private Object              pendDone         = new Object();

  private PlayGround(int numThreads)
  {
    this.numThreads = numThreads;
    pool = Executors.newFixedThreadPool(this.numThreads);
    Thread t = new Thread(this);
    t.start();
  }

  @Override
  public void run()
  {
    Queue<MyPlayerPair> pairs = getPlayerPairs();
    MyPlayerPair pair = null;
    while ((pair = pairs.poll()) != null)
    {
      play(pair);
    }
    
    ExecutorService es = (ExecutorService) pool;
    es.shutdown();
    try
    {
      es.awaitTermination(7, TimeUnit.DAYS);
    }
    catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    synchronized (pendDone)
    {
      synchronized (PlayGround.class)
      {
        the = null;
      }
      pendDone.notifyAll();
    }
  }

  public void play(final MyPlayerPair pair)
  {
    pool.execute(new Runnable()
    {
      @Override
      public void run()
      {
        playTheGame(pair.player, pair.ghosts);
      }
    });
  }

  private void playTheGame(PlayerResults player, PlayerResults ghosts)
  {
    Maze maze = new DummyMaze();

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
  }

  public void waitForDone()
  {
    synchronized (pendDone)
    {
      try
      {
        pendDone.wait();
      }
      catch (InterruptedException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public void registerPlayers(List<PlayerResults> playerResults)
  {
    availablePlayers = playerResults;
    synchronized (pendPlayers)
    {
      pendPlayers.notifyAll();
    }
  }

  public void registerGhosts(List<PlayerResults> ghostsResults)
  {
    availableGhosts = ghostsResults;
    synchronized (pendGhosts)
    {
      pendGhosts.notifyAll();
    }
  }

  public List<PlayerResults> getPlayers()
  {
    while (availablePlayers == null)
    {
      synchronized (pendPlayers)
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
    }
    return availablePlayers;
  }

  public List<PlayerResults> getGhosts()
  {
    while (availableGhosts == null)
    {
      synchronized (pendGhosts)
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
    }
    return availableGhosts;
  }

  public Queue<MyPlayerPair> getPlayerPairs()
  {
    if (playerPairs == null)
    {
      synchronized (lockPlayerPairs)
      {
        if (playerPairs == null)
        {
          playerPairs = new ConcurrentLinkedQueue<MyPlayerPair>();

          List<PlayerResults> playersList = getPlayers();
          List<PlayerResults> ghostsList = getGhosts();
          Collections.shuffle(playersList);
          Collections.shuffle(ghostsList);

          for (PlayerResults player : playersList)
          {
            for (PlayerResults ghosts : ghostsList)
            {
              MyPlayerPair mpp = new MyPlayerPair();
              mpp.player = player;
              mpp.ghosts = ghosts;
              playerPairs.add(mpp);
            }
          }
        }
      }
    }
    return playerPairs;
  }

}
