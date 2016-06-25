package br.com.sisnema.financeiroweb.negocio;

import java.util.List;

import br.com.sisnema.financeiroweb.dao.IDAO;
import br.com.sisnema.financeiroweb.exception.DAOException;
import br.com.sisnema.financeiroweb.exception.RNException;

/**
 * Classe Pai de todas as classes de negócio
 * 
 * @param <T> - Entidade a ser persistida/pesquisada
 */
public abstract class RN<T> implements IRN<T> {

	/**
	 * Atributo generico a TODAS as RNs para operações
	 * padrões às DAOs
	 */
	protected final IDAO<T> dao;
	
	/**
	 * Construtor para inicializar a instancia da DAO
	 */
	public RN(IDAO<T> dao) {
		super();
		this.dao = dao;
	}
	
	public void excluir(T model) throws RNException {
		try {
			dao.excluir(model);
		} catch (DAOException e) {
			throw new RNException(e.getMessage(), e);
		}
	}

	public T obterPorId(T filtro) {
		return dao.obterPorId(filtro);
	}

	public List<T> pesquisar(T filtros) {
		return dao.pesquisar(filtros);
	}
}








