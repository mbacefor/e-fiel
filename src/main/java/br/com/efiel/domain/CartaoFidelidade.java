package br.com.efiel.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A CartaoFidelidade.
 */
@Entity
@Table(name = "cartao_fidelidade")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "cartaofidelidade")
public class CartaoFidelidade implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "data_criacao", nullable = false)
    private LocalDate dataCriacao;

    @Column(name = "data_premio")
    private LocalDate dataPremio;

    @ManyToOne
    @JsonIgnoreProperties("cartoes")
    private Campanha campanha;

    @OneToMany(mappedBy = "cartaoFidelidade")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SeloCartao> selos = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("cartaoFidelidades")
    private User dono;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public CartaoFidelidade dataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
        return this;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getDataPremio() {
        return dataPremio;
    }

    public CartaoFidelidade dataPremio(LocalDate dataPremio) {
        this.dataPremio = dataPremio;
        return this;
    }

    public void setDataPremio(LocalDate dataPremio) {
        this.dataPremio = dataPremio;
    }

    public Campanha getCampanha() {
        return campanha;
    }

    public CartaoFidelidade campanha(Campanha campanha) {
        this.campanha = campanha;
        return this;
    }

    public void setCampanha(Campanha campanha) {
        this.campanha = campanha;
    }

    public Set<SeloCartao> getSelos() {
        return selos;
    }

    public CartaoFidelidade selos(Set<SeloCartao> seloCartaos) {
        this.selos = seloCartaos;
        return this;
    }

    public CartaoFidelidade addSelos(SeloCartao seloCartao) {
        this.selos.add(seloCartao);
        seloCartao.setCartaoFidelidade(this);
        return this;
    }

    public CartaoFidelidade removeSelos(SeloCartao seloCartao) {
        this.selos.remove(seloCartao);
        seloCartao.setCartaoFidelidade(null);
        return this;
    }

    public void setSelos(Set<SeloCartao> seloCartaos) {
        this.selos = seloCartaos;
    }

    public User getDono() {
        return dono;
    }

    public CartaoFidelidade dono(User user) {
        this.dono = user;
        return this;
    }

    public void setDono(User user) {
        this.dono = user;
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
        CartaoFidelidade cartaoFidelidade = (CartaoFidelidade) o;
        if (cartaoFidelidade.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cartaoFidelidade.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CartaoFidelidade{" +
            "id=" + getId() +
            ", dataCriacao='" + getDataCriacao() + "'" +
            ", dataPremio='" + getDataPremio() + "'" +
            "}";
    }
}
