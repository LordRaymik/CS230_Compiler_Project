import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main class of the program, compiles the given input into both assembly code, and hexadecimal machine code (object code)
 */
public class SimpleCompiler {


    /**
     * The main method of the program
     * @param args
     */
    public static void main(String[] args) {

        //the filepath for the compiler's input file
        String inputFilePath = "compilerInput.txt";

        //getting the inputs for the compiler program
        List<String> commandList = parseCommandListFromFile(inputFilePath);

        System.out.println(commandList);

    }


    /**
     * This functon is used to find and return the input file used in the program
     *
     * @param filePath the file path for the input as a string
     *
     * @return the {@link File} that stores the program's input
     */
    public static List<String> parseCommandListFromFile(String filePath) {

        //the final list of commands as strings
        List<String> commandList = new ArrayList<>();

        //attempting to open the file based on our filepath
        try {

            //the file containing the compiler's input
            File inputFile = new File(filePath);

            //the scanner used to read our file data
            Scanner scanner = new Scanner(inputFile);

            //as the file has been successfully found, we will print that to the console
            System.out.println("Input file found successfully...");

            //using a while loop to go through every line in the input file and saving them to a list of strings
            while (scanner.hasNext()) {

                //the line containing the current command in string form
                String currentCommandString = scanner.nextLine();

                //saving the current command string to the list of commands
                commandList.add(currentCommandString);

            } //end while loop

            //closing the scanner as we have finished using it
            scanner.close();

            //returning the final list of commands in string form
            return commandList;

        }
        //catching an exception if a file was not found
        catch (FileNotFoundException exception) {

            //if no file was found, we print to the console that it couldn't be found
            System.out.println("Input file not found...");

            return null;

        } //end exception handling

    } //end method parseCommandListFromFile


} //end class
