package br.com.efiel.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import br.com.efiel.domain.enumeration.TipoSelo;

/**
 * A SeloCartao.
 */
@Entity
@Table(name = "selo_cartao")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "selocartao")
public class SeloCartao implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 50, max = 300)
    @Column(name = "descricao", length = 300, nullable = false)
    private String descricao;

    @NotNull
    @DecimalMax(value = "0")
    @Column(name = "valor", nullable = false)
    private Double valor;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoSelo tipo;

    @ManyToOne
    @JsonIgnoreProperties("selos")
    private CartaoFidelidade cartaoFidelidade;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public SeloCartao descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public SeloCartao valor(Double valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public TipoSelo getTipo() {
        return tipo;
    }

    public SeloCartao tipo(TipoSelo tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(TipoSelo tipo) {
        this.tipo = tipo;
    }

    public CartaoFidelidade getCartaoFidelidade() {
        return cartaoFidelidade;
    }

    public SeloCartao cartaoFidelidade(CartaoFidelidade cartaoFidelidade) {
        this.cartaoFidelidade = cartaoFidelidade;
        return this;
    }

    public void setCartaoFidelidade(CartaoFidelidade cartaoFidelidade) {
        this.cartaoFidelidade = cartaoFidelidade;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SeloCartao seloCartao = (SeloCartao) o;
        if (seloCartao.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), seloCartao.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SeloCartao{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", valor=" + getValor() +
            ", tipo='" + getTipo() + "'" +
            "}";
    }
}
