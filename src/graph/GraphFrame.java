package graph;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import controller.IGraphController;
import graph.GraphComponent;
import graph.GraphFrame;

public class GraphFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private GraphComponent graphComponent = new GraphComponent(500);
	private IGraphController controller;

	public GraphFrame(IGraphController controller, String title) {
		super(title);
		this.controller = controller;

		// set resizable is false
		setResizable(false);
		setLayout(new BorderLayout());

		// Add scroll bar for component
		JScrollPane scrollPane = new JScrollPane(graphComponent);
		scrollPane.setPreferredSize(new Dimension(500, 500));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportView(graphComponent);
		add(scrollPane, BorderLayout.EAST);

		createMenu();
		createLeftBar();
	}

	// Create left tool bar for 5 buttons
	private void createLeftBar() {
		GridLayout layout = new GridLayout(5, 1);
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(layout);
		createLeftBarButton(leftPanel);
		add(leftPanel);
	}

	// Create left button for toolbar
	private void createLeftBarButton(JPanel panel) {
		JButton cirButton = new JButton("State");
		JButton endButton = new JButton("End");
		JButton pointButton = new JButton("Pointer");
		JButton startButton = new JButton("Start");
		JButton recognizedWord = new JButton("Words");

		pointButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graphComponent.freePoint();
			}
		});

		cirButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graphComponent.setCurrentButton("circle");
			}
		});

		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graphComponent.setInitialState();
			}
		});

		endButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graphComponent.setFinalState();
			}
		});

		recognizedWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String words = JOptionPane.showInputDialog(GraphFrame.this, "Enter Recognized word", "Word",
						JOptionPane.OK_OPTION | JOptionPane.INFORMATION_MESSAGE);
				if (words != "") {
					graphComponent.recognizeWords(words);
				}
			}
		});

		// Bind button to panel
		panel.add(pointButton);
		panel.add(cirButton);
		panel.add(startButton);
		panel.add(endButton);
		panel.add(recognizedWord);
	}

	// Create menu
	private void createMenu() {
		// Create main menu bar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// Create main menu file
		JMenu menu = new JMenu(Constants.MENU_FILE);
		menuBar.add(menu);

		// Create menu item create new
		createMenuItem(menu, Constants.MENU_ITEM_NEW, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.createFrame();
			}
		});

		// Create menu item open
		createMenuItem(menu, Constants.MENU_ITEM_OPEN, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.loadFrame(GraphFrame.this.graphComponent);
			}
		});

		// Create menu item save
		createMenuItem(menu, Constants.MENU_ITEM_SAVE, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.saveFrame(GraphFrame.this.graphComponent);
			}
		});
		
		
		// create menu change color
		createMenuItem(menu, Constants.MENU_ITEM_CHANGECOLOR, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				controller.changecolor(GraphFrame.this.graphComponent);
			}
		});

		// Create menu item close
		createMenuItem(menu, Constants.MENU_ITEM_CLOSE, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.deleteFrame(GraphFrame.this);
			}
		});

		// Bind event to button close window
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				controller.deleteFrame(GraphFrame.this);
			}
		});
	}

	// Create menu item
	private void createMenuItem(JMenu menu, String name, ActionListener action) {
		JMenuItem menuItem = new JMenuItem(name);
		menuItem.addActionListener(action);
		menu.add(menuItem);
	}
}
