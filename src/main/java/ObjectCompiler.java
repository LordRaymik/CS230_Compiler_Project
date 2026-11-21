import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObjectCompiler {


    /**
     * This is the public method that lets the object compiler be used <br>
     * Given a file path and a list of assembly instructions, it will compile said instructions and output them to a file
     *
     * @param inputFilePath a file path stored as a String
     */
    public static void compile(String inputFilePath) {

        List<String> assemblyInstructions = AssemblyCompiler.parseInstructionListFromFile(inputFilePath);

        //the list that will contain the hexadecimal byte code for the program
        List<String> objectCode = new ArrayList<>();

        //iterating through each assembly instruction qnd converting it to object code
        for (String instruction : assemblyInstructions) {

            objectCode.addAll(compileInstruction(instruction));

        } //end for each loop

        //writing our results to a file
        String fileName = "compilerOutput_Object.txt"; //the name of the output file
        String outputDirectory = "./outputs/"; //the directory the output file will be written to
        File directory = new File(outputDirectory); //turning these file specifications into a file object

        //if the directory we specified does not exist, then we must create it
        if (!directory.exists()) {
            //creating a new directory based on the specifications above
            directory.mkdir();
        }

        //writing the list of compiled assembly instructions to a file
        writeObjectCodeToFile(objectCode, outputDirectory, fileName);

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

        //first we check if the given instruction is "LDBA"
        if (instruction.indexOf("LDBA") == 0) { //checking if the instructions starts with "LDBA", which would signify its instruction type

            return compileLDBA(instruction);

        }
        //checking if the given instruction is "STBA"
        else if (instruction.indexOf("STBA") == 0) { //checking if the instructions starts with "STBA", which would signify its instruction type

            return compileSTBA(instruction);

        } else if (instruction.indexOf("LDWA") == 0) {

            return compileLDWA(instruction);

        } else if (instruction.indexOf("ADDA") == 0) {

            return compileADDA(instruction);

        } else if (instruction.indexOf("STOP") == 0) {

            return compileSTOP(instruction);

        } //end else if

        return List.of();

    } //end method compileInstruction


    /**
     * Compiles the assembly instruction LDBA into object code
     *
     * @param instruction
     *
     * @return
     */
    private static List<String> compileLDBA(String instruction) {

        List<String> objectCode = new ArrayList<>();

        objectCode.add("D0");

        int hexArgumentIndex = instruction.indexOf("0x");
        if (hexArgumentIndex != -1) {
            hexArgumentIndex += 2;
        } //end if

        //the argument being loaded to the accumulator
        String hexArgument = instruction.substring(hexArgumentIndex, instruction.indexOf(','));

        //breaking up the argument into byte sized blocks before adding them to the object code
        for (int i = 0; i < hexArgument.length() - 1; i = i + 2) {

            objectCode.add(hexArgument.substring(i, Math.min(hexArgument.length(), i + 2)));

        } //end for loop

        return objectCode;

    } //end method compileLDBA


    /**
     * Compiles the assembly instruction STBA into object code
     *
     * @param instruction
     *
     * @return
     */
    private static List<String> compileSTBA(String instruction) {

        List<String> objectCode = new ArrayList<>();

        objectCode.add("F1");

        int hexArgumentIndex = instruction.indexOf("0x");
        if (hexArgumentIndex != -1) {
            hexArgumentIndex += 2;
        } //end if

        //the argument being loaded to the accumulator
        String hexArgument = instruction.substring(hexArgumentIndex, instruction.indexOf(','));

        //breaking up the argument into byte sized blocks before adding them to the object code
        for (int i = 0; i < hexArgument.length() - 1; i = i + 2) {

            objectCode.add(hexArgument.substring(i, Math.min(hexArgument.length(), i + 2)));

        } //end for loop

        return objectCode;

    } //end method compileSTBA


    /**
     * Compiles the assembly instruction LDWA into object code
     *
     * @param instruction
     *
     * @return
     */
    private static List<String> compileLDWA(String instruction) {

        List<String> objectCode = new ArrayList<>();

        objectCode.add("C0");

        int argumentIndex = instruction.indexOf(" ");
        if (argumentIndex != -1) {
            argumentIndex += 1;
        } //end if

        //the argument being loaded to the accumulator
        String argument = instruction.substring(argumentIndex, instruction.indexOf(','));

        argument = Integer.toHexString(Integer.parseInt(argument));

        //padding the hexadecimal if it is too short so that it's properly formatted
        while (argument.length() < 4) {
            argument = "0" + argument;
        } //end while loop

        //making sure the hex value isn't too long
        argument = argument.substring(Math.max(0, argument.length() - 4));

        //breaking up the argument into byte sized blocks before adding them to the object code
        for (int i = 0; i < argument.length() - 1; i = i + 2) {

            objectCode.add(argument.substring(i, Math.min(argument.length(), i + 2)));

        } //end for loop

        return objectCode;

    } //end method compileLDWA


    /**
     * Compiles the assembly instruction ADDA into object code
     *
     * @param instruction
     *
     * @return
     */
    private static List<String> compileADDA(String instruction) {

        List<String> objectCode = new ArrayList<>();

        objectCode.add("60");

        int argumentIndex = instruction.indexOf(" ");
        if (argumentIndex != -1) {
            argumentIndex += 1;
        } //end if

        //the argument being loaded to the accumulator
        String argument = instruction.substring(argumentIndex, instruction.indexOf(','));

        argument = Integer.toHexString(Integer.parseInt(argument));

        //padding the hexadecimal if it is too short so that it's properly formatted
        while (argument.length() < 4) {
            argument = "0" + argument;
        } //end while loop

        //making sure the hex value isn't too long
        argument = argument.substring(Math.max(0, argument.length() - 4));

        //breaking up the argument into byte sized blocks before adding them to the object code
        for (int i = 0; i < argument.length() - 1; i = i + 2) {

            objectCode.add(argument.substring(i, Math.min(argument.length(), i + 2)));

        } //end for loop

        return objectCode;

    } //end method compileADDA


    /**
     * Compiles the assembly instruction STOP into object code
     *
     * @param instruction
     *
     * @return
     */
    private static List<String> compileSTOP(String instruction) {

        List<String> objectCode = new ArrayList<>();

        objectCode.add("00");

        return objectCode;

    } //end method compileLDBA


    /**
     * Writes the provided list of object code to a file based on the provided directory and name
     *
     * @param outputDirectory the directory the output file will be written to
     * @param fileName the name of the file that will store the object code from the provided list
     *
     */
    public static void writeObjectCodeToFile(List<String> objectCode, String outputDirectory, String fileName) {

        //attempting to write the given list to a file
        try {

            //the writer which will be used to write our list as a file in the specified directory and file
            FileWriter writer = new FileWriter(outputDirectory + "/" + fileName);

            //iterating through every byte of the object code in the provided list
            for (String objectByte : objectCode) {

                //writing the current byte to the file
                writer.write(objectByte + " ");

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
