package ec.com.erp.utilitario.commons.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Representa un nodo para la representaci\u00F3n de un objeto de una tupla de datos
 * 
 * @author egudino
 *
 */
public class NodeAlias {
	
	/**
	 * Separador del Conjunto de datos de la tupla
	 */
	private static final String GROUP_SEPARATOR = ",";
	
	/**
	 * Representa el valor nulo dentro del conjunto de datos de la tupla
	 */
	private static final String NULL_VALUE = "null";
	
	/**
	 * Clase del objeto que representa el Nodo
	 */
	private final Class<?> clazz;
	
	/**
	 * Nombre del alias (atributo) del objeto que representa el nodo dentro de la clase padre
	 */
	private final String alias;
	
	/**
	 * Lista de alias que contiene la tupla que representa al objeto representado por el nodo
	 */
	private final List<String> aliases;
	
	/**
	 * Lista de datos (tupla), que pertenecen a los atributos del objeto representado por el nodo
	 */
	private final List<Object> tuples;
	
	/**
	 * Lista de nodos hijos, que representa a los objetos que se deben instanciar bajo el objeto representado por este nodo
	 */
	private List<NodeAlias> refSubNode;
	
	/**
	 * Permite definir si el alias (atributo), del objeto representado por este nodo, es una coleccion en su clase padre
	 */
	private Boolean nodeCollection;
	
	/**
	 * Conjunto de datos de la tupla separados por ","
	 */
	private String tuplesGroup;
	
	/**
	 * Conjunto de alias separados por ","
	 */
	private String aliasesGroup;
	
	/**
	 * Representa a un objeto Id, este no es nulo si la clase padre tiene un atributo id
	 */
	private NodeAlias nodeId;
	
	/**
	 * Constructor del NodoAlias
	 * @param clazz Clase a la cual debe instanciarce el objeto representado por el Nodo
	 * @param alias Nombre del alias (atributo), del objeto representado por el Nodo, en la Clase padre
	 */
	public NodeAlias(Class<?> clazz, String alias) {
		this.clazz = clazz;
		this.alias = alias;
		this.aliases = new ArrayList<String>();
		this.tuples = new ArrayList<Object>();
		this.refSubNode = new ArrayList<NodeAlias>();
		this.nodeCollection = Boolean.FALSE;
		this.tuplesGroup = StringUtils.EMPTY;
		this.aliasesGroup = StringUtils.EMPTY;
	}

	/**
	 * Permite obtener el Tipo de clase a la cual debe representar el Objeto
	 * @return
	 */
	public Class<?> getClazz() {
		return this.clazz;
	}

	/**
	 * Permite obtener la lista de alias del conjunto de datos al cual representa el objeto, representado por el nodo
	 * @return
	 */
	public List<String> getAliases() {
		return aliases;
	}

	/**
	 * Permite agregar alias a la lista de alias
	 * @param alias Nombre del alias (atributo), de la Clase, del objeto representado por el nodo
	 */
	public void addAliases(String alias) {
		this.aliases.add(alias);
	}

	/**
	 * Permite obtener el conjunto de datos de la tupla que le pertenecen al objeto, representado por el Nodo
	 * @return
	 */
	public List<Object> getTuples() {
		return tuples;
	}

	/**
	 * Permite agregar un valor de la tupla
	 * @param tuple
	 */
	public void addTuple(Object tuple) {
		this.tuples.add(tuple);
	}

	/**
	 * Permite obtener la lista de sub Nodos, objetos de la clase del nodo
	 * @return
	 */
	public List<NodeAlias> getRefSubNode() {
		return refSubNode;
	}

	/**
	 * Permite agregar un sub-nodo
	 * @param subNode
	 */
	public void addRefSubNode(NodeAlias subNode) {
		this.refSubNode.add(subNode);
	}

	/**
	 * Permite obtener el alias (Nombre atributo), del objeto, en su clase padre
	 * @return
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * Permite verificar si el objeto representado por el nodo, es una coleccion
	 * @return
	 */
	public Boolean isNodeCollection() {
		return this.nodeCollection;
	}

	/**
	 * Permite definir si el objeto que representa el nodo es una coleccion
	 * @param isCollection
	 */
	public void setNodeCollection(Boolean isCollection) {
		this.nodeCollection = isCollection;
	}
	
	/**
	 * Permite obtener el conjunto de los datos de la tupla
	 * @author bcueva
	 * @return
	 */
	public String getTuplesGroup() {
		if(StringUtils.isEmpty(this.tuplesGroup)) {
			for(Object tuple: this.tuples) {
				if(StringUtils.isEmpty(this.tuplesGroup)) {
					this.tuplesGroup = ((tuple == null)? NULL_VALUE : tuple.toString());
				} else {
					this.tuplesGroup += GROUP_SEPARATOR + ((tuple == null)? NULL_VALUE : tuple.toString());
				}
			}
			
			if(this.nodeId != null) {
				this.tuplesGroup = this.nodeId.getTuplesGroup() + GROUP_SEPARATOR + this.tuplesGroup;
			}
		}
		
		return this.tuplesGroup;
	}
	
	/**
	 * Permite obtener el Conjunto de los alias (Nombres Atributos), que contiene la clase del objeto, represento por el nodo
	 * @author bcueva
	 * @return
	 */
	public String getAliasesGroup() {
		if(StringUtils.isEmpty(this.aliasesGroup)) {
			for(String als: this.aliases) {
				if(StringUtils.isEmpty(this.aliasesGroup)) {
					this.aliasesGroup = als;
				} else {
					this.aliasesGroup = GROUP_SEPARATOR + als;
				}
			}
			
			if(this.nodeId != null) {
				this.aliasesGroup = this.nodeId.getAliasesGroup() + GROUP_SEPARATOR + this.aliasesGroup;
			}
		}
		
		return this.aliasesGroup;
	}
	
	/**
	 * Permite a\u00F1anir las referencias a los sub-nodos, objetos que estan debajo del, objeto representado por el nodo
	 * @param refSubNode
	 */
	public void setRefSubNode(List<NodeAlias> refSubNode) {
		this.refSubNode = refSubNode;
	}
	
	/**
	 * Permite definir si la parte de la tupla que le pertenece al objeto es nula
	 * @author bcueva
	 * @return
	 */
	public Boolean isNullTuple() {
		for(Object tuple: this.tuples) {
			if(tuple != null) {
				return Boolean.FALSE;
			}
		}
		
		return Boolean.TRUE;
	}

	/**
	 * Permite asignar el Nodo de tipo Id, Objeto id de la clase representado por el nodo
	 * @param nodeId
	 */
	public void setNodeId(NodeAlias nodeId) {
		this.nodeId = nodeId;
	}
}

