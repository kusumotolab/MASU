package jp.ac.osaka_u.ist.sdl.scorpio.gui;


import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sdl.scorpio.Entity;


/**
 * ���ݑI�𒆂̗v�f�i�N���[���y�A�Ȃǂ�ۑ����邽�߂̃N���X�D �I�u�U�[�o�[�p�^�[����p���邱�Ƃ��ł���悤�ɁCjava.util.Observable ���p�����Ă���
 * 
 * @author higo
 */
public final class SelectedEntities<T extends Entity> extends Observable {

    /**
     * ���̃N���X�̃I�u�W�F�N�g��\��
     * 
     * @param label �I�u�W�F�N�g���������x������
     * @return ���x�������ɂ���Ď�����Ă��邱�̃N���X�̃I�u�W�F�N�g
     */
    public static final <S extends Entity> SelectedEntities<S> getInstance(final String label) {
        SelectedEntities<S> instance = (SelectedEntities<S>) INSTANCES.get(label);
        if (null == instance) {
            instance = new SelectedEntities<S>(label);
            INSTANCES.put(label, instance);
        }
        return instance;
    }

    /**
     * �V�����G���e�B�e�B�t�@�C����I���G���e�B�e�B�Ƃ��Ēǉ�����
     * 
     * @param entity �ǉ�����G���e�B�e�B
     * @param source �ύX��
     */
    public void add(final T entity, final Observer source) {

        if (null == entity) {
            throw new NullPointerException();
        }

        this.selectedEntities.add(entity);
        this.source = source;

        this.setChanged();
        this.notifyObservers(source);
    }

    /**
     * �V�����G���e�B�e�B�Q��I���G���e�B�e�B�Ƃ��Ēǉ�����
     * 
     * @param entities �ǉ�����G���e�B�e�B�Q
     * @param source �ύX��
     */
    public void addAll(final Collection<T> entities, final Observer source) {

        if (null == entities) {
            throw new NullPointerException();
        }

        this.selectedEntities.addAll(entities);
        this.source = source;

        this.setChanged();
        this.notifyObservers(source);
    }

    /**
     * �G���e�B�e�B��I���G���e�B�e�B����폜����
     * 
     * @param entity �폜����G���e�B�e�B
     * @param source �ύX��
     */
    public void remove(final T entity, final Observer source) {

        if (null == entity) {
            throw new NullPointerException();
        }

        this.selectedEntities.remove(entity);
        this.source = source;

        this.setChanged();
        this.notifyObservers(source);
    }

    /**
     * �G���e�B�e�B�Q��I���G���e�B�e�B����폜����
     * 
     * @param entities �폜����G���e�B�e�B�Q
     * @param source �ύX��
     */
    public void removeAll(final Collection<T> entities, final Observer source) {

        if (null == entities) {
            throw new NullPointerException();
        }

        this.selectedEntities.removeAll(entities);
        this.source = source;

        this.setChanged();
        this.notifyObservers(source);
    }

    /**
     * �V�����G���e�B�e�B��I���G���e�B�e�B�Ƃ��ăZ�b�g����D���ɑI������Ă����G���e�B�e�B�͑S�č폜�����
     * 
     * @param entity �Z�b�g����G���e�B�e�B
     * @param source �ύX��
     */
    public void set(final T entity, final Observer source) {

        if (null == entity) {
            throw new NullPointerException();
        }

        this.selectedEntities.clear();
        this.selectedEntities.add(entity);
        this.source = source;

        this.setChanged();
        this.notifyObservers(source);
    }

    /**
     * �V�����G���e�B�e�B�Q��I���G���e�B�e�B�Ƃ��ăZ�b�g����D���ɑI������Ă����G���e�B�e�B�͑S�č폜�����
     * 
     * @param entities �Z�b�g����G���e�B�e�B�Q
     * @param source �ύX��
     */
    public void setAll(final Collection<T> entities, final Observer source) {

        if (null == entities) {
            throw new NullPointerException();
        }

        this.selectedEntities.clear();
        this.selectedEntities.addAll(entities);
        this.source = source;

        this.setChanged();
        this.notifyObservers(source);
    }

    /**
     * �I���G���e�B�e�B�����݂��邩�ǂ�����Ԃ��D
     * 
     * @return �P�ȏ�̃G���e�B�e�B���I������Ă���ꍇ true�C�S���G���e�B�e�B���o�^����Ă��Ȃ��ꍇ false
     */
    public boolean isSet() {
        return !this.selectedEntities.isEmpty();
    }

    /**
     * �I���G���e�B�e�B��S�č폜����
     * 
     * @param source �ύX��
     */
    public void clear(final Observer source) {

        this.selectedEntities.clear();
        this.source = source;

        this.setChanged();
        this.notifyObservers(source);
    }

    /**
     * ���ݑI�𒆂̃G���e�B�e�B�� Set ��Ԃ�
     * 
     * @return ���ݑI�𒆂̃G���e�B�e�B�� Set ��Ԃ�
     */
    public SortedSet<T> get() {
        return Collections.unmodifiableSortedSet(this.selectedEntities);
    }

    /**
     * ���̃I�u�W�F�N�g��ύX�����Ō�̃I�u�W�F�N�g�Ԃ��D
     * 
     * @return ���̃I�u�W�F�N�g��ύX�����Ō�̃I�u�W�F�N�g
     */
    public Observer getSource() {
        return this.source;
    }

    /**
     * ���̃I�u�W�F�N�g�̃��x����Ԃ�
     * 
     * @return ���̃I�u�W�F�N�g�̃��x��
     */
    public String getLabel() {
        return this.label;
    }

    private static final Map<String, SelectedEntities<? extends Entity>> INSTANCES = new HashMap<String, SelectedEntities<? extends Entity>>();

    private SelectedEntities(final String label) {

        if (null == label) {
            throw new NullPointerException();
        }

        this.selectedEntities = new TreeSet<T>();
        this.source = null;
        this.label = label;
    }

    private final SortedSet<T> selectedEntities;

    private Observer source;

    private final String label;
}
