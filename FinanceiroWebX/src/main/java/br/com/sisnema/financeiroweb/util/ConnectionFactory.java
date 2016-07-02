package br.com.sisnema.financeiroweb.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsavel por retornar conex�es com o banco de dados
 */
public class ConnectionFactory {

	/**
	 * M�todo que retorna uma conex�o ativa com 
	 * o banco de dados
	 * 
	 * @param prmBanco - Contem os parametros necess�rios 
	 * 					 para se abrir uma conex�o: 
	 * 					 URL, USER, PASS
	 * 
	 * @return - Conex�o com o Banco de dados
	 * @author <a href='mailto:contato@sisnema.com.br'>Sisnema Inform�tica</a>
	 * @since 16/08/2012
	 * @throws SQLException
	 */
	public static Connection getConnectionMysql() throws SQLException{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			return DriverManager.getConnection( "jdbc:mysql://localhost:3306/financeiroweb",
												"root",
												"sisnema"
											  );
			
		} catch (ClassNotFoundException e) {
			System.out.println("Driver mapeado incorretamente.");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Connection getConnectionPostgres() throws SQLException{
		try {
			Class.forName("org.postgresql.Driver");
			
			return DriverManager.getConnection( "jdbc:postgresql:financeiroweb",
					"postgres",
					"postgres"
					);
			
		} catch (ClassNotFoundException e) {
			System.out.println("Driver mapeado incorretamente.");
			e.printStackTrace();
		}
		
		return null;
	}
}
