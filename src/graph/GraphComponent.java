package graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.event.MouseInputListener;

import core.State;
import core.Transition;
import machine.ChangeTransition;
import machine.ObservableAutomaton;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class GraphComponent extends JComponent implements MouseInputListener,KeyListener{	
	private static final long serialVersionUID = 1L;
	
	private List<Edge> listLine;
	private List<Integer> finalState;
	private List<Shape> listPoints;
	
	private ArrayList<Shape> tempEdge;
	private ArrayList<State> states;
	private ArrayList<Transition<String>> transitions;
	
	private int dx;
	private int dy;
	private int initialState;
	
	//index of circle have been clicked or dragged
	private int currently; 
	private int currentJoinPoint; 
	private int currentLine; 
	private int currentlyMove; 
	private boolean altPressed;
	private boolean movableLinePoint;
	private boolean movableJoinPoint;
	private boolean spacePressed;

	private Point2D desPoint;
	private Point2D sourcePoint;
	
	//current button to draw state
	private String currentButton = ""; 
		
	public GraphComponent(int size){
		setPreferredSize(new Dimension(size,size));		
		setFocusable(true);
		
		// Trigger mouser listener
		addMouseListener(this);
		
		// Trigger event move mouse
		addMouseMotionListener(this);
		
		// Trigger key listemer
		addKeyListener(this);
		
		// Init variables
		altPressed = false;
		currently = -1;
		currentButton = "";
		currentLine = -1; 
		currentlyMove = -1; 
		currentJoinPoint = -1;
		desPoint = new Point2D.Float();
		dx = 0;
		dy = 0;
		finalState = new ArrayList<Integer>();
		initialState = 0;
		listLine = new ArrayList<Edge>();		
		listPoints = new ArrayList<Shape>();		
		movableLinePoint = false;
		movableJoinPoint = false;
		spacePressed = false;
		states = new ArrayList<State>();
		tempEdge = new ArrayList<Shape>();
		transitions = new ArrayList<Transition<String>>();	
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);	
		pain(g);			
	};
	
	// Paint frame
	protected void pain(Graphics g){
		Font font = new Font("Serif", Font.BOLD, 20);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setFont(font);
		FontMetrics fm = g2d.getFontMetrics(font);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		// draw line
		Iterator<Edge> iteLine = listLine.iterator();
		while (iteLine.hasNext()) {
			Edge linePoint = iteLine.next();
			Point2D source = listPoints.get(linePoint.getSource()).getPointClick();
			Point2D des = listPoints.get(linePoint.getDestination()).getPointClick();
			String label = linePoint.getLabel();

			if (linePoint.getJoinPoint().size() > 0) {
				int index = linePoint.pointToRaw();

				Point2D desPoint;
				if (linePoint.getJoinPoint().size() == 1)
					desPoint = des;
				else
					desPoint = linePoint.getEdge(index + 1).getPoint();

				createArrowLabel(g2d, label, linePoint.getEdge(index).getPoint(), desPoint);
				g2d.setColor(Color.YELLOW);
				g2d.fill(createArrowShape(linePoint.getEdge(index).getPoint(), desPoint));
			} else {
				createArrowLabel(g2d, label, source, des);
				g2d.setColor(Color.YELLOW);
				g2d.fill(createArrowShape(source, des));
			}

			drawEdge(source, des, linePoint.getJoinPoint(), g2d);
		}
		
		//draw shape 
		Iterator<Shape> listIterator = listPoints.iterator();
		int labelCount = 0;
		while (listIterator.hasNext()) {
			String labelNumber = Integer.toString(labelCount);
			Shape p = listIterator.next();
			int internalRadius = p.getRadius();
			int height = internalRadius*2, width = internalRadius*2;
			
			g2d.setColor(Color.BLUE);
			if(labelCount == initialState)
				g2d.setColor(Color.RED);
			if(finalState.contains(labelCount))
				g2d.setColor(Color.BLACK);
			
			Ellipse2D ellipse = new Ellipse2D.Double((int) p.getPoint().getX() - internalRadius,
					(int) p.getPoint().getY() - internalRadius, width, height);
			g2d.fill(ellipse);
			 
			
            int widthLable = fm.stringWidth(labelNumber);
            
            //center String/text
            int cx = ((int)p.getPoint().getX() - widthLable/ 2) ;
            int cy = ((int)p.getPoint().getY() + fm.getHeight()/4);
            g2d.setColor(Color.white);
			g2d.drawString(labelNumber, cx, cy);
			labelCount++;
		}

		//draw line while mouse move
		if(currently > -1){
			if((altPressed)){
				drawEdge(sourcePoint, desPoint, tempEdge,g2d);
			}
		}
	}
	
	// Draw edge
	private void drawEdge(Point2D source, Point2D des, List<Shape> listEdge,Graphics2D g2d){
		Point2D nextPoint = source;
		if(listEdge.size() > 0){
			Iterator<Shape> edge = listEdge.iterator();
			while(edge.hasNext()){
				Point2D point = edge.next().getPoint();
				drawLine(nextPoint, point , g2d);
				nextPoint = point;
			}	
		}
		drawLine(nextPoint, des , g2d);
	}
	
	// draw line
	private void drawLine(Point2D source, Point2D des, Graphics2D g2d){
		Line2D line = new Line2D.Float(source, des);
		g2d.setColor(Color.GREEN);
		g2d.draw(line);
		g2d.setColor(Color.RED);
		Ellipse2D cirSource = new Ellipse2D.Double(source.getX() - 5, source.getY() - 5, 10 ,10);
		g2d.fill(cirSource);
		Ellipse2D cirDes = new Ellipse2D.Double(des.getX() - 5, des.getY() - 5, 10 ,10);
		g2d.fill(cirDes);
	}
		

	public boolean checkPointIsCircle(MouseEvent e){
		
		Iterator<Shape> listIterator = listPoints.iterator();
		int index=0;
		while(listIterator.hasNext()){
			
			Shape p = listIterator.next();
			boolean founded = false;

			if (((Ellipse2D) p.getBounds()).contains(e.getPoint())) {
				currently = index;
				founded = true;
			}
			
			if(founded){
				if(p.isCirclePointSelected().contains(e.getPoint()))
					movableLinePoint = true;
				return true;
			}
			index++;
		}
		return false;
	}
	
	public boolean checkPointIsCircle(Point2D point) {

		Iterator<Shape> listIterator = listPoints.iterator();
		int index = 0;
		while (listIterator.hasNext()) {
			Shape p = listIterator.next();

			if (((Ellipse2D) p.getBounds()).contains(point)) {
				currentlyMove = index;
				return true;
			}
			
			index++;
		}
		return false;
	}
	
	public boolean findPointInLine(MouseEvent e){
		
		Iterator<Edge> edges = listLine.iterator();
		int index=0;
		while(edges.hasNext()){
			Edge edge = edges.next();
			if(isJoinPoint(edge.getJoinPoint(),e.getPoint())){
				currentLine = index;
				return true;
			}
			index++;
		}
		return false;
	}
	
	private boolean isJoinPoint(ArrayList<Shape> joinPoits, Point2D point){
		Iterator<Shape> jpIter = joinPoits.iterator();
		int index = 0;
		while(jpIter.hasNext()){
			Shape sh = jpIter.next();
			if(((Ellipse2D) sh.getBounds()).contains(point)){
				currentJoinPoint = index;
				return true;
			}
			index++;
		}
		return false;
	}
	
	private void drawShape(int x, int y) {
		addShape(x, y);
		boolean st = false;
		boolean et = false;
		if(currently == initialState)
			st = true;
		states.add(new State(st, et));
	}
	
	private void addShape(int x, int y){
		Shape newShape;
		if (currentButton == Constants.CircleType) {
			newShape = new Shape(Constants.CircleType, new Point(x, y), Constants.Radius);
			listPoints.add(newShape);
			currently = listPoints.size() - 1;
		} 
	}
	
	public void setCurrentButton(String currentButton){
		this.currentButton = currentButton;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3){
			if(findPointInLine(e)){
				listLine.get(currentLine).removeEdge(currentJoinPoint);
				repaint();
			}else if(checkPointIsCircle(e)){
				
				if(currentLine > -1){
					listPoints.get(currentLine).setHaveLine(false);
					listPoints.get(currentLine).setHaveLine(false);
					listLine.remove(currentLine);
					repaint();
				}else{
					listLine.contains(listPoints.get(currently).getLineIndex());
					listPoints.remove(currently);
					repaint();
				}
			}
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		requestFocusInWindow();
		currently = -1;
		
		if(e.getButton() == MouseEvent.BUTTON1){
			if(findPointInLine(e)){
				movableJoinPoint = true;
				dx = e.getX() - (int)listLine.get(currentLine).getEdge(currentJoinPoint).getPoint().getX();
				dy = e.getY() - (int)listLine.get(currentLine).getEdge(currentJoinPoint).getPoint().getY();
			}else if(checkPointIsCircle(e)){
				dx = e.getX() - (int)listPoints.get(currently).getPoint().getX();
				dy = e.getY() - (int)listPoints.get(currently).getPoint().getY();
				sourcePoint = e.getPoint();
			}else{
					int x = e.getX();
					int y = e.getY();
					drawShape(x, y);
					repaint();
				}
			}
	}

	@Override
	public void mouseReleased(MouseEvent e) {		
		if((altPressed || spacePressed) && currently > -1){
			listPoints.get(currently).setDxy(dx, dy);
			
			if(checkPointIsCircle(desPoint)){
				int mdx = (int) (desPoint.getX() - (int)listPoints.get(currentlyMove).getPoint().getX());
				int mdy = (int) (desPoint.getY() - (int)listPoints.get(currentlyMove).getPoint().getY());
				listPoints.get(currentlyMove).setDxy(mdx, mdy);
			}else{
				int x = e.getX();
				int y = e.getY();
				
				addShape(x, y);
				
				currentlyMove = listPoints.size() - 1;
				listPoints.get(currentlyMove).setDxy(0 , 0);
			}
			
			//add label for line 
			String label = JOptionPane.showInputDialog(this,
					"Enter Label",
					"Enter Label", JOptionPane.OK_OPTION|JOptionPane.INFORMATION_MESSAGE);
			
			if(label != null && label != ""){

				listLine.add(new Edge(currently, currentlyMove, tempEdge, label));
				transitions.add(new Transition<String>(states.get(currently), states.get(currentlyMove), label));
				listPoints.get(currently).setHaveLine(true);
				listPoints.get(currently).setMovable(spacePressed);
				listPoints.get(currently).setLineIndex(currentLine);
				listPoints.get(currentlyMove).setHaveLine(true);
				listPoints.get(currentlyMove).setMovable(spacePressed);
			}
			repaint();
		}
		
		currentlyMove = -1;
		currentLine = -1;
		movableLinePoint = false;
		movableJoinPoint = false;
		altPressed = false;
		tempEdge.clear();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
		int x = e.getX();
		int y = e.getY();

		if (currently > -1) {
			if (altPressed) {
				desPoint = e.getPoint();

			} else {

				if (movableLinePoint) {
					dx = e.getX() - (int) listPoints.get(currently).getPoint().getX();
					dy = e.getY() - (int) listPoints.get(currently).getPoint().getY();
					listPoints.get(currently).setDxy(dx, dy);
				} else
					listPoints.get(currently).getPoint().setLocation(x - dx, y - dy);
			}
		}
		if (movableJoinPoint) {
			listLine.get(currentLine).getEdge(currentJoinPoint).getPoint().setLocation(x - dx, y - dy);
		}
		
		repaint();
	}
	
	public static java.awt.Shape createArrowShape(Point2D fromPt, Point2D toPt) {
	    Polygon arrowPolygon = new Polygon();
	    //arrowPolygon.addPoint(-6,1);
	    arrowPolygon.addPoint(3,1);
	    arrowPolygon.addPoint(3,3);
	    arrowPolygon.addPoint(6,0);
	    arrowPolygon.addPoint(3,-3);
	    arrowPolygon.addPoint(3,-1);
	    //arrowPolygon.addPoint(-6,-1);


	    Point2D midPoint = midpoint(fromPt, toPt);

	    double rotate = Math.atan2(toPt.getY() - fromPt.getY(), toPt.getX() - fromPt.getX());

	    AffineTransform transform = new AffineTransform();
	    transform.translate(midPoint.getX(), midPoint.getY());
	    double scale = 3; // 12 because it's the length of the arrow polygon.
	    transform.scale(scale, scale);
	    transform.rotate(rotate);

	    return transform.createTransformedShape(arrowPolygon);
	}
	
	public static void createArrowLabel(Graphics2D g2d, String label,Point2D fromPt, Point2D toPt) {
		AffineTransform oldXForm = g2d.getTransform();
		
	    Point2D midPoint = midpoint(fromPt, toPt);

	    double rotate = Math.atan2(toPt.getY() - fromPt.getY(), toPt.getX() - fromPt.getX());

	    AffineTransform transform = new AffineTransform();
	    transform.translate(midPoint.getX(), midPoint.getY());
	    transform.rotate(rotate);
	    g2d.setColor(Color.BLUE);
	    g2d.setTransform(transform);
	    g2d.drawString(label , -10 , -10);
	    g2d.setTransform(oldXForm);
	    
	}
	
	private static Point2D midpoint(Point2D fromPt, Point2D toPt){
		return new Point2D.Double(fromPt.getX() + (toPt.getX() - fromPt.getX())/2, fromPt.getY() + (toPt.getY() - fromPt.getY())/2);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.isAltDown()){
			altPressed = true;
			if(e.getKeyCode() == KeyEvent.VK_SPACE)
				tempEdge.add(new Shape("circle", desPoint, 5));
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(!e.isAltDown()){
			altPressed = false;
		}
		
	}
	
	public void setFinalState(){
		if(!finalState.contains(currently)){
			finalState.add(currently);
			states.get(currently).setTerminal(true);
			repaint();
		}
	}
	
	public void setFinalState(int state){
		finalState.add(state);
	}
	
	public void setInitialState(){
		if(currently > -1 && (initialState != currently)){
			states.get(initialState).setInitial(false);
			initialState = currently;
			states.get(currently).setTerminal(true);
			repaint();
		}
	}
	
	public void setInitialState(String state){
		initialState = Integer.parseInt(state);
	}
	
	public void freePoint(){
		currently = - 1;
		currentButton = "";
	}
	
	public void recognizeWords(String words){
		String[] word = words.split("");
		ChangeTransition ct = new ChangeTransition();
		
		try {
			ObservableAutomaton<String> obs = new ObservableAutomaton<String>(transitions);
			obs.addObserver(ct);
			if(obs.recognize(word))
				JOptionPane.showMessageDialog(null, "This words was recognized!");
			else
				JOptionPane.showMessageDialog(null, "This words was not recognized!");				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	public void move(Graphics g){}
	public void keyTyped(KeyEvent arg0) {}
	
	public List<Shape> getListPoints() {
		return this.listPoints;
	}
	
	public List<Edge> getListLine() {
		return this.listLine;
	}
	
	public int getInitialState() {
		return this.initialState;
	}
	
	public List<Integer> getFinalState() {
		return this.finalState;
	}
	
	public ArrayList<State> getArrayStates() {
		return this.states;
	}
	
	public ArrayList<Transition<String>> getArrayTransitions() {
		return this.transitions;
	}
}
