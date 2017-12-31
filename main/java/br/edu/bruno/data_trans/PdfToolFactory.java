package br.edu.bruno.data_trans;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import br.edu.bruno.data_trans.tool.PdfTool;

/**
 * Fábrica do gerador de PDF, é obrigatória a utilização do Padrão de Projeto <strong>Factory Method</strong>
 * pois o gerador precisa ter definido nele a localização dos arquivos temporários, isso é definido pela fábrica.
 * 
 * @author Bruno Ricci
 * @version 1.0.1
 *
 */
public final class PdfToolFactory implements Serializable {
	
	private static final long serialVersionUID = -5397412665648172268L;
	
	private static PdfToolFactory instancia;
	private static Log log = LogFactory.getLog(PdfToolFactory.class);
	
	private static final String KEY_LOCALIZACAO_XML_TEMP = "xmlTempLocation";
	private static final String KEY_LOCALIZACAO_TRANS_TEMP = "transTempLocation";
	
	private final File LOCALIZACAO_XML_TEMP;
	private final File LOCALIZACAO_TRANS_TEMP;
	
	private static final String LOCALIZACAO_PROPRIEDADES = "data_trans.properties";
	
	private Properties propriedades;

	private PdfToolFactory( String arquivoConfiguracao ) throws IOException{
		log.info("Creating a new instance for PdfToolFactory");
		//Leitura arquivo configuração
		log.debug( "Loading config file named ".concat( arquivoConfiguracao ) );
		
		propriedades = new Properties();
		propriedades.load( 
				getClass().getClassLoader().getResourceAsStream( 
						arquivoConfiguracao ) );
		log.debug( "Properties from config file loaded" );
		
		LOCALIZACAO_XML_TEMP = new File( propriedades.getProperty( KEY_LOCALIZACAO_XML_TEMP ) );
		if( !LOCALIZACAO_XML_TEMP.exists() || !LOCALIZACAO_XML_TEMP.isDirectory() )
			throw new IOException( "The XML directory does not exist or is not a directory" );
		
		LOCALIZACAO_TRANS_TEMP = new File( propriedades.getProperty( KEY_LOCALIZACAO_TRANS_TEMP ) );
		if( !LOCALIZACAO_TRANS_TEMP.exists() || !LOCALIZACAO_TRANS_TEMP.isDirectory() )
			throw new IOException( "The Transformed directory does not exist or is not a directory" );
		
		log.trace( "Property XML Temp Location: ".concat( LOCALIZACAO_XML_TEMP.getAbsolutePath() ) );
		log.trace( "Property Transformed Temp Location: ".concat( LOCALIZACAO_TRANS_TEMP.getAbsolutePath() ) );
	}
	
	/**
	 * Esta Fábrica utiliza o padrão Singleton para monopolizar o uso dos arquivos de configuração,
	 * 
	 * @return Uma instância da fábrica
	 * @throws IOException Arquivos de configurações não encontrados
	 */
	public static PdfToolFactory getInstance() throws IOException{
		log.debug("Getting a new instance of PdfToolFactory");
		if( instancia == null )
			instancia = new PdfToolFactory( LOCALIZACAO_PROPRIEDADES );
		return instancia;
	}
	
	/**
	 * Gera um criador de PDFs
	 * 
	 * @param xsltFile Arquivo XSLT com o design do PDF
	 * @return Gerador de PDF
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public PdfTool newPdfTool( File xsltFile ) throws SAXException, IOException{
		log.debug( "Generating Pdftool for XSL file at ".concat( xsltFile.getAbsolutePath() ) );
		return new PdfToolImpl( xsltFile, 
				LOCALIZACAO_TRANS_TEMP,
				LOCALIZACAO_XML_TEMP );
	}
	
}
