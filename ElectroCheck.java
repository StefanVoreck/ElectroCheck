import java.io.IOException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import javax.swing.SwingUtilities;
import java.util.Scanner;
import java.io.FileReader;

public class ElectroCheck{
	public static void main(String...args){
		// Import from lib.bin
		final Library lib = (Library)new ImExStream().importObject("lib.bin");
		
		// Import from preset
		//final Library lib = preset1();
		
		// Import from textFile
		//final Library lib = importFromText("libraryText");
		
		run(lib);
		
	}
	
	public static void run(final Library lib){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				new Viewer(lib);
			}
		});
	}
	
	public static Library preset1(){
		Library lib = new Library();
		lib.add(new Item("Atmega328", 5, 63.36f, Item.PENDING));
		lib.add(new Item("Dip-28", 10, 8.81f, Item.ORDERED));
		lib.add(new Item("HC-05", 5, 43.2f, Item.ARRIVED));
		lib.add(new Item("Headers", 12, 12.3f, Item.PENDING));
		lib.add(new Item("Headers", 12, 12.3f, Item.PENDING));
		lib.add(new Item("Headers", 12, 12.3f, Item.PENDING));
		lib.add(new Item("Resistor", 53, 3.36f, Item.ORDERED));
		lib.add(new Item("Capacitor", 15, 2.36f, Item.ARRIVED));
		return lib;
	}
	
	public static Library importFromText(String fileName){
		Library lib = new Library();
	
		try{
			Scanner scan = new Scanner(new FileReader(fileName));
			while(scan.hasNext()){
				String line = scan.nextLine();
				String temp = "";
				Item item = new Item();
				
				// Name
				for(int i = 1; i < 40; i++){
					temp += line.charAt(i);
				}
				item.setName(temp);
				temp = "";
				
				// Cost
				for(int i = 42; i < 49; i++){
					temp += line.charAt(i);
				}
				item.setCost(Float.parseFloat(temp));
				temp = "";
				
				// Quantity
				for(int i = 51; i < 55; i++){
					char c = line.charAt(i);
					if(!(c == ' ')){
						temp += c;
					}
				}
				item.setQuantity(Integer.parseInt(temp));
				temp = "";
				
				
				// Status
				for(int i = 67; i < 74; i++){
					temp += line.charAt(i);
				}
				if(temp.equals("PENDING")){
					item.setStatus(Item.PENDING);
				}else if(temp.equals("ORDERED")){
					item.setStatus(Item.ORDERED);
				}else if(temp.equals("ARRIVED")){
					item.setStatus(Item.ARRIVED);
				}else{
					System.out.println("Error 0x4332");
				}
				temp = "";
				
				// Order date
				for(int i = 80; i < 86; i++){
					char c = line.charAt(i);
					if(!(c == ' ')){
						temp += c;
					}
				}
				item.setOrderDate(Integer.parseInt(temp));
				temp = "";
				
				// Estimated delivery
				for(int i = 100; i < 106; i++){
					char c = line.charAt(i);
					if(!(c == ' ')){
						temp += c;
					}
				}
				item.setEstimatedDelivery(Integer.parseInt(temp));
				
				// Add item to library
				lib.add(item);
			}
		}catch(Exception e){
			System.out.println("Error 0x776");
		}
		
		return lib;
	}
}

// Om en ny Item redan finns i library under samma dag med samma kostnad, öka quantity i så fall.

// JComboBox i editItem

// Search button in Viewer

// Clone button in Viewer

@SuppressWarnings("unchecked")
class ImExStream<E>{
	public E importObject(String fileName){
		try{
			FileInputStream fis = new FileInputStream(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			E output = (E) ois.readObject();
			fis.close();
			ois.close();
			System.out.println("Import success");
			return output;
		}catch(Exception e){
			System.out.println("Import failed. Creating new library");
			return (E)new Library();
		}
	}
	
	public void exportObject(String fileName, E object){
		try{
			FileOutputStream fos = new FileOutputStream(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(object);
			fos.close();
			oos.close();
		}catch(Exception e){
			System.out.println("Error 0x223: Export failed");
		}
	}
}
