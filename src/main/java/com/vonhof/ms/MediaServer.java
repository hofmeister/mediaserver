package com.vonhof.ms;

import com.sun.jna.NativeLibrary;
import com.vonhof.babelshark.BabelShark;
import com.vonhof.babelshark.language.JsonLanguage;
import com.vonhof.ms.controller.NZBController;
import com.vonhof.ms.controller.SearchController;
import com.vonhof.ms.controller.SeriesController;
import com.vonhof.ms.controller.VideoController;
import com.vonhof.ms.nzb.NZBClubSearch;
import com.vonhof.ms.nzb.NZBSearchEngine;
import com.vonhof.webi.Webi;
import com.vonhof.webi.mvc.MVCRequestHandler;
import java.util.Timer;
import java.util.TimerTask;

public class MediaServer {
    
    public static void main( String[] args ) throws Exception {
        BabelShark.register(new JsonLanguage());
        
        Webi webi = new Webi(8081);
        
        webi.addBean(NZBSearchEngine.class, new NZBClubSearch());
        
        MVCRequestHandler handler = webi.add("/rest/",new MVCRequestHandler());
        
        handler.expose(new NZBController());
        handler.expose(new VideoController());
        handler.expose(new SearchController());
        handler.expose(new SeriesController());
        
        CheckForDownloadsTask downloadTask = new CheckForDownloadsTask();
        
        webi.addBean(downloadTask);
        
        final Timer timer = new Timer("scheduler");
        
        timer.scheduleAtFixedRate(downloadTask,0, 5*60*1000);
        
        webi.start();
    }
}
