package com.application.model.service;

import com.application.model.repository.UsuarioRepository;
import com.application.model.repository.UsuarioRepositoryHibernate;
import com.application.model.entity.Usuario;
import com.framework.util.repository.RepositoryException;
import com.framework.util.validator.Validator;
import com.framework.util.validator.ValidatorException;
import java.util.List;

public class UsuarioServiceImpl implements UsuarioService {
    private UsuarioRepository usuarioRepository;
    private Validator usuarioValidator;

    public UsuarioServiceImpl() {}

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Implemented interface methods ===========================================
    
    public void save(Usuario usuario) throws RepositoryException, ValidatorException {
        getUsuarioValidator().validate(usuario);
        getUsuarioRepository().save(usuario);
    }

    public void delete(Integer id) throws RepositoryException {
        Usuario usuario = getUsuarioRepository().findById(id);
        getUsuarioRepository().delete(usuario);
    }

    public List<Usuario> findAll() throws RepositoryException {
        return getUsuarioRepository().findAll();
    }

    public List<Usuario> findAll(String orderBy, String orderType) throws RepositoryException {
        return getUsuarioRepository().findAll(orderBy, orderType);
    }

    public Usuario findById(Integer id) throws RepositoryException {
        return getUsuarioRepository().findById(id);
    }

    public List<Usuario> findAllByExample(Usuario usuario) throws RepositoryException {
        return getUsuarioRepository().findAllByExample(usuario);
    }

    public List<Usuario> findAllWithPagination(int min, int max, String orderBy, String orderType) throws RepositoryException {
        return getUsuarioRepository().findAllWithPagination(min, max, orderBy, orderType);
    }

    public Integer count() throws RepositoryException {
        return getUsuarioRepository().count();
    }

    // Getters and Setters =====================================================

    public UsuarioRepository getUsuarioRepository() {
        if (usuarioRepository == null) {
            setUsuarioRepository(new UsuarioRepositoryHibernate());
        }

        return usuarioRepository;
    }

    public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Validator getUsuarioValidator() {
        if (usuarioValidator == null) {
            setUsuarioValidator(new Validator<Usuario>());
        }
        return usuarioValidator;
    }

    public void setUsuarioValidator(Validator usuarioValidator) {
        this.usuarioValidator = usuarioValidator;
    }
}
