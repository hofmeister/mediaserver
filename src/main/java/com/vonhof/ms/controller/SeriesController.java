package com.vonhof.ms.controller;

import com.vonhof.babelshark.BabelSharkInstance;
import com.vonhof.babelshark.annotation.Name;
import com.vonhof.matchit.Expression;
import com.vonhof.matchit.ExpressionContext;
import com.vonhof.matchit.ExpressionMatcher;
import com.vonhof.ms.Config;
import com.vonhof.ms.dto.EpisodeDTO;
import com.vonhof.ms.dto.SeriesDTO;
import com.vonhof.ms.nzb.NZBSearchResult;
import com.vonhof.webi.annotation.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.inject.Inject;

@Path("series")
@Name("series")
public class SeriesController {
    
    @Inject
    BabelSharkInstance bs;
    
    @Inject
    SearchController search;
    
    
    
    public List<SeriesDTO> list() {
        return Config.getSeries();
    }
    public List<EpisodeDTO> episodes(int index) {
        List<SeriesDTO> series = list();
        return episodes(series.get(index));
    }
    
    public List<EpisodeDTO> episodes(SeriesDTO serie) {
        ExpressionContext ctxt = new ExpressionContext();
        
        List<NZBSearchResult> result = search.search(serie.getQuery());
        List<EpisodeDTO> out = new ArrayList<EpisodeDTO>(); 
        Expression expr = ctxt.compile(serie.getPattern());
        for(NZBSearchResult row:result) {
            if (row.getSize() > serie.getMaxSize())
                continue;
            if (row.getSize() < serie.getMinSize())
                continue;
            ExpressionMatcher m = expr.matcher(row.getName());
            if (m.find()) {
                EpisodeDTO episode = new EpisodeDTO();
                episode.setName(serie.getName());
                episode.setEpisode(Integer.valueOf(m.group("episode")));
                episode.setSeason(Integer.valueOf(m.group("season")));
                if (out.contains(episode)) {
                    for(EpisodeDTO ep:out) {
                        if (ep.equals(episode)) {
                            ep.getNzbs().add(row);
                        }
                    }
                } else {
                    episode.getNzbs().add(row);
                    out.add(episode);
                }
            }
        }
        
        Collections.sort(out,new Comparator<EpisodeDTO>() {

            public int compare(EpisodeDTO t, EpisodeDTO t1) {
                int diff = t.getSeason()-t1.getSeason();
                if (diff == 0) {
                    diff = t.getEpisode()-t1.getEpisode();
                }
                return diff;
            }
        });
        
        return out;
    }

}
