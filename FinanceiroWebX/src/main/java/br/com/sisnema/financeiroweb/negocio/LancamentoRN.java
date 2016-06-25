package br.com.sisnema.financeiroweb.negocio;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateUtils;

import br.com.sisnema.financeiroweb.dao.LancamentoDAO;
import br.com.sisnema.financeiroweb.domain.SituacaoCheque;
import br.com.sisnema.financeiroweb.exception.DAOException;
import br.com.sisnema.financeiroweb.exception.RNException;
import br.com.sisnema.financeiroweb.model.Cheque;
import br.com.sisnema.financeiroweb.model.ChequeId;
import br.com.sisnema.financeiroweb.model.Conta;
import br.com.sisnema.financeiroweb.model.Lancamento;
import br.com.sisnema.financeiroweb.vo.LancamentoVO;

public class LancamentoRN extends RN<Lancamento> {

	public LancamentoRN() {
		super(new LancamentoDAO());
	}

	public List<LancamentoVO> pesquisar(Conta conta, Date dataInicio, Date dataFim) {
		return ((LancamentoDAO) dao).pesquisar(conta, dataInicio, dataFim);
	}
	
	public List<LancamentoVO> pesquisarListaPrincipal(Conta conta, Date dataInicio, Date dataFim) {
		List<LancamentoVO> lancamentos = pesquisar(conta, dataInicio, dataFim);
		float saldo = saldo(conta, DateUtils.addDays(dataInicio, -1));
		LancamentoVO lancInicial = new LancamentoVO(null, null, null, null, saldo, 1, null);
		
		for (LancamentoVO lancamentoVO : lancamentos) {
				  // passa para negativo caso a categoria do lancamento seja uma DESPESA
			saldo += (lancamentoVO.getValor().floatValue() * lancamentoVO.getFatorCategoria());
			lancamentoVO.setSaldoNaData(saldo);
		}
		
		lancamentos.add(0, lancInicial);
		
		return lancamentos;
	}

	public float saldo(Conta conta, Date date){
		float saldoInicial = conta.getSaldoInicial();
		float saldoNaData = ((LancamentoDAO) dao).saldo(conta, date);
		return saldoInicial + saldoNaData;
	}
	
	@Override
	public void excluir(Lancamento lancamento) throws RNException {
		try {
			
			if(lancamento.getCheque() != null){
				new ChequeRN().desvinculaLancamento(lancamento.getConta(), lancamento.getCheque().getId().getNumero());
				dao.flushAndClear();
				lancamento = obterPorId(lancamento);
			}
			dao.excluir(lancamento);
		} catch (DAOException e) {
			throw new RNException("Não foi possível excluir o cheque. Erro: "+e.getMessage());
		}
	}
	
	public void salvar(Lancamento model, Integer numeroCheque) throws RNException {
		

		try {
			boolean isUpdate = model.getCodigo() != null;
			boolean mudouCheque = false;
			Integer chequeOld = null;
			
			if(isUpdate){
				Lancamento lancOld = obterPorId(model);
				chequeOld = lancOld.getCheque().getId().getNumero();
				mudouCheque = ObjectUtils.notEqual(numeroCheque, chequeOld);
				dao.flushAndClear();
			}
			
			dao.salvar(model);
		
			model = obterPorId(model);
			
			if(mudouCheque){
				new ChequeRN().desvinculaLancamento(model.getConta(), chequeOld);
			}
			
			// O lancamento pode possuir um cheque
			if (numeroCheque != null) {
				
				ChequeId chequeId = new ChequeId( numeroCheque,
												  model.getConta().getCodigo());
				
				Cheque chequeAux = new Cheque(chequeId);
				
				Cheque cheque = new ChequeRN().obterPorId(chequeAux);
	
				// Antes de baixar o cheque, valida-se se o mesmo existe
				// e não esteja cancelado...
				if (cheque == null) {
					dao.cancelaTransacao();
					throw new RNException("Cheque não cadastrado");
					
				} else if (SituacaoCheque.CANCELADO.equals(cheque.getSituacao())) {
					dao.cancelaTransacao();
					throw new RNException("Cheque já cancelado");
					
				} else if (SituacaoCheque.BAIXADO.equals(cheque.getSituacao())) {
					dao.cancelaTransacao();
					throw new RNException("Cheque já baixado");
					
				} else {
					try {
						ChequeRN crn = new ChequeRN();
						
						// baixar o cheque, ou seja, alterar sua situação para Baixado
						crn.baixarCheque(chequeId, model);
						dao.comitaTransacao();
					} catch (RNException e) {
						dao.cancelaTransacao();
						throw new RNException("Erro ao baixar cheque: " + e.getMessage());
					}
				}
			}

		} catch (DAOException e) {
			throw new RNException(e.getMessage());
		}
	}

	public void salvar(Lancamento model) throws RNException {
		try {
			dao.salvar(model);
		} catch (DAOException e) {
			throw new RNException("Não foi possivel salvar o cheque. Erro: "+e.getMessage());
		}
	}
}
