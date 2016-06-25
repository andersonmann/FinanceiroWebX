package br.com.sisnema.financeiroweb.action;

import java.io.File;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletContext;

import org.primefaces.event.CaptureEvent;

import br.com.sisnema.financeiroweb.exception.RNException;
import br.com.sisnema.financeiroweb.model.Usuario;
import br.com.sisnema.financeiroweb.negocio.UsuarioRN;

@ManagedBean
@ViewScoped
public class PhotoCamView extends ActionBean<Usuario> implements Serializable {
     
	public PhotoCamView() {
		super(new UsuarioRN());
	}
	
	private static final long serialVersionUID = 8655921614458785897L;
	private static final String JPEG = ".jpeg";
	private String filename;
	private byte[] photoUser;
     
    public void salvar(){
    	try {
			Usuario us = obterUsuarioLogado();
			us.setPhoto(photoUser);
			negocio.salvar(us);
			apresentarMensagemDeSucesso("Foto atualizada com sucesso.");
		} catch (RNException e) {
			apresentarMensagemDeErro(e);
		}
    }
    
    public String oncapture(CaptureEvent captureEvent) {
    	try {
	        photoUser = captureEvent.getData();
	        filename = gerarFileName();

	        // Cria um link para o diretório das imagens
			File dir = getDirImages();
			
	        writePhoto(dir);
	        filename = null;
		} catch (Exception e) {
			apresentarMensagemDeErro("Erro ao gerar a imagem");
		}
        return null;
    }

	private void writePhoto(File dir) throws Exception {
		FileImageOutputStream imageOutput = null;
      
		try {
			// Caso usuário possua foto cria o diretorio para criar a imagem
			dir.mkdirs();
			File arquivo = new File(dir, filename + JPEG);
			
			imageOutput = new FileImageOutputStream(arquivo);
		    imageOutput.write(photoUser, 0, photoUser.length);
		} finally {
			if(imageOutput != null){
				imageOutput.close();
			}
		}
	}
	
	private void readPhoto(File file) {
		FileImageInputStream imageInput = null;
		try {
			if(!file.exists()){
				criaFotoFromDataBase(obterUsuarioLogado(), getDirImages());
			} else {
				try {
				
					imageInput = new FileImageInputStream(file);
					photoUser = new byte[(int) imageInput.length()];
					imageInput.readFully(photoUser);
				} finally {
					if(imageInput != null){
						imageInput.close();
					}
				}
			}
		} catch (Exception e) {
			apresentarMensagemDeErro("Erro ao ler a foto do usuário");
		}
	}

    /**
     * Retorna um link para o diretório das imagens na pasta do servidor de aplicação.
     * Não irá criar o diretório, apenas retornar uma referencia.
     */
	private File getDirImages() {
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String fileName = servletContext.getRealPath("") + File.separator + "resources" 
															+ File.separator + "imagens" 
															+ File.separator + "photoCam" 
															+ File.separator;
		return new File(fileName);
	}

	private String gerarFileName() {
		Usuario us = obterUsuarioLogado();
        return "photo_user_"+us.getCodigo();
	}

	public byte[] getPhotoUser() {
		return photoUser;
	}

	public void setPhotoUser(byte[] photoUser) {
		this.photoUser = photoUser;
	}

	/**
	 * Retorna o nome do arquivo da photo do usuario
	 */
	public String getFilename() {

		// caso seja o primeiro acesso
		if(filename == null){
			// obtem o usuário
			Usuario us = obterUsuarioLogado();
			
			// Gera o nome do foto
	        filename = gerarFileName();
	        
	        // Cria um link para o diretório das imagens
			File dir = getDirImages();
			
			// Verifica se existe o diretório de imagens na pasta temporária do servidor de aplicação
			if(!dir.exists()){
				
				criaFotoFromDataBase(us, dir);
			} else {
				readPhoto(new File(dir, filename+JPEG));
			}
		}
		return filename;
	}

	private void criaFotoFromDataBase(Usuario us, File dir) {
		// Caso não tenha encontrado, verifica se o usuario, possuiu uma imagem no banco
		if(us.getPhoto() != null){
			
			try {
				photoUser = us.getPhoto();
				
				// cria a imagem na pasta temporária do servidor
				writePhoto(dir);
			} catch (Exception e) {
				apresentarMensagemDeErro("Erro ao gerar a imagem");
			}
		}
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}