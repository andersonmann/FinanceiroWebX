package br.com.sisnema.financeiroweb.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Lancamento extends BaseEntity {

	private static final long serialVersionUID = -8196785385214901725L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer codigo;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	private String descricao;
	private BigDecimal valor;
	
	@ManyToOne
	@JoinColumn(name="cod_usuario", nullable=false)
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name="cod_conta", nullable=false)
	private Conta conta;
	
	@ManyToOne
	@JoinColumn(name="cod_categoria", nullable=false)
	private Categoria categoria;
	
	@OneToOne(mappedBy="lancamento")
	private Cheque cheque;

	public Lancamento() {
		// TODO Auto-generated constructor stub
	}

	public Lancamento(Integer codigo) {
		super();
		this.codigo = codigo;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((categoria == null) ? 0 : categoria.hashCode());
		result = prime * result + ((cheque == null) ? 0 : cheque.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((conta == null) ? 0 : conta.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof Lancamento)) {
			return false;
		}
		Lancamento other = (Lancamento) obj;
		if (categoria == null) {
			if (other.categoria != null) {
				return false;
			}
		} else if (!categoria.equals(other.categoria)) {
			return false;
		}
		if (cheque == null) {
			if (other.cheque != null) {
				return false;
			}
		} else if (!cheque.equals(other.cheque)) {
			return false;
		}
		if (codigo == null) {
			if (other.codigo != null) {
				return false;
			}
		} else if (!codigo.equals(other.codigo)) {
			return false;
		}
		if (conta == null) {
			if (other.conta != null) {
				return false;
			}
		} else if (!conta.equals(other.conta)) {
			return false;
		}
		if (data == null) {
			if (other.data != null) {
				return false;
			}
		} else if (!data.equals(other.data)) {
			return false;
		}
		if (descricao == null) {
			if (other.descricao != null) {
				return false;
			}
		} else if (!descricao.equals(other.descricao)) {
			return false;
		}
		if (usuario == null) {
			if (other.usuario != null) {
				return false;
			}
		} else if (!usuario.equals(other.usuario)) {
			return false;
		}
		if (valor == null) {
			if (other.valor != null) {
				return false;
			}
		} else if (!valor.equals(other.valor)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Lancamento [codigo=" + codigo + ", data=" + data + ", descricao=" + descricao + ", valor=" + valor
				+ ", usuario=" + usuario + ", conta=" + conta + ", categoria=" + categoria + ", cheque=" + cheque + "]";
	}
	
	public enum Fields {
		CODIGO("codigo"),
		DATA("data"),
		DESCRICAO("descricao"),
		VALOR("valor"),
		CONTA("conta"),
		CATEGORIA("categoria"),
		COD_CATEGORIA("categoria.codigo"),
		FATOR_CATEGORIA("categoria.fator"),
		CHEQUE("cheque"),
		NUMERO_CHEQUE("cheque.id.numero");
		
		private String property;

		private Fields(String property) {
			this.property = property;
		}
		
		@Override
		public String toString() {
			return property;
		}
	}

	public Cheque getCheque() {
		return cheque;
	}

	public void setCheque(Cheque cheque) {
		this.cheque = cheque;
	}

}
