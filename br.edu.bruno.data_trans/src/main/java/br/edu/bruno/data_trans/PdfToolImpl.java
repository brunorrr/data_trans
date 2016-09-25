package br.edu.bruno.data_trans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.xml.sax.SAXException;

import br.edu.bruno.data_trans.tool.PdfTool;

/**
 *
 * Primeira implementação da interface de criação de PDFs
 * 
 * @author Bruno Ricci
 * @version 1.0.1
 *
 */
public class PdfToolImpl implements Serializable, PdfTool {

	public static final long serialVersionUID = -8710763667615329435L;

	private static Log log = LogFactory.getLog(PdfToolImpl.class);

	private final File xsltFile;
	private final File localizacaoPdfTemp;
	private final File localizacaoXmlTemp;
	private final Random random = new Random();
	private final FopFactory fopFactory;
	private final FOUserAgent foUserAgent;
	private final TransformerFactory factory;

	PdfToolImpl( File xsltFile, File localizacaoPdfTemp, File localizacaoXmlTemp ) throws SAXException, IOException {
		this.xsltFile = xsltFile;
		this.localizacaoPdfTemp = localizacaoPdfTemp;
		this.localizacaoXmlTemp = localizacaoXmlTemp;

		factory = TransformerFactory.newInstance();
		fopFactory = FopFactory.newInstance( new File(".").toURI() );
		foUserAgent = fopFactory.newFOUserAgent();
	}

	public <T extends Serializable> File generatePdf(T dataFile) throws Exception {
		log.debug( "Reading data from ".concat( dataFile.getClass().getName() ) );

		String dataFileHash = Integer.toString( dataFile.hashCode() );
		StringBuffer fileName = new StringBuffer();
		fileName.append( dataFileHash.substring( dataFileHash.length() - 3 ) );
		fileName.append( new BigInteger(130, random).toString(10) );

		log.debug( "Converting Object to XML file named ".concat( fileName.toString() ) );
		File xmlFile = objectoToXml( dataFile , fileName.toString() );

		log.debug( "Converting XSL-FO to PDF file named ".concat( fileName.toString() ) );
		File pdfFile = foToPdf( xmlFile, fileName.toString() );
		return pdfFile;
	}

	private <T extends Serializable> File objectoToXml( T dataFile, String name ) throws Exception{
		log.debug( "Converting Object ".concat( dataFile.toString() ).concat( " to XML with name ".concat( name ) ) );
		try {
			//Obtendo JABX baseado no objeto
			log.debug( "Getting JAXB Context" );
			JAXBContext context = JAXBContext.newInstance( dataFile.getClass() );

			//Criando conversor
			log.debug( "Creating Mashaller for convert file" );
			Marshaller m = context.createMarshaller();

			//Definindo propriedade para deixar o XML em um formato bonito
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			log.debug( "Setting marshaller file layout property to ".concat( m.getProperty( Marshaller.JAXB_FORMATTED_OUTPUT ).toString() ) );

			File arquivo = new File( localizacaoXmlTemp.getAbsolutePath().concat( File.separator ).concat( name ).concat(".xml") );

			m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			m.marshal( dataFile,  arquivo );
			log.info( "Creating XML file at ".concat( arquivo.getAbsolutePath() ) );

			StringWriter sw = new StringWriter();
			m.marshal(  dataFile , sw );
			log.debug( "XML result: ".concat( sw.toString() ) );

			return arquivo;
		} catch (JAXBException e) {
			log.error( "Convertion error", e );
			throw new Exception( e );
		}
	}

	private File foToPdf( File xml, String name ) throws Exception{
		try {
			File pdfFile = new File( localizacaoPdfTemp.getAbsolutePath().concat( File.separator ).concat( name ).concat( ".pdf" ) );
			log.debug( "Creating PDF File to ".concat( pdfFile.getAbsolutePath() ) );

			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, new FileOutputStream( pdfFile ) );
			Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

			Result res = new SAXResult(fop.getDefaultHandler());
			StreamSource xmlSource = new StreamSource( xml );
			transformer.transform(xmlSource, res);
			log.info( "Creating PDF file at ".concat( pdfFile.getAbsolutePath() ) );

			return pdfFile;
		} catch (Exception e) {
			log.error( "Converting to PDF error", e );
			throw new Exception( e );
		}
	}

}
