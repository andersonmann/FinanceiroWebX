package br.com.sisnema.financeiroweb.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import br.com.sisnema.financeiroweb.domain.SituacaoCheque;

@Entity
public class Cheque extends BaseEntity {

	private static final long serialVersionUID = 1018450608232560747L;

	@EmbeddedId
	private ChequeId id;
	
	@ManyToOne
	@JoinColumn(name="cod_conta", insertable=false, updatable=false)
	private Conta conta;
	
	@Enumerated(EnumType.STRING)
	private SituacaoCheque situacao;
	
	@OneToOne
	@JoinColumn(nullable=true)
	private Lancamento lancamento;

	public Cheque() {
	}

	public Cheque(Conta conta) {
		super();
		this.conta = conta;
	}

	public Cheque(ChequeId id) {
		super();
		this.id = id;
	}

	public ChequeId getId() {
		return id;
	}

	public void setId(ChequeId id) {
		this.id = id;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public SituacaoCheque getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoCheque situacao) {
		this.situacao = situacao;
	}

	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((conta == null) ? 0 : conta.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lancamento == null) ? 0 : lancamento.hashCode());
		result = prime * result + ((situacao == null) ? 0 : situacao.hashCode());
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
		if (!(obj instanceof Cheque)) {
			return false;
		}
		Cheque other = (Cheque) obj;
		if (conta == null) {
			if (other.conta != null) {
				return false;
			}
		} else if (!conta.equals(other.conta)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (lancamento == null) {
			if (other.lancamento != null) {
				return false;
			}
		} else if (!lancamento.equals(other.lancamento)) {
			return false;
		}
		if (situacao == null) {
			if (other.situacao != null) {
				return false;
			}
		} else if (!situacao.equals(other.situacao)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Cheque [id=" + id + ", conta=" + conta + ", situacao=" + situacao + ", lancamento=" + lancamento + "]";
	}

}
