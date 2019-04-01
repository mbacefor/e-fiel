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
 * A Campanha.
 */
@Entity
@Table(name = "campanha")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "campanha")
public class Campanha implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 10, max = 150)
    @Column(name = "nome", length = 150, nullable = false)
    private String nome;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @NotNull
    @Size(min = 10, max = 300)
    @Column(name = "premio", length = 300, nullable = false)
    private String premio;

    @NotNull
    @Size(min = 10, max = 300)
    @Column(name = "regras", length = 300, nullable = false)
    private String regras;

    @NotNull
    @Column(name = "expira", nullable = false)
    private LocalDate expira;

    @NotNull
    @Min(value = 1)
    @Column(name = "numero_selos", nullable = false)
    private Integer numeroSelos;

    @OneToMany(mappedBy = "campanha")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CartaoFidelidade> cartoes = new HashSet<>();
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("campanhas")
    private Empresa empresa;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Campanha nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public byte[] getLogo() {
        return logo;
    }

    public Campanha logo(byte[] logo) {
        this.logo = logo;
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return logoContentType;
    }

    public Campanha logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public String getPremio() {
        return premio;
    }

    public Campanha premio(String premio) {
        this.premio = premio;
        return this;
    }

    public void setPremio(String premio) {
        this.premio = premio;
    }

    public String getRegras() {
        return regras;
    }

    public Campanha regras(String regras) {
        this.regras = regras;
        return this;
    }

    public void setRegras(String regras) {
        this.regras = regras;
    }

    public LocalDate getExpira() {
        return expira;
    }

    public Campanha expira(LocalDate expira) {
        this.expira = expira;
        return this;
    }

    public void setExpira(LocalDate expira) {
        this.expira = expira;
    }

    public Integer getNumeroSelos() {
        return numeroSelos;
    }

    public Campanha numeroSelos(Integer numeroSelos) {
        this.numeroSelos = numeroSelos;
        return this;
    }

    public void setNumeroSelos(Integer numeroSelos) {
        this.numeroSelos = numeroSelos;
    }

    public Set<CartaoFidelidade> getCartoes() {
        return cartoes;
    }

    public Campanha cartoes(Set<CartaoFidelidade> cartaoFidelidades) {
        this.cartoes = cartaoFidelidades;
        return this;
    }

    public Campanha addCartoes(CartaoFidelidade cartaoFidelidade) {
        this.cartoes.add(cartaoFidelidade);
        cartaoFidelidade.setCampanha(this);
        return this;
    }

    public Campanha removeCartoes(CartaoFidelidade cartaoFidelidade) {
        this.cartoes.remove(cartaoFidelidade);
        cartaoFidelidade.setCampanha(null);
        return this;
    }

    public void setCartoes(Set<CartaoFidelidade> cartaoFidelidades) {
        this.cartoes = cartaoFidelidades;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public Campanha empresa(Empresa empresa) {
        this.empresa = empresa;
        return this;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
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
        Campanha campanha = (Campanha) o;
        if (campanha.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), campanha.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Campanha{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            ", premio='" + getPremio() + "'" +
            ", regras='" + getRegras() + "'" +
            ", expira='" + getExpira() + "'" +
            ", numeroSelos=" + getNumeroSelos() +
            "}";
    }
}
