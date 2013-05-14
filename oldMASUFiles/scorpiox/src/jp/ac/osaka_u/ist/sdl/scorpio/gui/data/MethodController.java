package jp.ac.osaka_u.ist.sdl.scorpio.gui.data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * GUI�Ń��\�b�h���Ǘ����邽�߂̃N���X
 * 
 * @author higo
 */
public class MethodController {

	private MethodController() {
		this.methods = new TreeMap<Integer, MethodInfo>();
	}

	/**
	 * ���\�b�h��ǉ�����
	 * 
	 * @param method
	 *            �ǉ����郁�\�b�h
	 */
	public void add(final MethodInfo method) {

		if (null == method) {
			throw new IllegalArgumentException();
		}

		this.methods.put(method.getID(), method);
	}

	/**
	 * ID�Ŏw�肳�ꂽ���\�b�h��Ԃ�
	 * 
	 * @param id
	 *            ���\�b�h��ID
	 * @return ID�Ŏw�肳�ꂽ���\�b�h
	 */
	public MethodInfo getMethod(final int id) {
		return this.methods.get(id);
	}

	/**
	 * ���\�b�h��Collection��Ԃ�
	 * 
	 * @return ���\�b�h��Collection
	 */
	public Collection<MethodInfo> getMethods() {
		return Collections.unmodifiableCollection(this.methods.values());
	}

	/**
	 * ���\�b�h�̐���Ԃ�
	 * 
	 * @return�@���\�b�h�̐�
	 */
	public int getNumberOfMethods() {
		return this.methods.size();
	}

	/**
	 * �t�@�C�������ׂď���
	 */
	public void clear() {
		this.methods.clear();
	}

	private final SortedMap<Integer, MethodInfo> methods;

	/**
	 * �w�肳�ꂽFileController��Ԃ�
	 * 
	 * @param id
	 * @return
	 */
	public static MethodController getInstance(final String id) {

		MethodController controller = map.get(id);
		if (null == controller) {
			controller = new MethodController();
			map.put(id, controller);
		}

		return controller;
	}

	private static final Map<String, MethodController> map = new HashMap<String, MethodController>();
}
