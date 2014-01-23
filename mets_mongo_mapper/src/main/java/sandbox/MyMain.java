package sandbox;

import com.mongodb.BasicDBObject;
import de.unigoettingen.sub.tei.mongomapper.helper.teiHelper.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.List;

/**
 * Created by jpanzer on 17.01.14.
 */
public class MyMain {

    //private final String filePath = "/Users/jpanzer/Documents/projects/mongo_mapper/resources/dennis/digizeit/tei/PPN345572572_0053.xml";
    //private final String filePath = "/Users/jpanzer/Documents/projects/mongo_mapper/resources/tmp/text.xml";
    private final String filePath = "/Users/jpanzer/Documents/projects/mongo_mapper/resources/tmp/text1.xml";
    private final InputSource inputSource;
    private int count = 0;

    public static void main(String args[]) throws JAXBException, ParserConfigurationException, SAXException, IOException, XMLStreamException {
        new MyMain();
    }

    public MyMain() throws ParserConfigurationException, SAXException, IOException, XMLStreamException {

//        String path = new File(filePath).getAbsolutePath();
//        if (File.separatorChar != '/') {
//            path = path.replace(File.separatorChar, '/');
//        }
//
//        if (!path.startsWith("/")) {
//            path = "/" + path;
//        }
//        System.out.println(path);

        FileInputStream fileReader = new FileInputStream(filePath);
        this.inputSource = new InputSource(fileReader);
        //processWithJAXB();
        //processWithSAX();


        //processWithStaxIteratorLike();
        processWithStaxCursorLike(fileReader);
    }


    /**
     * like an iterator
     * @param fileInputStream
     */
    private void processWithStaxCursorLike(FileInputStream fileInputStream) throws XMLStreamException {
        MyStaxHandler sh = null;
        try {
            sh = new MyStaxHandler(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BasicDBObject basicDBObject = sh.processXMLFile();

    }


    /**
     * like a cursor
     * is more memory-efficient
     */
    private void processWithStaxIteratorLike(FileInputStream fileInputStream) throws XMLStreamException {
        MyStaxHandler sh = null;
        try {
            sh = new MyStaxHandler(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BasicDBObject basicDBObject = sh.processXMLFile();

    }


    private void processWithSAX() throws ParserConfigurationException, SAXException, IOException {

        MySAXHandler mySAXHandler = new MySAXHandler(this.filePath);

    }


    public void processWithJAXB() {

        JAXBContext jc = null;
        try {

            jc = JAXBContext.newInstance(TEI2.class);
            Unmarshaller um = jc.createUnmarshaller();
            TEI2 tei = (TEI2) um.unmarshal(new File(filePath));

            TeiHeader teiHeader = tei.getTeiHeader();
            System.out.println(teiHeader.toString());

            Text text = tei.getText();
            Body body = text.getBody();
            List<Object> list = body.getPOrMilestoneOrFigure();
            for (Object obj : list) {


                if (obj.getClass() == P.class)
                    processP((P) obj);
                else if (obj.getClass() == Milestone.class)
                    processMilestone((Milestone) obj);
                else if (obj.getClass() == Figure.class)
                    processFigure((Figure) obj);


                else if (obj.getClass() == Pb.class) {
                    processPb((Pb) obj);
                    this.count++;
                    if (count == 3)
                        break;
                }

            }


        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    private void processP(P obj) {
        System.out.println("id: " + obj.getId());

        for (Object o : obj.getSeqOrW()) {
            if (o.getClass() == Seq.class) {
                processSeq((Seq) o);
            } else if (o.getClass() == W.class) {
                processW((W) o);
            }
        }


    }

    private void processSeq(Seq o) {
        List<W> wList = o.getW();
        for (W w : wList) {
            processW(w);
        }


    }

    private void processW(W w) {
        System.out.println("w...");
        System.out.println(w.getFunction());
        System.out.println(w.getValue());

    }

    private void processMilestone(Milestone ms) {
        System.out.println("ms...");
        System.out.println(ms.getN());
        System.out.println(ms.getType());
    }


    private void processFigure(Figure fig) {
        System.out.println("fig...");
        System.out.println("id: " + fig.getId());
        System.out.println("function: " + fig.getFunction());

    }

    private void processPb(Pb pb) {
        System.out.println("pb...");
        pb.toString();

    }

}
