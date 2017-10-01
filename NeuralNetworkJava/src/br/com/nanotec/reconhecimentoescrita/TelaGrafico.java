package br.com.nanotec.reconhecimentoescrita;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TelaGrafico extends JPanel implements TelaInterface
{
  private Point ponto;
  private Set traco;
  private double[] sequencia;
  
  public TelaGrafico()
  {
  	super();  	
  }
  
  public void paint(Graphics g)
  {
  	g.setColor(Color.white);
  	g.fillRect(0,0,getWidth(),getHeight());	
  }
  
  public synchronized void setPoint(Point p)
  {
  	this.ponto = p;
		if(this.ponto!=null)
		{
			this.traco.add(this.ponto.clone());
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{				
					Graphics g = TelaGrafico.this.getGraphics();
					g.fillRect(TelaGrafico.this.ponto.x,TelaGrafico.this.ponto.y,5,5);
				}
			});
		}
  }
  
	public double[] getBytesCaracter()
	{
		int larguraArea = 10;
		int alturaArea = 10;
		int numSecoesHorizontal = 15;
		int numSecoesVertical = 15;
		double[][] matriz = new double[numSecoesHorizontal][numSecoesVertical];

		Iterator it = this.traco.iterator();
		while(it.hasNext())
		{
			Point ponto = (Point)it.next();
			// Verificar coordenadas na direção X e Y
			int posX = ponto.x;
			int posY = ponto.y;
			for(int i=0;i<numSecoesHorizontal;i++)
			{
				for(int j=0;j<numSecoesVertical;j++)
				{
					if(((posX >=(i*larguraArea)) && (posX <=((i+1)*larguraArea))) &&
					    ((posY >=(j*alturaArea)) && (posY <=((j+1)*larguraArea))))
					{
						matriz[j][i] = 1.0D;
					}
				}					
			}
		}		
		// Analisar matriz para geração de sequencia		
		this.sequencia = new double[numSecoesHorizontal*numSecoesVertical+1];
		this.sequencia[0] = 0.0D;
		int count = 1;
		for(int i=0;i<numSecoesHorizontal;i++)
		{
			for(int j=0;j<numSecoesVertical;j++)
			{
				this.sequencia[count++] = matriz[i][j];
			}
		}
		return this.sequencia;
	}
	
	public void iniciarGravacaoCaracter()
	{
		this.traco = new HashSet();
	}
}













