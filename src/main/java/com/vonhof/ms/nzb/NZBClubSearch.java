package com.vonhof.ms.nzb;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import nu.validator.htmlparser.common.XmlViolationPolicy;
import nu.validator.htmlparser.dom.HtmlDocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class NZBClubSearch implements NZBSearchEngine {
    private static final String url
            = "http://www.nzbclub.com/search.aspx?q=%s&rpp=100";
    
    public List<NZBSearchResult> search(String query) throws Exception {
        String queryUrl = String.format(url, query.replaceAll(" ", "+"));
        
        
        HtmlDocumentBuilder htmlDoc = new HtmlDocumentBuilder();
        htmlDoc.setIgnoringComments(true);
        htmlDoc.setContentSpacePolicy(XmlViolationPolicy.ALTER_INFOSET);
        Document doc = htmlDoc.parse(new InputSource(queryUrl));
        
        Element resultTablt = doc.getElementById("ctl00_ContentPlaceHolder2_RadGrid1_ctl00");
        Node tbodyNode = resultTablt.getChildNodes().item(5);
        NodeList rows = tbodyNode.getChildNodes();
        
        
        List<NZBSearchResult> out = new ArrayList<NZBSearchResult>();
        
        for(int i = 0; i < rows.getLength();i++) {
            Node row = rows.item(i);
            if (row.getNodeType() != Node.ELEMENT_NODE)
                continue;
            
            if (row.getTextContent().trim().equals("No records to display."))
                return out;
            
            String name = row.getChildNodes().item(3)
                                .getChildNodes().item(1)
                                .getChildNodes().item(0)
                                .getTextContent().trim();
            String size = row.getChildNodes().item(4)
                                .getChildNodes().item(1)
                                .getChildNodes().item(0)
                                .getTextContent().trim();
            String age = row.getChildNodes().item(5)
                                .getChildNodes().item(1)
                                .getTextContent().trim();
            
            String nzb = "http://www.nzbclub.com"+
                            row.getChildNodes().item(6)
                                .getChildNodes().item(1)
                                .getChildNodes().item(0)
                                .getAttributes().getNamedItem("href")
                                .getTextContent().trim();
            
            
            NZBSearchResult result = new NZBSearchResult();
            result.setAge(Integer.parseInt(age.replaceAll("[^0-9]+", "")));
            if (size.endsWith("GB"))
                result.setSize((int)(Double.parseDouble(size.replaceAll("[^0-9\\.]+", ""))*1000));
            else
                result.setSize((int)Double.parseDouble(size.replaceAll("[^0-9\\.]+", "")));
            result.setNzb(new URL(nzb));
            result.setName(name);
            
            out.add(result);
        }
        
        return out;
    }
}
