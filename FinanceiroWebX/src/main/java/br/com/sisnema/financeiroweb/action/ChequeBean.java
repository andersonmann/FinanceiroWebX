package br.com.sisnema.financeiroweb.action;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import br.com.sisnema.financeiroweb.exception.RNException;
import br.com.sisnema.financeiroweb.model.Cheque;
import br.com.sisnema.financeiroweb.negocio.ChequeRN;
import br.com.sisnema.financeiroweb.util.MensagemUtil;

@ManagedBean
@RequestScoped
public class ChequeBean extends ActionBean<Cheque> {
	
	private Cheque selecionado = new Cheque();
	private List<Cheque> lista = null;
	private Integer chequeInicial;
	private Integer chequeFinal;
	
	public ChequeBean() {
		super(new ChequeRN());
	}
	
	public void salvar() {
		
		int totalCheques = 0;
		if (chequeInicial == null || chequeFinal == null) {
			apresentarMensagemDeErro(MensagemUtil.getMensagem("cheque_erro_sequencia"));
			
		} else if (chequeFinal.intValue() < chequeInicial.intValue()) {
			apresentarMensagemDeErro(MensagemUtil.getMensagem("cheque_erro_inicial_final", chequeInicial, chequeFinal));
			
		} else {
			try {
				// Vai ao banco salvar sequencia de cheques
				totalCheques = ((ChequeRN) negocio).salvarSequencia(obterContaAtiva(), 
														chequeInicial, chequeFinal);
				// Mensagem de sucesso
				apresentarMensagemDeSucesso(MensagemUtil.getMensagem("cheque_info_cadastro", chequeFinal, totalCheques,120));
			} catch (RNException e) {
				apresentarMensagemDeErro("Erro ao salvar cheque: " + e.getMessage());
				return;
			}
			lista = null;
		}
	}
	
	public void excluir() {
		try {
			negocio.excluir(selecionado);
		} catch (RNException e) {
			apresentarMensagemDeErro(MensagemUtil.getMensagem("cheque_erro_excluir"));
			return;
		}
		
		lista = null;
	}
	
	public void cancelar() {
		try {
			((ChequeRN) negocio).cancelarCheque(selecionado);
		} catch (RNException e) {
			apresentarMensagemDeErro(MensagemUtil.getMensagem("cheque_erro_cancelar"));
			return;
		}
		
		lista = null;
	}

	public List<Cheque> getLista() {
		if (lista == null) {
			lista = negocio.pesquisar(new Cheque(obterContaAtiva()));
		}		
		
		return lista;
	}

	public Cheque getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Cheque selecionado) {
		this.selecionado = selecionado;
	}

	public Integer getChequeInicial() {
		return chequeInicial;
	}

	public void setChequeInicial(Integer chequeInicial) {
		this.chequeInicial = chequeInicial;
	}

	public Integer getChequeFinal() {
		return chequeFinal;
	}

	public void setChequeFinal(Integer chequeFinal) {
		this.chequeFinal = chequeFinal;
	}
}
