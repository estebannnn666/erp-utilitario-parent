package ec.com.erp.utilitario.dao.commons.hibernate.transformers;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.transform.ResultTransformer;

import ec.com.erp.utilitario.commons.util.ClasesUtil;
import ec.com.erp.utilitario.commons.util.NodeAlias;

/**
 * Permite construir la estructura de objetos, multinivel, dependiendo del nombre de los alias indicados
 * @author bcueva
 *
 */
@SuppressWarnings("serial")
public class MultiLevelResultTransformer implements ResultTransformer {
	
	/**
	 * Separador de los alias
	 */
	public static final String ALIAS_SEPARATOR = "_";
	
	/**
	 * Nombre del atributo dentro de una clase que tiene definido como "id", a un objeto
	 */
	private static final String ALIAS_ID_NODE = "id";
	
	/**
	 * Nombre del alias que contendra un nodo raiz
	 */
	private static final String ALIAS_ROOT_NODES = StringUtils.EMPTY;

	/**
	 * Tipo de clase resultante
	 */
	private final Class<?> resultClass;
	
	/**
	 * Crear una instancia del transformador
	 * @param resultClass Tipo de la clase, de la cual se va intancias los objetos de la lista resultante
	 */
	public MultiLevelResultTransformer(Class<?> resultClass) {
		if(resultClass == null) {
			throw new IllegalArgumentException("La clase principal no puede ser nula");
		}
		
		this.resultClass = resultClass;
	}
	
	/**
	 * Permite construir el alias dadecuado para la tupla, que represente al atributo dentro de la rama de objetos
	 * @author bcueva
	 * @param aliases Nombre del alias
	 * @return
	 */
	public static String createAlias(String... aliases) {
		StringBuilder alias = new StringBuilder();
		for(int i = 0; i < aliases.length; i++) {
			if(i==0) {
				alias.append(aliases[i]);
			} else {
				alias.append(ALIAS_SEPARATOR).append(aliases[i]);
			}
		}
		
		return alias.toString();
	}
	
	/**
	 * Establece los valores de cada elemento de <code>tuple</code> en la propiedad indicada
	 * correspondientemente en <code>aliases</code>.
	 * Cada elemento del aliases debe contener un path que indique el nombre de una propiedad
	 * accesible con reflexi&oacute;n.
	 * @author bcueva
	 * @param tuple Array con las tuplas resultantes
	 * @param aliases Array con el nombre de los alias
	 */
	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		NodeAlias rootNode = null;
		NodeAlias previousNode = null;
		NodeAlias indexNode = null;
		int numAliases = aliases.length;
		
		for(int i=0; i<numAliases; i++) {
			String alias = aliases[i];
			if(alias != null) {
				if(alias.contains(ALIAS_SEPARATOR)) {
					String[] als;
					
					indexNode = rootNode;
					while(alias.split(ALIAS_SEPARATOR).length > 2) {
						als = alias.split(ALIAS_SEPARATOR);
						previousNode = this.searchNodeAlias(indexNode, als[0]);
						if(previousNode == null) {
							previousNode = this.createNodeAlias(indexNode, als[0]);
							indexNode.addRefSubNode(previousNode);
							indexNode = previousNode;
						} else {
							indexNode = previousNode;
						}
						
						alias = alias.substring(als[0].length()+1);
					}
					
					als = alias.split(ALIAS_SEPARATOR);
					NodeAlias parentNode = null;
					if(indexNode != null) {
						parentNode = this.searchNodeAlias(indexNode, als[0]);
					} else {
						rootNode = new NodeAlias(this.resultClass, ALIAS_ROOT_NODES);
						indexNode = rootNode;
					}
					
					if(parentNode == null) {
						parentNode = this.createNodeAlias(indexNode, als[0]);
						 
						 if(parentNode.getAlias().equals(ALIAS_ID_NODE)) {
							 indexNode.setNodeId(parentNode);
						 }
						 
						 indexNode.addRefSubNode(parentNode);
					}
					
					parentNode.addAliases(als[1]);
					parentNode.addTuple(tuple[i]);
					
				} else {
					if(rootNode == null) {
						rootNode = new NodeAlias(this.resultClass, ALIAS_ROOT_NODES);
					}
					rootNode.addAliases(alias);
					rootNode.addTuple(tuple[i]);
				}
			}
		}
		
