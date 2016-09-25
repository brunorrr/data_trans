package br.edu.bruno.data_trans.test;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.edu.bruno.data_trans.PdfToolFactory;
import br.edu.bruno.data_trans.test.model.Aluno;
import br.edu.bruno.data_trans.test.model.Turma;
import br.edu.bruno.data_trans.tool.PdfTool;

public class TesteFactory {

	public static void main(String[] args) throws Exception {
		PdfToolFactory fabrica = PdfToolFactory.getInstance();
		PdfTool pdfTool = fabrica.newPdfTool( new File( "src/test/java/view/1.xsl" ) );
		
		Aluno a = new Aluno();
		a.setNome("Altair");
		a.setMatricula(1);
		a.setDataNascimento( new Date() );
		
		Aluno b = new Aluno();
		b.setNome("Ezio");
		b.setMatricula(2);
		b.setDataNascimento( new SimpleDateFormat("dd/MM/yyyy").parse("01/02/1970") );
		
		Aluno c = new Aluno();
		c.setNome("Carl");
		c.setMatricula(465);
		c.setDataNascimento( new SimpleDateFormat("dd/MM/yyyy").parse("15/12/1965") );
		
		Aluno d = new Aluno();
		d.setNome("Niko");
		d.setMatricula(500);
		d.setDataNascimento( new SimpleDateFormat("dd/MM/yyyy").parse("25/03/1980") );
		
		Aluno e = new Aluno();
		e.setNome("Bruno");
		e.setMatricula(1980);
		e.setDataNascimento( new SimpleDateFormat("dd/MM/yyyy").parse("24/10/1995") );
		
		List<Aluno> lista = new ArrayList<Aluno>();
		lista.add( a );
		lista.add( b );
		lista.add( c );
		lista.add( d );
		lista.add( e );
		
		Turma t = new Turma();
		t.setNome( "Uma turminha do barulho" );
		t.setAlunos( lista );
		
		File file = pdfTool.generatePdf( t );
		
//		Runtime.getRuntime().exec( "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe ".concat( file.getAbsolutePath() ) );
	}
	
}
