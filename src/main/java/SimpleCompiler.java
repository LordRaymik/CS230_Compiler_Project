import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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

        AssemblyCompiler.compile(inputFilePath);

        ObjectCompiler.compile("./outputs/compilerOutput_Assembly.txt");

    } //end main

} //end class
