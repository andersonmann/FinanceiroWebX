package br.com.sisnema.financeiroweb.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ChequeId implements Serializable {

	private static final long serialVersionUID = 6174561841602014344L;
	
	@Column(nullable=false)
	private Integer numero;
	
	@Column(name="cod_conta", nullable=false)
	private Integer conta;

	public ChequeId() {
	}

	public ChequeId(Integer numero, Integer conta) {
		super();
		this.numero = numero;
		this.conta = conta;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Integer getConta() {
		return conta;
	}

	public void setConta(Integer conta) {
		this.conta = conta;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((conta == null) ? 0 : conta.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ChequeId)) {
			return false;
		}
		ChequeId other = (ChequeId) obj;
		if (conta == null) {
			if (other.conta != null) {
				return false;
			}
		} else if (!conta.equals(other.conta)) {
			return false;
		}
		if (numero == null) {
			if (other.numero != null) {
				return false;
			}
		} else if (!numero.equals(other.numero)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ChequeId [numero=" + numero + ", conta=" + conta + "]";
	}

}
