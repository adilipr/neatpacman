package com.anji.neatpacman.simulate;

import java.util.ArrayList;

public class Node 
{
	int x;
	int y;
	boolean isvisited;
	ArrayList<Node> children;
	
	public Node()
	{
		super();
		isvisited=false;
	}
	public Node(int xx,int yy)
	{
		this();
		setData(xx,yy);
	}
	public void setData(int xx,int yy)
	{
		setX(xx);
		setY(yy);
	}
	public String getPtString()
	{
		return "("+getX()+","+getY()+")";
	}
	public void setX(int xx)
	{
		this.x=xx;
	}
	public void setY(int yy)
	{
		this.y=yy;
	}
	public int getX()
	{
		return this.x;
	}
	public int getY()
	{
		return this.y;
	}
	public void addChild(Node child)
	{
		if(children==null)
		{
			children=new ArrayList<Node>();
		}
		children.add(child);
	}
	public ArrayList<Node> getChildren()
	{
		if(this.children==null)
		{
			return new ArrayList<Node>();
		}
		return this.children;
	}
	public int getNumChildren()
	{
		if(this.children==null)
		{
			return 0;
		}
		return children.size();
	}
}
