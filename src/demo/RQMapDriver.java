package demo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.JApplet;
import javax.swing.JFrame;

import edu.cs.purdue.edu.helpers.LocationUpdate;

public class RQMapDriver extends JApplet implements Runnable {

	//static ServerSocket variable
	private static ServerSocket server;
	Socket socket;
	ObjectInputStream ois;


	Thread runner = null;

	final static int WIDTH  = 1900;
	final static int HEIGHT = 1200;

	Image    image;
	Graphics graphics;
	BerlinMap berlinMap; 

	int processedUpdates = 0;


	public void init() {
		berlinMap = new BerlinMap();
		try {
			server = new ServerSocket(DemoConstants.portNumber);
			System.out.println("Waiting for client request");
			//creating socket and waiting for client connection			
			socket = server.accept();

			// read from socket to ObjectInputStream object
			ois = new ObjectInputStream(socket.getInputStream());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//		DataReader reader = new DataReader();
		//		data = reader.readPoints(berlinMap);
	}

	public void start() {
		if ( runner == null ) {
			runner = new Thread( this );
			runner.start();
		}
	}

	public void stop() {
		if ( runner != null && runner.isAlive() )
			runner.stop();

		runner = null;
	}

	public void run() {
		while (runner != null) {
			repaint();
			//			try {
			//				Thread.sleep( 0 );
			//			} catch ( InterruptedException e ) {
			//				// do nothing
			//			}
		}
	}

	public void paint( Graphics g ) {
		update(g);
	}

	public void update(Graphics g) {
		image = createImage(WIDTH, HEIGHT );
		graphics = image.getGraphics();

		// clear the background to white
		graphics.setColor(Color.white );
		graphics.fillRect(0, 0, WIDTH, HEIGHT );

		// draw the lines
		graphics.setColor(Color.black);

		for (BerlinMap.Street street : berlinMap.streets) {
			graphics.drawLine(street.x1, street.y1, street.x2, street.y2);
		}

		graphics.setColor(Color.blue);
		addQuery(10000, 10000, 18000, 18000, graphics);
		addQuery(-2000, 3000, 4000, 10000, graphics);


		ArrayList<LocationUpdate> inside = new ArrayList<LocationUpdate>();
		ArrayList<LocationUpdate> outside = new ArrayList<LocationUpdate>();

		try {
			for (int i = 0; i < 500; i++) {
				String message = (String) ois.readObject();
				String[] parts = message.split(",");
				double xCoord = Double.parseDouble(parts[1]);
				double yCoord = Double.parseDouble(parts[2]);
				LocationUpdate l = new LocationUpdate(0, xCoord, yCoord);
				if (message.charAt(0) == '-') {
					outside.add(l);
				} else {
					inside.add(l);
				}
				//System.out.println("Message Received: " + message);	
				//System.out.println(processedUpdates++);
			}

			BerlinMap.scalePoints(inside);
			BerlinMap.scalePoints(outside);

			graphics.setColor(Color.red);
			for (LocationUpdate l : inside) {
				graphics.fillOval((int)l.getNewLocationXCoord(), (int)l.getNewLocationYCoord(), 7, 7);
			}

			graphics.setColor(Color.black);
			for (LocationUpdate l : outside) {
				graphics.fillOval((int)l.getNewLocationXCoord(), (int)l.getNewLocationYCoord(), 7, 7);
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//		if (data != null)
		//		{	
		//
		//			int max = processedUpdates + 150;
		//			for (; processedUpdates < data.size() && processedUpdates< max; processedUpdates++) {
		//				graphics.fillOval((int)data.get(processedUpdates).xCoord, (int)data.get(processedUpdates).yCoord, 7, 7);
		//			}
		//		}

		g.drawImage( image, 0, 0, this );
	}

	public static void main(String[] args) {
		// Create an instance of the applet class.
		JApplet applet = new RQMapDriver();

		// Send the applet an init() message.
		applet.init();
		applet.setVisible(true);

		final JFrame frame = new JFrame("(ST)^2 orm");

		frame.setSize(1900, 1200);
		applet.start();

		frame.setLayout(new BorderLayout());
		// include it as a component.  local testing can now start
		frame.getContentPane().add(applet, BorderLayout.CENTER);
		frame.setVisible(true);
	}

	private static void addQuery(int minX, int minY, int maxX, int maxY, Graphics graphics) {
		ArrayList<LocationUpdate> queryCoords = new ArrayList<LocationUpdate>();
		queryCoords.add(new LocationUpdate(0, minX, minY));
		queryCoords.add(new LocationUpdate(0, maxX, maxY));
		BerlinMap.scalePoints(queryCoords);

		graphics.drawLine((int)queryCoords.get(0).getNewLocationXCoord(), (int)queryCoords.get(0).getNewLocationYCoord(),
				(int)queryCoords.get(1).getNewLocationXCoord(), (int)queryCoords.get(0).getNewLocationYCoord());
		graphics.drawLine((int)queryCoords.get(0).getNewLocationXCoord(), (int)queryCoords.get(0).getNewLocationYCoord(),
				(int)queryCoords.get(0).getNewLocationXCoord(), (int)queryCoords.get(1).getNewLocationYCoord());
		graphics.drawLine((int)queryCoords.get(0).getNewLocationXCoord(), (int)queryCoords.get(1).getNewLocationYCoord(),
				(int)queryCoords.get(1).getNewLocationXCoord(), (int)queryCoords.get(1).getNewLocationYCoord());
		graphics.drawLine((int)queryCoords.get(1).getNewLocationXCoord(), (int)queryCoords.get(0).getNewLocationYCoord(),
				(int)queryCoords.get(1).getNewLocationXCoord(), (int)queryCoords.get(1).getNewLocationYCoord());
	}
	
}