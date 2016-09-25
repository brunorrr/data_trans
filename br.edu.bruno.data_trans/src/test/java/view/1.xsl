<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:barcode="org.krysalis.barcode4j.xalan.BarcodeExt" xmlns:common="http://exslt.org/common"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xalan="http://xml.apache.org"
	exclude-result-prefixes="barcode common xalan">
	<xsl:template name="formatarData">
		<xsl:param name="dateTime" />
		<xsl:variable name="datestr">
			<xsl:value-of select="substring-before($dateTime,'T')" />
		</xsl:variable>

		<xsl:variable name="mm">
			<xsl:value-of select="substring($datestr,6,2)" />
		</xsl:variable>

		<xsl:variable name="dd">
			<xsl:value-of select="substring($datestr,9,2)" />
		</xsl:variable>

		<xsl:variable name="yyyy">
			<xsl:value-of select="substring($datestr,1,4)" />
		</xsl:variable>

		<xsl:value-of select="concat($mm,'/', $dd, '/', $yyyy)" />
	</xsl:template>
	<xsl:template match="turma">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="simple"
					page-height="20cm" page-width="10.5cm" margin-left="0.5cm"
					margin-right="0.5cm">
					<fo:region-body margin-top="0.5cm" />
					<fo:region-before margin-left="3cm" margin-right="3cm"
						margin-top="1cm" />
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simple">
				<fo:flow flow-name="xsl-region-body">
					<fo:block text-align="center" font-family="Arial"
						font-size="13pt" font-weight="bold">
						Lista de Alunos por Turma
					</fo:block>
					<fo:block text-align="left" font-family="Arial" font-size="9pt"
						margin-top="0.5cm" margin-bottom=".6cm">
						Turma:
						<fo:inline font-weight="bold">
							<xsl:value-of select="nome" />
						</fo:inline>
					</fo:block>
					<fo:block font-family="Arial" font-size="7pt" font-weight="normal">
						<fo:table>
							<fo:table-column column-number="1" column-width="3cm" />
							<fo:table-column column-number="2" column-width="2cm" />
							<fo:table-column column-number="3" column-width="2cm" />
							<fo:table-header>
								<fo:table-row border="solid 0.1mm black">
									<fo:table-cell text-align="left" border="solid 0.1mm black">
										<fo:block>
											Nome
										</fo:block>
									</fo:table-cell>
									<fo:table-cell text-align="left" border="solid 0.1mm black">
										<fo:block>
											Matrícula
										</fo:block>
									</fo:table-cell>
									<fo:table-cell text-align="left" border="solid 0.1mm black">
										<fo:block>
											Data Nascimento
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-header>
							<fo:table-body height="10cm">
								<xsl:for-each select="./aluno">
									<fo:table-row border="solid 0.1mm black">

										<fo:table-cell text-align="left" border="solid 0.1mm black">
											<fo:block>
												<xsl:value-of select="nome" />
											</fo:block>

										</fo:table-cell>
										<fo:table-cell text-align="center" border="solid 0.1mm black">
											<fo:block>
												<xsl:value-of select="matricula" />
											</fo:block>

										</fo:table-cell>
										<fo:table-cell text-align="left" border="solid 0.1mm black">
											<fo:block>
												<xsl:call-template name="formatarData">
													<xsl:with-param name="dateTime" select="dataNascimento" />
												</xsl:call-template>
											</fo:block>

										</fo:table-cell>

									</fo:table-row>
								</xsl:for-each>

							</fo:table-body>
						</fo:table>
					</fo:block>

				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>