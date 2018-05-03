package ec.com.erp.utilitario.commons.hibernate.usertype;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.Spliterator;

import org.hibernate.collection.PersistentSet;
import org.hibernate.engine.SessionImplementor;

import ec.com.erp.utilitario.commons.dto.ListSet;

/**
 * Clase que extiende la funcionalidad de un org.hibernate.collection.PersistentSet
 * en la que se agrega la funcionalidad de listas mediante el uso de
 * objetos tipo ec.com.kruger.utilitario.dao.commons.dto.ListSet
 * @author cbarahona
 *
 */
@SuppressWarnings({ "serial", "rawtypes" })
public class PersistentListSet extends PersistentSet implements List{	
	
	public PersistentListSet(SessionImplementor session) {
		super(session);
		
	}

	public PersistentListSet(SessionImplementor session, Set collection) {		
		super(session,collection);
	}

	public boolean addAll(int paramInt, Collection paramCollection) {		
		return ((ListSet)set).addAll(paramInt, paramCollection);
	}

	public Object get(int paramInt) {		
		return ((ListSet)set).get(paramInt);
	}

	public Object set(int paramInt, Object paramE) {
		return ((ListSet)set).set(paramInt, paramE);
	}

	public void add(int paramInt, Object paramE) {
		((ListSet)set).add(paramInt,paramE);
		
	}

	public Object remove(int paramInt) {
		return ((ListSet)set).remove(paramInt);
	}

	public int indexOf(Object paramObject) {
		return ((ListSet)set).indexOf(paramObject);
	}

	public int lastIndexOf(Object paramObject) {
		return ((ListSet)set).lastIndexOf(paramObject);
	}

	public ListIterator listIterator() {
		return ((ListSet)set).listIterator();
	}

	public ListIterator listIterator(int paramInt) {
		return ((ListSet)set).listIterator(paramInt);
	}

	public List subList(int paramInt1, int paramInt2) {
		return ((ListSet)set).subList(paramInt1, paramInt2);
	}
	
	
	public int size() {
		if(isEmpty()){
			return 0;
		}
		return ((ListSet)set).size();
	}

	@Override
	public boolean isEmpty() {
		return set == null || ((ListSet)set).isEmpty();
	}

	@Override
	public boolean contains(Object paramObject) {
		return ((ListSet)set).contains(paramObject);
	}

	@Override
	public Iterator iterator() {
		return ((ListSet)set).iterator();
	}

	@Override
	public Object[] toArray() {
		return ((ListSet)set).toArray();
	}

	@Override
	public Object[] toArray(Object[] paramArrayOfT) {
		return ((ListSet)set).toArray(paramArrayOfT);
	}

	@Override
	public boolean add(Object paramE) {
		return ((ListSet)set).add(paramE);
	}

	@Override
	public boolean remove(Object paramObject) {
		return ((ListSet)set).remove(paramObject);
	}

	@Override
	public boolean containsAll(Collection paramCollection) {
		return ((ListSet)set).containsAll(paramCollection);
	}

	@Override
	public boolean addAll(Collection paramCollection) {		
		return ((ListSet)set).addAll(paramCollection);
	}

	@Override
	public boolean retainAll(Collection paramCollection) {
		return ((ListSet)set).retainAll(paramCollection);
	}

	@Override
	public boolean removeAll(Collection paramCollection) {
		return ((ListSet)set).removeAll(paramCollection);
	}

	@Override
	public void clear() {
		((ListSet)set).clear();		
	}

	@Override
	public Spliterator spliterator() {
		// TODO Auto-generated method stub
		return List.super.spliterator();
	}

}

