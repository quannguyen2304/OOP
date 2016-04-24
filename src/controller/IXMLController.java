package controller;

import java.io.File;

import org.w3c.dom.Document;

import graph.GraphComponent;

public interface IXMLController {
	// Create xml content
	public Document createXMLContent(GraphComponent com);
	
	// Load xml to component
	public GraphComponent loadXMLFile(GraphComponent com,  File xmlContent);
}
