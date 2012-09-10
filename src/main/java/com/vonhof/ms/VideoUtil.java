package com.vonhof.ms;

import com.vonhof.ms.dto.EpisodeDTO;
import com.vonhof.ms.dto.SeriesDTO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;

public class VideoUtil {
    private static final String[] movieExts = {"mov","mp4","avi","mkv","iso","img"};
    private static final MediaPlayerFactory mpFactory = new MediaPlayerFactory();;
    
    public static List<File> searchForMovies(File dir,int minSize) {
        long minByteSize = (long)minSize*1024L;
        List<File> out = new ArrayList<File>();
        if (!dir.exists() || !dir.isDirectory())
            return out;
        for(File file:dir.listFiles()) {
            if (file.isDirectory()) {
                out.addAll(searchForMovies(file,minSize));
                continue;
            }
            if (isMovie(file) && file.length() >= minByteSize) {
                out.add(file);
            }
        }
        
        return out;
    }
    
    public static boolean isMovie(File file) {
        for(String ext:movieExts) {
            if (file.getName().toLowerCase().endsWith("."+ext))
                return true;
        }
        return false;
    }

    public static void makeMP4(File movieFile, SeriesDTO serie, EpisodeDTO episode) {
        String dest = SeriesUtils.getEpisodePath(serie, episode);
        
        
        

        
    }
    
    public static void updateMP4Meta(File mp4,SeriesDTO serie,EpisodeDTO episode) {
        MediaMeta meta = mpFactory.getMediaMeta(mp4.getAbsolutePath(), true);
        
        meta.setTitle(serie.getName());
        meta.setAlbum(serie.getName());
        meta.setArtist(serie.getName());
        meta.setArtist(serie.getName());
        
    }
}
