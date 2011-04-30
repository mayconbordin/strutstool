package com.application.model.service;

import com.application.model.entity.Usuario;
import com.framework.util.repository.RepositoryException;
import com.framework.util.validator.ValidatorException;
import java.util.List;

public interface UsuarioService {
    public void save(Usuario usuario) throws RepositoryException, ValidatorException;
    public void delete(Integer id) throws RepositoryException;
    public List<Usuario> findAll() throws RepositoryException;
    public List<Usuario> findAll(String orderBy, String orderType) throws RepositoryException;
    public Usuario findById(Integer id) throws RepositoryException;

    public List<Usuario> findAllByExample(Usuario usuario) throws RepositoryException;
    public List<Usuario> findAllWithPagination(int min, int max, String orderBy, String orderType) throws RepositoryException;

    public Integer count() throws RepositoryException;
}
