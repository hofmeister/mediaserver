package com.vonhof.ms.controller;

import com.vonhof.babelshark.annotation.Name;
import com.vonhof.ms.nzb.NZBSearchEngine;
import com.vonhof.ms.nzb.NZBSearchResult;
import com.vonhof.webi.annotation.Parm;
import com.vonhof.webi.annotation.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

@Path("search")
@Name("search")
public class SearchController {
    @Inject
    NZBSearchEngine searchEngine;
    
    public List<NZBSearchResult> search(@Parm(required=true) String q) {
        try {
            return searchEngine.search(q);
        } catch (Exception ex) {
            Logger.getLogger(SearchController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return new ArrayList<NZBSearchResult>();
    }
}
