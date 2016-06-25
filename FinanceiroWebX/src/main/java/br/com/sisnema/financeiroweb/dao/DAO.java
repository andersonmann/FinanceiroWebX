package br.com.sisnema.financeiroweb.dao;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;

import org.hibernate.Session;

import br.com.sisnema.financeiroweb.exception.DAOException;
import br.com.sisnema.financeiroweb.exception.LockException;
import br.com.sisnema.financeiroweb.util.JPAUtil;

/**
 * Classe abstrata que herdara os comportamentos de {@link IDAO} e conterá atributos 
 * e funcionalidades genéricas a todas as filhas 
 */
public abstract class DAO<T> implements IDAO<T> {

	/**
	 * Como todas as DAOS irão possuir uma sessao, criaremos a mesma
	 * na classe pai, sendo ela HERDADA pelas filhas....
	 */
    protected EntityManager em;
	
	/**
	 * Método construtor de DAO para INICIALIZAR a sessao
	 * do hibernate
	 */
	public DAO() {
		em = JPAUtil.getEntityManager();
	}
	
	public void salvar(T model) throws DAOException {
		try {
			getSession().saveOrUpdate(model);
			comitaTransacao();
			iniciaTransacao();
			
		} catch (OptimisticLockException ole){
			cancelaTransacao();
			iniciaTransacao();
			
			throw new LockException("Este registro acaba de ser atualizado por outro usuário. "
					+ "Refaça a pesquisa", ole);
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
	
	public void excluir(T model) throws DAOException {
		try {
			getSession().delete(model);
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
	
    protected final Session getSession() {
    	if(!em.isOpen()){
    		em = JPAUtil.getEntityManager();
    	}
    	return (Session) em.unwrap(Session.class);
    }
    
    public final void comitaTransacao() {
    	getSession().getTransaction().commit();
	}
    
    public final void cancelaTransacao() {
    	JPAUtil.getEntityManager().getTransaction().rollback();
    }
    
    protected final void iniciaTransacao() {
    	JPAUtil.getEntityManager().getTransaction().begin();
    }
    
    public void flushAndClear(){
    	getSession().flush();
    	getSession().clear();
    }
}






