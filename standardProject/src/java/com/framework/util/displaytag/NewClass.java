package com.framework.util.displaytag;

import com.application.model.service.UsuarioService;
import com.application.model.service.UsuarioServiceImpl;
import java.util.List;
import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;

/**
 *
 * @author maycon
 */
public class NewClass implements PaginatedList {
    private UsuarioService usuarioService = new UsuarioServiceImpl();

    public List getList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getPageNumber() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getObjectsPerPage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getFullListSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getSortCriterion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SortOrderEnum getSortDirection() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getSearchId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
