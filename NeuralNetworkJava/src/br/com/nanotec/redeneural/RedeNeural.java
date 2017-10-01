package br.com.nanotec.redeneural;

import java.util.Random;

public class RedeNeural 
{
	private double[] camada_X_in = new double[500];
	private double[] camada_Y_in = new double[500];
	private double[] camada_Z_in = new double[500];
	private double[] camada_X = new double[500];
	private double[] camada_Y = new double[500];
	private double[] camada_Z = new double[500];
	private double[] camada_Training = new double[500];

	private int numNeuronsCamadaX = 0; //Nº de neurônios na camada X (i)
	private int numNeuronsCamadaZ = 0; //Nº de neurônios na camada Z (j)
	private int numNeuronsCamadaY = 0; //Nº de neurônios na camada Y (k)

	private double[][] peso_W = new double[500][500];
	private double[][] peso_V = new double[500][500];
	private double[][] peso_dW = new double[500][500];
	private double[][] peso_dV = new double[500][500];	
	private double[] peso_dK = new double[500];
	private double[] peso_dJ = new double[500];
	private double[] peso_dJ_in = new double[500];

	private double alpha = 0.1; //Learning Rate
	private double erroLimite = 0.05;
	private int ciclos = 1000;

	private FuncaoAtivacao ativacao;
  /**
   * RedeNeural constructor comment.
   */
  public RedeNeural() {
    super();
  }
  /**
   * RedeNeural constructor comment.
   */
  public RedeNeural(int numNeuronsCamadaX, int numNeuronsCamadaZ, int numNeuronsCamadaY, double learningRate, double erro, int maxCiclos) 
  {
    super();

    ativacao = new FuncaoAtivacao();
	
    //Número de neurônios na camada X
    this.numNeuronsCamadaX = numNeuronsCamadaX;
    //Número de neurônios na camada Z
    this.numNeuronsCamadaZ = numNeuronsCamadaZ;
    //Número de neurônios na camada Y
    this.numNeuronsCamadaY = numNeuronsCamadaY;
    //Learning Rate
    alpha = learningRate;
    //Erro limite
    erroLimite = erro;
    //Ciclos
    ciclos = maxCiclos;

    //Inicializar variáveis
    for(int l=0;l<200;l++)
    {
      camada_X_in[l] = 0;
      camada_Y_in[l] = 0;
      camada_Z_in[l] = 0;
      camada_X[l] = 0; 
      camada_Y[l] = 0;
      camada_Z[l] = 0;
      camada_Training[l] = 0;
		
      peso_dJ[l] = 0;
      peso_dJ_in[l] = 0;
      peso_dK[l] = 0;

      for(int m=0;m<200;m++)
      {
        peso_W[l][m]=0;
        peso_V[l][m]=0;
        peso_dW[l][m]=0;
        peso_dV[l][m]=0;			
      }
    }
    inicializarPesos();
  }

  /**
   * Insert the method's description here.
   * Creation date: (28/09/01 09:47:33)
   */
  public void inicializarPesos() 
  {
		System.out.println("Inicializando pesos...");

    int x = 0, y = 0, z = 0;
	
    //***Numeros de Pesos por Camada***
    //Camada X-Z
    //int iNumPesosW = (numNeuronsCamadaX + 1)*(numNeuronsCamadaZ);
    //Camada Z-Y
    //int iNumPesosV = (numNeuronsCamadaZ + 1)*(numNeuronsCamadaY);
		
		//************************************************************
		//Código utilizado quando existir pesos previamente calculados
		//Pesos pesos = new Pesos();
		//double[][] pesoXZ = pesos.getPesoXZ();
		//double[][] pesoZY = pesos.getPesoZY();
		//************************************************************

    //Camada X-Z
    for(x=0;x<=numNeuronsCamadaX;x++)
    {
      for(z=1;z<=numNeuronsCamadaZ;z++)
      {
        //peso_W[x][z] = pesoXZ[x][z];
        peso_W[x][z] = getNumeroRandomico();
      }
    }	
    //Camada Z-Y
    for(z=0;z<=numNeuronsCamadaZ;z++)
    {
      for(y=1;y<=numNeuronsCamadaY;y++)
      {
        //peso_V[z][y] = pesoZY[z][y];
        peso_V[z][y] = getNumeroRandomico();
      }
    }
    System.out.println("Pesos inicializados com sucesso!!!");
  }