		return rootNode;
	}
	
	/**
	 * Permite crear un nodo a partir de su clase padre
	 * @author bcueva
	 * @param indexNode Nodo que contiene la clase padre del nodo a crear
	 * @param alias Nombre del alias que representa al nodo a crear, y que es el nombre del atributo en el Nodo Padre (Clase Padre)
	 * @return
	 * @throws SecurityException
	 */
	private NodeAlias createNodeAlias(NodeAlias indexNode, String alias) {
		NodeAlias nodeAlias = null;
		Class<?> clazz = indexNode.getClazz();
		Class<?> genericClass = null;
		Field field = null;
		
		do {
			try {
				field = clazz.getDeclaredField(alias);
				if (genericClass == null) {
					genericClass = field.getType();
				}
			} catch(NoSuchFieldException e) {
				Type tipo = clazz.getGenericSuperclass();
				if(tipo instanceof ParameterizedType) {
					ParameterizedType parametizedType = (ParameterizedType) tipo;
					Type[] typeArr = parametizedType.getActualTypeArguments();
					if (typeArr[0] != indexNode.getClazz()) {
						if ( typeArr[0] instanceof Class) {
							genericClass = (Class<?>) typeArr[0];
						}
					}
					clazz = (Class<?>) parametizedType.getRawType();
				} else {
					clazz = (Class<?>) tipo;
				}
			}
			
		} while(field==null && clazz != null);
		
		if(field == null && clazz == null) {
			throw new HibernateException( "No existe el atributo "+ alias +" en la clase: " + indexNode.getClazz());
		}
		
		if(field != null) {
			Type type = field.getGenericType();
			
			if(type instanceof ParameterizedType) {
				 ParameterizedType parametizedType = (ParameterizedType) type;
				 Type[] typeArr = parametizedType.getActualTypeArguments();
				 nodeAlias = new NodeAlias((Class<?>) typeArr[0], alias);
				 nodeAlias.setNodeCollection(Boolean.TRUE);
			 } else {
				 if(genericClass == null) {
					 nodeAlias = new NodeAlias((Class<?>) type, alias);
				 } else {
					 nodeAlias = new NodeAlias(genericClass, alias);
				 }
			 }
		}
		return nodeAlias;
	}
	
	/**
	 * Permite buscar un nodo dentro de un arbol definido por la tupla
	 * @author bcueva
	 * @param node Nodo raiz desde donde se va ha empezar la busqueda
	 * @param alias Nombre del alias del nodo
	 * @return
	 */
	private NodeAlias searchNodeAlias(NodeAlias node, String alias) {
		if(node.getAlias().equals(alias)) {
			return node;
		}
		
		for(NodeAlias nodeAlias : node.getRefSubNode()) {
			NodeAlias nodeResult = this.searchNodeAlias(nodeAlias, alias);
			if(nodeResult != null) {
				return nodeResult;
			}
		}
		
		return null;
	}

	
	/**
	 * Retorna una colecci&oacute;n de elementos transformados
	 * @author bcueva
	 * @param Lista resultante con los objetos de las tuplas consultadas
	 */	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List transformList(List collection) {
		List<NodeAlias> resultList = new ArrayList<NodeAlias>();
		
		for(Object objectNode: collection) {
			resultList.add((NodeAlias) objectNode);
		}
		
		resultList = this.deleteRepeatNodes(resultList);
		
		List<Object> objetosList = (List<Object>) this.createList(this.resultClass);
		
		if(CollectionUtils.isNotEmpty(resultList)){
			for(NodeAlias nodeAlias:resultList) {
				Object object = this.createObject(nodeAlias.getClazz(), nodeAlias);
				objetosList.add(object);
			}
		}
		
		return objetosList;
	}
	
	/**
	 * Permite eliminar nodos repetidos, parte de tuplas repetidas y anidarlas al objeto principal
	 * @author bcueva
	 * @param collection Coleccion de Nodos
	 * @return
	 */
	private List<NodeAlias> deleteRepeatNodes(List<NodeAlias> collection) {
		
		if(CollectionUtils.isEmpty(collection)) {
			return null;
		}
		
		List<NodeAlias> resultList = new ArrayList<NodeAlias>();
		List<NodeAlias> tempNodes = new ArrayList<NodeAlias>();
		NodeAlias rootNode;
		
		while(!CollectionUtils.isEmpty(collection)) {
			rootNode = null;
			for(NodeAlias node: collection) {
				if(rootNode == null) {
					rootNode = node;
					resultList.add(node);
					tempNodes.add(node);
					continue;
				}
				
				if(rootNode.getTuplesGroup().equals(node.getTuplesGroup()) && rootNode.getAliasesGroup().equals(node.getAliasesGroup())) {
					for(NodeAlias childNode: node.getRefSubNode()) {
						rootNode.addRefSubNode(childNode);
					}
					tempNodes.add(node);
				}
			}
			
			for(NodeAlias node:tempNodes) {
				collection.remove(node);
			}
			
			tempNodes.clear();
			
			rootNode.setRefSubNode(this.deleteRepeatNodes(rootNode.getRefSubNode()));
		}
		
		return resultList;
	}
	
	/**
	 * Permite crear un objeto a partir de su clase y nodo y nos devuelde un objeto del tipo de la clase indicada
	 * @author bcueva
	 * @param clazz Tipo de clase
	 * @param node NodeAlias que representa al objeto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> Object createObject(Class<T> clazz, NodeAlias node) {
		Object object = null;
		List<Object> listObjects;
		List<NodeAlias> tempNodes = new ArrayList<NodeAlias>();
		Class<?> collectionClass;
		String collectionAlias;
		
		try {
			if(!node.isNullTuple()) {
				object = clazz.newInstance();
				for(int i=0; i<node.getAliases().size(); i++) {
					ClasesUtil.invocarMetodoSet(object, node.getAliases().get(i), node.getTuples().get(i));
				}
				
				if(!CollectionUtils.isEmpty(node.getRefSubNode())) {
					while(node.getRefSubNode().size() > 0) {
						listObjects = null;
						Object childObject;
						collectionClass = null;
						collectionAlias = StringUtils.EMPTY;
						for(NodeAlias aliasNode: node.getRefSubNode()) {
							
							if(aliasNode.isNodeCollection()) {
								if(collectionClass == null && collectionAlias.equals(StringUtils.EMPTY)) {
									collectionClass = aliasNode.getClazz();
									collectionAlias = aliasNode.getAlias();
								}
								
								if(aliasNode.getClazz() == collectionClass) {
									
									childObject = this.createObject(aliasNode.getClazz(), aliasNode);
									
									if(childObject != null) {
										if(CollectionUtils.isEmpty(listObjects)) {
											listObjects = (List<Object>) this.createList(aliasNode.getClazz());
										}
										
										listObjects.add(childObject);
									}
									tempNodes.add(aliasNode);
								}
								
							} else {
								childObject = this.createObject(aliasNode.getClazz(), aliasNode);
								ClasesUtil.invocarMetodoSet(object, aliasNode.getAlias(), childObject);
								tempNodes.add(aliasNode);
							}
						}
						
						if(collectionClass != null) {
							ClasesUtil.invocarMetodoSet(object, collectionAlias, listObjects);
						}
						
						for(NodeAlias aliasNode: tempNodes) {
							node.getRefSubNode().remove(aliasNode);
						}
						
						tempNodes.clear();
					}
				}
			}
		} catch (InstantiationException e) {
			throw new HibernateException( "No se puede instanciar la clase resultante: " + clazz.getName(), e);
		} catch (IllegalAccessException e) {
			throw new HibernateException( "No se puede instanciar la clase resultante: " + clazz.getName(), e);
		}
		
		return object;
	}
	
	/**
	 * Permite crear una lista de los objetos del tipo T
	 * @author bcueva
	 * @param clazz
	 * @return
	 */
	private <T> List<T> createList(Class<T> clazz) {
		return new ArrayList<T>();
	}	
}

