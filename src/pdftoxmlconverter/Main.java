

package pdftoxmlconverter;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.XfaForm;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import static pdftoxmlconverter.Main.streamResult;
/**
 *
 * @author sapan.kumar1
 */
public class Main {

    static StreamResult streamResult;
	static TransformerHandler handler;
	static AttributesImpl atts;

   public static void main(String[] args) throws IOException, DocumentException, TransformerException{
        selectPDFFiles();
 }


 //allow pdf files selection for converting
 public static void selectPDFFiles() throws IOException, DocumentException, TransformerException{

  JFileChooser chooser = new JFileChooser();
      FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF","pdf");
      chooser.setFileFilter(filter);
      chooser.setMultiSelectionEnabled(true);
      int returnVal = chooser.showOpenDialog(null);
      if(returnVal == JFileChooser.APPROVE_OPTION) {
               File[] Files=chooser.getSelectedFiles();
               System.out.println("Please wait...");
               for( int i=0;i<Files.length;i++){

               String fname =Files[i].getAbsolutePath().replaceAll(".pdf","");
             
                   
               System.out.println(fname+".pdf");
              
                convertPDFToXML(Files[i].toString(),fname+".xml");
                }
   System.out.println("Conversion complete");
                }

      
      

  }
 public static void convertPDFToXML(String SRC,String  DEST) throws IOException, DocumentException, TransformerException{
         File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Main().readXml(SRC, DEST);
      
      }
 public void readXml(String src, String dest)
            throws IOException, DocumentException, TransformerException {
     
     try{
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
     catch(Exception e){
         System.out.println(e);
     }
    }

}