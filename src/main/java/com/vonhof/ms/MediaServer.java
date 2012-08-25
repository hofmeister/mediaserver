package com.vonhof.ms;

import com.vonhof.babelshark.BabelShark;
import com.vonhof.babelshark.language.JsonLanguage;
import com.vonhof.ms.controller.NZBController;
import com.vonhof.ms.controller.VideoController;
import com.vonhof.webi.Webi;
import com.vonhof.webi.mvc.MVCRequestHandler;

/**
 * Hello world!
 *
 */
public class MediaServer 
{
    public static void main( String[] args ) throws Exception {
        BabelShark.register(new JsonLanguage());
        
        
        Webi webi = new Webi(8081);
        
        MVCRequestHandler handler = webi.add("/rest/",new MVCRequestHandler());
        
        handler.expose(new NZBController());
        handler.expose(new VideoController());
        
        webi.start();
    }
}
