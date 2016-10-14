import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

public class MyMouseAdapter extends MouseAdapter {
	private Random minePosition = new Random();
	private int clickedRow;
	private int clickedColumn;
	public ArrayList<Integer> hasMines = new ArrayList<Integer>(20);
	public Color newColor = null;
	public boolean hasAMine;

	public void mineGenerator() {  // Generates the mines
		for (int i =0; i < MyPanel.totalMines;i++){
			int rowValue = minePosition.nextInt(8)+1;
			int columnValue = minePosition.nextInt(8)+1;		
			hasMines.add(rowValue);
			hasMines.add(columnValue);
			for (int j = 0; j < hasMines.size(); j++) {
				if (hasMines.get(j) == 0) {
					hasMines.remove(j);
					hasMines.add(minePosition.nextInt(8) + 1);
				}
			}

			System.out.println("row = " + rowValue + " column = " + columnValue + " i = " + i);
		}
		System.out.println(hasMines);
	}

	public void isAMine() {   // Detects the mines
		for (int i = 0; i < hasMines.size(); i++) {
			int row = hasMines.get(i);
			i++;
			int column = hasMines.get(i);
			if ((clickedRow == row) && (clickedColumn == column)) {
				newColor = Color.BLACK;
				System.out.println("GameOver"); // Must be replaced with end event.
				//				newColor = Color.GRAY;
				//				System.out.println("Smething happened");
			}
			else {
				hasAMine = false;
			}
			//			else if ((clickedRow == row) && (clickedColumn == column)) {  // if the clicked square equals a mine position, end game

			//			Need to make code to mark as empty
			//			else if (clickedRow == row) || (clickedColumn == column)) {
			//				newColor = Color.GRAY;
			//				System.out.println("Lucky you.");
			//			}



		}
	}
	public void mineFlagger() {  // Defective
		for (int i = 0; i < hasMines.size(); i++) {
			int row = hasMines.get(i);
			i++;
			int column = hasMines.get(i);
			if ((clickedRow == row) && (clickedColumn == column)) {
				newColor = Color.RED;
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		Component c = e.getComponent();
		while (!(c instanceof JFrame)) {
			c = c.getParent();
			if (c == null) {
				return;
			}
		}
		JFrame myFrame = (JFrame) c;
		MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);
		Insets myInsets = myFrame.getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		e.translatePoint(-x1, -y1);
		int x = e.getX();
		int y = e.getY();
		myPanel.x = x;
		myPanel.y = y;
		myPanel.mouseDownGridX = myPanel.getGridX(x, y);
		myPanel.mouseDownGridY = myPanel.getGridY(x, y);
		myPanel.repaint();
		switch (e.getButton()) {
		case 1:		//Left mouse button
			newColor = null;

			break;
		case 3:		//Right mouse button
			newColor = Color.RED;
			//Do nothing
			break;
		default:    //Some other button (2 = Middle mouse button, etc.)
			//Do nothing
			break;
		}
	}
	public void mouseReleased(MouseEvent e) {
		Component c = e.getComponent();
		while (!(c instanceof JFrame)) {
			c = c.getParent();
			if (c == null) {
				return;
			}
		}
		JFrame myFrame = (JFrame)c;
		MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
		Insets myInsets = myFrame.getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		e.translatePoint(-x1, -y1);
		int x = e.getX();
		int y = e.getY();
		myPanel.x = x;
		myPanel.y = y;
		int gridX = myPanel.getGridX(x, y);
		int gridY = myPanel.getGridY(x, y);
		switch (e.getButton()) {
		case 1:		//Left mouse button
			newColor = Color.GRAY;  // Sets uncovered mine's color

			if ((myPanel.mouseDownGridX == -1) || (myPanel.mouseDownGridY == -1)) {
				//Had pressed outside

			} else {
				if ((gridX == -1) || (gridY == -1)) {
					//Is releasing outside
					//Do nothing
				} else {
					if ((myPanel.mouseDownGridX != gridX) || (myPanel.mouseDownGridY != gridY)) {
						//Released the mouse button on a different cell where it was pressed
						//Do nothing
					} else {
						//Released the mouse button on the same cell where it was pressed
						if ((gridX == 0) || (gridY == 0)) {
							//On the left column and on the top row... do nothing
							//
							//							Color newColor = null;
							//							switch (generator.nextInt(5)) {
							//							case 0:
							//								newColor = Color.YELLOW;
							//								break;
							//							case 1:
							//								newColor = Color.MAGENTA;
							//								break;
							//							case 2:
							//								newColor = Color.BLACK;
							//								break;
							//							case 3:
							//								newColor = new Color(0x964B00);   //Brown (from http://simple.wikipedia.org/wiki/List_of_colors)
							//								break;
							//							case 4:
							//								newColor = new Color(0xB57EDC);   //Lavender (from http://simple.wikipedia.org/wiki/List_of_colors)
							//								break;
							//							}
							//							myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = newColor;
							//							myPanel.repaint();
							//			
							//							
						} 
						else {
							//On the grid other than on the left column and on the top row:

							clickedRow = myPanel.mouseDownGridX;
							clickedColumn = myPanel.mouseDownGridY;
							isAMine();

							//							switch (generator.nextInt(5)) {
							//							case 0:
							//								newColor = Color.YELLOW;
							//								break;
							//							case 1:
							//								newColor = Color.MAGENTA;
							//								break;
							//							case 2:
							//								newColor = Color.BLACK;
							//								break;
							//							case 3:
							//								newColor = new Color(0x964B00);   //Brown (from http://simple.wikipedia.org/wiki/List_of_colors)
							//								break;
							//							case 4:
							//								newColor = new Color(0xB57EDC);   //Lavender (from http://simple.wikipedia.org/wiki/List_of_colors)
							//								break;
							/*
							 * Switch new Color to new Color (r, g, b)
							 */
							//							}
							myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = newColor;
							myPanel.repaint();
						}
					}
				}
			}
			myPanel.repaint();
			break;
		case 3:		//Right mouse button
			if ((myPanel.mouseDownGridX == -1) || (myPanel.mouseDownGridY == -1)) {
				//Had pressed outside

			} else {
				if ((gridX == -1) || (gridY == -1)) {
					//Is releasing outside
					//Do nothing
				} else {
					if ((myPanel.mouseDownGridX != gridX) || (myPanel.mouseDownGridY != gridY)) {
						//Released the mouse button on a different cell where it was pressed
						//Do nothing
					} else {
						//Released the mouse button on the same cell where it was pressed
						if ((gridX == 0) || (gridY == 0)) {
							//On the left column and on the top row... do nothing
						} 
						else {
							//On the grid other than on the left column and on the top row:
							clickedRow = myPanel.mouseDownGridX;
							clickedColumn = myPanel.mouseDownGridY;
							mineFlagger();
							myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = newColor;
							myPanel.repaint();
						}
					}
				}
			}
			myPanel.repaint();
			break;
			//Do nothing
		
		}
	}
}
