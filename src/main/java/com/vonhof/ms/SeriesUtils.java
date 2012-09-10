package com.vonhof.ms;

import com.vonhof.ms.dto.EpisodeDTO;
import com.vonhof.ms.dto.SeriesDTO;

public class SeriesUtils {
    public static String getDestinationDir() {
        String destDir = (String) Config.getConfig().get("destDir");
        
        if (destDir.isEmpty())
            destDir = ".";
        
        if (!destDir.endsWith("/"))
            destDir += "/";
        return destDir;
    }
    
    public static String getEpisodePath(SeriesDTO serie,EpisodeDTO episode) {
        return getDestinationDir()+SeriesUtils.getEpisodeFilename(serie, episode);
    }
    public static String getEpisodeFilename(SeriesDTO serie,EpisodeDTO episode) {
        String epNum = episode.getEpisode() < 10 ? 
                            String.format("0%s",episode.getEpisode()) 
                            : String.valueOf(episode.getEpisode());

        String seNum = episode.getSeason() < 10 ? 
                            String.format("0%s",episode.getSeason()) 
                            : String.valueOf(episode.getSeason());

        return String.format("%s S%sE%s.mp4",serie.getName(),epNum,seNum);
    } 
}
