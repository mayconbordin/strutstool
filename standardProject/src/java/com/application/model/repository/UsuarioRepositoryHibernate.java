package com.application.model.repository;

import com.application.model.entity.Usuario;
import com.framework.util.hibernate.GenericRepositoryHibernate;

public class UsuarioRepositoryHibernate extends GenericRepositoryHibernate<Usuario, Integer> implements UsuarioRepository {
    public UsuarioRepositoryHibernate() {
        super(Usuario.class);
    }
}
