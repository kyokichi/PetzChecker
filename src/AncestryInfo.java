import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.HashMap;

public class AncestryInfo
{
    int start; // the starting position in the pet file
    byte[] petData;
    
    int pos;
    
    Ancestor root;
    
    int ancestors;
    HashMap<String, Integer> breeds;
    HashMap<String, Integer> owners;
    HashMap<Ancestor, Integer> petz;
    
    public AncestryInfo(byte[] petData, int start)
    {
        this.petData = petData;
        this.start = start;
        
        pos = start;
        breeds = new HashMap<String, Integer>();
        owners = new HashMap<String, Integer>();
        petz = new HashMap<Ancestor, Integer>();
        
        root = new Ancestor();
        pos = root.setData(petData, pos);
        
        // need to remove the child's breed and owner??
        //System.out.println(petz); 
    }
    
    public String generateHeaderRow(String delim)
    {
        String[] headers = { "GUID",
                        "Breed Personality",
                        "Owner Name",
                        "Adoption Date",
                        "Generations",
                        "Gender",
                        "Blood Status",
                        "Ancestors",
                        "Full Tree",
                        "Tree Trimmed",
                        "Inbred %",
                        "Generations Stored in Tree",
                        "Mom",
                        "Dad"
        };
        
        String result = "";
        
        for(int i = 0; i < headers.length; i++)
        {
            result += headers[i] + delim;
        }
        
        return result;
    }
    
    public String generateRowOutput(String delim)
    {
        String mom = "None";
        String dad = "None";
        
        if(root.mother != null)
            mom = root.mother.name;
        
        if(root.father != null)
            dad = root.father.name;
        
        return "\"" + root.getGUID() + "\"" + delim
            + "\"" + root.breed + "\"" + delim
            + "\"" + root.owner + "\"" + delim
            + "\"" + root.date + "\"" + delim
            + "\"" + root.generations + "\"" + delim
            + "\"" + root.gender + "\"" + delim
            + "\"" + root.blood + "\"" + delim
            + "\"" + ancestors + "\"" + delim
            + "\"" + isFullTree() + "\"" + delim
            + "\"" + isTreeTrimmed() + "\"" + delim
            + "\"" + percentInbred() + "\"" + delim
            + "\"" + root.getGenerationsStored() + "\"" + delim
            + "\"" + mom + "\"" + delim
            + "\"" + dad + "\"" + delim;
    }
    
    public double percentInbred()
    {
        int count = 0; // repeated
        
        for(Ancestor p : petz.keySet())
        {
            int n = petz.get(p);
            
            if(n > 1)
            {
                count += n;
            }
        }
        
        // check for division by zero
        if(ancestors == 0)
        {
            return 0;
        }
        else
        {
            return (double)count / ancestors * 100;
        }
    }
    
    public boolean isFullTree()
    {
        // because 2^0 is 1 and that won't match the number of ancestors
        if(root.generations == 1)
        {
            return true;
        }
        
        // the number of ancestors should equal 2 to power of n which is the number of generations
        return ancestors == Math.pow(2, root.generations-1);
    }
    
    public boolean isTreeTrimmed()
    {
        // will this work for Prism's tree trimmed petz?
        for(Ancestor p : petz.keySet())
        {
            if(p.getGUID().equals("0000-0000-0000-0000"))
            {
                return true;
            }
        }
        
        return false;
    }
    
    private void addToMap(HashMap<String, Integer> map, String key)
    {
        if(map.containsKey(key))
        {
            int n = map.get(key);
            map.put(key, n+1);
        }
        else
        {
            map.put(key, 1);
        }
    }
    
    private void addToPetMap(Ancestor key)
    {
        if(petz.containsKey(key))
        {
            int n = petz.get(key);
            petz.put(key, n+1);
        }
        else
        {
            petz.put(key, 1);
        }
    }
    
    
    class Ancestor
    {
        byte[] GUID;
        //String fGUID; // formatted

        String name;
        String breed;
        String owner;
        String date;
        int generations;
        String gender;
        String blood;

        Ancestor mother;
        Ancestor father;

        public String toString()
        {
            return getGUID();
        }
        
        public boolean equals(Object otherObject)
        {
            if(otherObject instanceof Ancestor)
            {
                Ancestor a = (Ancestor)otherObject;
                return a.getGUID().equals(this.getGUID());
            }
            else
            {
                return false;
            }
        }
        
        public int hashCode()
        {
            return getGUID().hashCode();
        }

        public String getGUID()
        {
            String result = "";

            for(int i = 0; i < GUID.length; i++)
            {
                if(i != 0 && i % 4 == 0)
                {
                    result += "-";
                }

                result += String.format("%x", GUID[i]);
            }

            return result;
        }
        
        public int getGenerationsStored()
        {
            if(mother == null && father == null)
            {
                return 1;
            }
            
            int momSide = 1 + mother.getGenerationsStored();
            int dadSide = 1 + father.getGenerationsStored();
            
            return Math.max(momSide, dadSide);
        }

        public int setData(byte[] petData, int start)
        {
            int pos = start;

            // Globally Unique Indentifier (GUID)
            GUID = new byte[16];
            for(int i = 0; i < GUID.length; i++)
            {
                GUID[i] = petData[i+pos];
            }
            pos += 16;

            //System.out.println(getGUID());

            // Pet'z name
            int length = Helper.convertByteArrayToInt32(petData, pos);
            pos += 4;
            name = Helper.getStringFromLength(pos, petData, length);
            pos += length;


            length = Helper.convertByteArrayToInt32(petData, pos);
            pos += 4;
            breed = Helper.getStringFromLength(pos, petData, length);
            pos += length;
            addToMap(breeds, breed);


            length = Helper.convertByteArrayToInt32(petData, pos);
            pos += 4;
            owner = Helper.getStringFromLength(pos, petData, length);
            pos += length;
            addToMap(owners, owner);


            int adoption = Helper.convertByteArrayToInt32(petData, pos);
            pos += 4;

            int birthday = Helper.convertByteArrayToInt32(petData, pos);
            pos += 4;

            long d = adoption;
            if(adoption == 0)
            {
                d = birthday;
            }

            SimpleDateFormat df = new SimpleDateFormat("M/d/yyyy h:m:s a");
            //df.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = df.format(new Date((long)d * 1000));


            generations = Helper.convertByteArrayToInt32(petData, pos);
            pos += 4;


            boolean isPurebred = (petData[pos] & (byte)1) > 0;
            boolean isFemale = (petData[pos] & ((byte)1 << 1)) > 0;

            gender = "Male";
            if(isFemale)
            {
                gender = "Female";
            }

            blood = "Mixed Breed";
            if(isPurebred)
            {
                blood = "Purebred";
            }

            pos += 1;


            int hasParents = petData[pos];
            if(hasParents == 1)
            {
                pos += 1;
                mother = new Ancestor();
                pos = mother.setData(petData, pos);

                pos += 1;
                father = new Ancestor();
                pos = father.setData(petData, pos);

                ancestors += 2;

                //System.out.println(this + " has parents (mom) " + mother + " and (dad) " + father);
            }
            else if(hasParents == 0)
            {
                // there's two 0's and then a 1 so that's why I need to move over 2
                pos += 2;
            }
            else
            {
                System.out.println("Something went wrong");
            }

            /*System.out.printf(" ( 0x%x 0x%x 0x%x 0x%x ) %s", 
                petData[pos], petData[pos+1], petData[pos+2], petData[pos+3], delim);*/
            
            addToPetMap(this);

            return pos;
        }
    }
}
