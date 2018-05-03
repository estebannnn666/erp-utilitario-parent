package ec.com.erp.utilitario.commons.dto;


import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;

import org.apache.commons.collections.set.ListOrderedSet;

/**
 * Clase que implementa toda la funcionalidad de una estructura
 * java.util.Set, y que adicionalmente puede utilizarse como
 * una estructura java.util.List
 * @author cbarahona
 *
 */
@SuppressWarnings({ "serial", "rawtypes" })
public class ListSet extends ListOrderedSet implements List{

	public int lastIndexOf(Object o) {		
		return setOrder.lastIndexOf(o);
	}

	public ListIterator listIterator() {
		return setOrder.listIterator();
	}

	public ListIterator listIterator(int index) {
		return setOrder.listIterator(index);
	}

	@SuppressWarnings("unchecked")
	public Object set(int index, Object element) {
		if(collection.remove(setOrder.get(index))){
			if(collection.add(element)){
				return setOrder.set(index, element);
			}
			
		}
		return null;
	}

	public List subList(int fromIndex, int toIndex) {		
		return setOrder.subList(fromIndex, toIndex);
	}

	@Override
	public Spliterator spliterator() {
		// TODO Auto-generated method stub
		return List.super.spliterator();
	}

}

