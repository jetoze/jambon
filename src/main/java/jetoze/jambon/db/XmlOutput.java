package jetoze.jambon.db;

import java.io.File;
import java.io.IOException;

import tzeth.exhume.XmlBuilder;

final class XmlOutput {
    private final XmlBuilder xml;
    
    XmlOutput(XmlBuilder xml) {
        this.xml = xml;
    }
    
    public String toXml() {
        return this.xml.toXml();
    }
    
    public void toFile(File file) throws IOException {
        this.xml.writeToFile(file);
    }
}