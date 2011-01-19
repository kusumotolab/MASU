package jp.ac.osaka_u.ist.sdl.scorpio.io;

import java.io.File;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import jp.ac.osaka_u.ist.sdl.scorpio.Scorpioui;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CodeCloneController;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.ElementInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.FileController;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.FileInfo;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class XMLReader extends DefaultHandler {

	private XMLReader(final String id) {
		this.id = id;
		this.stateStack = new Stack<STATE>();
	}

	public static void read(final String filename, final String id) {

		try {
			final SAXParserFactory factory = SAXParserFactory.newInstance();
			final SAXParser parser = factory.newSAXParser();
			parser.parse(new File(filename), new XMLReader(id));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * ドキュメント開始に実行
	 */
	@Override
	public void startDocument() {
		FileController.getInstance(Scorpioui.ID).clear();
		CodeCloneController.getInstance(Scorpioui.ID).clear();
	}

	/**
	 * 要素の開始タグ読み込み時
	 */
	@Override
	public void startElement(final String uri, final String localName,
			final String qName, final Attributes attributes) {

		final STATE state = this.getSTATE(qName);
		this.stateStack.push(state);

		switch (state) {
		case RESULT:
			break;
		case FILEINFO:
			break;
		case FILE:
			this.file = new FileInfo();
			break;
		case FILEID:
			break;
		case FILELOC:
			break;
		case PDGNODE:
			break;
		case DUPLICATEDRATIO:
			break;
		case FILEPATH:
			break;
		case CLONEINFO:
			break;
		case CLONESET:
			this.cloneset = new CloneSetInfo();
			break;
		case CLONE:
			this.codeclone = new CodeCloneInfo();
			break;
		case GAP:
			break;
		case METHOD:
			break;
		case ELEMENT:
			this.element = new ElementInfo();
			break;
		case OWNERFILEID:
			break;
		case FROMLINE:
			break;
		case FROMCOLUMN:
			break;
		case TOLINE:
			break;
		case TOCOLUMN:
			break;
		default:
			throw new IllegalStateException();
		}
	}

	/**
	 * テキストデータ読み込み時
	 */
	@Override
	public void characters(char[] ch, int offset, int length) {
		final STATE state = this.stateStack.peek();
		switch (state) {
		case RESULT:
			break;
		case FILEINFO:
			break;
		case FILE:
			break;
		case FILEID:
			this.file.setID(Integer.parseInt(new String(ch, offset, length)));
			break;
		case FILELOC:
			this.file
					.setLOC((Integer.parseInt(new String(ch, offset, length))));
			break;
		case PDGNODE:
			this.file.setNumberOfPDGNodes((Integer.parseInt(new String(ch,
					offset, length))));
			break;
		case DUPLICATEDRATIO:
			break;
		case FILEPATH:
			this.file.setName(new String(ch, offset, length));
			break;
		case CLONEINFO:
			break;
		case CLONESET:
			break;
		case CLONE:
			break;
		case GAP:
			this.codeclone.setNumberOfGapps(Integer.parseInt(new String(ch,
					offset, length)));
			break;
		case METHOD:
			this.codeclone.setNumberOfMethods(Integer.parseInt(new String(ch,
					offset, length)));
			break;
		case ELEMENT:
			break;
		case OWNERFILEID:
			this.element.setFileID(Integer.parseInt(new String(ch, offset,
					length)));
			break;
		case FROMLINE:
			this.element.setFromLine(Integer.parseInt(new String(ch, offset,
					length)));
			break;
		case FROMCOLUMN:
			this.element.setFromColumn(Integer.parseInt(new String(ch, offset,
					length)));
			break;
		case TOLINE:
			this.element.setToLine(Integer.parseInt(new String(ch, offset,
					length)));
			break;
		case TOCOLUMN:
			this.element.setToColumn(Integer.parseInt(new String(ch, offset,
					length)));
			break;
		default:
			throw new IllegalStateException();
		}
	}

	/**
	 * 要素の終了タグ読み込み時
	 */
	@Override
	public void endElement(String uri, String localName, String qName) {

		final STATE state = this.stateStack.pop();

		switch (state) {
		case RESULT:
			break;
		case FILEINFO:
			break;
		case FILE:
			FileController.getInstance(this.id).add(this.file);
			this.file = null;
			break;
		case FILEID:
			break;
		case FILELOC:
			break;
		case PDGNODE:
			break;
		case DUPLICATEDRATIO:
			break;
		case FILEPATH:
			break;
		case CLONEINFO:
			break;
		case CLONESET:
			CodeCloneController.getInstance(this.id).add(this.cloneset);
			this.cloneset = null;
			break;
		case CLONE:
			this.cloneset.add(this.codeclone);
			this.codeclone = null;
			break;
		case GAP:
			break;
		case METHOD:
			break;
		case ELEMENT:
			this.codeclone.add(this.element);
			this.element = null;
			break;
		case OWNERFILEID:
			break;
		case FROMLINE:
			break;
		case FROMCOLUMN:
			break;
		case TOLINE:
			break;
		case TOCOLUMN:
			break;
		default:
			throw new IllegalStateException();
		}
	}

	/**
	 * ドキュメント終了時
	 */
	@Override
	public void endDocument() {
	}

	private STATE getSTATE(final String tagname) {

		if (tagname.equals("RESULT")) {
			return STATE.RESULT;
		} else if (tagname.equals("FILEINFO")) {
			return STATE.FILEINFO;
		} else if (tagname.equals("FILE")) {
			return STATE.FILE;
		} else if (tagname.equals("FILEID")) {
			return STATE.FILEID;
		} else if (tagname.equals("PDGNODE")) {
			return STATE.PDGNODE;
		} else if (tagname.equals("DUPLICATEDRADIO")) {
			return STATE.DUPLICATEDRATIO;
		} else if (tagname.equals("FILEPATH")) {
			return STATE.FILEPATH;
		} else if (tagname.equals("FILELOC")) {
			return STATE.FILELOC;
		} else if (tagname.equals("CLONEINFO")) {
			return STATE.CLONEINFO;
		} else if (tagname.equals("CLONESET")) {
			return STATE.CLONESET;
		} else if (tagname.equals("CLONE")) {
			return STATE.CLONE;
		} else if (tagname.equals("GAP")) {
			return STATE.GAP;
		} else if (tagname.equals("METHOD")) {
			return STATE.METHOD;
		} else if (tagname.equals("ELEMENT")) {
			return STATE.ELEMENT;
		} else if (tagname.equals("OWNERFILEID")) {
			return STATE.OWNERFILEID;
		} else if (tagname.equals("FROMLINE")) {
			return STATE.FROMLINE;
		} else if (tagname.equals("FROMCOLUMN")) {
			return STATE.FROMCOLUMN;
		} else if (tagname.equals("TOLINE")) {
			return STATE.TOLINE;
		} else if (tagname.equals("TOCOLUMN")) {
			return STATE.TOCOLUMN;
		} else {
			throw new IllegalStateException();
		}
	}

	private final Stack<STATE> stateStack;

	private FileInfo file;

	private ElementInfo element;

	private CodeCloneInfo codeclone;

	private CloneSetInfo cloneset;

	private final String id;

	private enum STATE {
		RESULT, FILEINFO, FILE, FILEID, FILELOC, PDGNODE, DUPLICATEDRATIO, FILEPATH, CLONEINFO, CLONESET, CLONE, GAP, METHOD, ELEMENT, OWNERFILEID, FROMLINE, FROMCOLUMN, TOLINE, TOCOLUMN, ;

	}
}
