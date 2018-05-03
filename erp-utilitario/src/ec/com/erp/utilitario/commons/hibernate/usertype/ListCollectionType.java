package ec.com.erp.utilitario.commons.hibernate.usertype;


import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.usertype.UserCollectionType;

import ec.com.erp.utilitario.commons.dto.ListSet;

/**
 * Tipo de dato de usuario para que Hibernate retorne objetos
 * de tipo ec.com.kruger.utilitario.dao.commons.hibernate.usertype.PersistenListSet
 * en lugar de objetos de tipo org.hibernate.collection.PersistentSet.
 * @author cbarahona
 *
 */
public class ListCollectionType implements UserCollectionType {

	static int lastInstantiationRequest = -2;
	
	// could be common for all collection implementations.
	@SuppressWarnings("rawtypes")
	public boolean contains(Object collection, Object obj) {
		Set set = (Set)collection;
		return set.contains(obj);
	}
	
	// could be common for all collection implementations.
	@SuppressWarnings("rawtypes")
	public Iterator getElementsIterator(Object collection) {
		return ((Collection)collection).iterator();
	}
	
	
	public PersistentCollection instantiate(SessionImplementor session, CollectionPersister persister) throws HibernateException {
		return new PersistentListSet(session);
	}

	@SuppressWarnings("rawtypes")
	public PersistentCollection wrap(SessionImplementor session, Object collection) {		
		ListSet setList = new ListSet();
		setList.addAll((Collection)collection);
		return new PersistentListSet(session, setList);
	
	}

	public Object indexOf(Object collection, Object entity) {
		int l = ( (ListSet) collection ).indexOf(entity);
		if(l<0) {
			return null;
		} else {
			return new Integer(l);
		}
	}

	@SuppressWarnings("rawtypes")
	public Object replaceElements(Object original, Object target, CollectionPersister persister, Object owner, Map copyCache, SessionImplementor session) throws HibernateException {
		ListSet result = (ListSet) target;
		result.clear();
		result.addAll((Collection)original);
		return result;
	}

	public Object instantiate(int anticipatedSize) {
		lastInstantiationRequest = anticipatedSize;
		
		return new ListSet();
	}

	
}