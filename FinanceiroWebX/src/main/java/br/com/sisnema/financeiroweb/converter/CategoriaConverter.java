package br.com.sisnema.financeiroweb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.sisnema.financeiroweb.model.Categoria;
import br.com.sisnema.financeiroweb.negocio.CategoriaRN;

@FacesConverter(forClass=Categoria.class)
public class CategoriaConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component, String codCategoria) {
		return new CategoriaRN().obterPorId(new Categoria(Integer.valueOf(codCategoria)));
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		Categoria c = (Categoria) value;
		return c.getCodigo().toString();
	}

}
