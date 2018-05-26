package pdftoxmlconverter;

import com.itextpdf.license.LicenseKey;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.XfaForm;
import com.itextpdf.tool.xml.xtra.xfa.XFAFlattener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PDFTOXMLCONVERTER {

    public static final String SRC = "/home/sanjaynegi/PDF-XML/MCA_SIgnature.pdf";
    public static final String DEST = "/home/sanjaynegi/PDF-XML/MCA_SIgnature.xml";

    public static void main(String[] args) throws IOException, DocumentException, TransformerException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PDFTOXMLCONVERTER().readXml(SRC, DEST);
    }

    public void readXml(String src, String dest)
            throws IOException, DocumentException, TransformerException {
        PdfReader reader = new PdfReader(src);
       
        AcroFields form = reader.getAcroFields();
        XfaForm xfa = form.getXfa();
         
        Node node = xfa.getDatasetsNode();
        
        
        NodeList list = node.getChildNodes();
           
        for (int i = 0; i < list.getLength(); i++) {
            if ("data".equals(list.item(i).getLocalName())) {
                node = list.item(i);
                break;
            }
        }
        list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            if ("movies".equals(list.item(i).getLocalName())) {
                node = list.item(i);
                break;
            }
        }
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        FileOutputStream os = new FileOutputStream(dest);
        tf.transform(new DOMSource(node), new StreamResult(os));
        reader.close();
    }

}
