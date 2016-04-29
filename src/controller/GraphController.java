package controller;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import graph.Constants;
import graph.GraphComponent;
import graph.GraphFrame;

public class GraphController implements IGraphController {
	private static final List<JFrame> frames = new ArrayList<JFrame>();
	private static final IXMLController xmlController = new XMLController();
	private static final FileNameExtensionFilter filterFile = new FileNameExtensionFilter(".xml", "xml");

	// Create new frame
	@Override
	public JFrame createFrame() {
		GraphFrame frame = new GraphFrame(this, Constants.TITLE);
		int pos = 30 * (frames.size() % 5);
		frame.setLocation(pos, pos);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		frames.add(frame);
		return frame;
	}

	// Delete current frame
	@Override
	public void deleteFrame(JFrame frame) {
		if (frames.size() > 1) {
			frames.remove(frame);
			frame.dispose();
		} else {
			quit();
		}
	}

	// Save frame to file xml
	@Override
	public void saveFrame(GraphComponent com) {
		// Show pop up to save
		JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
		fileChooser.setDialogTitle(Constants.DIALOG_SAVE_TITLE);
		fileChooser.setFileFilter(filterFile);

		if (fileChooser.showSaveDialog(com) == JFileChooser.APPROVE_OPTION) {

			try {
				File saveFile = fileChooser.getSelectedFile();

				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				Document doc = xmlController.createXMLContent(com);

				if (doc == null) {
					JOptionPane.showMessageDialog(com, "Cannot save file");
					return;
				}

				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(saveFile + ".xml");
				transformer.transform(source, result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// Load automata from file txt
	@Override
	public void loadFrame(GraphComponent com) {
		// Load file
		JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
		fileChooser.setDialogTitle(Constants.DIALOG_OPEN_TITLE);
		fileChooser.setFileFilter(filterFile);

		if (fileChooser.showOpenDialog(com) == JFileChooser.OPEN_DIALOG) {
			if (fileChooser.getSelectedFile().getName().endsWith(".xml")) {
				xmlController.loadXMLFile(com, fileChooser.getSelectedFile());

				// Redraw
				com.repaint();
			} else {
				JOptionPane.showMessageDialog(null, "File not correct", "Error", 0);
			}
		}
	}

	// Run program
	@Override
	public void run() {
		createFrame();
	}

	// Exit program
	private void quit() {
		int answer = JOptionPane.showConfirmDialog(null, Constants.DIALOG_QUIT_MSG, Constants.DIALOG_QUIT_TITLE,
				JOptionPane.YES_NO_OPTION);
		if (answer == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	// Change color
	@Override
	public void changecolor(GraphComponent com) {

		String color = (String) JOptionPane.showInputDialog(null, Constants.DIALOG_CHANGECOLOR_TITLE,
				Constants.DIALOG_CHANGECOLOR_TITLE, JOptionPane.QUESTION_MESSAGE, null, Constants.choices,
				Constants.choices[1]);

		switch (color) {
		case Constants.ColorOrange:
			com.setColorState(Color.orange);
			break;

		case Constants.ColorYellow:
			com.setColorState(Color.yellow);
			break;

		case Constants.ColorGreen:
			com.setColorState(Color.green);
			break;

		case Constants.ColorPink:
			com.setColorState(Color.pink);
			break;

		case Constants.ColorMagenta:
			com.setColorState(Color.magenta);
			break;

		default:
			com.setColorState(Color.blue);
			break;
		}

		com.repaint();
	}
}