import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Comparator;
import java.io.Serializable;
import javax.swing.JOptionPane;

public class Item implements Serializable{
	static final long serialVersionUID = -4670818665121132854L;
	
	public static final String FORMAT = "|%-40s|%7.2f|%5d|%7.2f|%10s|%11s|%19s|";
	private static final String HEADER_FORMAT = "|%-40s|%7s|%5s|%7s|%10s|%11s|%19s|";
	public static final String HEADER = String.format(HEADER_FORMAT, "Article", "Cost", "QTY", "PPU", "Status", "Order date", "Estimated delivery");
	
	public static final int PENDING = 2;
	public static final int ORDERED = 1;
	public static final int ARRIVED = 0;

	// Fields
	private String name;
	private int quantity;
	private float cost;
	private int status;
	private int orderDate;
	private int estimatedDelivery;

	public Item(){
		this("", 0, 0, 0);
	}
	
	public Item(String name){
		this(name, 0, 0, 0);
	}
	
	public Item(String name, int quantity, float cost, int status){
		this.name = name;
		this.quantity = quantity;
		this.cost = cost;
		this.status = status;
		orderDate = getToday();
		estimatedDelivery = estimateDelivery();
	}
	
	// Getters
	public String getName(){return name;}
	public int getQuantity(){return quantity;}
	public float getCost(){return cost;}
	public int getStatus(){return status;}
	public float getPPU(){return (float)getQuantity() == 0.0f ? 0.0f : getCost() / (float)getQuantity();}
	public int getOrderDate(){return orderDate;}
	public int getEstimatedDelivery(){return estimatedDelivery;}
	
	// Setters
	public void setName(String name){this.name = name.toUpperCase();}
	public void setQuantity(int quantity){this.quantity = quantity;}
	public void setCost(float cost){this.cost = cost;};
	public void setStatus(int status){this.status = status;}
	public void setOrderDate(int orderDate){
		if(("" + orderDate).length() == 6 || orderDate == 0){
			this.orderDate = orderDate;
		}else{
			JOptionPane.showMessageDialog(null, "Type in format YYMMDD");
		}
	}
	public void setEstimatedDelivery(int estimatedDelivery){
		if(("" + estimatedDelivery).length() == 6 || estimatedDelivery == 0){
			this.estimatedDelivery = estimatedDelivery;
		}else{
			JOptionPane.showMessageDialog(null, "Type in format YYMMDD");
		}
	}
	
	// Returns date in format YYMMDD
	private int getToday(){
		Calendar cal = new GregorianCalendar();
		return calendarToInt(cal);
	}
	
	private int estimateDelivery(){
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_YEAR, 45);
		return calendarToInt(cal);
	}
	
	public int calendarToInt(){
		return calendarToInt(new GregorianCalendar());
	}
	
	private int calendarToInt(Calendar cal){		
		String s = "";
		int temp;
	
		// Year
		s += cal.get(Calendar.YEAR) - 2000;
		
		// Month
		temp = cal.get(Calendar.MONTH) + 1;
		s += temp < 10 ? "0" + temp : temp;
		
		// Day
		temp = cal.get(Calendar.DAY_OF_MONTH);
		s += temp < 10 ? "0" + temp : temp;
		
		return Integer.parseInt(s);
	}
	
	public boolean isEmpty(){
		if(getName().equals("")) return true;
		return false;
	}
	
	private String statusToString(){
		switch(status){
			// PENDING
			case 0:{
				return "PENDING";
			}
			
			// ORDERED
			case 1:{
				return "ORDERED";
			}
			
			// ARRIVED
			case 2:{
				return "ARRIVED";
			}
			
			// Should never occur
			default:{
				JOptionPane.showMessageDialog(null, "Error 9093");
				return "PENDING";
			}
		}
	}
	
	@Override
	public String toString(){
		return String.format(FORMAT, getName(), getCost(), getQuantity(), getPPU(), statusToString(), getOrderDate(), getEstimatedDelivery());
	}
}

class StatusComparator implements Comparator<Item>{
	public int compare(Item i1, Item i2){
		int diff = i1.getStatus() - i2.getStatus();
		if(diff == 0){
			return (new NameComparator()).compare(i1, i2);
		}else{
			return diff;
		}
	}
}

class DateComparator implements Comparator<Item>{
	public int compare(Item i1, Item i2){
		return i2.getOrderDate() - i1.getOrderDate();
	}
}

class NameComparator implements Comparator<Item>{
	public int compare(Item i1, Item i2){
		int diff = (i1.getName()).compareTo(i2.getName());
		if(diff == 0){
			return (new DateComparator()).compare(i1, i2);
		}else{
			return diff;
		}
	}
}
