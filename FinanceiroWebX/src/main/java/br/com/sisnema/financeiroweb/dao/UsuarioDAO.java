package br.com.sisnema.financeiroweb.dao;

import java.util.List;

import javax.persistence.OptimisticLockException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.com.sisnema.financeiroweb.exception.DAOException;
import br.com.sisnema.financeiroweb.exception.LockException;
import br.com.sisnema.financeiroweb.model.Usuario;

public class UsuarioDAO extends DAO<Usuario> {

	public void salvar(Usuario model) throws DAOException {
		try {
			getSession().save(model);
		} catch (Exception e) {
			throw new DAOException("N�o foi poss�vel inserir o usu�rio. Erro: "+e.getMessage(),e);
		}
	}
	
	public void alterar(Usuario usuarioDaTela) throws DAOException {
		try {
			// obtemos um usuario do banco e colocamos na sessao do hibernate
			Usuario usuarioDoBanco = obterPorId(usuarioDaTela);
			usuarioDaTela.setPermissao(usuarioDoBanco.getPermissao());
			
			// removemos da sessao o objeto para n�o dar conflito
			// com o objeto a ser salvo proveniente do banco
			getSession().evict(usuarioDoBanco);
			
			getSession().update(usuarioDaTela);
			comitaTransacao();
			iniciaTransacao();
			
		} catch (OptimisticLockException ole){
			cancelaTransacao();
			iniciaTransacao();
			
			throw new LockException("Este registro acaba de ser atualizado por outro usu�rio. "
					+ "Refa�a a pesquisa", ole);
		} catch (Exception e) {
			throw new DAOException("N�o foi poss�vel alterar o usu�rio. Erro: "+e.getMessage(),e);
		}
	}


	public Usuario obterPorId(Usuario filtro) {
		return getSession().get(Usuario.class, filtro.getCodigo());
	}

	public List<Usuario> pesquisar(Usuario filtros) {
		// defimos a criteria basica (select * from Usuario)
		Criteria criteria = getSession().createCriteria(Usuario.class);
		
		if(StringUtils.isNotBlank(filtros.getNome())){
			criteria.add(Restrictions.ilike("nome", filtros.getNome(),MatchMode.ANYWHERE));
		}
		
		if(filtros.getNascimento() != null){
			criteria.add(Restrictions.eq("nascimento", filtros.getNascimento()));
		}
		
		criteria.addOrder(Order.asc("nome"));
		
		return criteria.list();
	}
	
	public Usuario buscarUsuarioPorLoginESenha(String login, String senha){
		Criteria criteria = getSession().createCriteria(Usuario.class, "usuario");

		// criamos um join na pesquisa retornando todas as permiss�es do usu�rio
		criteria.createAlias("usuario.permissao", "permissao", JoinType.INNER_JOIN);
		
		criteria.add(Restrictions.eq("login", login));
		criteria.add(Restrictions.eq("senha", senha));
		
		Usuario user = (Usuario) criteria.uniqueResult();
		
		// outra forma, seria ap�s a pesquisa, inicializar os atributos em quest�o
		if(user != null){
			Hibernate.initialize(user.getPermissao());
		}
		
		return user;
	}
	
	public Usuario buscarUsuarioPorLogin(String login){
		Criteria criteria = getSession().createCriteria(Usuario.class);
		criteria.add(Restrictions.eq("login", login));
		return (Usuario) criteria.uniqueResult();
	}
}














