// Student Name : Akash Singh (ID 801022198)
//Importing the required packages
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Decoding {

	public static void main(String[] args) throws IOException {
        
		//Reading the encoded file name and bits size from command line arguments
		String[] filename = args[0].split(".lzw");
		String fname = filename[0];
		String bit_size = args[1];
		int bits = Integer.parseInt(bit_size);
		 //Initializing the maximum size of dictionary
		int max_table_size = (int) Math.pow(2, bits);
		//Declaring and initializing the dictionary with 0 255 ASCII codes with respective character
		LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
		for (int i = 0; i < 256; i++) {
			String temp = (char) i + "";
			map.put(i, temp);
		}
	   //Reading the encoded file name and bits size from command line arguments
		InputStream inputStream = new FileInputStream(args[0]);
		Reader inputStreamReader = new InputStreamReader(inputStream, "UTF-16BE");
		ArrayList<String> output = new ArrayList<String>();
		String NEW_STRING = null;
		int data = inputStreamReader.read();
		//System.out.println(data);
		String STRING = null;
		if (data != -1) {
			STRING = map.get(data);
			output.add(STRING);
		}

		int codes = 256;
		while ((data = inputStreamReader.read()) != -1) {
			// data = inputStreamReader.read();
			int code = data;
			//System.out.println(code);
			if (map.containsKey(code)) {
				NEW_STRING = map.get(code);
			} else {
				NEW_STRING = STRING + STRING.charAt(0);
			}
			output.add(NEW_STRING);
			if (map.size() < max_table_size) {
				map.put(codes++, STRING + NEW_STRING.charAt(0));
			}
			STRING = NEW_STRING;
		}
		
		inputStreamReader.close();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < output.size(); i++) {
			sb.append(output.get(i));
		}
		//Converting the decoded data from StringBuffer object to String 
		String final_string = sb.toString();
        //Writing the decoded file in format filename_decoded.txt
		PrintWriter pwriter = new PrintWriter(fname + "_decoded.txt");
		pwriter.write(final_string);
		pwriter.flush();
		pwriter.close();
	}

}
