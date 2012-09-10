package com.vonhof.ms.controller;

import com.cheesmo.nzb.client.ClientConfig;
import com.cheesmo.nzb.client.NzbClient;
import com.vonhof.babelshark.annotation.Name;
import com.vonhof.ms.Config;
import com.vonhof.ms.dto.DownloadDTO;
import com.vonhof.ms.dto.NZBServerDTO;
import com.vonhof.webi.annotation.Parm;
import com.vonhof.webi.annotation.Path;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Path("nzb")
@Name("nzb")
public class NZBController {
    private NZBServerDTO server = new NZBServerDTO();
    
    private ClientConfig config = new ClientConfig();
    
    
    public void sync() {
        Map<String, Object> configMap = Config.getConfig();
        config.setDownloadDir((String)configMap.get("tmpDir"));
        config.setCacheDir((String)configMap.get("cacheDir"));
        config.setServer((String)configMap.get("server"));
        config.setPort((Integer)configMap.get("port"));
        config.setMaxConnections((Integer)configMap.get("maxConnections"));
        
        config.setUsername((String)configMap.get("username"));
        config.setPassword((String)configMap.get("password"));
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
    
    public List<String> download(final @Parm(required=true) URL nzbUrl) {
        final NzbClient client = new NzbClient(config);
        
        return client.start(nzbUrl.toString());
    }
    
}
