package br.com.sisnema.financeiroweb.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class LancamentoVO implements Serializable {

	private static final long serialVersionUID = -877250878759376739L;
	
	private Integer codigo;
	private Date data;
	private String descricao;
	private BigDecimal valor;
	private float saldoNaData;
	private int fatorCategoria;
	private Integer cheque;

	public LancamentoVO() {
	}


	public LancamentoVO(Integer codigo, Date data, String descricao, BigDecimal valor, float saldoNaData,
			int fatorCategoria, Integer cheque) {
		super();
		this.codigo = codigo;
		this.data = data;
		this.descricao = descricao;
		this.valor = valor;
		this.saldoNaData = saldoNaData;
		this.fatorCategoria = fatorCategoria;
		this.cheque = cheque;
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

	public float getSaldoNaData() {
		return saldoNaData;
	}

	public void setSaldoNaData(float saldoNaData) {
		this.saldoNaData = saldoNaData;
	}

	public int getFatorCategoria() {
		return fatorCategoria;
	}

	public void setFatorCategoria(int fatorCategoria) {
		this.fatorCategoria = fatorCategoria;
	}

	@Override
	public String toString() {
		return "LancamentoVO [codigo=" + codigo + ", data=" + data + ", descricao=" + descricao + ", valor=" + valor
				+ ", saldoNaData=" + saldoNaData + ", fatorCategoria=" + fatorCategoria + "]";
	}

	public enum Fields {
		CODIGO("codigo"),
		DESCRICAO("descricao"),
		VALOR("valor"),
		SALDO_NA_DATA("saldoNaData"),
		FATOR_CATEGORIA("fatorCategoria"),
		DATA("data"),
		CHEQUE("cheque")
		;
		
		private String property;

		private Fields(String property) {
			this.property = property;
		}
		
		@Override
		public String toString() {
			return property;
		}
	}

	public Integer getCheque() {
		return cheque;
	}

	public void setCheque(Integer cheque) {
		this.cheque = cheque;
	}
}
