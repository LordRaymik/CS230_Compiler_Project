import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Main class of the program, compiles the given input into both assembly code, and hexadecimal machine code (object code)
 */
// Laura Romero
    // Silas Johnson

public class SimpleCompiler {


    /**
     * The main method of the program
     * @param args
     */
    public static void main(String[] args) {

        //the filepath for the compiler's input file
        String inputFilePath = "compilerInput.txt";

        compile(inputFilePath);

    }


    /**
     * This is the public method that lets the simple compiler be used <br>
     * Given a file path, it will compile the text within it
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

        //DEBUG
        //printing the final list of instructions
        for (String instruction : compiledInstructions) {
            System.out.println(instruction);
        } //end for each loop

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

        return null;

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
                System.out.println(stringToPrint);
                compiledInstructions.addAll(compilePrintStringInstructions(stringToPrint));

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
    private static List<String> parseInstructionListFromFile(String filePath) {

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


} //end class
