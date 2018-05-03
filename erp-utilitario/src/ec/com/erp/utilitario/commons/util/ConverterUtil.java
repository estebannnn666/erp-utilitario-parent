package ec.com.erp.utilitario.commons.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Clase que permite realizar coversiones de diferentes tipos de datos
 * 
 * @author Patricio J. Castillo
 * @author Gerardo Yand&uacute;n
 * @author mv
 * @author cbarahona
 * @version 1.0, 14/02/2007
 * @since J2SDK1.4.2
 * 
 */
public class ConverterUtil {

	/**
	 * La variable<code>DATE_DIA</code> especifica el d&iacute;a
	 */
	public final static String DATE_DIA = "d";

	/**
	 * La variable <code>DATE_MES</code> especifica el mes
	 */
	public final static String DATE_MES = "m";

	/**
	 * La variable<code>DATE_ANIO</code> especifica el a&ntilde;o
	 */
	public final static String DATE_ANIO = "y";

	/**
	 * La variable <code>VACIO</code> especifica un campo vacio
	 */
	public final static String VACIO = "";

	/**
	 * La variable <code>FORMATO_NUMERICO</code> especifica un formato
	 * numerico
	 */
	public static final String FORMATO_NUMERICO = "#0.00";

	/**
	 * La variable <code>MASCARA_TARJETA</code> especifica la mascara de
	 * tarjeta
	 */
	public final static String MASCARA_TARJETA = "XXXXXX-XX-XXXXXXX-X-XXXXXXXXXXXXXXXXXXXXXXXXXXX";

	/**
	 * La variable <code>plantillaFechaLetras1</code> especifica la plantilla
	 * de fecha
	 */
	public final static String PLANTILLA_FECHA_LETRAS1 = "#mes# del #anio#";

	/**
	 * La variable <code>plantillaFechaLetras2</code>especifica la plantilla
	 * fecha con letra2
	 */
	public final static String PLANTILLA_FECHA_LETRAS2 = "#dia# de #mes# del #anio#";

	/**
	 * La variable <code>plantillaFechaLetras2</code>especifica la plantilla fecha con letra3
	 */
	public final static String PLANTILLA_FECHA_LETRAS3 = "#dia# de #mes# del #anio# #hora#:#minute#";
	
	/**
	 * La variable <code>DIA_FIN_MES</code> especifica el dia del fin de mes
	 */
	// 0, ene, feb, mar, abr, may, jun, jul, ago, sep, oct, nov, dic
	public final static int[] DIA_FIN_MES = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	public final static int DIAS_POR_MES = 30;

	public final static int DIAS_POR_ANIO = 365;

	/**
	 * Constructor
	 */
	public ConverterUtil() {
	}

	
	/**
	 * Transforma un fecha de tipo String a un tipo de dato Date.
	 * 
	 * @param asfecha
	 *            la fecha que se quiere resolver
	 * @return Date
	 */
	public static Date parseStringToDate(String asfecha) {
		return parseStringToDate(asfecha, "yyyy-MM-dd");
	}

	/**
	 * Parsear string a date
	 * 
	 * @param asfecha
	 *            la fecha que se quiere resolver
	 * @param asparametro
	 * @return dates Date
	 */

	public static Date parseStringToDate(String asfecha, String asparametro) {
		if (asfecha == null)
			asfecha = returnCurrentDate();

		if (asparametro == null)
			asparametro = "yyyy-MM-dd";

		SimpleDateFormat sdfInput = new SimpleDateFormat(asparametro);
		ParsePosition par = new ParsePosition(0);
		Date dates = sdfInput.parse(asfecha, par);

		return dates;
	}
	
	/**
	 * Devolver la fecha actual como string
	 * 
	 * @return String
	 */
	public static String returnCurrentDate() {
		Date today = new java.util.Date();
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(today);
		String sToday = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		String dayToday = sToday.toString();

		int dayTodayInt = Integer.parseInt(dayToday);

		if (dayTodayInt < 10)
			dayToday = "0" + dayToday;

		String sToday1 = String.valueOf(cal.get(Calendar.MONTH) + 1);
		String monthToday = sToday1.toString();
		int monthTodayInt = Integer.parseInt(monthToday);

		if (monthTodayInt < 10)
			monthToday = "0" + monthToday;

		String sToday2 = String.valueOf(cal.get(Calendar.YEAR));
		String yearToday = sToday2.toString();
		String vsfecha = yearToday + "-" + monthToday + "-" + dayToday;
		return vsfecha;
	}
}
