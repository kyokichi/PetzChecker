import java.io.*;

public class Main
{
    public static FileWriter per;
    public static FileWriter inherited;
    
    public static FileWriter out;
    
    public static void main(String[] args) throws IOException
    {
        //mainData();
        mainTest();
    }
    
    
    
    public static void mainTest() throws IOException
    {
        per = new FileWriter("petz-personality-data.csv");
        inherited = new FileWriter("petz-inherited-data.csv");
        
        // INHERITED DATA isn't done yet because I need to find the right spot
        // Looks data needs to be formatted properly because when the allele range is 0, then we don't want to print it
        
        traverseFilesIn(".", ",");
        
        per.close();
        inherited.close();
    }
    
    public static void processData(Pet pet, boolean firstRow) throws UnsupportedEncodingException, IOException
    {
        
        // This is where I write the code that I want to happen
        String delim = ",";
        boolean allData = false; // true doesn't work for the header row sadly!
        
        pet.generatePersonalityData(allData);
        pet.generateInheritedData(allData);
        
        if(firstRow)
        {
            per.write("Name" + delim + pet.sprite.generateHeaderRow(delim)
                    + pet.behavior.generateHeaderRow(delim) + "\n");
            
            inherited.write("Name" + delim + pet.sprite.generateHeaderRow(delim) 
                    + pet.behavior.generateHeaderRow(delim) 
                    + pet.inheritedLooks1.generateHeaderRow(delim) + "\n");
        }
        
        per.write(pet.name + delim + pet.sprite.generateRowOutput(delim) 
                + pet.behavior.generateRowOutput(delim) + "\n");
        
        inherited.write(pet.name + " Gene 1" + delim + pet.inheritedSprite1.generateRowOutput(delim) 
                + pet.inheritedBehavior1.generateRowOutput(delim) 
                + pet.inheritedLooks1.generateRowOutput(delim) + "\n" 
                + pet.name + " Gene 2" + delim + pet.inheritedSprite2.generateRowOutput(delim) 
                + pet.inheritedBehavior2.generateRowOutput(delim) 
                + pet.inheritedLooks2.generateRowOutput(delim) + "\n" );
        
        // all the code that modifies the pet file to be what I want for output purposes
        // the pet file should have and keep track of a bit of data that I want for output
    }
    
    
    // Data people care about
    public static void mainData() throws IOException
    {
        out = new FileWriter("petz.csv");
        
        traverseFilesIn(".", ",");
        
        out.close();
    }
    
    public static void processDataMain(Pet pet, boolean firstRow) throws UnsupportedEncodingException, IOException
    {
        // This is where I write the code that I want to happen
        String delim = ",";
        boolean allData = false;
        
        pet.generatePersonalityData(allData);
        pet.generateAncestryInfo();
        pet.generateLNZ();
        
        //System.out.println(pet.name);
        
        if(firstRow)
        {
            out.write("Name" + delim + "Favorite Color" + delim + "Favorite Flavor" 
                    + delim + "Food Finickiness" + delim + "Signature Moves" + delim
                    + "Breed Face" + delim
                    + pet.ancestry.generateHeaderRow(delim)
                    + "Profile Comment" + delim
                    + pet.behavior.generateHeaderRow(delim) + "\n");
        }
        
        out.write(pet.name + delim + "\"" + pet.sprite.getColor() + "\"" + delim 
                + "\"" + pet.sprite.getFlavor() + "\"" + delim
                + "\"" + pet.sprite.getFoodFinickiness() + "\"" + delim 
                + "\"" + pet.sprite.getSignatureMoves() + "\"" + delim
                + pet.lnz.face + delim
                + pet.ancestry.generateRowOutput(delim)
                + "\"" + pet.profile + "\"" + delim 
                + pet.behavior.generateRowOutput(delim) + "\n");
    }
    
    public static void traverseFilesIn(String path, String delim) throws IOException
    {
        // for each file in the directory
        // check the extension, should be .pet for now
        // then process the file
        
        File dir = new File(path);
        File[] dirList = dir.listFiles();
        
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
                
                processData(pet, firstRow);
                
                if(firstRow)
                {
                    firstRow = false;
                }
                
                inputStream.close();
            }
        }
    }
}