package controller;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import core.State;
import core.Transition;
import graph.Edge;
import graph.GraphComponent;
import graph.Shape;

public class XMLController implements IXMLController {

	// Create xml content
	@Override
	public Document createXMLContent(GraphComponent com) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		Document doc = null;

		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();

			// root elements
			Element rootElement = createElement(doc, "automate");
			doc.appendChild(rootElement);

			// start point
			createElement(doc, "startpoint", rootElement, com.getInitialState() + "");

			// end point
			createXMLContentForFinalStates(doc, rootElement, com.getFinalState());

			// color
			createElement(doc, "color", rootElement, Integer.toString(com.getColorState().getRGB()));

			// edges elements
			createXMLContentForEdges(doc, rootElement, com.getListLine());

			// shapes elements
			createXMLContentForShapes(doc, rootElement, com.getListPoints(), "");

			// states
			createXMLContentForStates(doc, rootElement, com.getArrayStates());

			return doc;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return doc;
	}

	// Load xml to component
	@Override
	public GraphComponent loadXMLFile(GraphComponent com, File xmlContent) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;

		// init component
		com.initGraphComponent();

		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc;
			try {
				doc = dBuilder.parse(xmlContent);

				List<Edge> lstEdge = com.getListLine();
				List<State> lstState = com.getArrayStates();
				List<Transition<String>> lstTransition = new ArrayList<>();

				// start point
				com.setInitialState(doc.getElementsByTagName("startpoint").item(0).getTextContent());

				// end point
				LoadXMLToListFinalPoint(doc, com.getFinalState());

				// color
				com.setColorState(
						new Color(Integer.parseInt(doc.getElementsByTagName("color").item(0).getTextContent())));

				// edges
				LoadXMLToListEdge(doc, lstEdge);

				// shapes
				LoadXMLToListShape(doc, com.getListPoints(), "shape");

				// states
				LoadXMLToListState(doc, lstState);

				// transitions
				com.setTransitions(LoadTransitionsFromEdges(lstEdge, lstTransition, lstState));
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return com;
	}

	// ===============================================================
	// Private methods
	// ===============================================================

	// ======================== Save Section ==========================
	// Create xml content for final states
	private void createXMLContentForFinalStates(Document doc, Element rootElement, List<Integer> lstFinalState) {
		Element finalStatesNode = createElement(doc, "finalstates", rootElement);

		for (Integer integer : lstFinalState) {
			// create child
			createElement(doc, "finalstate", finalStatesNode, integer + "");
		}
	}

	// Create xml content for edges
	private void createXMLContentForEdges(Document doc, Element rootElement, List<Edge> lstEdge) {
		Element edgesNode = createElement(doc, "edges", rootElement);

		for (Edge edge : lstEdge) {
			// create child
			Element edgeChild = createElement(doc, "edge", edgesNode);

			// source attribute
			createElement(doc, "source", edgeChild, edge.getSource() + "");

			// destination attribute
			createElement(doc, "destination", edgeChild, edge.getDestination() + "");

			// movable attribute
			createElement(doc, "movable", edgeChild, edge.isMovable() + "");

			// label attribute
			createElement(doc, "label", edgeChild, edge.getLabel());

			// join point attribute
			createXMLContentForShapes(doc, edgeChild, edge.getJoinPoint(), "jointpoint");
		}
	}

	// Create xml content for shapes
	private void createXMLContentForShapes(Document doc, Element rootElement, List<Shape> lstShape,
			String currentNameNode) {
		Element shapesNode;
		boolean createChild = false;

		if (currentNameNode == "") {
			shapesNode = createElement(doc, "shapes", rootElement);
			createChild = true;
		} else {
			shapesNode = createElement(doc, currentNameNode, rootElement);
		}

		for (Shape shape : lstShape) {
			// create child
			Element shapeChild = createChild ? createElement(doc, "shape", shapesNode) : shapesNode;

			// type element
			createElement(doc, "type", shapeChild, shape.getType());

			// point2d element
			createElement(doc, "point", shapeChild, shape.getPoint().getX() + "-" + shape.getPoint().getY());

			// radius element
			createElement(doc, "radius", shapeChild, shape.getRadius() + "");

			// dx element
			createElement(doc, "dx", shapeChild, shape.getDx() + "");

			// dy element
			createElement(doc, "dy", shapeChild, shape.getDy() + "");

			// have line element
			createElement(doc, "lineindex", shapeChild, shape.getLineIndex() + "");

			// line index element
			createElement(doc, "haveline", shapeChild, shape.isHaveLine() + "");

			// movable element
			createElement(doc, "movable", shapeChild, shape.isMovable() + "");
		}
	}

	// Create xml content for states
	private void createXMLContentForStates(Document doc, Element rootElement, ArrayList<State> arrStates) {
		Element statesNode = createElement(doc, "states", rootElement);

		for (State integer : arrStates) {
			Element stateChild = createElement(doc, "state", statesNode);

			// initial
			createElement(doc, "initial", stateChild, integer.getInitial() + "");

			// terminal
			createElement(doc, "terminal", stateChild, integer.getTerminal() + "");
		}
	}

	// Create element with node name
	private Element createElement(Document doc, String nodeName) {
		return doc.createElement(nodeName);
	}

	// Create element with node name and add to note parent
	private Element createElement(Document doc, String nodeName, Element nodeParent) {
		Element nodeChild = doc.createElement(nodeName);
		nodeParent.appendChild(nodeChild);

		return nodeChild;
	}

	// Create element with node name, value and add to note parent
	private Element createElement(Document doc, String nodeName, Element nodeParent, String value) {
		Element nodeChild = doc.createElement(nodeName);
		nodeChild.appendChild(doc.createTextNode(value));
		nodeParent.appendChild(nodeChild);

		return nodeChild;
	}

	// ======================== Load Section ==========================
	// load xml content to list final point
	private void LoadXMLToListFinalPoint(Document doc, List<Integer> lstFinalPoint) {
		NodeList nListFinalPoint = doc.getElementsByTagName("finalstate");

		for (int i = 0; i < nListFinalPoint.getLength(); i++) {
			lstFinalPoint.add(Integer.parseInt(nListFinalPoint.item(i).getTextContent()));
		}
	}

	// load xml content to list edge
	private void LoadXMLToListEdge(Document doc, List<Edge> lstEdge) {
		NodeList nListEdges = doc.getElementsByTagName("edge");

		for (int i = 0; i < nListEdges.getLength(); i++) {
			NodeList nListEdgeElement = nListEdges.item(i).getChildNodes();

			// join point
			ArrayList<Shape> jointPoint = new ArrayList<Shape>();
			AddShapeToList(nListEdgeElement.item(4).getChildNodes(), jointPoint, true);

			lstEdge.add(new Edge(Integer.parseInt(nListEdgeElement.item(0).getTextContent()),
					Integer.parseInt(nListEdgeElement.item(1).getTextContent()),
					Boolean.valueOf(nListEdgeElement.item(2).getTextContent()),
					nListEdgeElement.item(3).getTextContent(), jointPoint));
		}
	}

	// load xml content to list shape
	private void LoadXMLToListShape(Document doc, List<Shape> lstShape, String tagName) {
		NodeList nListShapes = doc.getElementsByTagName(tagName);
		AddShapeToList(nListShapes, lstShape, false);
	}

	// add shape to list shape
	private void AddShapeToList(NodeList nodesShapes, List<Shape> lstShape, boolean hasAddJointPoint) {
		if (hasAddJointPoint) {
			if (nodesShapes.getLength() > 0) {
				String pointString = nodesShapes.item(1).getTextContent();
				Point2D point = new Point2D.Double(Double.parseDouble(pointString.split("-")[0]),
						Double.parseDouble(pointString.split("-")[1]));

				lstShape.add(new Shape(nodesShapes.item(0).getTextContent(), point,
						Integer.parseInt(nodesShapes.item(2).getTextContent()),
						Integer.parseInt(nodesShapes.item(3).getTextContent()),
						Integer.parseInt(nodesShapes.item(4).getTextContent()),
						Integer.parseInt(nodesShapes.item(5).getTextContent()),
						Boolean.valueOf(nodesShapes.item(6).getTextContent()),
						Boolean.valueOf(nodesShapes.item(7).getTextContent())));
			}
		} else {
			for (int i = 0; i < nodesShapes.getLength(); i++) {
				NodeList nListShapeElement = nodesShapes.item(i).getChildNodes();

				if (nListShapeElement.getLength() > 0) {
					String pointString = nListShapeElement.item(1).getTextContent();
					Point2D point = new Point2D.Double(Double.parseDouble(pointString.split("-")[0]),
							Double.parseDouble(pointString.split("-")[1]));

					lstShape.add(new Shape(nListShapeElement.item(0).getTextContent(), point,
							Integer.parseInt(nListShapeElement.item(2).getTextContent()),
							Integer.parseInt(nListShapeElement.item(3).getTextContent()),
							Integer.parseInt(nListShapeElement.item(4).getTextContent()),
							Integer.parseInt(nListShapeElement.item(5).getTextContent()),
							Boolean.valueOf(nListShapeElement.item(6).getTextContent()),
							Boolean.valueOf(nListShapeElement.item(7).getTextContent())));
				}
			}
		}
	}

	// load xml content to list state
	private void LoadXMLToListState(Document doc, List<State> lstState) {
		NodeList nListStates = doc.getElementsByTagName("state");

		for (int i = 0; i < nListStates.getLength(); i++) {
			NodeList nListStateElement = nListStates.item(i).getChildNodes();

			lstState.add(new State(Boolean.valueOf(nListStateElement.item(0).getTextContent()),
					Boolean.valueOf(nListStateElement.item(1).getTextContent())));
		}
	}

	// load xml content to list transition
	private ArrayList<Transition<String>> LoadTransitionsFromEdges(List<Edge> lstEdge,
			List<Transition<String>> lstTransition, List<State> lstState) {
		for (Edge edge : lstEdge) {
			lstTransition.add(new Transition<String>(lstState.get(edge.getSource()),
					lstState.get(edge.getDestination()), edge.getLabel()));
		}

		return (ArrayList<Transition<String>>) lstTransition;
	}

}
