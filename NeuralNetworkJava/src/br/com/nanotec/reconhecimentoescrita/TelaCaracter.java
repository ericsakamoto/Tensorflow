package br.com.nanotec.reconhecimentoescrita;

import java.awt.Graphics;
import java.awt.Point;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TelaCaracter extends JPanel implements TelaInterface
{
  private Point ponto;
  private Set traco;
  private double[] sequencia;
  
  public TelaCaracter()
  {
  	super();  	
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
					Graphics g = TelaCaracter.this.getGraphics();
					g.fillRect(TelaCaracter.this.ponto.x,TelaCaracter.this.ponto.y,5,5);
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
		
		// Imprimir matriz
		/*
		for(int i=0;i<numSecoesHorizontal;i++)
		{
			for(int j=0;j<numSecoesVertical;j++)
			{
				System.out.print(new Double(matriz[i][j]).intValue() + " ");
			}
			System.out.println("");
		}
		*/
		
		// Analisar matriz para geração de sequencia		
		this.sequencia = new double[31];
		// Vertical
		for(int i=0;i<numSecoesHorizontal;i++)
		{
			int count = 0;
			for(int j=0;j<numSecoesVertical;j++)
			{
				if(matriz[i][j]==1.0D)
				{
					count++;
				}
			}
			if(count%2==0)
				this.sequencia[numSecoesHorizontal+i+1] = 0.0D;
			else
				this.sequencia[numSecoesHorizontal+i+1] = 1.0D;
		}

		// Horizontal
		for(int j=0;j<numSecoesVertical;j++)
		{
			int count = 0;
			for(int i=0;i<numSecoesHorizontal;i++)
			{
				if(matriz[i][j]==1.0D)
				{
					count++;
				}	
			}
			if(count%2==0)
				this.sequencia[j+1] = 0.0D;
			else			
				this.sequencia[j+1] = 1.0D;
			
		}		
		return this.sequencia;
	}
	
	public void iniciarGravacaoCaracter()
	{
		this.traco = new HashSet();
	}
}













