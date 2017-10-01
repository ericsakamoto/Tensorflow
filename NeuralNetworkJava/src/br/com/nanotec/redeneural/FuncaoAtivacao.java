package br.com.nanotec.redeneural;

/**
 * Insert the type's description here.
 * Creation date: (28/09/01 13:51:29)
 * @author: Administrator
 */
public class FuncaoAtivacao 
{
  /**
   * FuncaoAtivacao constructor comment.
   */
  public FuncaoAtivacao() 
  {
    super();
  }
  //***Derivada da Função Ativação***
  public double chamarDFuncaoAtivacao(double in_Param)
  {
    double ret = 0;
    double ativacao = 0;

    ativacao = chamarFuncaoAtivacao(in_Param);
    ret = ativacao*(1-ativacao);

    return ret;
  }
  //***Função Ativação***
  public double chamarFuncaoAtivacao(double in_Param)
  {
    double ret = 0;

    //Binary Sigmoid
    ret = (1)/(1+Math.exp(-(in_Param)));

    return ret;
  }
}
