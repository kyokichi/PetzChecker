public class VeterinaryHistory
{
    String gender; // gender from Vet History
    String neutered;
    String dependant;
    String pregnant;
    
    // set by constructor
    private byte[] petData;
    private int start;
    
    int endOfSection;
    
    public VeterinaryHistory(byte[] petData, int start)
    {
        this.start = start;
        this.petData = petData;
        
        int pos = start;
        
        if(petData[pos] == 0)
            gender = "Male";
        else if(petData[pos] == 1)
            gender = "Female";
        else
            gender = "Non-binary";
        pos++;
        
        
        if(petData[pos] == 0)
            neutered = "Not spayed/neutered";
        else if(petData[pos] == 1)
            neutered = "Spayed/Neutered";
        else
            neutered = "Unknown";
        pos++;
        
        if(petData[pos] == 0)
            dependant = "None";
        else if(petData[pos] == 1)
            dependant = "Has Dependant Child";
        else
            dependant = "Unknown";
        pos++;
        
        
        if(petData[pos] == 0)
            pregnant = "Not pregnant";
        else if(petData[pos] == 1)
            pregnant = "Pregnant";
        else
            pregnant = "Unknown";
        pos++;
        
        
        int numVetSections = Helper.convertByteArrayToInt32(petData, pos);
        pos += 4;
        
        // between 0 to 8 sections present
        // I'm going to skip for right now
        for(int i = 0; i < numVetSections; i++)
        {
            pos += 4; // tag
            
            pos += 4; // max number of records
            
            int numRecords = Helper.convertByteArrayToInt32(petData, pos);
            pos += 4;
            
            pos += 4 * numRecords; // array of timestamps
            
            int number = Helper.convertByteArrayToInt32(petData, pos);
            pos += 4;
            
            pos += 4 * number; // array of number of leves
        }
    
        // biorhythms sections
        int bioNumber = Helper.convertByteArrayToInt32(petData, pos);
        pos += 4;

        for(int i = 0; i < bioNumber; i++)
        {
            pos += 4 * 10; // ignoring this for now
        }
        
        endOfSection = pos;
    }
}
