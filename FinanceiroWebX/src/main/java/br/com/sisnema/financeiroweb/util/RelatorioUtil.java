package br.com.sisnema.financeiroweb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.sisnema.financeiroweb.exception.UtilException;
import br.com.sisnema.financeiroweb.model.BaseEntity;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class RelatorioUtil {

	public static final int	RELATORIO_PDF					= 1;
	public static final int	RELATORIO_EXCEL					= 2;
	public static final int	RELATORIO_HTML					= 3;
	public static final int	RELATORIO_PLANILHA_OPEN_OFFICE	= 4;

	public StreamedContent geraRelatorio(HashMap<String, Object> parametrosRelatorio, String nomeRelatorioJasper, String nomeRelatorioSaida,
										 int tipoRelatorio, List<? extends BaseEntity> colecao) throws UtilException {
		
		StreamedContent arquivoRetorno = null;

		try {
			
			
			String caminhoRelatorio = FacesContext.getCurrentInstance()
												  .getExternalContext()
												  .getRealPath("/resources/reports");
			
			String pathImg = FacesContext.getCurrentInstance()
										 .getExternalContext()
										 .getRealPath("/resources/imagens");
			
			parametrosRelatorio.put("pathImg", pathImg);
			
			String arquivoJasper = caminhoRelatorio + File.separator + nomeRelatorioJasper + ".jasper";


			String caminhoArquivoRelatorio = caminhoRelatorio + File.separator + 
										     nomeRelatorioSaida + ".pdf";
			
			JasperPrint impressoraJasper = null;
			if(colecao == null || colecao.isEmpty()){
				Connection conexao = this.getConexao();
				impressoraJasper = JasperFillManager.fillReport(arquivoJasper, parametrosRelatorio, conexao);
				conexao.close();
				
			} else {
				JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(colecao);
				impressoraJasper = JasperFillManager.fillReport(arquivoJasper, parametrosRelatorio, ds);
			}

			JasperExportManager.exportReportToPdfFile(impressoraJasper, caminhoArquivoRelatorio);

			InputStream conteudoRelatorio = new FileInputStream(caminhoArquivoRelatorio);
			arquivoRetorno = new DefaultStreamedContent(conteudoRelatorio, "application/pdf" , nomeRelatorioSaida + ".pdf" );
		} catch (JRException e) {
			throw new UtilException("N�o foi poss�vel gerar o relat�rio.", e);
			
		} catch (FileNotFoundException e) {
			throw new UtilException("Arquivo do relat�rio n�o encontrado.", e);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arquivoRetorno;
	}
	
	private Connection getConexao() throws UtilException, SQLException {
		return ConnectionFactory.getConnectionPostgres();
	}
	
}
