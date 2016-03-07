import java.util.Calendar;
import java.util.GregorianCalendar;

public class Test{
	public static void main(String...args){
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		System.out.println(cal.get(Calendar.MONTH));
	}
}
