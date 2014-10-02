package demo;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

import edu.cs.purdue.edu.helpers.LocationUpdate;

public class BerlinMap {

	public class Street {
		public int x1;
		public int y1;
		public int x2;
		public int y2;
		
		public Street(int x1, int y1, int x2, int y2) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}
	}
	
	public ArrayList<Street> streets;
	
	public static int minX, minY;
	public static int scale = 33;
	public static int offset = 6000;

	// Loads the map
	public BerlinMap() {		
		minX = 0; minY = 0;
		streets = new ArrayList<BerlinMap.Street>();
		try{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream("berlin-streets.txt");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null)   {
			     StringTokenizer st = new StringTokenizer(strLine);
			     int xStart = Integer.parseInt(st.nextToken());
			     int yStart = Integer.parseInt(st.nextToken());
			     int xEnd = Integer.parseInt(st.nextToken());
			     int yEnd = Integer.parseInt(st.nextToken());
			     streets.add(new Street(xStart, yStart, xEnd, yEnd));
			     
			     if (minX > xStart) {
			    	 minX = xStart;
			     }
			     if (minX > xEnd) {
			    	 minX = xStart;
			     }			     
			     if (minY > yStart) {
			    	 minY = xStart;
			     }			     
			     if (minY > yEnd) {
			    	 minY = xStart;
			     }
			}
			//Close the input stream
			in.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		
		scaleMap();
	}
	
	private void scaleMap() {
		minX = -minX + offset;
		minY = -minY;
		
		for (Street street : streets) {
			street.x1 += minX;
			street.x1 /= scale;
			
			street.x2 += minX;
			street.x2 /= scale;
			
			street.y1 += minY;
			street.y1 /= scale;
			
			street.y2 += minY;
			street.y2 /= scale;
		}
	}

	public static void scalePoints(ArrayList<LocationUpdate> locations) {
		for (LocationUpdate loc : locations) {
			loc.setNewLocationXCoord((loc.getNewLocationXCoord() + minX) / scale);
			
			loc.setNewLocationYCoord((loc.getNewLocationYCoord() + minY) / scale);			
		}
	}
}


