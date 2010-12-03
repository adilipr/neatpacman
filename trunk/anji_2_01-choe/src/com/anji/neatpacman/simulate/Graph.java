package com.anji.neatpacman.simulate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
//import org.apache.commons.collections 
import pacman.GameModel;
import pacman.PacMan;

public class Graph 
{
	HashMap<String,Node> mapStrNode;//map to retrieve node from string eg:<(1,2),Node>
	HashMap<String,Integer> mapStrIndex;//map to retrieve index(for matrix) from string eg:<(1,2),2>
	//BidiMap<String,Integer> bidimapStrIndex;
	HashMap<Integer,String> mapIndexStr;
	Node root;//starting node
	int[][] spl;//2-D array to store shortest path lengths
	int numnodes;//counter number of nodes
	HashMap<String,Node> mapJunctions;
	public Graph()
	{
		mapStrNode=new HashMap<String,Node>();
		mapStrIndex=new HashMap<String,Integer>();
		mapIndexStr=new HashMap<Integer,String>();
		root=new Node(1,1);
		numnodes=0;
		mapJunctions=new HashMap<String,Node>();
	}
	public void createGraph(PacMan pacman)
	{
		GameModel gamemodel;
		//PacMan pacman = null;
		//PacMan pacman;
		//pacman.init();
		gamemodel=new GameModel(pacman, 4);
		int [][] gameState= new int[28][31];
		gamemodel.loadPacManMaze();
		gameState=gamemodel.getGameState();
		Queue<Node> q = new LinkedList<Node>();
		q.add(root);
		Node head=new Node();
		Node child;
		String ptString;
		String childptString;
		int i=1,j=1;
		int numOpenings=0;
		while(!q.isEmpty())
		{
			numOpenings=0;
			head=q.peek();
			q.remove();
			i=head.getX();
			j=head.getY();
			ptString=head.getPtString();
			if(!head.isvisited)
			{
				head.isvisited=true;
				if(!mapStrNode.containsKey(ptString))
				{
					mapStrNode.put(ptString, head);
					mapStrIndex.put(ptString, numnodes);
					mapIndexStr.put(numnodes,ptString);
					numnodes++;
				}
				if(!GameModel.hasNorthWall(gameState[i][j]))
				{
					if(j!=0)
					{
						child=new Node(i,j-1);
						childptString=child.getPtString();
						if(!mapStrNode.containsKey(childptString))
						{
							mapStrNode.put(childptString, child);
							mapStrIndex.put(childptString, numnodes);
							mapIndexStr.put(numnodes,childptString);
							numnodes++;
						}
						else
						{
							child=mapStrNode.get(childptString);
						}
						numOpenings++;
						q.add(child);
						head.addChild(child);
					}
				}
				if(!GameModel.hasSouthWall(gameState[i][j]))
				{
					if(j!=30)
					{
						child=new Node(i,j+1);
						childptString=child.getPtString();
						if(!mapStrNode.containsKey(childptString))
						{
							mapStrNode.put(childptString, child);
							mapStrIndex.put(childptString, numnodes);
							mapIndexStr.put(numnodes,childptString);
							numnodes++;
						}
						else
						{
							child=mapStrNode.get(childptString);
						}
						numOpenings++;
						q.add(child);
						head.addChild(child);
					}
				}
				if(!GameModel.hasWestWall(gameState[i][j]))
				{
					if(i!=0)
					{
						child=new Node(i-1,j);
						childptString=child.getPtString();
						if(!mapStrNode.containsKey(childptString))
						{
							mapStrNode.put(childptString, child);
							mapStrIndex.put(childptString, numnodes);
							mapIndexStr.put(numnodes,childptString);
							numnodes++;
						}
						else
						{
							child=mapStrNode.get(childptString);
						}
						numOpenings++;
						q.add(child);
						head.addChild(child);
					}
				}
				if(!GameModel.hasEastWall(gameState[i][j]))
				{
					if(i!=27)
					{
						child=new Node(i+1,j);
						childptString=child.getPtString();
						if(!mapStrNode.containsKey(childptString))
						{
							mapStrNode.put(childptString, child);
							mapStrIndex.put(childptString, numnodes);
							mapIndexStr.put(numnodes,childptString);
							numnodes++;
						}
						else
						{
							child=mapStrNode.get(childptString);
						}
						numOpenings++;
						q.add(child);
						head.addChild(child);
					}
				}
				if(numOpenings>2 && !mapJunctions.containsKey(head))
				{
					mapJunctions.put(ptString, head);
				}
			}
		}
		//special case- to handle opening in maze, connect (0,14) and (27,14)
		head=mapStrNode.get("(0,14)");
		child=mapStrNode.get("(27,14)");
		head.addChild(child);
		
		head=mapStrNode.get("(27,14)");
		child=mapStrNode.get("(0,14)");
		head.addChild(child);
		
	}
	public void createMatrix()
	{
		int i,j;
		//Initialize spl to 9999
		spl=new int[numnodes][numnodes];
		for(i=0;i<numnodes;i++)
		{
			for(j=0;j<numnodes;j++)
			{
				spl[i][j]=9999;
				if(i==j)
				{
					spl[i][j]=0;
				}
			}
		}
		Node temp;
		ArrayList<Node> childs;
		Set<String> set=mapStrNode.keySet();
		Iterator<String> iter=set.iterator();
		int counter=0;
		while(iter.hasNext())
		{
			temp=mapStrNode.get(iter.next());
			i=mapStrIndex.get(temp.getPtString());
			childs=temp.getChildren();
			for(int ii=0;ii<childs.size();ii++)
			{
				j=mapStrIndex.get(childs.get(ii).getPtString());
				spl[i][j]=1;
				counter++;
			}
		}
		//debugging purpose
		counter=300;
	}
	public void calculateSPL()
	{
		int i,j,k;
		int n=numnodes;
		for(k=0;k<n;k++)
		{
			for(i=0;i<n;i++)
			{
				for(j=0;j<n;j++)
				{
					spl[i][j]=Math.min(spl[i][j],spl[i][k]+spl[k][j]);
				}
			}
		}
		//debugging purpose
		n=numnodes;
	}
	public void writeSPLToFile()
	{
		int i,j;
		String xString,yString,outputStr;
		int n=numnodes;
		try
		{
			// Create file 
		    FileWriter fstream = new FileWriter("distances.txt",true);
		    BufferedWriter out = new BufferedWriter(fstream);
			for(i=0;i<n;i++)
			{
				xString=mapIndexStr.get(i);
				for(j=0;j<n;j++)
				{
					yString=mapIndexStr.get(j);
					outputStr=xString+" "+yString+" "+spl[i][j];
					try
					{
						out.write(outputStr);
						out.write("\r\n");
					}
					catch (IOException e) 
					{
						e.printStackTrace();
						System.out.println("IO exception\n"+e.getMessage());
					}
				}
			}
		    //Close the output stream
		    out.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("File not found\n");
		}
	}
	public void callGraphFunctions(PacMan pacman)
	{
		createGraph(pacman);
		createMatrix();
		calculateSPL();
		writeSPLToFile();
	}
/*	public static void main(String args[])
	{
		Graph g=new Graph();
		g.createGraph();
	}*/
}
