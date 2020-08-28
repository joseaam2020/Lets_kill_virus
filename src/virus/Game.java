package virus;

import java.awt.*;
import javax.swing.JFrame;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import static java.awt.Color.BLACK;

public class Game extends Canvas implements Runnable {
	
	public static final long serialVersionUID = 1L;
	public static final int WIDTH = 160;
	public static final int  HEIGHT = WIDTH/12 * 9;
	public static final int SCALE = 3;
	public static final String NAME = "Game";
	public boolean running = false;
	public int tickCount = 0;
	public JFrame frame;
	private BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	public Game(){
		setMinimumSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		setMaximumSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));

		frame = new JFrame(NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(this,BorderLayout.CENTER);
		frame.pack();

		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	public synchronized void start(){
		running = true;
		new Thread(this).start();
	}
	public synchronized void stop(){
		running = false;	
	}
	public void run(){
		long last_time=System.nanoTime();
		double nspt = 1000000000D/60D;
		int frames = 0;
		int ticks = 0;
		long last_timer = System.currentTimeMillis();
		double delta = 0;

		while(running){
			long now = System.nanoTime();
			delta += (now - last_time)/nspt;
			last_time = now;
			boolean should_render = true;

			while(delta >= 1){
				ticks++;
				tick();
				delta-=1;
				should_render = true;
			}

			try{
				Thread.sleep(2);
			} catch (InterruptedException e){
				e.printStackTrace();
			}

			if(should_render){
				frames++;
				render();
			}
			
			if (System.currentTimeMillis()-last_timer > 1000){
				last_timer += 1000;
				frames = 0;
				ticks = 0;	
			}
		}
	}
	
	public static void main(String[] args){
		new Game().start();
	}
	
	public void tick(){
		tickCount++;	
		for(int i=0;i<pixels.length;i++){
			pixels[i] = i + tickCount;
		}
	}

	public void render(){
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;	
		}
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image,0,0,getWidth(),getHeight(),null);
		g.dispose();
		bs.show();
	}
	
}
 
