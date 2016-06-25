package br.com.sisnema.financeiroweb.action;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import br.com.sisnema.financeiroweb.exception.RNException;
import br.com.sisnema.financeiroweb.model.Conta;
import br.com.sisnema.financeiroweb.negocio.ContaRN;

@ManagedBean
@RequestScoped
public class ContaBean extends ActionBean<Conta> {

	public ContaBean() {
		super(new ContaRN());
	}
	
	private Conta selecionada = new Conta();
	
	private List<Conta> lista;
	
	public void salvar(){
		try {
			selecionada.setUsuario(obterUsuarioLogado());
			negocio.salvar(selecionada);
			
			if(obterContaAtiva().getCodigo().equals(selecionada.getCodigo())){
				ajustaContaSessao(selecionada);
			}
			
			apresentarMensagemDeSucesso("Conta salva com sucesso");
			lista = null;
			selecionada = new Conta();
			
		} catch (RNException e) {
			apresentarMensagemDeErro(e);
		}
	}

	public void excluir(){
		try {
			negocio.excluir(selecionada);
			apresentarMensagemDeSucesso("Conta excluida com sucesso");
			lista = null;
			selecionada = new Conta();
		} catch (RNException e) {
			apresentarMensagemDeErro(e);
		}
	}
	
	public void tornarFavorita(){
		try {
			((ContaRN) negocio).tornarFavorita(selecionada);
		} catch (RNException e) {
			apresentarMensagemDeErro(e);
		}
	}

	public List<Conta> getLista() {
		if(lista == null){
			lista = negocio.pesquisar(new Conta(obterUsuarioLogado()));
		}
		return lista;
	}
	
	public Conta getSelecionada() {
		return selecionada;
	}

	public void setSelecionada(Conta selecionada) {
		this.selecionada = selecionada;
	}
}
