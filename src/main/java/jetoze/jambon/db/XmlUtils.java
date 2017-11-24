package jetoze.jambon.db;

import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

import org.xml.sax.SAXException;

import tzeth.exhume.sax.ExhumeSaxParser;

final class XmlUtils {
    public static <T> T loadFromFile(File file, Supplier<T> builder) throws SAXException, IOException {
        ExhumeSaxParser parser = new ExhumeSaxParser(builder);
        parser.parseFile(file);
        return builder.get();
    }
    
    public static <T> T loadFromXml(String xml, Supplier<T> builder) throws SAXException {
        ExhumeSaxParser parser = new ExhumeSaxParser(builder);
        parser.parseXml(xml);
        return builder.get();
    }

    private XmlUtils() {/**/}
}
