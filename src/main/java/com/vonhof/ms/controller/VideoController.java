package com.vonhof.ms.controller;

import com.vonhof.babelshark.annotation.Name;
import com.vonhof.webi.annotation.Path;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.LibVlcFactory;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;

@Path("video")
@Name("video")
public class VideoController {
    private final LibVlc libVlc = LibVlcFactory.factory().create();

    public void play(String file,int port) {
        
        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(libVlc);
        HeadlessMediaPlayer mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();
        
        String options = formatHttpStream("0.0.0.0", port);
        
        mediaPlayer.playMedia(file, options);
    }
    
    private static String formatHttpStream(String serverAddress, int serverPort) {
        StringBuilder sb = new StringBuilder(60);
        sb.append(":sout=#duplicate{dst=std{access=http,mux=ts,");
        sb.append("dst=");
        sb.append(serverAddress);
        sb.append(':');
        sb.append(serverPort);
        sb.append("}}");
        return sb.toString();
    }
}
