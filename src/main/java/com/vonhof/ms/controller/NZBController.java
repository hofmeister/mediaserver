package com.vonhof.ms.controller;

import com.cheesmo.nzb.client.ClientConfig;
import com.cheesmo.nzb.client.NzbClient;
import com.vonhof.babelshark.annotation.Name;
import com.vonhof.ms.dto.DownloadDTO;
import com.vonhof.ms.dto.NZBServerDTO;
import com.vonhof.webi.annotation.Parm;
import com.vonhof.webi.annotation.Path;
import java.net.MalformedURLException;
import java.net.URL;

@Path("nzb")
@Name("nzb")
public class NZBController {
    private NZBServerDTO server = new NZBServerDTO();
    
    private ClientConfig config = new ClientConfig();
    
    
    private void sync() {
        config.setDownloadDir("/Users/Henrik/tmp");
        config.setCacheDir("/Users/Henrik/tmp/cache");
        config.setServer(server.getHost());
        config.setPort(server.getPort());
        config.setMaxConnections(server.getConnections());
        
        config.setUsername(server.getUsername());
        config.setPassword(server.getPassword());
    }
            
            
    
    public NZBServerDTO setup( String host,
                                String username,
                                String password,
                                @Parm(defaultValue="23") int port,
                                @Parm(defaultValue="10") int connections) {
        server.setHost(host);
        server.setPort(port);
        server.setConnections(connections);
        server.setUsername(username);
        server.setPassword(password);
        
        sync();
        
        return server;
    }
    
    public DownloadDTO download(final @Parm(required=true) String nzb) throws MalformedURLException {
        DownloadDTO out = new DownloadDTO();
        
        URL url = new URL(nzb);
        out.setFilename(url.getFile());
        out.setPercent(0);
        
        final NzbClient client = new NzbClient(config);
        
        new Thread("downloading") {
            @Override
            public void run() {
                client.start(nzb);
            }
        }.start();
        
        
        return out;
    }
    
}
