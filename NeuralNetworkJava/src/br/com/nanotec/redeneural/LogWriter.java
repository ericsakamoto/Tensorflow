package br.com.nanotec.redeneural;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author eric.sakamoto
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class LogWriter
{
	private RandomAccessFile hFile;
	
	public LogWriter()
	{

	}
	
	public void open(String path)
	{
		try
		{
			this.hFile = new RandomAccessFile(path,"rw");	
		}
		catch(FileNotFoundException fnfex)
		{
			fnfex.printStackTrace();	
		}
	}
	
	public void write(String texto)
	{
		try
		{
			hFile.write(texto.getBytes());		
			hFile.write("\n".getBytes());
		}
		catch(IOException fnfex)
		{
			fnfex.printStackTrace();	
		}			
	}	
	
	public void close()
	{
		try
		{
			this.hFile.close();
		}
		catch(IOException fnfex)
		{
			fnfex.printStackTrace();	
		}		
	}	
}
