package com.vonhof.ms;

import com.vonhof.babelshark.BabelShark;
import com.vonhof.babelshark.BabelSharkInstance;
import com.vonhof.babelshark.Input;
import com.vonhof.babelshark.node.ObjectNode;
import com.vonhof.babelshark.node.SharkNode;
import com.vonhof.babelshark.node.SharkType;
import com.vonhof.ms.dto.SeriesDTO;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {
    private static final Config instance = new Config();
    private static final Logger LOG =  Logger.getLogger(Config.class.getName());
    
    public static Config instance() {
        return instance;
    }
    
    private static BabelSharkInstance bs = BabelShark.getDefaultInstance();
    
    public static ObjectNode getSetup() {
        try {
            return bs.read(new Input(Config.class.getResourceAsStream("/setup.json"),"json"), ObjectNode.class);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return new ObjectNode();
    }
    
    public static String getTmpDir() {
        String tmpDir = (String) getConfig().get("tmpDir");
        
        if (tmpDir.isEmpty())
            tmpDir = ".";
        
        if (!tmpDir.endsWith("/"))
            tmpDir += "/";
        return tmpDir;
    }
    
    public static Map<String,Object> getConfig() {
        ObjectNode setup = getSetup();
        SharkNode config = setup.get("config");
        return bs.read(config, SharkType.forMap(Map.class, Object.class));
    }
    
    public static List<SeriesDTO> getSeries() {
        ObjectNode setup = getSetup();
        SharkNode seriesNode = setup.get("series");
        return bs.read(seriesNode, SharkType.forCollection(List.class, SeriesDTO.class));
    }
}
