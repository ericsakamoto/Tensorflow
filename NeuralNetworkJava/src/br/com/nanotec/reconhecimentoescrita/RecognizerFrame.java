package br.com.nanotec.reconhecimentoescrita;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import br.com.nanotec.redeneural.RedeNeural;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class RecognizerFrame extends JFrame implements ActionListener, MouseMotionListener, MouseListener
{
	public static final int MODO_TREINAMENTO = 0;
	public static final int MODO_RECONHECIMENTO = 1;
	public static final int MODO_AQUISICAO = 2;	
	
  private JButton btoModoTreinamento = new JButton("Treinamento"); 
  private JButton btoModoReconhecimento = new JButton("Reconhecimento");
  private JButton btoModoAquisicao = new JButton("Aquisição");  
  private JButton btoLimpar = new JButton("Limpar");    
  private TelaGrafico tela = new TelaGrafico();
  private JLabel lblResultado = new JLabel("Resultado");
  private JLabel lblCaracter = new JLabel();
  private JLabel lblModo = new JLabel();
  
  private RedeNeural rn;
	private int camada_x = 225;
	private int camada_z = 100;
	private int camada_y = 2;		
	private double learningRate = 0.2D;
	private double erro = 0.01D;  
	private int maxCiclos = 1000;
	private int modo;	
	private boolean retorno;
	
	private double[][] entrada;
	private double[][] saida;	
  
	public static void main(String[] args)
	{
		new RecognizerFrame("Reconhecimento de Escrita");
	}

  public RecognizerFrame()
  {
 
  }

  public RecognizerFrame(String arg)
  {
    super(arg);
    
    this.modo = MODO_AQUISICAO;

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100,300,300,210);
		this.getContentPane().setLayout(null);
		
		btoModoReconhecimento.setBounds(160,5,130,20);
		btoModoReconhecimento.setVisible(true);
		btoModoTreinamento.setBounds(160,30,130,20);
		btoModoTreinamento.setVisible(true);
		btoModoAquisicao.setBounds(160,55,130,20);
		btoModoAquisicao.setVisible(true);
		btoLimpar.setBounds(160,80,130,20);
		btoLimpar.setVisible(true);
		lblResultado.setBounds(160,100,100,20);
		lblResultado.setVisible(true);
		lblCaracter.setBounds(160,100,100,100);
		lblCaracter.setBackground(Color.white);
		lblCaracter.setVisible(true);		
		lblModo.setBounds(10,160,280,20);
		lblModo.setBackground(Color.white);
		lblModo.setVisible(true);				
		lblModo.setText("MODO AQUISIÇÃO");
		tela.setBounds(5,5,150,150);
		tela.setBackground(Color.white);
		tela.setVisible(true);
		
		btoModoReconhecimento.addActionListener(this);
		btoModoTreinamento.addActionListener(this);
		btoModoAquisicao.addActionListener(this);
		btoLimpar.addActionListener(this);
		tela.addMouseListener(this);
		tela.addMouseMotionListener(this);
				
		this.getContentPane().add(btoModoReconhecimento);
		this.getContentPane().add(btoModoTreinamento);
		this.getContentPane().add(btoModoAquisicao);
		this.getContentPane().add(btoLimpar);
		this.getContentPane().add(lblResultado);
		this.getContentPane().add(lblCaracter);
		this.getContentPane().add(lblModo);
		this.getContentPane().add(tela);
		
		this.show();    
  }

	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("Treinamento"))
		{
			treinarRedeNeural();
			aguardarFimTreinamento();
		}
		else if(e.getActionCommand().equals("Reconhecimento"))
		{
			this.modo = MODO_RECONHECIMENTO;
			lblModo.setText("MODO RECONHECIMENTO");
		}			
		else if(e.getActionCommand().equals("Aquisicao"))
		{		
			this.modo = MODO_AQUISICAO;
			lblModo.setText("MODO AQUISIÇÃO");
		}
		else if(e.getSource()==btoLimpar)
		{
			this.tela.repaint();	
		}
	}

	public void mouseDragged(MouseEvent e)
	{
		this.tela.setPoint(e.getPoint());		
	}

	public void mouseMoved(MouseEvent e)
	{
		
	}

	public void mouseClicked(MouseEvent e)
	{
		
	}

	public void mousePressed(MouseEvent e)
	{
		this.tela.iniciarGravacaoCaracter();
	}

	public void mouseReleased(MouseEvent e)
	{
		if(this.modo == MODO_RECONHECIMENTO)
			reconhecerCaracter();
		else if(this.modo == MODO_AQUISICAO)
			gravarCaracter();
	}

	public void mouseEntered(MouseEvent e)
	{
		
	}

	public void mouseExited(MouseEvent e)
	{
		
	}
	
	public void reconhecerCaracter()
	{		
		double[] entrada = this.tela.getBytesCaracter();
		processarEntradaRedeNeural(entrada);
		double[] saida = this.rn.usarRedeNeural(entrada);
		processarSaidaRedeNeural(saida);
	}
	
	private void processarEntradaRedeNeural(double[] entrada)
	{
    StringBuffer sb = new StringBuffer();
    for(int i=1;i<30;i++)    
    {
			sb.append(new Double(entrada[i]).intValue());
			sb.append(" ");
    }
    System.out.println("Entrada = " + sb.toString().trim());
    sb = null;
	}	
	
	private void processarSaidaRedeNeural(double[] saida)
	{
    StringBuffer sb = new StringBuffer();
    for(int j=1;j<3;j++)    
    {
    	double valorSuperior = saida[j] + erro;
    	double valorInferior = saida[j] - erro;    	
    	if(valorSuperior<=1 && valorInferior>=0.7)
	     	sb.append("1");
    	else
	    	sb.append("0");
     	sb.append(" ");	    	
    }
    System.out.println("Saída   = " + sb.toString().trim());              
    sb = null;
	}
	
	public void treinarRedeNeural()	
	{
		new Thread(new Runnable()
	  	{
	  		public void run()
	  		{							
					RecognizerFrame.this.lblModo.setText("MODO TREINAMENTO");
					RecognizerFrame.this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
					RecognizerFrame.this.rn = new RedeNeural(camada_x,camada_z,camada_y,learningRate,erro,maxCiclos); 
					getDadosGrafico();   
 			    RecognizerFrame.this.lblModo.setText("TREINAMENTO EM ANDAMENTO...");
					RecognizerFrame.this.retorno = RecognizerFrame.this.rn.treinarRedeNeural(entrada,saida);
					RecognizerFrame.this.rn.exibirPesos("c:/logRN.txt");
					
	  		}
		  }
		).start();
	}
	
	public void aguardarFimTreinamento()
	{
		new Thread(new Runnable()
	  	{
	  		public void run()
	  		{							
					while(!RecognizerFrame.this.retorno)
					{
						try{Thread.sleep(5000);}catch(Exception ex){ex.printStackTrace();}
					}
			    RecognizerFrame.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			    RecognizerFrame.this.lblModo.setText("MODO TREINAMENTO");		
	  		}
	  	}
	  ).start();
	}
	
	public void gravarCaracter()
	{
		double[] entrada = this.tela.getBytesCaracter();		
		for(int i=0;i<entrada.length;i++)
		{
			System.out.print(new Double(entrada[i]).intValue());	
		}
		System.out.println("");
	}

	public void getDadosGrafico()	
	{
		double[][] e = 
		{
		/*Q1*/{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,1,1,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,1,1,0,0,0,0,0,0,0,1,0,0,0,0,0,1,1,0,0,0,0,0,0,0,1,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		/*C1*/{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,1,0,0,0,0,0,0,1,1,0,0,0,0,0,0,1,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,1,1,0,0,0,0,0,1,0,0,0,0,0,0,0,1,1,0,0,0,0,0,1,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,1,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		/*T1*/{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,1,0,0,0,0,0,0,0,0,0,1,1,0,0,0,1,1,0,0,0,0,0,0,0,1,1,0,0,0,0,0,1,0,0,0,0,0,0,1,1,0,0,0,0,0,0,1,1,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},

		/*Q2*/{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		/*C2*/{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,1,0,0,0,0,0,0,0,1,1,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,1,0,0,0,0,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		/*T2*/{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,1,0,0,0,0,0,0,0,0,0,1,1,0,0,0,1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,1,0,0,0,0,0,1,1,0,0,0,0,0,0,1,1,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},

		/*Q3*/{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		/*C3*/{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,1,1,1,0,0,1,1,1,0,0,0,0,0,0,1,1,0,0,0,0,0,1,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,1,1,0,0,0,0,0,1,1,0,0,0,0,0,0,1,1,0,0,0,0,0,1,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,1,1,1,0,0,1,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		/*T3*/{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,0,1,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,1,1,0,0,0,1,1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,1,0,0,0,0,0,1,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,1,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},

		/*Q4*/{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,1,1,1,0,0,0,0,1,1,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,1,1,1,1,0,0,0,0,1,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		/*C4*/{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,1,0,0,0,0,0,0,1,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,1,0,0,0,0,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		/*T4*/{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,1,0,0,0,0,0,1,1,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},

		/*Q5*/{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,1,1,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		/*C5*/{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,1,0,0,0,0,0,0,1,1,0,0,0,0,0,1,1,0,0,0,0,0,1,1,0,0,0,0,0,0,0,1,0,0,0,0,0,1,1,0,0,0,0,0,0,1,1,0,0,0,0,0,1,1,0,0,0,0,0,0,1,1,0,0,0,0,0,0,1,1,0,0,0,0,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		/*T5*/{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,1,0,0,0,0,0,0,0,0,0,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		};
		
		double[][] s = 
		{
		 {0,0,0}, // Quadrado
	   {0,0,1}, // Circulo
	   {0,1,0}, // Triangulo
		 {0,0,0}, // Quadrado
	   {0,0,1}, // Circulo
	   {0,1,0}, // Triangulo
		 {0,0,0}, // Quadrado
	   {0,0,1}, // Circulo
	   {0,1,0}, // Triangulo
		 {0,0,0}, // Quadrado
	   {0,0,1}, // Circulo
	   {0,1,0}, // Triangulo
		 {0,0,0}, // Quadrado
	   {0,0,1}, // Circulo
	   {0,1,0}, // Triangulo	   	   	   	   
		};
		
		this.entrada = e;
		this.saida = s;		
	}

	public void getDadosCaracter()	
	{	
		double[][] e = 
		{{0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,0,0}, //1
	   {0,0,0,0,1,0,1,0,0,0,1,1,1,0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,1,0,0}, //2
	   {0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,1,0,0}, //3
	   {0,0,0,0,0,0,0,1,1,1,0,0,1,0,0,0,0,0,0,0,0,0,1,0,1,1,1,1,1,0,0}, //4
	   {0,0,0,0,0,1,1,1,1,1,0,1,1,0,0,0,0,0,1,1,1,1,0,0,1,1,0,0,1,0,0}, //5
	   {0,0,0,0,1,1,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1,0,0}, //6
	   {0,0,0,0,1,1,1,1,0,0,0,0,1,0,0,0,0,0,1,1,0,1,0,1,0,1,0,1,1,0,0}, //7
	   {0,0,0,0,1,0,0,1,1,1,0,0,1,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0}, //8
	   {0,0,0,0,1,0,1,0,0,0,1,1,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0}, //9
	   {0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0}};//0
		double[][] s = 
	  {{0,0,0,0,1}, // 1
	   {0,0,0,1,0}, // 2
	   {0,0,0,1,1}, // 3
	   {0,0,1,0,0}, // 4
	   {0,0,1,0,1}, // 5
	   {0,0,1,1,0}, // 6
	   {0,0,1,1,1}, // 7
	   {0,1,0,0,0}, // 8
	   {0,1,0,0,1}, // 9
	   {0,0,0,0,0}};// 0 
		this.entrada = e;
		this.saida = s;			
	}	   
}

















