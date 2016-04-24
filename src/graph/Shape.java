package graph;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Shape {
	private String type;
	private Point2D point;
	private int radius;
	private int dx, dy;

	public int getDx() {
		return dx;
	}

	public int getDy() {
		return dy;
	}

	private boolean haveLine;
	private boolean movable;
	private int lineIndex;

	public Shape(String type, Point2D point, int radius) {
		this.type = type;
		this.point = point;
		this.radius = radius;

		// dx = 0 and dy 0 get center of shape
		this.dx = 0;
		this.dy = 0;
		this.lineIndex = 0;
		haveLine = false;
		movable = false;
	}

	public Shape(String type, Point2D point, int radius, int dx, int dy, int lineIndex, boolean haveLine,
			boolean movable) {
		this.type = type;
		this.point = point;
		this.radius = radius;
		this.dx = dx;
		this.dy = dy;
		this.haveLine = haveLine;
		this.lineIndex = lineIndex;
		this.movable = movable;
	}

	// Gets line index
	public int getLineIndex() {
		return lineIndex;
	}

	// Sets line index
	public void setLineIndex(int lineIndex) {
		this.lineIndex = lineIndex;
	}

	// Sets move for shape
	public void setMovable(boolean movable) {
		this.movable = movable;
	}

	// Set line for shape
	public void setHaveLine(boolean haveLine) {
		this.haveLine = haveLine;
	}

	// Check shape move
	public boolean isMovable() {
		return movable;
	}

	// Check shape have line
	public boolean isHaveLine() {
		return haveLine;
	}

	// Sets dx and dy
	public void setDxy(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}

	// Gets point click
	public Point2D getPointClick() {
		return new Point2D.Double(point.getX() + dx, point.getY() + dy);
	}

	// Sets radius
	public void setRadius(int radius) {
		this.radius = radius;
	}

	// Gets radius
	public int getRadius() {
		return radius;
	}

	// Gets type
	public String getType() {
		return type;
	}

	// Sets type
	public void setType(String type) {
		this.type = type;
	}

	// Gets point
	public Point2D getPoint() {
		return point;
	}

	// Sets point
	public void setPoint(Point2D point) {
		this.point = point;
	}

	// Add shape
	public void add(Shape newShape) {
		this.type = newShape.getType();
		this.point = newShape.getPoint();
	}

	// Gets shape bound
	public Object getBounds() {
		return new Ellipse2D.Double(point.getX() - radius, point.getY() - radius, radius * 2, radius * 2);
	}

	// Check point select is circle
	public Ellipse2D isCirclePointSelected() {
		return new Ellipse2D.Double(point.getX() + dx - 5, point.getY() + dy - 5, 10, 10);
	}

}
