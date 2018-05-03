package ec.com.erp.utilitario.commons.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * La clase <code>ClasesUtil</code> contiene m&eacute;todos para manejo de clases por
 * medio de reflexi&oacute;n.
 * 
 * @author Mario Braganza
 * @author cbarahona
 * @version 1.5
 * @since 5.0, 15/08/2006
 * 
 */
public class ClasesUtil {

	// --------------------------------------------------------------------------
	/**
	 * Invoca por medio de la reflecci&oacute;n un m&eacute;todo
	 * <code>setNombreAtributo(parametro)
	 * </code> de un determinado objeto.
	 * 
	 * @param obj
	 *            el objeto del cual se invocar� el m&eacute;todo
	 *            <code>getNombreAtributo()</code>
	 * @param nombreAtributo
	 *            atributo del objeto. Si se requiere invocar el m&eacute;todo
	 *            <code>set</code> del atributo de un objeto que est&aacute; dentro de
	 *            otro, se debe escribir cada atributo separado por un punto (.)
	 * @param valor
	 *            el valor que ser&aacute; establecido en el atributo como valor
	 *            Ejemplos:
	 *            <ul>
	 *            <li>
	 *            Si se requiere invocar el m&eacute;todo <code>set</code> del
	 *            <code>atributo1</code> que pertenece al <code>objeto1</code>,
	 *            <code>nombreAtributo</code> ser&aacute;igual a
	 *            <code>"atributo1"</code></li>
	 *            <li>
	 *            Si el <code>objeto2</code> est&aacute; dentro del
	 *            <code>objeto1</code> y se requiere invocar al m&eacute;todo
	 *            <code>sset</code> del <code>atributo2</code> perteneciente al
	 *            <code>objeto2</code>, <code>nombreAtributo</code> ser&aacute;igual
	 *            a <code>"objeto2.atributo2"
	 * 		</code></li>
	 *            </ul>
	 * @return cualquiera de los siguientes:
	 *         <ul>
	 *         <li>
	 *         el resultado de invocar al m&eacute;todo
	 *         <code>setNombreAtributo(parametro)</code> si el
	 *         <code>atributo</code> es diferente de <code>null</code></li>
	 *         <li>
	 *         <code>null</code> en cualquier otro caso</li>
	 *         </ul>
	 * 
	 */
	public static void invocarMetodoSet(Object bean, String nombreAtributo, Object valor) {
		try {
			final String set = "set";
			final String get = "get";
			Object propertyBean = bean;
			String token = nombreAtributo;
			if (nombreAtributo.indexOf(".") >= 0) {
				StringTokenizer tokenizer = new StringTokenizer(nombreAtributo, ".");

				while (tokenizer.hasMoreTokens()) {
					token = tokenizer.nextToken();
					if (tokenizer.hasMoreTokens()) {
						Object propertyValue = PropertyUtils.getProperty(propertyBean, token);
						if (propertyValue == null) {
							String capitalizedToken = StringUtils.capitalize(token);
							Class<?> classPropertyBean = propertyBean.getClass();

							Method getMethod = classPropertyBean.getMethod(get + capitalizedToken);
							Class<?> propertyClass = getMethod.getReturnType();

							propertyValue = propertyClass.newInstance();

							Method setMethod = classPropertyBean.getMethod(set + capitalizedToken, propertyClass);
							setMethod.invoke(propertyBean, propertyValue);

						}
						propertyBean = propertyValue;
					}
				}

			}

			String capitalizedProperty = StringUtils.capitalize(token);
			Class<?> classBean = propertyBean.getClass();

			Method getMethod = classBean.getMethod(get + capitalizedProperty);
			Class<?> propertyClass = getMethod.getReturnType();

			Method method = classBean.getMethod(set + capitalizedProperty, propertyClass);
			method.invoke(propertyBean, valor);

			// BeanUtils.setProperty(bean, nombreAtributo, valor);
		} catch (IllegalAccessException e) {
			System.out.println("Error"+ e);
		} catch (InvocationTargetException e) {
			System.out.println("Error"+ e);
		} catch (NoSuchMethodException e) {
			System.out.println("Error"+ e);
		} catch (InstantiationException e) {
			System.out.println("Error"+ e);
		} catch (SecurityException e) {
			System.out.println("Error"+ e);
		}
	}
	
		
}