  public boolean treinarRedeNeural(double input[][], double output[][]) 
  {
    int x = 0, y = 0, z = 0;
    double[] erro = new double[numNeuronsCamadaY+1];
    double erroTotal = 0.0D;
    double erroTotalFinal = 0.0D;
    int counter = 0;

    do
    {				
			for(int index=0;index<input.length;index++)		
			{
				double[] entrada = input[index];
				double[] saida = output[index];
				
		    for(int m=1;m<=numNeuronsCamadaX;m++)
		    {
		      camada_X[m]=entrada[m];
		    }	
		
		    for(int n=1;n<=numNeuronsCamadaY;n++)
		    {
		      camada_Training[n]=saida[n];
		    }		
		
	      //Cálculo na Primeira Camada
	      for(z=1;z<=numNeuronsCamadaZ;z++)
	      {
	        camada_Z_in[z] = camada_Z_in[z] + peso_V[0][z];
				
	        for(x=1;x<=numNeuronsCamadaX;x++)
	        {
	          camada_Z_in[z] = camada_Z_in[z] + camada_X[x]*peso_V[x][z];
	        }
				
	        camada_Z[z] = ativacao.chamarFuncaoAtivacao(camada_Z_in[z]);			
	      }
	
	      //Cálculo na Segunda Camada
	      for(y=1;y<=numNeuronsCamadaY;y++)
	      {
	        camada_Y_in[y] = camada_Y_in[y] + peso_W[0][y];
						
	        for(z=1;z<=numNeuronsCamadaZ;z++)
	        {
	          camada_Y_in[y] = camada_Y_in[y] + camada_Z[z]*peso_W[z][y];
	        }
						
	        camada_Y[y] = ativacao.chamarFuncaoAtivacao(camada_Y_in[y]);								
	      }
	
	      //***Backpropagation of Error***
	
	      //Y >>> Z
	      for(y=1;y<=numNeuronsCamadaY;y++)
	      {
	        peso_dK[y] = (camada_Training[y] - camada_Y[y])*ativacao.chamarDFuncaoAtivacao(camada_Y_in[y]);
	        peso_dW[0][y] = (alpha)*(peso_dK[y]);
						
	        for(z=1;z<=numNeuronsCamadaZ;z++)
	        {
	          peso_dW[z][y] = (alpha)*(peso_dK[y])*(camada_Z[z]);
	        }
	      }
	
	      //Z >>> X
	      for(z=1;z<=numNeuronsCamadaZ;z++)
	      {
	        for(y=1;y<=numNeuronsCamadaY;y++)
	        {
	          peso_dJ_in[z] = peso_dJ_in[z] + (peso_dK[y])*(peso_W[z][y]);
	        }
	
	        peso_dJ[z] = peso_dJ_in[z]*ativacao.chamarDFuncaoAtivacao(camada_Z_in[z]);
	        peso_dV[0][z] = (alpha)*(peso_dJ[z]);
						
	        for(x=1;x<=numNeuronsCamadaX;x++)
	        {
	          peso_dV[x][z] = (alpha)*(peso_dJ[z])*(camada_X[x]);
	        }
	
	      }
	
	      //***Atualização dos Pesos***
	      for(z=0;z<=numNeuronsCamadaZ;z++)
	      {
	        for(y=1;y<=numNeuronsCamadaY;y++)	
	        {
	          peso_W[z][y] = peso_W[z][y] + peso_dW[z][y];	
	        }
	      }
					
	      for(x=0;x<=numNeuronsCamadaX;x++)	
	      {		
	        for(z=1;z<=numNeuronsCamadaZ;z++)
	        {
	          peso_V[x][z] = peso_V[x][z] + peso_dV[x][z];
	        }		
	      }
	
	      //***Reinicializar as variáveis***//
	      for(int p=0;p<100;p++)
	      {
	        camada_X_in[p]=0;
	        camada_Y_in[p]=0;
	        camada_Z_in[p]=0;
									
	        peso_dJ[p]=0;
	        peso_dJ_in[p]=0;
	        peso_dK[p]=0;
	
	        for(int q=0;q<100;q++)
	        {
	          peso_dW[p][q]=0;
	          peso_dV[p][q]=0;				
	        }
	      }	
			}
			    
      //***Testar condições de parada e calcular o erro total quadrático***//
      erroTotal = 0;
      erroTotalFinal = 0;

      for(y=1;y<=numNeuronsCamadaY;y++)
      {
        erro[y] = (camada_Training[y] - camada_Y[y]);
        erroTotal = erroTotal + erro[y]*erro[y];
      }
		
      erroTotalFinal = Math.sqrt(erroTotal);     

      counter++;
      
      if(counter%100==0)
      	System.out.println(erroTotalFinal);

    }while((erroTotalFinal > erroLimite) && counter < ciclos);

    System.out.println("Erro: " + erroTotalFinal + " - Ciclos: " + counter);
			
    return true;
  }
  
