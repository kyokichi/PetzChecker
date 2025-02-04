
import java.util.ArrayList;

public class Allele
{
    int start; // keep track of this for editing eventually
    byte[] petData; // shared by all the files
    
    // read from the file
    int center;
    String centerString; // will be set with byte data if the value is not known how its used
    int range;
    boolean isInt; // if the center is read as an integer
    ArrayList<String> dataStrings; // might be null if isInt is true
    
    // I'll have to figure out if these are ever important
    int weight;
    byte weight_type;
    byte combine_type;
    int offset;
    int rate;
    
    
    // reads and sets all the variables
    public Allele(int startPosition, byte[] petData)
    {
        start = startPosition;
        this.petData = petData;
        
        int pos = start;
        
        // first 4 bytes is the CENTER
        // which for Sprite is the pet's preferred value for this adjective
        // if its 0xFFFFFFFFFF, then this is ignored and not used in any calculations
        
        byte[] centerB = {petData[pos], petData[pos+1], petData[pos+2], petData[pos+3]};
        center = Helper.convertByteArrayToInt32(petData, pos);
        pos += 4;
        
        
        // next 4 bytes is the RANGE
        // the max possible deviation from the Center
        // the Center is, naturally the midpoint of this range
        // Therefore the max devation is range/2 on either side of the center value
        range = Helper.convertByteArrayToInt32(petData, pos);
        pos += 4;
        
        
        // next byte is the BITMASK FLAG
        // 0 then Center is an integer
        // 1 then Center is a bitmask
        byte bitmask = petData[pos];
        pos++;
        
        if(bitmask == 1)
            isInt = false;
        else
            isInt = true;
        
        
        // next 4 bytes is the weight
        // a percent value of some kind? always appears to be 100?
        // effectively dictates how important this trait is when calculating a petz affinity for something
        weight = Helper.convertByteArrayToInt32(petData, pos);
        pos += 4;
        
        
        // next byte is WEIGHT TYPE
        // always appears to be 1 in the sprite except in Discipline? or 0?
        weight_type = petData[pos];
        pos++;

        // next byte is COMBINE TYPE which is unknown or 0?
        combine_type = petData[pos];
        pos++;

        
        // next 4 bytes is Offset
        // appears to always be 0? Uknown effect
        offset = Helper.convertByteArrayToInt32(petData, pos);
        pos += 4;
        
        // next 4 bytes is the centering rate
        // Always 60? Unknown effect
        rate = Helper.convertByteArrayToInt32(petData, pos);
        pos += 4;
        
        
        // if it's supposed to be an int, than the centerString is just the center
        // other things might change this
        if(isInt)
        {
            centerString = center + "";
        }
        else
        {
            dataStrings = new ArrayList<String>();
        }
        
        /* String.format("CENTER: 0x%x 0x%x 0x%x 0x%x -- as int: %d",
            petData[pos], petData[pos+1], petData[pos+2], petData[pos+3], center); */
    }
    
    
    // two types of output:
    // either center and range are both posted
    // or the bitmask makes some sort of different kind of output usually a single string or list
    public String tableString(String delim)
    {
        if(isInt) // just take the center and range
        {
            return centerString + delim + range + delim;
        }
        else
        {
            return getBitData() + delim;
        }
    }
    
    public String tableAllData(String delim)
    {
        String row = "";
        
        if(dataStrings == null) // just take the center and range
        {
            row += centerString + delim + range + delim;
        }
        else // if(dataStrings != null)
        {
            row += getBitData() + delim + range + delim;
        }
        
        row += weight + delim + weight_type + delim + combine_type + delim
                + offset + delim + rate + delim;
        
        return row;
    }
    
    public String getBitData()
    {
        String result = "";
        for(String s : dataStrings)
        {
            result += "["+s+"] ";
        }
        return result;
    }
}
