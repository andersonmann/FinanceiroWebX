package br.com.sisnema.financeiroweb.negocio;

import br.com.sisnema.financeiroweb.dao.UsuarioDAO;
import br.com.sisnema.financeiroweb.domain.UsuarioPermissao;
import br.com.sisnema.financeiroweb.exception.DAOException;
import br.com.sisnema.financeiroweb.exception.RNException;
import br.com.sisnema.financeiroweb.model.Usuario;

public class UsuarioRN extends RN<Usuario> {

	public UsuarioRN() {
		super(new UsuarioDAO());
	}
	
	public void salvar(Usuario usuarioDaTela) throws RNException {
		
		if(usuarioDaTela.getCodigo() != null){
			try {
				UsuarioDAO uDAO = (UsuarioDAO) dao;
				
				uDAO.alterar(usuarioDaTela);
			} catch (DAOException e) {
				throw new RNException(e.getMessage(),e);
			}
			
		} else {
			
			try {
				Usuario userExistente = buscarUsuarioPorLogin(usuarioDaTela.getLogin());
				
				if(userExistente != null){
					throw new RNException("J� existe um usu�rio com o login informado");
				}
				
				usuarioDaTela.getPermissao().add(UsuarioPermissao.ROLE_USUARIO);
				
				dao.salvar(usuarioDaTela);
				new CategoriaRN().salvaEstruturaPadrao(usuarioDaTela);
				
			} catch (DAOException e) {
				throw new RNException(e.getMessage(),e);
			}
		}
	}

	public Usuario buscarUsuarioPorLoginESenha(String login, String senha){
		return ((UsuarioDAO) dao).buscarUsuarioPorLoginESenha(login, senha);
	}
	
	public Usuario buscarUsuarioPorLogin(String login){
		return ((UsuarioDAO) dao).buscarUsuarioPorLogin(login);
	}

	@Override
	public void excluir(Usuario model) throws RNException {
		try {
			new CategoriaRN().excluir(model);
			dao.excluir(model);
		} catch (DAOException e) {
			throw new RNException(e.getMessage(), e);
		}
	}


}

















