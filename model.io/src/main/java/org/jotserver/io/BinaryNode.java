package org.jotserver.io;

import org.jotserver.net.CData;
import org.jotserver.net.CDataInputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BinaryNode {
	
	private static final int NODE_START = 0xFE;
	private static final int NODE_END = 0xFF;
	private static final int ESCAPE_CHAR = 0xFD;
	
	private List<BinaryNode> children;
	private int type;
	private ByteArrayOutputStream data;

	public static BinaryNode load(String file) throws IOException {
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		BinaryNode ret = load(in);
		in.close();
		return ret;
	}
	
	public static BinaryNode load(InputStream in) throws IOException {
		CDataInputStream cin = new CDataInputStream(in);
		long version = cin.readU32();
		if(version > 0) {
			throw new IOException("Invalid version!");
		} else {
			BinaryNode root = new BinaryNode();
			if(cin.readByte() == NODE_START) {
				root.parseNode(cin);
				return root;
			} else {
				throw new IOException("Invalid format.");
			}
		}
	}
	
	private BinaryNode() {
		children = new ArrayList<BinaryNode>();
		type = 0;
		data = new ByteArrayOutputStream();
	}
	
	private void parseNode(CDataInputStream in) throws IOException {
		type = in.readByte();
		int b = in.readByte();
		while(b != NODE_END) {
			
			if(b == NODE_START) {
				BinaryNode child = new BinaryNode();
				addChild(child);
				child.parseNode(in);
			} else {
				if(b == ESCAPE_CHAR) {
					b = in.readByte();
				}
				CData.writeByte(data, b);
			}
			b = in.readByte();
		}
	}

	public int getType() {
		return type;
	}
	
	private void addChild(BinaryNode node) {
		children.add(node);
	}
	
	public InputStream getDataStream() {
		return new ByteArrayInputStream(data.toByteArray());
	}

	public List<BinaryNode> getChildren() {
		return children;
	}

	public BinaryNode getFirstChild() {
		return children.get(0);
	}
}
