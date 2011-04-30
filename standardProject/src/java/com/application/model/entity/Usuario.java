package com.application.model.entity;

import com.framework.util.validator.constraints.NotNull;
import com.framework.util.validator.constraints.XSSFilter;
import java.io.Serializable;
import java.util.Date;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;

@Indexed
public class Usuario implements Serializable {
    @DocumentId
    private int id;

    @Field(index=Index.TOKENIZED,store=Store.YES)
    private String nome;

    @Field(index=Index.TOKENIZED)
    private String endereco;

    @Field(index=Index.TOKENIZED)
    private int idade;

    @Field(index=Index.UN_TOKENIZED, store=Store.YES)
    @DateBridge(resolution=Resolution.SECOND)
    private Date nascimento;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @NotNull
    @XSSFilter
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    @NotNull
    @XSSFilter
    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    @NotNull
    @XSSFilter
    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }
    @NotNull
    @XSSFilter
    public Date getNascimento() {
        return nascimento;
    }

    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

}
