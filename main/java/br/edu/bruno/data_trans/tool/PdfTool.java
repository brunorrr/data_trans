package br.edu.bruno.data_trans.tool;

import java.io.File;
import java.io.Serializable;

/**
 * Interface Biblioteca PDF Tool
 * 
 * Com essa interface você poderá utilizar a biblioteca de conversão de Objetos em PDFs, para usa-la,
 * tenha as classes beans com as anotações Java Bind, e um documento XSLT que servirá como marcador para os itens,
 * 
 * @author Bruno Ricci
 * @version 1.0.0
 * @see javax.xml.bind.annotation
 * 
 */
public interface PdfTool {

	/**
	 * Gera o PDF baseado em uma bean serializável
	 * 
	 * @version 1.0
	 * @param dataFile Bean a ser convertida
	 * @return Objeto do tipo File com o PDF gerado
	 * @throws Exception Exceções internas do processamento
	 */
	public < T extends Serializable > File generatePdf( T dataFile ) throws Exception ;
	
}
