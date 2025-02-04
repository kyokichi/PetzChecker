import java.io.*;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        traverseFilesIn(".", "petz.csv", ",");
    }
    
    public static Pet processData(Pet pet) throws UnsupportedEncodingException, IOException
    {
        // This is where I write the code that I want to happen
        boolean toTable = true;
        String delim = ",";
        boolean hasNotes = true;
        pet.generatePersonalityData(toTable, delim, hasNotes);
        
        // all the code that modifies the pet file to be what I want for output purposes
        // the pet file should have and keep track of a bit of data that I want for output
        
        return pet;
    }
    
    
    
    public static void traverseFilesIn(String path, String outputFilename,
            String delim) throws IOException
    {
        // for each file in the directory
        // check the extension, should be .pet for now
        // then process the file
        
        File dir = new File(path);
        File[] dirList = dir.listFiles();
        
        FileWriter fw = new FileWriter(outputFilename);
        boolean firstRow = true;
        
        if(dirList != null)
        {
            for(File f : dirList)
            {
                String inputFile = f.getName();
                String name = "";
                boolean isPet = false;
                
                if(inputFile.length() > 5 && inputFile.substring(inputFile.length()-5).equals(".baby"))
                {                    
                    name = inputFile.substring(0, inputFile.length()-5);
                }
                
                else if(inputFile.length() > 4 && inputFile.substring(inputFile.length()-4).equals(".pet"))
                {                    
                    name = inputFile.substring(0, inputFile.length()-4);
                    isPet = true;
                }
                
                else
                {
                    continue;
                }
                
                InputStream inputStream = new FileInputStream(f);
                long fileSize = f.length();
                byte[] allBytes = new byte[(int)fileSize];
                inputStream.read(allBytes);
                
                Pet pet = new Pet(allBytes, name, isPet);
                
                pet = processData(pet);
                
                if(firstRow)
                {
                    fw.write("Name" + delim + pet.sprite.tableHeaders + "\n");
                    firstRow = false;
                }
                
                fw.write(pet.name + delim + pet.sprite.table + "\n");
            }
        }
        
        fw.close();
    }
}