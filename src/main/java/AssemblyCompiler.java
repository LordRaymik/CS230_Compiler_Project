import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AssemblyCompiler {


    /**
     * This is the public method that lets the assembly compiler be used <br>
     * Given a file path, it will compile the text within it and output it to a file
     *
     * @param inputFilePath a file path stored as a String
     */
    public static void compile(String inputFilePath) {

        //getting the inputs for the compiler program
        List<String> instructionList = parseInstructionListFromFile(inputFilePath);

        //the final list of compiled instructions
        List<String> compiledInstructions = new ArrayList<>();

        //iterating through the list of instructions and compiling each one
        for (String instruction : instructionList) {

            compiledInstructions.addAll(compileInstruction(instruction));

        } //end for each loop

        //adding the final instructions needed
        compiledInstructions.add("STOP");
        compiledInstructions.add(".END");

        //writing our results to a file
        String fileName = "compilerOutput_Assembly.txt"; //the name of the output file
        String outputDirectory = "./outputs/"; //the directory the output file will be written to
        File directory = new File(outputDirectory); //turning these file specifications into a file object

        //if the directory we specified does not exist, then we must create it
        if (!directory.exists()) {
            //creating a new directory based on the specifications above
            directory.mkdir();
        }

        //writing the list of compiled assembly instructions to a file
        writeInstructionListToFile(compiledInstructions, outputDirectory, fileName);

    } //end method compile


    /**
     * method used to compile a single instruction
     *
     * @param instruction the instruction being compiled
     *
     * @return the compiled instruction as a string
     */
    private static List<String> compileInstruction(String instruction) {

        /*
        first we must parse which instruction this is, then call the relevant method to compile that instruction
         */

        //first we check if the given instruction is "println"
        int printlnIndex = instruction.indexOf("println");
        if (printlnIndex == 0) { //checking if the instructions starts with "println", which would signify its instruction type

            return compilePrintln(instruction);

        } //end if

        return List.of();

    } //end method compileInstruction


    /**
     *
     * This method is called under the assumption that the given instruction is "println"
     *
     * @param instruction the String containing the instruction
     *
     * @return the compiled println instruction
     */
    private static List<String> compilePrintln(String instruction) {

        //the list containing each compiled instruction that is derived from the given instruction the method compiles
        List<String> compiledInstructions = new ArrayList<>();

        //finding the bounds of the print instruction
        int start = instruction.indexOf("(");
        int end = instruction.indexOf(")", start + 1);

        //checking if the bounds of the instruction we found are valid
        if (start != -1 && end != -1 && start < end) {

            int stringStart = instruction.indexOf("\"", start + 1);
            int stringEnd = instruction.indexOf("\"", stringStart + 1);

            //checking if the bounds of the string are valid
            if (stringStart != -1 && stringEnd != -1 && stringStart < stringEnd) {

                String stringToPrint = instruction.substring(stringStart + 1, stringEnd);
                compiledInstructions.addAll(compilePrintStringInstructions(stringToPrint));
                compiledInstructions.addAll(compilePrintCharacterInstructions('\n'));

            } //end if

        } //end if

        return compiledInstructions;

    } //end method compilePrintln


    /**
     * This method takes a string and returns the list of properly formatted assembly instructions that will print out each of its characters
     *
     * @param stringToPrint the string to be printed
     *
     * @return the list of instructions needed to print the provided string
     */
    private static List<String> compilePrintStringInstructions(String stringToPrint) {

        //the list of instructions needed to print the provided string
        List<String> compiledPrintInstructions = new ArrayList<>();

        //iterating through each character in the string that needs to be printed
        for (int i = 0; i < stringToPrint.length(); i++) {

            //compiling the instructions needed to print the character at position "i" in the string
            //and adding them to the final list of instructions needed to print the full string
            compiledPrintInstructions.addAll(compilePrintCharacterInstructions(stringToPrint.charAt(i)));

        } //end for loop

        return compiledPrintInstructions;

    } //end method compilePrintStringInstructions


    /**
     * This method takes a character and returns a list of properly formatted assembly instructions that will print it out
     *
     * @param character the character being printed
     *
     * @return the list of instructions needed to print the character
     */
    private static List<String> compilePrintCharacterInstructions(char character) {

        List<String> printInstructions = new ArrayList<>();

        printInstructions.add("LDBA " + characterToHexASCII(character) + ", i");
        printInstructions.add("STBA 0xFC16, d");

        return printInstructions;
    } //end method compilePrintCharacterInstructions


    /**
     * Converts the given character into its hexadecimal ASCII value
     *
     * @param character the character to be converted
     *
     * @return the hexadecimal ASCII value of the provided character
     */
    private static String characterToHexASCII(char character) {
        return "0x" + Integer.toHexString(character);
    } //end method characterToHexASCII


    /**
     * This functon is used to find and return the input file used in the program
     *
     * @param filePath the file path for the input as a string
     *
     * @return the {@link File} that stores the program's input
     */
    public static List<String> parseInstructionListFromFile(String filePath) {

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

    } //end method parseInstructionListFromFile


    /**
     * Writes the provided list of instructions to a file based on the provided directory and name
     *
     * @param outputDirectory the directory the output file will be written to
     * @param fileName the name of the file that will store the instructions from the provided list
     *
     */
    public static void writeInstructionListToFile(List<String> instructionList, String outputDirectory, String fileName) {

        //attempting to write the given list to a file
        try {

            //the writer which will be used to write our list as a file in the specified directory and file
            FileWriter writer = new FileWriter(outputDirectory + "/" + fileName);

            //iterating through every instruction in the provided list
            for (String instruction : instructionList) {

                //writing the current instruction to the file
                writer.write(instruction + "\n");

            } //end for each loop

            //closing the writer now that the contents of the file have been written
            writer.close();

        }
        //catching an exception if something happened with the IO
        catch (IOException exception) {

            System.out.println(exception);

        } //end exception handling

    } //end method writeToFile

} //end class
