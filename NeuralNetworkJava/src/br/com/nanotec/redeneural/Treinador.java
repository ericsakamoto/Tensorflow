package br.com.nanotec.redeneural;

public class Treinador 
{
  public Treinador()
  {
  }

  public static void main(String[] args)
  {
    Treinador treinador = new Treinador();
    treinador.treinarRede();
  }

  public void treinarRede()
  {
		int camada_x = 30;
		int camada_z = 30;
		int camada_y = 4;		
		double learningRate = 0.2D;
  	double erro = 0.01D;
    RedeNeural rn = new RedeNeural(camada_x,camada_z,camada_y,learningRate,erro,10000);

    double[][] entrada = 
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
    /*
    {{0,1,0},                          
 	   {0,0,0},
	   {0,0,1},
	   {0,1,1}};
	  */
    /*
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
    */

    double[][] saida = 
      {{0,0,0,0,1},
       {0,0,0,1,0},
       {0,0,0,1,1},
       {0,0,1,0,0},
       {0,0,1,0,1},
       {0,0,1,1,0},
       {0,0,1,1,1},
       {0,1,0,0,0},
       {0,1,0,0,1},
       {0,0,0,0,0}};    
    /*
    {{0,1},
	   {0,0},
     {0,1},
     {0,1}};
    */
    /*
      {{0,0,0,0,1},
       {0,0,0,1,0},
       {0,0,0,1,1},
       {0,0,1,0,0},
       {0,0,1,0,1},
       {0,0,1,1,0},
       {0,0,1,1,1},
       {0,1,0,0,0},
       {0,1,0,0,1},
       {0,0,0,0,0}};
    */

    rn.treinarRedeNeural(entrada,saida);
		
		//rn.exibirPesos();
		
    for(int indice = 0;indice<entrada.length;indice++)
    {
	    StringBuffer sb = new StringBuffer();
	    
	    sb.append("Entrada: ");
	    for(int i=1;i<entrada[indice].length;i++)
	    {
	     	sb.append(new Double(entrada[indice][i]).intValue());
	     	sb.append(" ");
	    }
	    
	    double[] ret = rn.usarRedeNeural(entrada[indice]);
	    
	    sb.append("=");
	    sb.append(" ");
	    for(int j=1;j<5;j++)    
	    {
	    	double valor = ret[j] + erro;
	    	if(valor>=1)
		     	sb.append("1");
	    	else
		    	sb.append("0");
	     	sb.append(" ");	    	
	    }
	    System.out.println(sb.toString().trim());              
	    sb = null;
    }


		/*
    int indice = 0;
    System.out.print("Entrada: " + new Double(entrada[indice][1]).intValue() + " " + new Double(entrada[indice][2]).intValue());
    double[] ret = rn.usarRedeNeural(entrada[indice]);
    System.out.print(" = " + Math.round(ret[1]) + "\n");              

    indice = 1;
    System.out.print("Entrada: " + new Double(entrada[indice][1]).intValue() + " " + new Double(entrada[indice][2]).intValue());
    ret = rn.usarRedeNeural(entrada[indice]);
    System.out.print(" = " + Math.round(ret[1]) + "\n");

    indice = 2;
    System.out.print("Entrada: " + new Double(entrada[indice][1]).intValue() + " " + new Double(entrada[indice][2]).intValue());
    ret = rn.usarRedeNeural(entrada[indice]);
    System.out.print(" = " + Math.round(ret[1]) + "\n");

    indice = 3;
    System.out.print("Entrada: " + new Double(entrada[indice][1]).intValue() + " " + new Double(entrada[indice][2]).intValue());
    ret = rn.usarRedeNeural(entrada[indice]);
    System.out.print(" = " + Math.round(ret[1]) + "\n");
    */
  }
}
