package com.application.controller;

import com.application.model.entity.Usuario;
import com.application.model.search.UsuarioSearchMap;
import com.application.model.service.UsuarioService;
import com.application.model.service.UsuarioServiceImpl;
import com.framework.util.pagination.manager.PagingLookupManagerException;
import com.framework.util.search.EntitySearchMap;
import com.framework.util.struts.StrutsController;
import com.framework.util.repository.RepositoryException;
import com.framework.util.search.SearchAware;
import com.framework.util.validator.ValidatorException;
import com.opensymphony.xwork2.ModelDriven;

public class UsuarioController extends StrutsController 
        implements ModelDriven<Usuario>, SearchAware {
    
    private UsuarioService usuarioService;
    private Usuario usuario = new Usuario();
    private Integer usuarioId;

    // Actions =================================================================
    
    public String index() {
        try {
            paginatedList = paginateListFactory.getPaginatedListFromRequest(request);

            if (isSearch()) {
                paginatedList = pagingManager.getSearchRecordsPage(searchParams, paginatedList);
            } else {
                paginatedList = pagingManager.getAllRecordsPage(paginatedList);
            }
        } catch (PagingLookupManagerException ex) {
            errorHandler(ex);
        }
        
        statusHandler();
        return SUCCESS;
    }

    public String edit() {
        try {
            if (isSave()) {
                getUsuarioService().save(usuario);
                return SUCCESS_SAVE;
            } else {
                usuario = getUsuarioService().findById(usuarioId);
                if (usuario == null) {
                    return NOT_FOUND;
                }
            }
        } catch (RepositoryException ex) {
            errorHandler(ex);
        } catch (ValidatorException ex) {
            errorHandler(ex);
        }

        return SUCCESS;
    }

    public String add() {
        try {
            if (isSave()) {
                getUsuarioService().save(usuario);
                return SUCCESS_SAVE;
            }
        } catch (RepositoryException ex) {
            errorHandler(ex);
        } catch (ValidatorException ex) {
            errorHandler(ex);
        }

        return SUCCESS;
    }

    public void delete() {
        try {
            getUsuarioService().delete(usuarioId);
        } catch (RepositoryException ex) {
            errorHandler(ex);
        }
    }

    // Implemented interface methods ===========================================

    public Usuario getModel() {
        return usuario;
    }

    public boolean isSearchValid() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public EntitySearchMap getEntitySearchMap() {
        return new UsuarioSearchMap();
    }

    // Getters and Setters =====================================================
    
    public UsuarioService getUsuarioService() {
        if (usuarioService == null) {
            setUsuarioService(new UsuarioServiceImpl());
        }
        
        return usuarioService;
    }

    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }
}