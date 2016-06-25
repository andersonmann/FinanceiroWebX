package br.com.sisnema.financeiroweb.negocio;

import br.com.sisnema.financeiroweb.dao.ChequeDAO;
import br.com.sisnema.financeiroweb.domain.SituacaoCheque;
import br.com.sisnema.financeiroweb.exception.DAOException;
import br.com.sisnema.financeiroweb.exception.RNException;
import br.com.sisnema.financeiroweb.model.Cheque;
import br.com.sisnema.financeiroweb.model.ChequeId;
import br.com.sisnema.financeiroweb.model.Conta;
import br.com.sisnema.financeiroweb.model.Lancamento;


/**
 * Classe que efetuara as regras de negocio de cheque
 */
public class ChequeRN extends RN<Cheque> {
	
	public ChequeRN() {
		super(new ChequeDAO());
	}


	@Override
	public void excluir(Cheque cheque) throws RNException {
		// Só permite excluir cheques que estejam com a situação
		// Não emitido
		if (SituacaoCheque.NAO_EMITIDO == cheque.getSituacao()){
			try {
				dao.excluir(cheque);
			} catch (DAOException e) {
				throw new RNException(e);
			}
		} else {
			throw new RNException("Não é possível excluir cheque, status não permitido para operação.");
		}
	}
	

	/**
	 * Método que salva uma sequencia de cheques (inicial e final)
	 */
	public int salvarSequencia(Conta conta, Integer chequeInicial, Integer chequeFinal) throws RNException {
		
		Cheque cheque = null;
		Cheque chequeVerifica = null;
		ChequeId chequeId = null;
		int contaTotal = 0;
		
		// Loop para salvar cheques de: chequeInicial até: chequeFinal
		for (int i = chequeInicial; i <= chequeFinal; i++) {
			chequeId = new ChequeId(i, conta.getCodigo());
			
			chequeVerifica = obterPorId(new Cheque(chequeId));
			
			//Só insere caso o cheque não existe
			if (chequeVerifica == null) {
				cheque = new Cheque(chequeId);
				
				// Situação inicial: Não emitido
				cheque.setSituacao(SituacaoCheque.NAO_EMITIDO);
				salvar(cheque);
				// Váriavel que vai informar ao final do processo
				// quantos cheques realmente foram inseridos,
				// eliminando-se os duplicados.
				contaTotal++;
			}
		}
		
		return contaTotal;
	}

	
	public void cancelarCheque(Cheque cheque) throws RNException {
		
		// Só é possível cancelar cheques que não tenham sido emitidos
		if (SituacaoCheque.NAO_EMITIDO == cheque.getSituacao()){
			try {
				cheque.setSituacao(SituacaoCheque.CANCELADO);
				dao.salvar(cheque);
			} catch (DAOException e) {
				throw new RNException(e);
			}
		} else {
			throw new RNException("Não é possível cancelar cheque, status não permitido para operação.");
		}
	}
	
	/**
	 * Método chamado ao se UTILIZAR um cheque para um lançamento
	 */
	public void baixarCheque(ChequeId chequeId, Lancamento lancamento) throws RNException {
		Cheque chequeAux = new Cheque(chequeId);
		Cheque cheque = obterPorId(chequeAux);
		
		if (cheque != null) {
			try {
				// Baixar o cheque == alterar a situacao de Não Emitido para Baixado
				cheque.setSituacao(SituacaoCheque.BAIXADO);
				cheque.setLancamento(lancamento);
				dao.salvar(cheque);
			} catch (DAOException e) {
				throw new RNException(e);
			}
		}
	}
	
	/**
	 * AO se alterar um lancamento e tirar o cheque
	 * deve-se passar o cheque para não emitido
	 */
	public void desvinculaLancamento(Conta conta, Integer numeroCheque) throws RNException {
		
		Cheque cheque = obterPorId(new Cheque(new ChequeId(numeroCheque, conta.getCodigo())));
		
		if(cheque == null){
			return;
		}
		
		if (SituacaoCheque.CANCELADO == cheque.getSituacao()){
			throw new RNException("Não é possível usar cheque cancelado.");
		} else {
			try {
				cheque.setSituacao(SituacaoCheque.NAO_EMITIDO);
				cheque.setLancamento(null);
				dao.salvar(cheque);
			} catch (DAOException e) {
				throw new RNException(e);
			}
		} 
	}


	public void salvar(Cheque model) throws RNException {
		try {
			dao.salvar(model);
		} catch (DAOException e) {
			throw new RNException("Não foi possível salvar o cheque. Erro: "+e.getMessage());
		}
	}
}
