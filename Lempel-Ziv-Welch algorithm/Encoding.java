// Student Name : Akash Singh (ID 801022198)

//Importing the required packages
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;


public class Encoding {

	public static void main(String[] args) throws IOException {
		
		//Reading the arguments from command line
		String[] filename = args[0].split(".txt");
		String fname = filename[0];
		int bits = Integer.parseInt(args[1]);
		
		StringBuffer sb = new StringBuffer();
		//Reading the input file given Command line argument
		FileReader f = null;
		try {
			f = new FileReader(args[0]);
			BufferedReader reader = new BufferedReader(f);
			int i;
			while ((i = reader.read()) != -1) {
				sb.append((char) i);
			}
			reader.close();
			f.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//Converting the file data from StringBuffer object to String object
		String symbols = sb.toString();
        // Initializing the maximum size of dictionary
		int max_table_size = (int) Math.pow(2, bits);
		//Creating a known dictionary of single characters 
		LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
		//Initializing the dictionary with 0 to 255 ASCII codes
		for (int i = 0; i < 256; i++) {
			String temp = (char) i + "";
			map.put(temp, i);
		}
		int code = 256;
		//Declaring the ArrayList output to store output integers
		ArrayList<Integer> output = new ArrayList<Integer>();
		String STRING = "";
		String symbol = null;
		boolean flag = false;
		for (int i = 0; i < symbols.length(); i++) {
			flag = true;
			symbol = symbols.charAt(i) + "";
			if (map.containsKey(STRING + symbol)) {
				STRING = STRING + symbol;
			} else {
				output.add(map.get(STRING));
				if (map.size() < max_table_size) {
					map.put(STRING + symbol, code++);
				}
				STRING = symbol;
			}
		}
		if (flag) {
			output.add(map.get(STRING));
		}
        //Writing the compressed encoded file in the format filename + extension
		OutputStream outputStream = new FileOutputStream(fname + ".lzw");
		Writer outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-16BE");
		for (int i = 0; i < output.size(); i++) {
			outputStreamWriter.write(output.get(i));
		}
		outputStreamWriter.close();

	}
}