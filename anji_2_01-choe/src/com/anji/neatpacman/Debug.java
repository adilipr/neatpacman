package com.anji.neatpacman;

import java.io.PrintStream;

public class Debug
{

  private static boolean _enabled = false;

  public static void setEnable(boolean enabled)
  {
    _enabled = enabled;
  }
  
  private static PrintStream _out = System.err;
  
  public static void setOutputStream(PrintStream out)
  {
    _out = out;
  }

  public void debug(String fmt, Object... objs)
  {
    if (_enabled)
    {
      String msg = String.format(fmt, objs);
      _out.println();
      _out.format("%s: %s", this, msg);
      _out.println();
    }
  }

}
