package image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class RandImage {
	
	public static Object[] getImage(){
		Graphics g = null;
		BufferedImage image = null;
		String sRand="";
		try{
			int width=60, height=20;
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			g = image.getGraphics();
			Random random = new Random();
			g.setColor(getRandColor(200,250));
			g.fillRect(0, 0, width, height);
			g.setFont(new Font("Times New Roman",Font.PLAIN,18));
			g.setColor(new Color(255,255,255));
			g.drawRect(0,0,width-1,height-1);
		
			g.setColor(getRandColor(160,200));
			for (int i=0;i<155;i++){
				int x = random.nextInt(width);
				int y = random.nextInt(height);
				int xl = random.nextInt(12);
				int yl = random.nextInt(12);
				g.drawLine(x,y,x+xl,y+yl);
			}
			
			for (int i=0;i<4;i++){
				String rand=String.valueOf(random.nextInt(10));
				sRand+=rand;
				g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));
				g.drawString(rand,13*i+6,16);
			}
		}finally{
			if(g!=null) g.dispose();
		}
		return new Object[]{image, sRand};
	}
	
	protected static Color getRandColor(int fc,int bc){
		Random random = new Random();
		if(fc>255) fc=255;
		if(bc>255) bc=255;
		int r=fc+random.nextInt(bc-fc);
		int g=fc+random.nextInt(bc-fc);
		int b=fc+random.nextInt(bc-fc);
		return new Color(r,g,b);
	}

}
