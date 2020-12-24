import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class WriteToFile {
    public static void writeOutput(String fileName, HashMap<Integer, Double> output, String algorithm, int i) {
        try {

            BufferedWriter out = openFile2(fileName);
            String o = "" + algorithm;
            out.write(o);
            out.newLine();
            for (Integer it : output.keySet()) {
                o = "," + "," + "" + output.get(it) + "," + it;
                out.write(o);
                out.newLine();
            }
            out.close();
        } catch (IOException e) {
            System.err.println("Couldn't write to file");
        }
    }

    public static BufferedWriter openFile2(String fileName) {
        BufferedWriter out = null;
        try {
            FileWriter s = new FileWriter(fileName, false);
            out = new BufferedWriter(s);
            out.newLine();


        } catch (Exception e) {
            System.err.println("Couldn't open the file");
        }
        return out;

    }

}