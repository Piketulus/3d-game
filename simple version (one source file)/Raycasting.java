import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Raycasting extends JFrame {
	private static final long serialVersionUID = 1L;

	private static boolean[] keys = new boolean[65536];
	
	private static int spriteSheetWidth = 832;
	private static int width = 800;
	//private static int height = 600;
	//private static int mapWidth = 24;
	//private static int mapHeight = 24;
	private static int texWidth = 64;
	private static int texHeight = 64;
	
	private static int[][] worldMap=
		{
		  {5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5},
		  {5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5},
		  {5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5},
		  {5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5},
		  {5,0,0,0,0,0,2,2,2,2,2,0,0,0,0,3,0,3,0,3,0,0,0,5},
		  {5,0,0,0,0,0,2,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,5},
		  {5,0,0,0,0,0,2,0,0,0,2,0,0,0,0,3,0,0,0,3,0,0,0,5},
		  {5,0,0,0,0,0,2,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,5},
		  {5,0,0,0,0,0,2,2,0,2,2,0,0,0,0,3,0,3,0,3,0,0,0,5},
		  {5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5},
		  {5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5},
		  {5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5},
		  {5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5},
		  {5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5},
		  {5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5},
		  {5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5},
		  {5,4,4,4,4,4,4,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5},
		  {5,4,0,4,0,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5},
		  {5,4,0,0,0,0,5,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5},
		  {5,4,0,4,0,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5},
		  {5,4,0,4,4,4,4,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5},
		  {5,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5},
		  {5,4,4,4,4,4,4,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5},
		  {5,5,5,5,5,5,5,5,5,5,5,0,0,5,5,5,5,5,5,5,5,5,5,5},
		  {4,4,4,4,4,4,4,4,4,4,4,0,0,4,4,4,7,7,7,7,7,7,7,7},
		  {4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,7},
		  {4,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7},
		  {4,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7},
		  {4,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,7},
		  {4,0,4,0,0,0,0,5,5,5,5,5,5,5,5,5,7,7,0,7,7,7,7,7},
		  {4,0,5,0,0,0,0,5,0,5,0,5,0,5,0,5,7,0,0,0,7,7,7,1},
		  {4,0,6,0,0,0,0,5,0,0,0,0,0,0,0,5,7,0,0,0,0,0,0,8},
		  {4,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,7,7,1},
		  {4,0,8,0,0,0,0,5,0,0,0,0,0,0,0,5,7,0,0,0,0,0,0,8},
		  {4,0,0,0,0,0,0,5,0,0,0,0,0,0,0,5,7,0,0,0,7,7,7,1},
		  {4,0,0,0,0,0,0,5,5,5,5,0,5,5,5,5,7,7,7,7,7,7,7,1},
		  {6,6,6,6,6,6,6,6,6,6,6,0,6,6,6,6,6,6,6,6,6,6,6,6},
		  {8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
		  {6,6,6,6,6,6,0,6,6,6,6,0,6,6,6,6,6,6,6,6,6,6,6,6},
		  {4,4,4,4,4,4,0,4,4,4,6,0,6,2,2,2,2,2,2,2,3,3,3,3},
		  {4,0,0,0,0,0,0,0,0,4,6,0,6,2,0,0,0,0,0,2,0,0,0,2},
		  {4,0,0,0,0,0,0,0,0,0,0,0,6,2,0,0,5,0,0,2,0,0,0,2},
		  {4,0,0,0,0,0,0,0,0,4,6,0,6,2,0,0,0,0,0,2,2,0,2,2},
		  {4,0,6,0,6,0,0,0,0,4,6,0,0,0,0,0,5,0,0,0,0,0,0,2},
		  {4,0,0,5,0,0,0,0,0,4,6,0,6,2,0,0,0,0,0,2,2,0,2,2},
		  {4,0,6,0,6,0,0,0,0,4,6,0,6,2,0,0,5,0,0,2,0,0,0,2},
		  {4,0,0,0,0,0,0,0,0,4,6,0,6,2,0,0,0,0,0,2,0,0,0,2},
		  {4,4,4,4,4,4,4,4,4,4,1,0,1,2,2,2,2,2,2,3,3,3,3,3},
		  {8,8,8,8,8,8,8,8,8,8,8,0,4,6,4,4,6,4,6,4,4,4,6,4},
		  {8,0,0,0,0,0,0,0,0,0,8,0,0,0,0,0,0,0,0,0,0,0,0,4},
		  {8,0,3,3,0,0,0,0,0,8,8,4,0,0,0,0,0,0,0,0,0,0,0,6},
		  {8,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6},
		  {8,0,3,3,0,0,0,0,0,8,8,4,0,0,0,0,0,0,0,0,0,0,0,4},
		  {8,0,0,0,0,0,0,0,0,0,8,4,0,0,0,0,0,6,6,6,0,6,4,6},
		  {8,8,8,8,0,8,8,8,8,8,8,4,4,4,4,4,4,6,0,0,0,0,0,6},
		  {7,7,7,7,0,7,7,7,7,0,8,0,8,0,8,0,8,4,0,4,0,6,0,6},
		  {7,7,0,0,0,0,0,0,7,8,0,8,0,8,0,8,8,6,0,0,0,0,0,6},
		  {7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,6,0,0,0,0,0,4},
		  {7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,6,0,6,0,6,0,6},
		  {7,7,0,0,0,0,0,0,7,8,0,8,0,8,0,8,8,6,4,6,0,6,6,6},
		  {7,7,7,7,0,7,7,7,7,8,8,4,0,6,8,4,8,3,3,3,0,3,3,3},
		  {2,2,2,2,0,2,2,2,2,4,6,4,0,0,6,0,6,3,0,0,0,0,0,3},
		  {2,2,0,0,0,0,0,2,2,4,0,0,0,0,0,0,4,3,0,0,0,0,0,3},
		  {2,0,0,0,0,0,0,0,2,4,0,0,0,0,0,0,4,3,0,0,0,0,0,3},
		  {1,0,0,0,0,0,0,0,1,4,4,4,4,4,6,0,6,3,3,0,0,0,3,3},
		  {2,0,0,0,0,0,0,0,2,2,2,1,2,2,2,6,6,0,0,5,0,5,0,5},
		  {2,2,0,0,0,0,0,2,2,2,0,0,0,2,2,0,5,0,5,0,0,0,5,5},
		  {2,0,0,0,0,0,0,0,2,0,0,0,0,0,2,5,0,5,0,5,0,5,0,5},
		  {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5},
		  {2,0,0,0,0,0,0,0,2,0,0,0,0,0,2,5,0,5,0,5,0,5,0,5},
		  {2,2,0,0,0,0,0,2,2,2,0,0,0,2,2,0,5,0,5,0,0,0,5,5},
		  {2,2,2,2,1,2,2,2,2,2,2,1,2,2,2,5,5,5,5,5,5,5,5,5}
		};
	
	private static int numSprites = 23;
	
	private static double[] zBuffer = new double[width];

	private static int[] spriteOrder = new int[numSprites];
	private static double[] spriteDistance = new double[numSprites];
	
	public void processEvent(AWTEvent e) {
		switch(e.getID()) {
			case WindowEvent.WINDOW_CLOSING:
				System.exit(0);
			case KeyEvent.KEY_PRESSED:
			case KeyEvent.KEY_RELEASED:
				keys[((KeyEvent)e).getKeyCode()] = e.getID() == KeyEvent.KEY_PRESSED;
		}
	}
	
	public static void main(String[] args) {
		    double[][] sprite = 
			{
				{20.5 + 48,11.5,10.0}, 
				{18.5 + 48,4.5, 10.0}, 
				{10.0 + 48,4.5, 10.0}, 
				{10.0 + 48,12.5,10.0}, 
				{3.5 + 48, 6.5, 10.0}, 
				{3.5 + 48, 20.5,10.0}, 
				{3.5 + 48, 14.5,10.0}, 
				{14.5 + 48,20.5,10.0}, 
				{20.5 + 48,18.5,10.0},
				{18.5 + 48, 10.5,9.0}, 
				{18.5 + 48, 11.5,9.0}, 
				{18.5 + 48, 12.5,9.0},
				{21.5 + 48, 16.5,9.0},
				{19.5 + 48, 16.5,9.0},
				{19.5 + 48, 13.5,8.0},
				{21.5 + 48, 1.5, 8.0}, 
				{15.5 + 48, 1.5, 8.0}, 
				{16.2 + 48, 1.2, 8.0},
				{16.0 + 48, 1.8, 8.0}, 
				{3.5 + 48,  2.5, 8.0}, 
				{9.5 + 48, 15.5, 8.0}, 
				{10.5 + 48,15.8, 8.0},
				{10.0 + 48, 15.1,8.0}
			};
		  int w = 800, h = 600;
		  double posX = 70, posY = 12;
		  double dirX = -1, dirY = 0;
		  double planeX = 0, planeY = 0.66;
		  
		  double time = 0;
		  double oldTime = 0;
		  int gunPosX = 25, gunPosY = 25, gunSize = 576, gunTex = 12;
		  boolean shoot = false;
		  boolean first = false;
		  
		  Raycasting rayc = new Raycasting();
		  rayc.setTitle("Raycasting Engine			Made by: Joel Pietilainen");
		  rayc.enableEvents(KeyEvent.KEY_PRESSED);
		  rayc.setSize(w, h);
		  rayc.setResizable(false);
		  rayc.setLocationRelativeTo(null);
		  rayc.setVisible(true);
		  
		  Random rand = new Random();
		  
		  BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		  int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		  
		  Graphics gr = rayc.getGraphics();
		  
		  int[][] texture = new int[13][texHeight * texWidth];
		  
		  int[] result = new int[spriteSheetWidth * 64];
		  
		  try {
			ImageIO.read(Raycasting.class.getResource("wolftextures.png")).getRGB(0, 0, spriteSheetWidth, 64, result, 0, spriteSheetWidth);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		  
		  for(int x = 0; x < texWidth; x++)
			  for(int y = 0; y < texHeight; y++)
			  {
				texture[0][texWidth * y + x] = result[(x) + y * spriteSheetWidth];//red brick with eagle
			    texture[1][texWidth * y + x] = result[(x + 64) + y * spriteSheetWidth];//red brick
			    texture[2][texWidth * y + x] = result[(x + 128) + y * spriteSheetWidth];//purple stone
			    texture[3][texWidth * y + x] = result[(x + 192) + y * spriteSheetWidth];//stone
				texture[4][texWidth * y + x] = result[(x + 256) + y * spriteSheetWidth];//blue stone
			    texture[5][texWidth * y + x] = result[(x + 320) + y * spriteSheetWidth];//mossy stone
			    texture[6][texWidth * y + x] = result[(x + 384) + y * spriteSheetWidth];//wood
			    texture[7][texWidth * y + x] = result[(x + 448) + y * spriteSheetWidth];//color stone
			    texture[8][texWidth * y + x] = result[(x + 512) + y * spriteSheetWidth];//barrel
			    texture[9][texWidth * y + x] = result[(x + 576) + y * spriteSheetWidth];//pillar
			    texture[10][texWidth * y + x] = result[(x + 640) + y * spriteSheetWidth];//green light
			    texture[11][texWidth * y + x] = result[(x + 704) + y * spriteSheetWidth];
			    texture[12][texWidth * y + x] = result[(x + 768) + y * spriteSheetWidth];
			  }
		  int counter = 0;
		  while(!keys[27]) {
			  
			  
			  
			  for(int x = 0; x < w; x++) {
				  double xCamera = x * 2 / (double)w - 1;
				  double rayPosX = posX;
				  double rayPosY = posY;
				  double rayDirX = dirX + planeX * xCamera;
				  double rayDirY = dirY + planeY * xCamera;

				  int mapX = (int) rayPosX;
				  int mapY = (int) rayPosY;
				  
				  int stepX = 0;
				  int stepY = 0;
				  double deltaDistX = Math.sqrt(1 + (rayDirY * rayDirY) / (rayDirX * rayDirX));
				  double deltaDistY = Math.sqrt(1 + (rayDirX * rayDirX) / (rayDirY * rayDirY));

				  double sideDistX;
				  double sideDistY;
				  double perpWallDist;
				  
				  int hit = 0;
				  int side = 0;
				  
				  if(rayDirX < 0) {
					  stepX = -1;
					  sideDistX = (rayPosX - mapX) * deltaDistX;
				  } else {
					  stepX = 1;
					  sideDistX = (mapX + 1.0 - rayPosX) * deltaDistX;
				  }
				  if(rayDirY < 0) {
					  stepY = -1;
					  sideDistY = (rayPosY - mapY) * deltaDistY;
				  } else {
					  stepY = 1;
					  sideDistY = (mapY + 1.0 - rayPosY) * deltaDistY;
				  }
				  
				  while (hit == 0)
			      {
			        if (sideDistX < sideDistY)
			        {
			          sideDistX += deltaDistX;
			          mapX += stepX;
			          side = 0;
			        }
			        else
			        {
			          sideDistY += deltaDistY;
			          mapY += stepY;
			          side = 1;
			        }
			        if (worldMap[mapX][mapY] > 0) hit = 1;
			      } 
				  
				  if (side == 0)
				      perpWallDist = Math.abs((mapX - rayPosX + (1 - stepX) / 2) / rayDirX);
				  else
				      perpWallDist = Math.abs((mapY - rayPosY + (1 - stepY) / 2) / rayDirY);
				  
				  double wallHeight = Math.abs(h / perpWallDist);
				  
				  int drawStart = (int) (-wallHeight / 2 + h / 2);
				  if(drawStart < 0) drawStart = 0;
				  int drawEnd = (int) (wallHeight / 2 + h / 2);
				  if(drawEnd >= h) drawEnd = h;
				  
				  int texNum = worldMap[mapX][mapY] - 1;
			       
			      double wallX;
			      if (side == 1) wallX = rayPosX + ((mapY - rayPosY + (1 - stepY) / 2) / rayDirY) * rayDirX;
			      else wallX = rayPosY + ((mapX - rayPosX + (1 - stepX) / 2) / rayDirX) * rayDirY;
			      wallX -= Math.floor((wallX));
			       
			      int texX = (int)(wallX * (double)(texWidth));
			      if(side == 0 && rayDirX > 0) texX = texWidth - texX - 1;
			      if(side == 1 && rayDirY < 0) texX = texWidth - texX - 1;
				  
			      for(int y = drawStart; y<drawEnd; y++)
			      {
			        int d = (int)(y * 256 - h * 128 + wallHeight * 128); 
			        int texY = (int) (((d * texHeight) / wallHeight) / 256);
			        int color = texture[texNum][Math.abs(texHeight * texY + texX)];
			        if(side == 1) color = (color >> 1) & 8355711;
			        pixels[x + y * w] = color;
			      } 
			      
			      zBuffer[x] = perpWallDist;
			      
			      //Floor Casting
			      
			      
			      double floorXWall, floorYWall;
				  
				  if(side == 0 && rayDirX > 0) {
					  floorXWall = mapX;
					  floorYWall = mapY + wallX;
				  }
				  else if(side == 0 && rayDirX < 0) {
					  floorXWall = mapX + 1.0;
					  floorYWall = mapY + wallX;
				  } else if(side == 1 && rayDirY > 0) {
					  floorXWall = mapX + wallX;
					  floorYWall = mapY;
				  } else {
					  floorXWall = mapX + wallX;
					  floorYWall = mapY + 1.0;
				  }
				  
				  double distWall, distPlayer, currentDist;
				  distWall = perpWallDist;
				  distPlayer = 0.0;
				  
				  if (drawEnd < 0) drawEnd = h;
				  
				  for(int y = drawEnd + 1; y < h; y++) {
					  currentDist = h / (2.0 * y - h);
					  double weight = (currentDist - distPlayer) / (distWall - distPlayer);
					  
					  double currentFloorX = weight * floorXWall + (1.0 - weight) * posX;
					  double currentFloorY = weight * floorYWall + (1.0 - weight) * posY;

					  int floorTexX, floorTexY;
					  floorTexX = (int)(currentFloorX * texWidth) % texWidth;
					  floorTexY = (int)(currentFloorY * texHeight) % texHeight;
					  
					  //int checkerBoard = ((int)currentFloorX + (int)currentFloorY) % 2;
					  int floorTexture = 3;
					  //int mapFloorX = (int) currentFloorX;
					  //int mapFloorY = (int) currentFloorY;
					  
					  //if(checkerBoard == 0) floorTexture = 5;
					  //else floorTexture = 3;
					  
					  pixels[x + y * w] = (texture[floorTexture][floorTexX + floorTexY * texWidth] >> 1) & 8355711;
					  pixels[x + (h - y) * w] = (texture[6][floorTexX + floorTexY * texWidth]);
				  }
				  
			    }
			  
		  for(int i = 0; i < numSprites; i++) {
			      spriteOrder[i] = i;
			      spriteDistance[i] = ((posX - sprite[i][0]) * (posX - sprite[i][0]) + (posY - sprite[i][1]) * (posY - sprite[i][1]));
		  }
		  
		  for(int i = 0; i < numSprites; i++)
		    {
		      double spriteX = sprite[spriteOrder[i]][0] - posX;
		      double spriteY = sprite[spriteOrder[i]][1] - posY;
		      
		      double invDet = 1.0 / (planeX * dirY - dirX * planeY);
		      
		      double transformX = invDet * (dirY * spriteX - dirX * spriteY);
		      double transformY = invDet * (-planeY * spriteX + planeX * spriteY);
		            
		      int spriteScreenX = (int)((w / 2) * (1 + transformX / transformY));
		      
		      int spriteHeight = Math.abs((int)(h / (transformY))); 
		      int drawStartY = -spriteHeight / 2 + h / 2;
		      if(drawStartY < 0) drawStartY = 0;
		      int drawEndY = spriteHeight / 2 + h / 2;
		      if(drawEndY >= h) drawEndY = h - 1;
		      
		      int spriteWidth = Math.abs((int) (h / (transformY)));
		      int drawStartX = -spriteWidth / 2 + spriteScreenX;
		      if(drawStartX < 0) drawStartX = 0;
		      int drawEndX = spriteWidth / 2 + spriteScreenX;
		      if(drawEndX >= w) drawEndX = w - 1;
		      
		      for(int stripe = drawStartX; stripe < drawEndX; stripe++)
		      {
		        int texX = (int)(256 * (stripe - (-spriteWidth / 2 + spriteScreenX)) * texWidth / spriteWidth) / 256;
		        
		        if(transformY > 0 && stripe > 0 && stripe < w && transformY < zBuffer[stripe]) 
		        for(int y = drawStartY; y < drawEndY; y++) 
		        {
		          int d = (y) * 256 - h * 128 + spriteHeight * 128; 
		          int texY = ((d * texHeight) / spriteHeight) / 256;
		          int color = texture[(int)(sprite[spriteOrder[i]][2])][Math.abs(texWidth * texY + texX)]; 
		          if(((color & 0x00FFFFFF) != 0) && ((color & 0x00FFFFFF) != 0x0066c7)) pixels[stripe + y * w] = color;
		        }
		      }
		    }
		  
		  counter++;
		//render gun on screen
		  
		  if(shoot) {
			  if(counter % 25 < 12)
				  gunTex = 11;
			  else gunTex = 12;
		  } else {
			  gunTex = 12;
		  }
		  first = false;
		  for(int y = gunPosY; y < gunPosY + gunSize && y < h; y++) {
			  double yp = (y - gunPosY) / (double) gunSize;
			  int yTex = (int) (yp * texHeight);
			  for(int x = gunPosX; x < gunPosX + gunSize && x < w; x++) {
				  double xp = (x - gunPosX) / (double) gunSize;
				  int xTex = (int) (xp * texWidth);
				  
				  int color = texture[gunTex][(xTex % texWidth) + (yTex % texHeight) * 64];
				  if(color != 0xFFB400B4 && color != 0xFF9C009C) pixels[x + y * w] = color;
			  }
		  }
		  	  shoot = false;
		  
			  gr.drawImage(img, 0, 0, w, h, null);
			  gr.setColor(Color.green);
			  gr.drawString("X = " + posY + ", Y = " + posX, 50, 50);
			  //input
			  oldTime = time;
			  time = System.currentTimeMillis();
			  double frameTime = (time - oldTime) / 1000.0;
			  
			  double moveSpeed = frameTime * 2.0;
			  double rotSpeed = frameTime * 3.0;
			  double collisionDist = 0.4;
			  boolean collisionFront = false, collisionBack = false;
			  double nextPosFX = posX + dirX * (moveSpeed + collisionDist);
			  double nextPosFY = posY + dirY * (moveSpeed + collisionDist);
			  
			  double nextPosBX = posX - dirX * (moveSpeed + collisionDist);
			  double nextPosBY = posY - dirY * (moveSpeed + collisionDist);
			  
			  for(int i = 0; i < numSprites; i++) {
				  if((int)nextPosFX == (int)sprite[i][0] && (int)nextPosFY == (int)sprite[i][1] && sprite[i][2] != 10.0) collisionFront = true;
				  if((int)nextPosBX == (int)sprite[i][0] && (int)nextPosBY == (int)sprite[i][1] && sprite[i][2] != 10.0) collisionBack = true;

			  }
			  
			  if(keys[38] || keys[KeyEvent.VK_W]) {
				  if((worldMap[(int)(posX + dirX * (moveSpeed + collisionDist))][(int)(posY)] <= 0) && !collisionFront) posX += dirX * moveSpeed;
			      if((worldMap[(int)(posX)][(int)(posY + dirY * (moveSpeed + collisionDist))] <= 0) && !collisionFront) posY += dirY * moveSpeed;
			  }
			  if(keys[40] || keys[KeyEvent.VK_S]) {
				  if((worldMap[(int)(posX - dirX * (moveSpeed + collisionDist))][(int)(posY)] <= 0) && !collisionBack) posX -= dirX * moveSpeed;
			      if((worldMap[(int)(posX)][(int)(posY - dirY * (moveSpeed + collisionDist))] <= 0) && !collisionBack) posY -= dirY * moveSpeed;
			  }
			  if(keys[39] || keys[KeyEvent.VK_D]) {
				  double oldDirX = dirX;
				  dirX = dirX * Math.cos(-rotSpeed) - dirY * Math.sin(-rotSpeed);
				  dirY = oldDirX * Math.sin(-rotSpeed) + dirY * Math.cos(-rotSpeed);
				  double oldPlaneX = planeX;
			      planeX = planeX * Math.cos(-rotSpeed) - planeY * Math.sin(-rotSpeed);
			      planeY = oldPlaneX * Math.sin(-rotSpeed) + planeY * Math.cos(-rotSpeed);
			  }
			  if(keys[37] || keys[KeyEvent.VK_A]) {
				  double oldDirX = dirX;
				  dirX = dirX * Math.cos(rotSpeed) - dirY * Math.sin(rotSpeed);
				  dirY = oldDirX * Math.sin(rotSpeed) + dirY * Math.cos(rotSpeed);
				  double oldPlaneX = planeX;
			      planeX = planeX * Math.cos(rotSpeed) - planeY * Math.sin(rotSpeed);
			      planeY = oldPlaneX * Math.sin(rotSpeed) + planeY * Math.cos(rotSpeed);
			  }
			  if(keys[KeyEvent.VK_SPACE]) {
				  if(!shoot) gunTex = 11;
				  shoot = true;
			  }
			  
		  }
	}
	
}