package jp.ac.osaka_u.ist.sdl.scdetector.settings;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.DISSOLUTION;

public final class Configuration {

	public static Configuration INSTANCE = new Configuration();

	private Configuration() {
		this.c = Integer.MAX_VALUE;
		this.d = new HashSet<String>();
		this.l = null;
		this.m = SMALL_METHOD.UNHASHED;
		this.e = MERGE.FALSE;
		this.f = DISSOLUTION.FALSE;
		this.g = OUTPUT_FORMAT.SET;
		this.h = HEURISTICS.ON;
		this.o = null;
		this.p = PDG_TYPE.INTRA;
		this.q = new HashSet<DEPENDENCY_TYPE>();
		this.q.add(DEPENDENCY_TYPE.DATA);
		this.q.add(DEPENDENCY_TYPE.CONTROL);
		this.q.add(DEPENDENCY_TYPE.EXECUTION);
		this.s = 7;
		this.t = new HashSet<SLICE_TYPE>();
		this.t.add(SLICE_TYPE.BACKWARD);
		this.t.add(SLICE_TYPE.FORWARD);
		this.u = CONTROL_FILTER.NO_USE;
		this.v = VERBOSE.FALSE;
		this.w = 1;
		this.x = Integer.MAX_VALUE;
		this.y = Integer.MAX_VALUE;
		this.z = Integer.MAX_VALUE;
		this.pv = VARIABLE_NORMALIZATION.TYPE;
		this.pi = CALL_NORMALIZATION.NO;
		this.pc = CAST_NORMALIZATION.NO;
		this.po = OPERATION_NORMALIZATION.NO;
		this.pl = LITERAL_NORMALIZATION.TYPE;
		this.pr = REFERENCE_NORMALIZATION.NO;
	}

	public int getC() {
		return this.c;
	}

	public void setC(final int c) {
		this.c = c;
	}

	public Set<String> getD() {
		return Collections.unmodifiableSet(this.d);
	}

	public void addD(final String d) {
		this.d.add(d);
	}

	public MERGE getE() {
		return this.e;
	}

	public void setE(final MERGE e) {
		this.e = e;
	}

	public DISSOLUTION getF() {
		return this.f;
	}

	public void setF(final DISSOLUTION f) {
		this.f = f;
	}

	public OUTPUT_FORMAT getG() {
		return this.g;
	}

	public void setG(final OUTPUT_FORMAT g) {
		this.g = g;
	}

	public HEURISTICS getH() {
		return this.h;
	}

	public void setH(final HEURISTICS h) {
		this.h = h;
	}

	public String getL() {
		return this.l;
	}

	public void setL(final String l) {
		this.l = l;
	}

	public SMALL_METHOD getM() {
		return this.m;
	}

	public void setM(final SMALL_METHOD m) {
		this.m = m;
	}

	public String getO() {
		return this.o;
	}

	public void setO(final String o) {
		this.o = o;
	}

	public PDG_TYPE getP() {
		return this.p;
	}

	public void setP(final PDG_TYPE p) {
		this.p = p;
	}

	public Set<DEPENDENCY_TYPE> getQ() {
		return Collections.unmodifiableSet(this.q);
	}

	public void addQ(final DEPENDENCY_TYPE dependency) {
		this.q.add(dependency);
	}

	public void resetQ() {
		this.q.clear();
	}

	public int getS() {
		return this.s;
	}

	public void setS(final int s) {
		this.s = s;
	}

	public Set<SLICE_TYPE> getT() {
		return Collections.unmodifiableSet(this.t);
	}

	public void addT(SLICE_TYPE t) {
		this.t.add(t);
	}

	public void resetT() {
		this.t.clear();
	}

	public CONTROL_FILTER getU() {
		return this.u;
	}

	public void setU(CONTROL_FILTER u) {
		this.u = u;
	}

	public VERBOSE getV() {
		return this.v;
	}

	public void setV(final VERBOSE v) {
		this.v = v;
	}

	public int getW() {
		return this.w;
	}

	public void setW(final int w) {
		this.w = w;
	}

	public int getX() {
		return this.x;
	}

