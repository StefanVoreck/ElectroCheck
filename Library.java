import java.util.ArrayList;
import java.util.Collections;
import javax.swing.DefaultListModel;
import java.util.Arrays;
import java.io.Serializable;

@SuppressWarnings("unchecked")
public class Library extends DefaultListModel<Item> implements Serializable{
	static final long serialVersionUID = 6503479122148437810L;

	public Library(){
		super();
	}
	
	public Library(Item item){
		super();
		add(item);
	}
	
	public void add(Item item){
		addElement(item);
	}
	
	public void sortLibrary(){
		Object[] oArray = toArray();
		Item[] tArray = new Item[oArray.length];
		
		for(int i = 0; i < tArray.length; i++){
			tArray[i] = (Item)oArray[i];
		}
		
		Arrays.sort(tArray, new StatusComparator());
		
		for(int i = 0; i < tArray.length; i++){
			setElementAt(tArray[i], i);
		}
	}
	
	public void printLibrary(){
		Item[] itemArray = (Item[])toArray();
		System.out.println("----------------------------------------------------------------------");
		for(int i = 0; i < itemArray.length; i++){
			System.out.printf("%s\n", itemArray[i].toString());
		}
		System.out.println("----------------------------------------------------------------------");
	}
	
	public void refresh(){
		fireContentsChanged(this, 0, getSize());
	}
}
