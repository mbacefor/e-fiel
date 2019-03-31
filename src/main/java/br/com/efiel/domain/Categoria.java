package br.com.efiel.domain;


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
 * A Categoria.
 */
@Entity
@Table(name = "categoria")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "categoria")
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(min = 10, max = 200)
    @Column(name = "descricao", length = 200, unique = true)
    private String descricao;

    @NotNull
    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "categoria_empresas",
               joinColumns = @JoinColumn(name = "categoria_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "empresas_id", referencedColumnName = "id"))
    private Set<Empresa> empresas = new HashSet<>();

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

    public Categoria descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean isAtivo() {
        return ativo;
    }

    public Categoria ativo(Boolean ativo) {
        this.ativo = ativo;
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Set<Empresa> getEmpresas() {
        return empresas;
    }

    public Categoria empresas(Set<Empresa> empresas) {
        this.empresas = empresas;
        return this;
    }

    public Categoria addEmpresas(Empresa empresa) {
        this.empresas.add(empresa);
        empresa.getCartegorias().add(this);
        return this;
    }

    public Categoria removeEmpresas(Empresa empresa) {
        this.empresas.remove(empresa);
        empresa.getCartegorias().remove(this);
        return this;
    }

    public void setEmpresas(Set<Empresa> empresas) {
        this.empresas = empresas;
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
        Categoria categoria = (Categoria) o;
        if (categoria.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), categoria.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Categoria{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", ativo='" + isAtivo() + "'" +
            "}";
    }
}