  /**
   * Insert the method's description here.
   * Creation date: (28/09/01 11:08:07)
   */
  public double[] usarRedeNeural(double entrada[])
  {
    int x = 0, y = 0, z = 0;

    //Inicializar neurônios
    for(int m=1;m<=numNeuronsCamadaX;m++)
    {
      camada_X[m]=entrada[m];
    }	

    // Inicializar inputs do neurônios
    for(int p=0;p<100;p++)
    {
      camada_X_in[p]=0;
      camada_Y_in[p]=0;
      camada_Z_in[p]=0;
    }    
	
    //Cálculo na Primeira Camada
    for(z=1;z<=numNeuronsCamadaZ;z++)
    {
      camada_Z_in[z] = camada_Z_in[z] + peso_V[0][z];		
      for(x=1;x<=numNeuronsCamadaX;x++)
      {
        camada_Z_in[z] = camada_Z_in[z] + (camada_X[x])*(peso_V[x][z]);
      }					
      camada_Z[z] = ativacao.chamarFuncaoAtivacao(camada_Z_in[z]);
    }
	
    //Cálculo na Segunda Camada
    for(y=1;y<=numNeuronsCamadaY;y++)
    {
      camada_Y_in[y] = camada_Y_in[y] + peso_W[0][y];
      for(z=1;z<=numNeuronsCamadaZ;z++)
      {
        camada_Y_in[y] = camada_Y_in[y] + (camada_Z[z])*(peso_W[z][y]);
      }
      camada_Y[y] = ativacao.chamarFuncaoAtivacao(camada_Y_in[y]);
    }	

    return camada_Y;
  }

  private double getNumeroRandomico()
  {
    Random rnd = new Random(System.currentTimeMillis());
    double ret = rnd.nextDouble();
    rnd = null;
    try
    {
      Thread.sleep(10);
    }
    catch(InterruptedException iex)
    {
      iex.printStackTrace();
    }

    int sinal;
    String s = Double.toString(ret);
    s.trim();
    int digito = Integer.parseInt(s.substring(s.length()-1,s.length()));
    if(digito%2==0)
      sinal = -1;
    else
      sinal = +1;          
    return sinal*ret/100;
  }
  
  public void exibirPesos(String path)
  {
  	LogWriter log = new LogWriter();
  	log.open(path);
    //Camada X-Z
    log.write("// Pesos Camada X-Z");    
    log.write("double[][] pesoXZ = new double[" + (numNeuronsCamadaX+1) + "][" + (numNeuronsCamadaZ+1) + "];");
    for(int x=0;x<=numNeuronsCamadaX;x++)
    {		
      for(int z=1;z<=numNeuronsCamadaZ;z++)
      {
        log.write("pesoXZ[" + x + "][" + z + "] = " + peso_W[x][z] + ";");
      }
      log.write("");
    }	
    //Camada Z-Y
    log.write("// Pesos Camada Z-Y");
    log.write("double[][] pesoZY = new double[" + (numNeuronsCamadaZ+1) + "][" + (numNeuronsCamadaY+1) + "];");    
    for(int z=0;z<=numNeuronsCamadaZ;z++)
    {
      for(int y=1;y<=numNeuronsCamadaY;y++)
      {
        log.write("pesoZY[" + z + "][" + y + "] = " + peso_V[z][y] + ";");
      }
      log.write("");      
    }  
    log.close();
  }  
}
