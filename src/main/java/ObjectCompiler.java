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

        System.out.println(objectCode);

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

    } //end method compileLDBA


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

} //end class
