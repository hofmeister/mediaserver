package com.vonhof.ms;


import com.vonhof.ms.controller.NZBController;
import com.vonhof.ms.controller.SearchController;
import com.vonhof.ms.controller.SeriesController;
import com.vonhof.ms.controller.VideoController;
import com.vonhof.ms.dto.EpisodeDTO;
import com.vonhof.ms.dto.SeriesDTO;
import com.vonhof.ms.nzb.NZBSearchResult;
import java.io.File;
import java.util.List;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

public class CheckForDownloadsTask extends TimerTask {
    private static final Logger LOG = Logger.getLogger(CheckForDownloadsTask.class.getName());
    private static volatile boolean running = false;
    
    @Inject
    SearchController searchController;
    
    @Inject
    NZBController nzbController;
    
    @Inject
    VideoController videoController;
    
    @Inject
    SeriesController seriesController;

    @Override
    public void run() {
        if (running) 
            return;
        
        running = true;
        File tmpDir = new File(Config.getTmpDir()+"current/");
        try {
            nzbController.sync();

            List<SeriesDTO> series = seriesController.list();
            

            for(SeriesDTO serie:series) {
                List<EpisodeDTO> episodes = seriesController.episodes(serie);

                for(EpisodeDTO episode:episodes) {
                    String filename = SeriesUtils.getEpisodePath(serie, episode);

                    File epFile = new File(filename);

                    if (epFile.exists() 
                            || episode.getNzbs().isEmpty()) {
                        LOG.log(Level.INFO, "Episode exists:{0}", filename);
                        continue;
                    }


                    NZBSearchResult searchResult = episode.getNzbs().get(0);

                    List<String> files = nzbController.download(searchResult.getNzb());

                    if (files.isEmpty()) {
                        LOG.log(Level.SEVERE, "No files wsa downloaded successfully");
                        continue;
                    }


                    File firstFile = new File(files.get(0));


                    if (tmpDir.exists()) {
                        tmpDir.delete();
                    }
                    if (!tmpDir.mkdir()) {
                        LOG.log(Level.SEVERE, "Could not create tmp dir:{0}", tmpDir.getAbsolutePath());
                        continue;
                    }

                    if (!UnpackUtil.unpack(firstFile, tmpDir)) {
                        LOG.log(Level.SEVERE, "Could not unpack:{0}", firstFile.getAbsolutePath());
                        continue;
                    }
                    //Search tmp dir for movie files
                    
                    List<File> movieFiles = VideoUtil.searchForMovies(tmpDir,serie.getMinSize());
                    
                    if (movieFiles.isEmpty()) {
                        LOG.log(Level.SEVERE, "No video files found in download");
                        continue;
                    }
                    if (movieFiles.size() > 1) {
                        LOG.log(Level.WARNING, "Too many video files found in download");
                    }
                    
                    File movieFile = movieFiles.get(0);
                    
                    VideoUtil.makeMP4(movieFile,serie,episode);
                    
                }
            }
        } finally {
            if (tmpDir.exists()) {
                tmpDir.delete();
            }
            running = false;
        }
    }

}
