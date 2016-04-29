package controller;

import javax.swing.JFrame;

import graph.GraphComponent;

public interface IGraphController {
	public JFrame createFrame();
	
	public void deleteFrame(JFrame frame);
	
	public void saveFrame(GraphComponent frame);
	
	public void loadFrame(GraphComponent frame);
	
	public void run();
	
	public void changecolor(GraphComponent com);
}