	public void setX(final int x) {
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(final int y) {
		this.y = y;
	}

	public int getZ() {
		return this.z;
	}

	public void setZ(final int z) {
		this.z = z;
	}

	public VARIABLE_NORMALIZATION getPV() {
		return this.pv;
	}

	public void setPV(final VARIABLE_NORMALIZATION pv) {
		this.pv = pv;
	}

	public CALL_NORMALIZATION getPI() {
		return this.pi;
	}

	public void setPI(final CALL_NORMALIZATION pi) {
		this.pi = pi;
	}

	public CAST_NORMALIZATION getPC() {
		return this.pc;
	}

	public void setPC(final CAST_NORMALIZATION pc) {
		this.pc = pc;
	}

	public OPERATION_NORMALIZATION getPO() {
		return this.po;
	}

	public void setPO(final OPERATION_NORMALIZATION po) {
		this.po = po;
	}

	public LITERAL_NORMALIZATION getPL() {
		return this.pl;
	}

	public void setPL(final LITERAL_NORMALIZATION pl) {
		this.pl = pl;
	}

	public REFERENCE_NORMALIZATION getPR() {
		return this.pr;
	}

	public void setPR(final REFERENCE_NORMALIZATION pr) {
		this.pr = pr;
	}

	/**
	 * �n�b�V���l����������臒l�ȏ�̌�����ꍇ�́C�X���C�X���_�Ƃ��Ȃ����߂̃I�v�V����
	 */
	private int c;

	/**
	 * ��͑Ώۃf�B���N�g�����w�肷�邽�߂̃I�v�V����
	 */
	private Set<String> d;

	/**
	 * �n�b�V���l�������ŘA�����đ��݂��Ă��钸�_���W�񂷂邩���w�肷�邽�߂̃I�v�V����
	 */
	private MERGE e;

	/**
	 * ���⎮���ח��x�܂ŕ������邩�ǂ������w�肷�邽�߂̃I�v�V����
	 */
	private DISSOLUTION f;

	/**
	 * �o�͌`�����w�肷�邽�߂̃I�v�V����
	 */
	private OUTPUT_FORMAT g;

	/**
	 * �o�����Ɋ�Â��t�B���^�����O���s�����ǂ������w�肷�邽�߂̃I�v�V����
	 */
	private HEURISTICS h;

	/**
	 * ��͑Ώۃv���O���~���O������w�肷�邽�߂̃I�v�V����
	 */
	private String l;

	/**
	 * 臒l�������������\�b�h���n�b�V�������邩�ǂ������w�肷�邽�߂̃I�v�V����
	 */
	private SMALL_METHOD m;

	/**
	 * ��͌��ʂ��o�͂���t�@�C�����w�肷�邽�߂̃I�v�V����
	 */
	private String o;

	/**
	 * �p����PDG�̃^�C�v���w�肷�邽�߂̃I�v�V����
	 */
	private PDG_TYPE p;

	/**
	 * PDG�ŗp����ˑ��֌W���w�肷�邽�߂̃I�v�V����
	 */
	private Set<DEPENDENCY_TYPE> q;

	/**
	 * �o�͂���R�[�h�N���[���̃T�C�Y�̉������w�肷�邽�߂̃I�v�V�����D �傫���͕��̐���\���D
	 */
	private int s;

	/**
	 * �p����X���C�X�̃^�C�v���w�肷�邽�߂̃I�v�V����
	 */
	private Set<SLICE_TYPE> t;

	/**
	 * �R���g���[���m�[�h�݂̂��X���C�X��_�ɂ��邩�ǂ����̃I�u�W����
	 */
	private CONTROL_FILTER u;

	/**
	 * �璷�ȏo�͂��s�����߂̃I�v�V����
	 */
	private VERBOSE v;

	/**
	 * ���o�ɗp����X���b�h�����w�肷�邽�߂̃I�v�V����
	 */
	private int w;

	/**
	 * �f�[�^�ˑ��ӂ��������_���\�[�X�R�[�h��ŗ���Ă��Ă��悢����̒l
	 */
	private int x;

	/**
	 * ����ˑ����������_���\�[�X�R�[�h��ŗ���Ă��Ă��悢����̒l
	 */
	private int y;

	/**
	 * ���s�ˑ����������_���\�[�X�R�[�h��ŗ���Ă��Ă��悢����̒l
	 */
	private int z;

	/**
	 * �ϐ����p�̐��K�����x�����w�肷�邽�߂̃I�v�V���� no: �ϐ��������̂܂܎g�� type: �ϐ������^���ɐ��K������D all:
	 * �S�Ă̕ϐ��𓯈ꎚ��ɐ��K������D
	 */
	private VARIABLE_NORMALIZATION pv;

	/**
	 * (���\�b�h�܂��̓R���X�g���N�^)�Ăяo���̐��K�����x�����w�肷�邽�߂̃I�v�V���� no: �Ăяo�����͂��̂܂܁C���������p����
	 * type_with_arg: �Ăяo������Ԃ�l�̌^���ɕϊ�����C���������p����D type_without_arg:
	 * �Ăяo������Ԃ�l�̌^���ɕϊ�����C�������͗p���Ȃ��D all: �S�Ă̌Ăяo���𓯈ꎚ��ɐ��K������D�������͗p���Ȃ�
	 */
	private CALL_NORMALIZATION pi;

	/**
	 * �L���X�g�g�p�̐��K�����x�����w�肷�邽�߂̃I�v�V���� 0: �L���X�g�g�p�����̂܂ܗp����D 1: �L���X�g�g�p���^�ɐ��K������D 2:
	 * �S�ẴL���X�g�g�p�𓯈ꎚ��ɐ��K������D
	 */
	private CAST_NORMALIZATION pc;

	/**
	 * 
	 * �P�����Z�C�񍀉��Z�C�O�����Z�̐��K�����x�����w�肷�邽�߂̃I�v�V���� no: ���Z�����̂܂ܗp���� type: ���Z�����̌^�ɐ��K������ all:
	 * �S�Ẳ��Z�𓯈�̎���ɐ��K������
	 */
	private OPERATION_NORMALIZATION po;

	/**
	 * 
	 * ���e�����̐��K�����x�����w�肷�邽�߂̃I�v�V���� no: ���e���������̂܂ܗp���� type: ���e���������̌^�̐��K������ all:
	 * �S�Ẵ��e�����𓯈�̎���ɐ��K������
	 */
	private LITERAL_NORMALIZATION pl;

	/**
	 * 
	 * �N���X�Q�Ɩ��𐳋K�����邽�߂̃I�v�V���� 0: �N���X�Q�Ƃ͐��K�����Ȃ� 1: �S�ẴN���X�Q�Ƃ𓯈ꎚ��ɐ��K������
	 */
	private REFERENCE_NORMALIZATION pr;
}
