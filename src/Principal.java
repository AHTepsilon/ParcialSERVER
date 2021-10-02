import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.Gson;

import model.Particles;
import processing.core.PApplet;

public class Principal extends PApplet
{

	public static void main(String[] args) 
	{
		PApplet.main("Principal");
	}
	
	@Override
	public void settings() //void Awake
	{
		size(800, 500);
	}
	
	BufferedWriter writer;
	BufferedReader reader;
	
	private Socket socket;
	
	private ArrayList<Particles> particleArrList;
	
	int r, g, b;
	int x, y;
	String name;
	int amount;
	
	int randomNum;
	
	@Override
	public void setup() //void Start
	{
		particleArrList = new ArrayList<Particles>();
		
		initServer();
	}
	
	@Override
	public void draw() //void Update
	{		
		background(255);
		
		for(int i = 0; i < particleArrList.size(); i++)
		{
			fill(r, g, b);
			strokeWeight(4);
			circle(x, y, 50);
			
			move();
		}

	}
	
	public void initServer()
	{
		new Thread(
				() -> 
				{
					try {
						ServerSocket server = new ServerSocket(4000);
						System.out.println("Awaiting Connection...");
						socket = server.accept();
						System.out.println("Client Connected");
						
						InputStream is = socket.getInputStream();
						InputStreamReader isr = new InputStreamReader(is);
						reader = new BufferedReader(isr);
						
						OutputStream os = socket.getOutputStream();
						OutputStreamWriter osw = new OutputStreamWriter(os);
						writer = new BufferedWriter(osw);
						
						while(true)
						{
							System.out.println("Awaiting message...");
							String line = reader.readLine();
							System.out.println("Received message: " + line);
							
							Gson gson = new Gson();
							Particles particle = gson.fromJson(line, Particles.class);
							
							x = particle.getX();
							y = particle.getY();
							
							r = particle.getR();
							g = particle.getB();
							b = particle.getG();
							
							name = particle.getName();
							
							amount = particle.getAmount();
							
							System.out.println(x + " " + y + " " + name + " " + amount + " " + r + ", " + g + ", " + b);
							
							for(int i = 0; i < amount; i++)
							{
								particleArrList.add(new Particles(x, y, name, amount, r, g, b));
								
								randomNum = (int) random(0, 4);
							}
								
						}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}).start();
	}
	
	public void move()
	{
		switch(randomNum)
		{
		case 0:
			x++;
			break;
		case 1:
			x--;
			break;
		case 2:
			y++;
			break;
		case 3:
			y--;
			break;
		}
	}

}
