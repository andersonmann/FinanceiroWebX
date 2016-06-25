package br.com.sisnema.financeiroweb.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;

import br.com.sisnema.financeiroweb.domain.UsuarioPermissao;
import br.com.sisnema.financeiroweb.exception.RNException;
import br.com.sisnema.financeiroweb.model.Conta;
import br.com.sisnema.financeiroweb.model.Usuario;
import br.com.sisnema.financeiroweb.negocio.ContaRN;
import br.com.sisnema.financeiroweb.negocio.UsuarioRN;

/**
 * Objetivo desta classe � conter os dados do usuario logado
 * armazendo em um �nico objeto tudo que possa ser necessario,
 * o qual estar� em escopo de sess�o.
 */
@ManagedBean
@SessionScoped
public class ContextoBean implements Serializable {

	private static final long serialVersionUID = 1685484721761615411L;
	
	/** Contem a instancia do usuario logado  */
	private Usuario usuarioLogado = null;
	
	private Conta contaAtiva;
	
	private List<String> landscapes;
	
	private Locale locale;
	
	private List<Locale> idiomas;
	
	public ContextoBean() {
		landscapes = new ArrayList<String>();
		landscapes.add("n1.jpg");
		landscapes.add("n2.jpg");
		landscapes.add("n3.jpg");
		landscapes.add("n4.jpg");
		landscapes.add("n5.jpg");
		landscapes.add("n6.jpg");
		landscapes.add("n7.jpg");
		landscapes.add("n8.jpg");
	}
	
	public Usuario getUsuarioLogado() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
														.getExternalContext()
														.getSession(false);
		
		usuarioLogado = (Usuario) session.getAttribute(LoginBean.USUARIO_LOGADO);
		return usuarioLogado;
	}
	
	
	/**
	 * M�todo que recebe uma determinada permiss�o e verifica se o usu�rio a possui
	 * @param role - permiss�o a ser verificada
	 * @return valor boleano informando se usu�rio possui a permiss�o parametrizada
	 */
	public boolean hasRole(String role){
		Usuario user = getUsuarioLogado();
		return user != null && user.getPermissao().contains(UsuarioPermissao.valueOf(role));
	}

	public boolean hasRole(Usuario us, String role){
		return us.getPermissao().contains(UsuarioPermissao.valueOf(role));
	}
	
	public Conta getContaAtiva() {
		// primeiro acesso a conta esta nula
		if (contaAtiva == null) {
			Usuario usuario = getUsuarioLogado();

			if(usuario == null){
				return null;
			}
			
			ContaRN contaRN = new ContaRN();
			contaAtiva = contaRN.buscarFavorita(usuario);

			// Caso usuario nao possua conta favorita
			if (contaAtiva == null) {
				// busca todas as contas do usuario
				List<Conta> contas = contaRN.pesquisar(new Conta(usuario));
				if (contas != null) {
					
					// sai do loop apos encontrar primeira conta
					for (Conta conta : contas) {
						contaAtiva = conta;
						break;
					}
				}
			}
		}
		return contaAtiva;
	}

	/**
	 * Mais a frente teremos o programa lancamento
	 * que ira fazer um lancamento em uma CONTA_ATIVA
	 * logo toda a vez que alteramos uma conta na combo
	 * devemos alterar-la na sessao.
	 */
	public void setContaAtiva(ValueChangeEvent event) {
		Integer codigo = (Integer) event.getNewValue();
		ContaRN contaRN = new ContaRN();
		
		contaAtiva = contaRN.obterPorId(new Conta(codigo));
	}
	

	public void setContaAtiva(Conta c) {
		contaAtiva = c;
	}


	public List<String> getLandscapes() {
		return landscapes;
	}
	
	/**
	 * Retorna uma lista de Locale (idiomas suportados pelo sistema, 
	 * definido no arquivo faces-config.xml)
	 */
	public List<Locale> getIdiomas() {
		// Obtemos a inst�ncia corrente do JSF
		FacesContext context = FacesContext.getCurrentInstance();

		// Com ela obtemos uma lista dos idiomas suportados (definidos no arquivo faces-config.xml)
		Iterator<Locale> locales = context.getApplication().getSupportedLocales();
		idiomas = new ArrayList<Locale>();
		
		// Iteramos este objeto e retornamos uma lista SIMPLES com os Nomes dos mesmos
		while(locales.hasNext()) {
			idiomas.add(locales.next());
		}
		
		return idiomas;
	}
	
	/**
	 * Altera o idioma default do usuario na base de dados
	 * para que sempre fique nesse idioma (at� ser alterado)
	 */
	public void setIdiomaUsuario(String idioma) {
		UsuarioRN usuarioRN = new UsuarioRN();
		try {
			usuarioLogado = usuarioRN.obterPorId(getUsuarioLogado());
			usuarioLogado.setIdioma(idioma);
			usuarioRN.salvar(usuarioLogado);
		} catch (RNException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao setar o idioma: " + e.getMessage(), null));
			return;
		}

		// AP�S ter alterado o idioma do usuario
		// seta o novo idioma no locale da sessao
		String[] info = idioma.split("_");
		locale = new Locale(info[0], info[1]);
		
		FacesContext context = FacesContext.getCurrentInstance();
		context.getViewRoot().setLocale(locale);
		
	}
	
	public Locale getLocale() {
		if (locale == null) {
			// Entra logo ap�s o login, quando o objeto ainda n�o foi criado
			Usuario usuario = getUsuarioLogado();
			
			// necess�rio para o cadastro usuario n�o estando logado
			if(usuario == null){
				locale = new Locale("pt", "BR");
				return locale;
			}
			
			String idioma = usuario.getIdioma();
			String[] info = idioma.split("_");
			locale = new Locale(info[0], info[1]);
		}
		
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}













