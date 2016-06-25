package br.com.sisnema.financeiroweb.negocio;

import br.com.sisnema.financeiroweb.dao.ContaDAO;
import br.com.sisnema.financeiroweb.exception.DAOException;
import br.com.sisnema.financeiroweb.exception.RNException;
import br.com.sisnema.financeiroweb.model.Conta;
import br.com.sisnema.financeiroweb.model.Usuario;

public class ContaRN extends RN<Conta> {

	public ContaRN() {
		super(new ContaDAO());
	}
	
	public void salvar(Conta model) throws RNException {
		try {
			dao.salvar(model);
		} catch (DAOException e) {
			throw new RNException("Não foi possível salvar a Conta. Erro: "+e.getMessage(), e);
		}
	}
	
	public Conta buscarFavorita(Usuario u){
		return ((ContaDAO) dao).buscarFavorita(u);
	}
	
	public void tornarFavorita(Conta contaDaTela) throws RNException{
		Conta contaFavorita = buscarFavorita(contaDaTela.getUsuario());
		
		if(contaFavorita != null ){
				
			if(contaFavorita.getCodigo().equals(contaDaTela.getCodigo())){
				return;
			}
			
			contaFavorita.setFavorita(false);
		}
		
		contaDaTela.setFavorita(true);
		
		salvar(contaDaTela);
	}
}





















