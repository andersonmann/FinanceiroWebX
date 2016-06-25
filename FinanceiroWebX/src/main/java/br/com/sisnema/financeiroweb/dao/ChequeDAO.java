package br.com.sisnema.financeiroweb.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.sisnema.financeiroweb.exception.DAOException;
import br.com.sisnema.financeiroweb.model.Cheque;

public class ChequeDAO extends DAO<Cheque> {

	public Cheque obterPorId(Cheque filtro) {
		return getSession().get(Cheque.class, filtro.getId());
	}

	public List<Cheque> pesquisar(Cheque filtros) {
		Criteria criteria = getSession().createCriteria(Cheque.class);
		
		criteria.add(Restrictions.eq("conta", filtros.getConta()));
		criteria.addOrder(Order.asc("id.numero"));
		
		return criteria.list();
	}
	
	@Override
	public void salvar(Cheque model) throws DAOException {
		getSession().saveOrUpdate(model);
	}

}