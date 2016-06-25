package br.com.sisnema.financeiroweb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Conta extends BaseEntity {

	private static final long serialVersionUID = 6505981133006523165L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="cod_conta")
	private Integer codigo;

	@Column(name="des_conta", nullable=false)
	private String descricao;
	
	@Column(name="saldo_inicial", nullable=false)
	private float saldoInicial;
	
	private boolean favorita;
	
	@ManyToOne
	@JoinColumn(name="cod_usuario",nullable=false)
	private Usuario usuario;

	public Conta() {
	}

	public Conta(Integer codigo) {
		super();
		this.codigo = codigo;
	}

	public Conta(Usuario usuario) {
		super();
		this.usuario = usuario;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public float getSaldoInicial() {
		return saldoInicial;
	}

	public void setSaldoInicial(float saldoInicial) {
		this.saldoInicial = saldoInicial;
	}

	public boolean isFavorita() {
		return favorita;
	}

	public void setFavorita(boolean favorita) {
		this.favorita = favorita;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + (favorita ? 1231 : 1237);
		result = prime * result + Float.floatToIntBits(saldoInicial);
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
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
		if (!(obj instanceof Conta)) {
			return false;
		}
		Conta other = (Conta) obj;
		if (codigo == null) {
			if (other.codigo != null) {
				return false;
			}
		} else if (!codigo.equals(other.codigo)) {
			return false;
		}
		if (descricao == null) {
			if (other.descricao != null) {
				return false;
			}
		} else if (!descricao.equals(other.descricao)) {
			return false;
		}
		if (favorita != other.favorita) {
			return false;
		}
		if (Float.floatToIntBits(saldoInicial) != Float.floatToIntBits(other.saldoInicial)) {
			return false;
		}
		if (usuario == null) {
			if (other.usuario != null) {
				return false;
			}
		} else if (!usuario.equals(other.usuario)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Conta [codigo=" + codigo + ", descricao=" + descricao + ", saldoInicial=" + saldoInicial + ", favorita="
				+ favorita + ", usuario=" + usuario + "]";
	}

}
