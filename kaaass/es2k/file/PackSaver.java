package kaaass.es2k.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import kaaass.es2k.log.Log;
import kaaass.es2k.log.Logger;
import kaaass.es2k.packet.Packet;
import kaaass.es2k.packet.PacketContainer;
import kaaass.es2k.packet.PacketContainer.Key;
import kaaass.es2k.packet.PacketRegister;
import kaaass.es2k.packet.Path;
import kaaass.es2k.packet.pack.DataPacket;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PackSaver {
	public static Logger logger = new Logger("PackSaver");
	
	private File file;
	private Document document;

	public PackSaver() {
		try {
			file = new File("data/packet.xml");
			if (!new File("data").exists()) {
				new File("data").mkdir();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.document = builder.parse(file.getPath());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean writeData() {
		try {
			Log.d(logger, "writeData");
			for (int i : PacketRegister.saveableList) {
				Packet p = PacketRegister.packetList.get(i);
				this.document
						.appendChild(getElementByPath(p.getRootPath(), -1));
				Log.d(logger, "writeData:" + p.getRootPath());
			}
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			PrintWriter pw = new PrintWriter(new FileOutputStream(file));
			StreamResult result = new StreamResult(pw);
			transformer.transform(source, result);
			Log.d(logger, "生成XML文件成功!");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private Element getElementByPath(Path pth, int startIndex) {
		Log.d(logger, "getElementByPath(" + pth.toString() + ", " + startIndex + ")");
		if (pth.getType() == Path.NULL)
			return null;
		Element result = null;
		PacketContainer p = PacketRegister.getContainerByPath(pth);
		if (startIndex == -1) {
			result = this.document.createElement("packet");
			result.setAttribute("name", pth.getRoot());
		} else if (startIndex >= 0) {
			result = this.document.createElement("tag");
			result.setAttribute("name", pth.getTag(startIndex));
		}
		for (Key k : p.getKeyList()) {
			if (k.getSaveable()) {
				Element tem = this.document.createElement("key");
				tem.setAttribute("type", k.getTypeName());
				switch (k.getType()) {
				case Key.INT:
					tem.setAttribute("metadata", k.getMetaName());
					tem.appendChild(this.document.createTextNode(String
							.valueOf(k.getLong())));
					continue;
				case Key.FLOAT:
					if (k.getMetadata() == Key.META_FLOAT) {
						tem.setAttribute("metadata", k.getMetaName());
						tem.appendChild(this.document.createTextNode(String
								.valueOf(k.getFloat())));
					} else {
						tem.setAttribute("metadata", k.getMetaName());
						tem.appendChild(this.document.createTextNode(String
								.valueOf(k.getDouble())));
					}
					continue;
				case Key.BOOLEAN:
					tem.appendChild(this.document.createTextNode(String
							.valueOf((Boolean) k.getValue())));
					continue;
				case Key.ARRAY:
					String str = "";
					tem.setAttribute("metadata", k.getMetaName());
					switch (k.getMetadata()) {
					case Key.META_INT:
						for (long i : (long[]) k.getValue()) {
							str += "," + i;
						}
						tem.appendChild(this.document.createTextNode(str
								.substring(1)));
						continue;
					case Key.META_FLOAT:
					case Key.META_DOUBLE:
						for (double i : (double[]) k.getValue()) {
							str += "," + i;
						}
						tem.appendChild(this.document.createTextNode(str
								.substring(1)));
						continue;
					case Key.META_STRING:
						for (String i : (String[]) k.getValue()) {
							str += "," + i;
						}
						tem.appendChild(this.document.createTextNode(str
								.substring(1)));
						continue;
					}
				case Key.STRING:
					tem.appendChild(this.document.createTextNode((String) k
							.getValue()));
					continue;
				}
				result.appendChild(tem);
			}
		}
		for (PacketContainer i : p.getTagList()) {
			result.appendChild(getElementByPath(i.getPath(), startIndex + 1));
		}
		return result;
	}

	public DataPacket readData(Path path) {
		return null;
	}

	public void createXml(String fileName) {
		Element root = this.document.createElement("employees");
		this.document.appendChild(root);
		Element employee = this.document.createElement("employee");
		employee.setAttribute("a", "b");
		Element name = this.document.createElement("name");
		name.appendChild(this.document.createTextNode("丁宏亮"));
		employee.appendChild(name);
		Element sex = this.document.createElement("sex");
		sex.appendChild(this.document.createTextNode("m"));
		employee.appendChild(sex);
		Element age = this.document.createElement("age");
		age.appendChild(this.document.createTextNode("30"));
		employee.appendChild(age);
		root.appendChild(employee);
		TransformerFactory tf = TransformerFactory.newInstance();
		try {
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.ENCODING, "gb2312");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
			StreamResult result = new StreamResult(pw);
			transformer.transform(source, result);
			System.out.println("生成XML文件成功!");
		} catch (TransformerConfigurationException e) {
			System.out.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (TransformerException e) {
			System.out.println(e.getMessage());
		}
	}

	public void parserXml(String fileName) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(fileName);
			NodeList employees = document.getChildNodes();
			for (int i = 0; i < employees.getLength(); i++) {
				Node employee = employees.item(i);
				NodeList employeeInfo = employee.getChildNodes();
				for (int j = 0; j < employeeInfo.getLength(); j++) {
					Node node = employeeInfo.item(j);
					NodeList employeeMeta = node.getChildNodes();
					for (int k = 0; k < employeeMeta.getLength(); k++) {
						System.out.println(employeeMeta.item(k).getNodeName()
								+ ":" + employeeMeta.item(k).getTextContent());
					}
				}
			}
			System.out.println("解析完毕");
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println(e.getMessage());
		} catch (SAXException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}