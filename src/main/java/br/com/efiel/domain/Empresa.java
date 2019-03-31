package br.com.efiel.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * Empresa que tem campanhas
 */
@ApiModel(description = "Empresa que tem campanhas")
@Entity
@Table(name = "empresa")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "empresa")
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 10, max = 90)
    @Column(name = "nome", length = 90, nullable = false)
    private String nome;

    @Size(max = 14)
    @Column(name = "cnpj", length = 14, unique = true)
    private String cnpj;

    @NotNull
    @Size(min = 10, max = 200)
    @Column(name = "endereco", length = 200, nullable = false)
    private String endereco;

    @Size(max = 14)
    @Column(name = "fone", length = 14)
    private String fone;

    @OneToMany(mappedBy = "empresa")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Campanha> campanhas = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("empresas")
    private User criador;

    @ManyToMany(mappedBy = "empresas")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Categoria> cartegorias = new HashSet<>();

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

    public Empresa nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public Empresa cnpj(String cnpj) {
        this.cnpj = cnpj;
        return this;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEndereco() {
        return endereco;
    }

    public Empresa endereco(String endereco) {
        this.endereco = endereco;
        return this;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getFone() {
        return fone;
    }

    public Empresa fone(String fone) {
        this.fone = fone;
        return this;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public Set<Campanha> getCampanhas() {
        return campanhas;
    }

    public Empresa campanhas(Set<Campanha> campanhas) {
        this.campanhas = campanhas;
        return this;
    }

    public Empresa addCampanhas(Campanha campanha) {
        this.campanhas.add(campanha);
        campanha.setEmpresa(this);
        return this;
    }

    public Empresa removeCampanhas(Campanha campanha) {
        this.campanhas.remove(campanha);
        campanha.setEmpresa(null);
        return this;
    }

    public void setCampanhas(Set<Campanha> campanhas) {
        this.campanhas = campanhas;
    }

    public User getCriador() {
        return criador;
    }

    public Empresa criador(User user) {
        this.criador = user;
        return this;
    }

    public void setCriador(User user) {
        this.criador = user;
    }

    public Set<Categoria> getCartegorias() {
        return cartegorias;
    }

    public Empresa cartegorias(Set<Categoria> categorias) {
        this.cartegorias = categorias;
        return this;
    }

    public Empresa addCartegorias(Categoria categoria) {
        this.cartegorias.add(categoria);
        categoria.getEmpresas().add(this);
        return this;
    }

    public Empresa removeCartegorias(Categoria categoria) {
        this.cartegorias.remove(categoria);
        categoria.getEmpresas().remove(this);
        return this;
    }

    public void setCartegorias(Set<Categoria> categorias) {
        this.cartegorias = categorias;
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
        Empresa empresa = (Empresa) o;
        if (empresa.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), empresa.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Empresa{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", cnpj='" + getCnpj() + "'" +
            ", endereco='" + getEndereco() + "'" +
            ", fone='" + getFone() + "'" +
            "}";
    }
}
