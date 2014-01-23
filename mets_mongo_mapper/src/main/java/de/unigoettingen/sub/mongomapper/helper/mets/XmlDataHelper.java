package de.unigoettingen.sub.mongomapper.helper.mets;

import com.mongodb.BasicDBObject;
import de.unigoettingen.sub.mongomapper.helper.BasicDBObjectHelper;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jpanzer.
 * mets_mongo_mapper.
 * <p/>
 * 12/2013
 * <p/>
 * The methods of this class should be helpful to process xmlData elements.
 */
class XmlDataHelper {

    private final BasicDBObjectHelper basicDBObjectHelper = new BasicDBObjectHelper();

    public XmlDataHelper() {

    }

    /**
     * Processes the the xmlData node. It retrieves elements attributes,
     * values and childnodes and stores these in a BasicDBObject.
     *
     * @param xmlData The xmlData node to process.
     * @return The (JSON-like) object in which the retrieved content is stored.
     */
    public BasicDBObject processXmlData(Node xmlData) {


        BasicDBObject basicDBObject = new BasicDBObject();

        // process the mods:mods attributes
        Node mods = xmlData.getFirstChild();

        if (mods != null) {

            NamedNodeMap attr_map = mods.getAttributes();
            Node attr_node;

            attr_node = attr_map.getNamedItem("ID");
            if (attr_node != null)
                basicDBObjectHelper.createBasicDBObject("ID", attr_node.getNodeValue(), basicDBObject);

            attr_node = attr_map.getNamedItem("version");
            if (attr_node != null)
                basicDBObjectHelper.createBasicDBObject("version", attr_node.getNodeValue(), basicDBObject);


            // process the mods:...
            List<BasicDBObject> basicDBObjectList = new ArrayList<BasicDBObject>();
            processChildNodes(getListFromNodeList(mods.getChildNodes()), basicDBObjectList);
            basicDBObject.append(mods.getNodeName(), basicDBObjectList);

        }
        return basicDBObject;
    }

    private List<Node> getListFromNodeList(NodeList childNodes) {

        List<Node> nodeList = new ArrayList<Node>();
        for (int n = 0; n < childNodes.getLength(); n++) {
            Node node1 = childNodes.item(n);
            if (node1.getNodeType() == Node.ELEMENT_NODE)
                nodeList.add(node1);
        }
        return nodeList;
    }


    /**
     * Processes the the child node and its child recursivly. It retrieves
     * elements attributes, values and child nodes and stores these in a
     * BasicDBObject.
     *
     * @param children          The child node to process.
     * @param basicDBObjectList List of BasicDBObjects in which to append the retrueved child elements
     */
    private void processChildNodes(List<Node> children, List<BasicDBObject> basicDBObjectList) {


        for (Node child : children) {

            BasicDBObject node_basicDBObject = new BasicDBObject();
            BasicDBObject elemets_basicDBObject = new BasicDBObject();

            // process attributes
            NamedNodeMap namedNodeMap = child.getAttributes();

            processAttributes(namedNodeMap, elemets_basicDBObject, child);

            NodeList childNodeList = child.getChildNodes();

            // process values
            Node node = child.getFirstChild();
            if (node != null && node.getNodeType() == Node.TEXT_NODE) {

                String value = node.getNodeValue();
                if (value != null && !value.equals("")) {
                    processValue(value, elemets_basicDBObject);
                }
            }

            List<BasicDBObject> basicDBObjectList1 = new ArrayList<BasicDBObject>();
            processChildNodes(getListFromNodeList(childNodeList), basicDBObjectList1);
            if (!basicDBObjectList1.isEmpty())
                elemets_basicDBObject.append("child", basicDBObjectList1);

            node_basicDBObject.append(child.getNodeName(), elemets_basicDBObject);
            basicDBObjectList.add(node_basicDBObject);
        }
    }

    private void processValue(String nodeValue, BasicDBObject basicDBObject) {

        if (nodeValue != null && !nodeValue.equals(""))
            basicDBObject.append("value", nodeValue.trim().replaceAll("\\s+", " "));
    }

    private void processAttributes(NamedNodeMap attributes, BasicDBObject basicDBObject, Node child) {

        // process attributes
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute_node = attributes.item(i);
            String attr = attribute_node.getNodeName();
            String value = attribute_node.getNodeValue().trim().replaceAll("\\s+", " ");
            basicDBObject.append(attr, value);
        }
    }
}
