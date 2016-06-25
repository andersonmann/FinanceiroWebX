package br.com.sisnema.financeiroweb.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.sisnema.financeiroweb.model.Conta;
import br.com.sisnema.financeiroweb.model.Usuario;

public class ContaDAO extends DAO<Conta> {

	public Conta obterPorId(Conta filtro) {
		return getSession().get(Conta.class, filtro.getCodigo());
	}

	public List<Conta> pesquisar(Conta filtros) {
		Criteria criteria = getSession().createCriteria(Conta.class); 
		
		if(filtros.getUsuario() != null){
			criteria.add(Restrictions.eq("usuario", filtros.getUsuario()));
		}
		
		criteria.addOrder(Order.asc("descricao"));
		
		return criteria.list();
	}
	
	public Conta buscarFavorita(Usuario u){
		Criteria criteria = getSession().createCriteria(Conta.class); 
		criteria.add(Restrictions.eq("usuario", u));
		criteria.add(Restrictions.eq("favorita", true));
		
		return (Conta) criteria.uniqueResult();
	}

}












