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
import com.google.gson.JsonSyntaxException;

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
	
	int dir;
	
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

			//strokeWeight(4);
			noStroke();
			circle(particleArrList.get(i).getX(), particleArrList.get(i).getY(), 50);
			fill(particleArrList.get(i).getR(), particleArrList.get(i).getG(), particleArrList.get(i).getB());
			
			showTag();
			
			randomNum = (int)random(0, 5);
			System.out.println(randomNum);
			
			switch(randomNum)
			{
			case 0:
				particleArrList.get(i).setX(particleArrList.get(i).getX()+2);
				break;
			case 1:
				particleArrList.get(i).setX(particleArrList.get(i).getX()-2);;
				break;
			case 2:
				particleArrList.get(i).setY(particleArrList.get(i).getY()+2);;
				break;
			case 3:
				particleArrList.get(i).setY(particleArrList.get(i).getY()-2);;;
				break;
			}
			
			if(particleArrList.get(i).getX() >= 800)
			{
				particleArrList.get(i).setX(799);
				System.out.println("no no");
			}
			
			else if(particleArrList.get(i).getX() <= 0)
			{
				particleArrList.get(i).setX(1);
				System.out.println("no no");
			}
			
			if(particleArrList.get(i).getY() >= 500)
			{
				particleArrList.get(i).setY(499);
				System.out.println("no no");
			}
			
			else if(particleArrList.get(i).getY() <= 0)
			{
				particleArrList.get(i).setY(1);
				System.out.println("no no");
			}
		}
		
		//System.out.println(particleArrList.size());

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
							
							try
							{
								Gson gson = new Gson();
								Particles particle = gson.fromJson(line, Particles.class);
							
								for(int i = 0; i < particle.getAmount(); i++)
								{
									x = particle.getX();
									y = particle.getY();
								
									r = particle.getR();
									g = particle.getG();
									b = particle.getB();
								
									name = particle.getName();
								
									amount = particle.getAmount();
								
									System.out.println(x + " " + y + " " + name + " " + amount + " " + r + ", " + g + ", " + b);
								
									particleArrList.add(new Particles(x, y, name, amount, r, g, b));
								}
							} 
							catch(JsonSyntaxException err)
							{
								particleArrList.clear();
							}
							catch(IllegalStateException ise)
							{
								particleArrList.clear();
							}
								
						}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}).start();
	}
	
	public void showTag()
	{
		for(int i = 0; i < particleArrList.size(); i++)
		{
			if(dist(mouseX, mouseY, particleArrList.get(i).getX(), particleArrList.get(i).getY()) < 25)
			{
				fill(0);
				text(particleArrList.get(i).getName(), mouseX, mouseY);
				fill(r, g, b);
				
				particleArrList.get(i).setX(mouseX);
			}
		}
	}

}
