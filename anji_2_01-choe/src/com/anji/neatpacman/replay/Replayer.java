package com.anji.neatpacman.replay;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.anji.integration.Activator;
import com.anji.integration.ActivatorTranscriber;
import com.anji.integration.TranscriberException;
import com.anji.neatpacman.Config;
import com.anji.neatpacman.GameState;
import com.anji.neatpacman.ghosts.GhostsPlayer;
import com.anji.neatpacman.player.PacmanPlayer;
import com.anji.neatpacman.simulate.Simulator;
import com.anji.persistence.FilePersistence;
import com.anji.util.DummyConfiguration;
import com.anji.util.Properties;

public class Replayer extends WindowAdapter
{

  public static final String propertyFilePath = "replay.properties";

  private boolean            windowClosing    = false;

  public void replay(Properties props, Chromosome pacmanChromosome, Chromosome ghostsChromosome)
      throws TranscriberException
  {
    // prepare players
    ActivatorTranscriber transcriber = new ActivatorTranscriber();
    transcriber.init(props);
    Activator pacmanActivator = transcriber.newActivator(pacmanChromosome);
    Activator ghostsActivator = transcriber.newActivator(ghostsChromosome);
    PacmanPlayer pl = new PacmanPlayer(pacmanActivator);
    GhostsPlayer gh = new GhostsPlayer(ghostsActivator);

    int numOfGhosts = Config.get().getNumOfGhosts();
    int ms = Config.get().getMilliSecBetweenTicks();

    Simulator maze = new Simulator();
    maze.frame.addWindowListener(this);
    maze.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    GameState playerState = null;
    GameState[] ghostsStates = null;
    int n = 0;
    while (!windowClosing && !maze.isGameOver())
    {
      maze.tick();
      n++;

      try
      {
        Thread.sleep(ms);
      }
      catch (InterruptedException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      playerState = maze.getPacManState();
      ghostsStates = new GameState[numOfGhosts];
      for (int i = 0; i < numOfGhosts; ++i)
        ghostsStates[i] = maze.getGhostState(i);

      // feed input to NN, and get responses
      byte[] playerMoves = pl.move(playerState);
      byte[] ghostsMove = gh.move(ghostsStates);

      maze.setPacManDirection(playerMoves);
      for (int i = 0; i < numOfGhosts; ++i)
      {
        maze.setGhostDirection(i, ghostsMove[i]);
      }
    }
  }

  @Override
  public void windowClosing(WindowEvent e)
  {
    windowClosing = true;
    super.windowClosing(e);
  }

  private static Chromosome loadChromosome(Configuration config, String chromosomeFp)
      throws ParserConfigurationException, SAXException, IOException
  {
    InputStream in = new FileInputStream(chromosomeFp);
    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document doc = builder.parse(in);
    return FilePersistence.chromosomeFromXml(config, doc.getFirstChild());
  }

  public static void main(String[] args) throws IOException, ParserConfigurationException,
      SAXException, TranscriberException
  {
    if (args.length != 2)
    {
      System.err.println("args: <pacman-chromosome> <ghosts-chromosome>");
      System.exit(-1);
    }

    // load properties
    Properties props = new Properties(propertyFilePath);

    // init properties used in simulation
    Config.init(props);
    boolean gui = Config.get().isGuiEnabled();
    if (!gui)
    {
      System.err.println("WARNING - you must enable GUI in property file to show replay!");
      System.exit(-2);
    }

    // load chromesomes
    Configuration config = new DummyConfiguration();
    Chromosome pacmanChromosome = loadChromosome(config, args[0]);
    Chromosome ghostsChromosome = loadChromosome(config, args[1]);

    Replayer rep = new Replayer();
    rep.replay(props, pacmanChromosome, ghostsChromosome);
  }

}
