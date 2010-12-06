package com.anji.neatpacman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Map;

import pacman.Thing;

public class Utils
{
  
  public static int BUF_SIZE = 4096;
  
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
  
  private static byte[] directions = { Thing.UP, Thing.DOWN, Thing.LEFT, Thing.RIGHT };
  
  public static byte outputsToDirection(double[] outputs)
  {
    if (outputs.length != directions.length)
    {
      System.err.println("error in converting outputs to direction!");
      return Thing.STILL;
    }
    return directions[argmax(outputs)];
  }
  
  public static <K, V> V mapUse(Map<K, V> map, K key, V defaultValue)
  {
    V t = map.get(key);
    return t == null ? defaultValue : t;
  }

  public static double sum(double[] v)
  {
    double s = 0;
    for (int i = 0; i < v.length; ++i)
    {
      s += v[i];
    }
    return s;
  }

  public static int argmin(double[] v)
  {
    double min = Double.POSITIVE_INFINITY;
    int j = -1;
    for (int i = 0; i < v.length; ++i)
    {
      if (min > v[i])
      {
        min = v[i];
        j = i;
      }
    }
    return j;
  }

  public static double min(double[] v)
  {
    double min = Double.POSITIVE_INFINITY;
    for (int i = 0; i < v.length; ++i)
    {
      if (min > v[i])
      {
        min = v[i];
      }
    }
    return min;
  }
  
  public static String readToString(String filePath) throws IOException
  {
    StringBuilder sb = new StringBuilder();
    
    BufferedReader br = new BufferedReader(new FileReader(filePath));
    char[] buf = new char[BUF_SIZE];
    int len = br.read(buf);
    for (int i = 0; i < len; ++i)
    {
      sb.append(buf[i]);
    }
    
    return sb.toString();
  }
  
}
