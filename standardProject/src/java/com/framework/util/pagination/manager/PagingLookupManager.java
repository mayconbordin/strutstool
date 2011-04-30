package com.framework.util.pagination.manager;

import com.framework.util.pagination.ExtendedPaginatedList;
import com.framework.util.search.SearchParams;
import java.io.Serializable;

public interface PagingLookupManager<T, ID extends Serializable> {
    public ExtendedPaginatedList getAllRecordsPage(ExtendedPaginatedList paginatedList) throws PagingLookupManagerException;
    public ExtendedPaginatedList getSearchRecordsPage(SearchParams params, ExtendedPaginatedList paginatedList) throws PagingLookupManagerException;
}