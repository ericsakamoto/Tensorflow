package br.com.nanotec.reconhecimentoescrita;
/**
 * @author eric.sakamoto
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class TelaFactory
{
	public static final int CARACTER = 1;
	public static final int GRAFICO = 2;
	
	public TelaFactory()
	{
		
	}
	
	public TelaInterface getTela(int tipo)
	{
		switch(tipo)	
		{
			case CARACTER:
				return new TelaCaracter();
			case GRAFICO:	
				return new TelaGrafico();		
			default:
				return new TelaGrafico();
		}
	}
}
