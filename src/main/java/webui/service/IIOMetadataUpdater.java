package webui.service;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;

import org.springframework.stereotype.Service;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("IIOMetadataUpdater")
public class IIOMetadataUpdater {

	public String fetchParameter(String filename) throws IOException {
		File out = new File(filename);
		try (ImageInputStream input = ImageIO.createImageInputStream(out)) {
			Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
			ImageReader reader = readers.next(); // TODO: Validate that there are readers

			reader.setInput(input);
			IIOMetadata metadata = reader.getImageMetadata(0);
			// Print the metadata to the console
//			String[] names = metadata.getMetadataFormatNames();
//			for (String name : names) {
//				Node root = metadata.getAsTree(name);
//				System.out.println("Metadata for format " + name + ":");
//				printNode(root, "");
//			}

			String value = getTextEntry(metadata, "parameters");
			System.out.println("value: " + value);
			return value;
		}
	}

	private void printNode(Node node, String indent) {
		System.out.println(indent + node.getNodeName() + ": " + node.getNodeValue());
		NamedNodeMap attributes = node.getAttributes();
		if (attributes != null) {
			for (int i = 0; i < attributes.getLength(); i++) {
				Node attribute = attributes.item(i);
				System.out.println(indent + "  " + attribute.getNodeName() + ": " + attribute.getNodeValue());
			}
		}
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			printNode(child, indent + "  ");
		}
	}

//	private static String createOutputName(final File file) {
//		String name = file.getName();
//		int dotIndex = name.lastIndexOf('.');
//
//		String baseName = name.substring(0, dotIndex);
//		String extension = name.substring(dotIndex);
//
//		return baseName + "_copy" + extension;
//	}
//
//	private static void addTextEntry(final IIOMetadata metadata, final String key, final String value)
//			throws IIOInvalidTreeException {
//		IIOMetadataNode textEntry = new IIOMetadataNode("TextEntry");
//		textEntry.setAttribute("keyword", key);
//		textEntry.setAttribute("value", value);
//
//		IIOMetadataNode text = new IIOMetadataNode("Text");
//		text.appendChild(textEntry);
//
//		IIOMetadataNode root = new IIOMetadataNode(IIOMetadataFormatImpl.standardMetadataFormatName);
//		root.appendChild(text);
//
//		metadata.mergeTree(IIOMetadataFormatImpl.standardMetadataFormatName, root);
//	}

	private String getTextEntry(final IIOMetadata metadata, final String key) {
		IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(IIOMetadataFormatImpl.standardMetadataFormatName);
		NodeList entries = root.getElementsByTagName("TextEntry");

		for (int i = 0; i < entries.getLength(); i++) {
			IIOMetadataNode node = (IIOMetadataNode) entries.item(i);
			if (node.getAttribute("keyword").equals(key)) {
				return node.getAttribute("value");
			}
		}

		return null;
	}
}