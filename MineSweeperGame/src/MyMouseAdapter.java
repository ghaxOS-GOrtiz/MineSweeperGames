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
	private int row;
	private int column;
	private boolean isFlagged;
	private ArrayList<Integer> flagLocations = new ArrayList<Integer>(22);
	private int flagCounter = 0;

	public  ArrayList<Boolean> usedFlags = new ArrayList<Boolean>();
	public ArrayList<Integer> hasMines = new ArrayList<Integer>(20);
	public Color newColor = null;
	public boolean hasAMine;


	public void mineGenerator() {  // Generates the mines
		flagLocations.add(0);
		flagLocations.add(0);
		for (int i =0; i < MyPanel.totalMines;i++){
			int rowValue = minePosition.nextInt(8)+1;
			int columnValue = minePosition.nextInt(8)+1;		
			hasMines.add(rowValue);
			hasMines.add(columnValue);
			usedFlags.add(false);
			for (int j = 0; j < hasMines.size(); j++) {
				if (hasMines.get(j) == 0) {
					hasMines.remove(j);
					hasMines.add(minePosition.nextInt(8) + 1);
				}
			}
			System.out.println("row = " + rowValue + " column = " + columnValue );
			System.out.println(usedFlags);
		}

	}

	public void isAMine() {   // Detects the mines
		for (int i = 0; i < hasMines.size(); i++) {
			row = hasMines.get(i);
			i++;
			column = hasMines.get(i);
			if ((clickedRow == row) && (clickedColumn == column)) {
				newColor = Color.BLACK	;
				System.out.println("GameOver");
				hasAMine = true;
			}
			else {
				hasAMine = false;
			}
		}
	}
	public void mineFlagger() {  
		for (int i = 0; i <10; i++) {
			row = i + 1;
			for (int j = 0; j < 10; j++) {
				column = j + 1;
				for (int f1 = 0; f1 < flagLocations.size(); f1++) {


					if ((clickedRow == row) && (clickedColumn == column))  { 
						flagLocations.add(clickedRow);
						flagLocations.add(clickedColumn);
						newColor = Color.RED;
						usedFlags.remove(f1);
						usedFlags.add(f1, true);
						flagCounter++;

						System.out.println(flagLocations);
						System.out.println(usedFlags);
						//						return;
					}

					else if ((clickedRow == row) && (clickedColumn == column)) {  // Read array list, then remove said element from list
						flagLocations.remove(f1 - 1);
						flagLocations.remove(f1 - 1);
						usedFlags.remove(f1);
						usedFlags.add(f1, false);
						newColor = Color.WHITE;
					}
				}
			}
			System.out.println(flagLocations);
		}


		System.out.println(isFlagged);
	}
	public int flagUp() {
		for (int i = 0; i < 10; i++){
			if (usedFlags.get(i) == true) {
				flagCounter++;
			}
		}
		return flagCounter;
	}
	public int flagDown() {
		for (int i = 0; i < 10; i++) {
			if (usedFlags.get(i) == false) {
				flagCounter--;
			}
		}
		return flagCounter;
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
						} 
						else {
							//On the grid other than on the left column and on the top row:

							clickedRow = myPanel.mouseDownGridX;
							clickedColumn = myPanel.mouseDownGridY;
							isAMine();
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
