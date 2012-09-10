package com.vonhof.ms.nzb;

import java.util.List;


public interface NZBSearchEngine {
    public List<NZBSearchResult> search(String query) throws Exception;
    
}
