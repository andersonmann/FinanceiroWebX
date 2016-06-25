package br.com.sisnema.financeiroweb.action;

import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.apache.commons.lang3.time.DateUtils;

import br.com.sisnema.financeiroweb.exception.RNException;
import br.com.sisnema.financeiroweb.model.Cheque;
import br.com.sisnema.financeiroweb.model.Lancamento;
import br.com.sisnema.financeiroweb.negocio.LancamentoRN;
import br.com.sisnema.financeiroweb.vo.LancamentoVO;

@ManagedBean
@RequestScoped
public class LancamentoBean extends ActionBean<Lancamento> {
	
	private List<LancamentoVO> lista;
	private List<LancamentoVO> listaAteHoje;
	private List<LancamentoVO> listaFuturos;
	private Lancamento editado = new Lancamento();
	private Integer numeroCheque;

	public LancamentoBean() {
		super(new LancamentoRN());
		novo();
	}

	public void novo() {
		editado = new Lancamento();
		editado.setData(new Date());
		lista = null;
		numeroCheque = null;
	}

	public void editar(Integer codigo){
		editado = negocio.obterPorId(new Lancamento(codigo));
		
		Cheque cheque = editado.getCheque();
		if (cheque != null) {
			numeroCheque = cheque.getId().getNumero();
		}
	}
	
	public void salvar(){
		boolean isInsert = false;
		try {
			
			isInsert = (editado.getCodigo() == null);
			
			editado.setUsuario(obterUsuarioLogado());
			editado.setConta(obterContaAtiva());
			((LancamentoRN) negocio).salvar(editado, numeroCheque);
			novo();
		} catch (RNException e) {
			if(isInsert){
				editado.setCodigo(null);
			}
			apresentarMensagemDeErro(e);
		}
	}
	
	public void excluir(Integer codigo){
		try {
			editado = negocio.obterPorId(new Lancamento(codigo));
			negocio.excluir(editado);
			novo();
		} catch (RNException e) {
			apresentarMensagemDeErro(e); 
		}
	}

	public List<LancamentoVO> getLista() {
		
		if(lista == null){
			
			lista = ((LancamentoRN) negocio).pesquisarListaPrincipal( obterContaAtiva(), 
																	  DateUtils.addMonths(new Date(), -1), 
																	  null);
		}
		
		return lista;
	}

	public List<LancamentoVO> getListaAteHoje() {
		if(listaAteHoje == null){
			listaAteHoje = ((LancamentoRN) negocio).pesquisar(obterContaAtiva(), null, new Date());
		}
		return listaAteHoje;
	}

	public List<LancamentoVO> getListaFuturos() {
		if(listaFuturos == null){
			
			listaFuturos = ((LancamentoRN) negocio).pesquisar( obterContaAtiva(), 
															   DateUtils.addDays(new Date(), 1), 
															   null);
		}
		return listaFuturos;
	}

	public Lancamento getEditado() {
		return editado;
	}

	public void setEditado(Lancamento editado) {
		this.editado = editado;
	}

	public Integer getNumeroCheque() {
		return numeroCheque;
	}

	public void setNumeroCheque(Integer numeroCheque) {
		this.numeroCheque = numeroCheque;
	}
}
