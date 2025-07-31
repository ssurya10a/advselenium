package generateUtility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class JavaUtility {

	public int generateRandomNumber() {
		Random r = new Random();
		int number = r.nextInt(1000);
		return number;
	}
	
	public String getSystemCurrentDate() {
		Date dobj = new Date();
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sim.format(dobj);
		return currentDate;
	}
	
	public String getDateAfterSpecifiedDays(int days) {
	    Calendar cal = Calendar.getInstance(); // current date
	    cal.add(Calendar.DAY_OF_MONTH, days); // add specified days

	    SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
	    String date = sim.format(cal.getTime()); // format the new date
	    return date;
	}

}
