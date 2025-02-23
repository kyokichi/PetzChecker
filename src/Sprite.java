/*
 * A generic piece of petz data
 */

public class Sprite
{
    // 41 pieces of data
    String[] headers = { "-Type", "-Chrz", "-Toyz", "-Prop", "-Part", 
        "-3D", "Color", "Flavor", "-Size", "-Mass", "-Friction", "Tasty", "Edible",
        "Fatty", "-Liquid", "Drug", "Aphrodisiac", "Discipline", "Chew", "Tug",
        "Density", "-Thickness", "-Soft", "Fuzzy", "Round", "Bounce", "Swatty", 
        "Pretty", "Vain", "Paint", "Groom", "BadNoisy", "NiceNoisy", "Flies",
        "Rideable", "Mouselike", "Unknown 1", "Signature Moves", "Unknown 3", 
        "Flavor Finickiness", "Unknown 5"};
    // inside the loop, I'll modify these values based on whether they have a center and range
    
    private final String[] allColors = {"white", "black", "red", "green", "yellow", "blue", "purple", "pink", // 7
                                  "orange", "brown", "gray", "clear" }; // 11
    
    private final String[] allFlavors = {"chicken", "beef", "fish", "turkey", "milk", "sweet", "catnip", "cheese", // 7
                                    "plastic", "rubber", "soft", "bone", "wood", "metal", "water", "rock", // 15
                                    "unknown1", "hairball/fleaspray", "unknown2", "unknown3", "unknown4", "unknonw5", "unknown6", "chemicals", // 23
                                    "garbage", "unknown7", "fleabottle", "plants", "healthy" }; //28
    
    private final String[] animations = {"01", "02", "03", "04", "05", "06", "07", "08",
                                    "09", "10", "11", "12", "13", "14", "15", "16",
                                    "17", "18", "19", "20", "21", "22", "23", "24",
                                    "25", "26", "27", "28", "29", "30", "31", "32" };
    
    
    
    // set by constructor
    private byte[] petData;
    private int start;
    
    
    // stats that people care about
    int color = 6;
    int flavor = 7;
    int signatureMoves = 37;
    int foodFinickiness = 39;
    
    boolean allData;
    Allele[] data;
    int alleleSize;
    
    public Sprite(byte[] petData, int start, boolean allData)
    {
        this(petData, start, allData, true);
    }
    
    public Sprite(byte[] petData, int start, boolean allData, boolean hasOffset)
    {
        int allele_size = 23;
        if(!hasOffset)
            allele_size = 15;
        
        this.petData = petData;
        this.start = start;
        this.allData = allData;
    
        int n = Helper.convertByteArrayToInt32(petData, start);
        System.out.println("Sprite: " + n);
        
        
        if(n != 41) // check to see if N is not equal to then I should abort mission
        {
            System.out.println("Problem in Sprite: " + n);
        }
        
        data = new Allele[n];
        
        for(int col = 0; col < n; col++)
        {
            int pos = (start + 4) + (col * allele_size);
            
            if(headers[col].charAt(0) != '-') // if there's no hyphen, we don't skip it
            {
                data[col] = new Allele(headers[col], pos, petData, hasOffset);
                alleleSize = data[col].size;
                
                data[col].text = data[col].center + ""; // default unless otherwise stated below
                
                // time to set some proper data
                if(data[col].isBitmask)
                {
                    if(col == color) // COLOR
                    {
                        data[col].setTextFromAdjectives(allColors);
                    }
                    else if(col == flavor) // FLAVOR
                    {
                        data[col].setTextFromAdjectives(allFlavors);
                    }
                    else if(col == signatureMoves) // SIGNATURE MOVES
                    {
                        data[col].setTextFromAdjectives(animations);
                    }
                    else if(col == foodFinickiness) // FOOD FINICKINESS https://gyiyg.petz.quest/foodfinickiness
                    {
                        int center = data[col].center;
                        if(center == 1)
                        {
                            data[col].text = "No food finickiness";
                        }
                        else if(center == 2)
                        {
                            data[col].text = "Only likes 1 flavor";
                        }
                        else if(center == 3)
                        {
                            data[col].text = "Dislikes only cheese";
                        }
                        else
                        {
                            data[col].text = center + "Not naturally occurring value.";
                        }
                    }
                }
            }
            else if(allData)
            {
                data[col] = new Allele(headers[col].substring(1), pos, petData, hasOffset);
                alleleSize = data[col].size;
            }
            // else its just null
        }
    }
    
    public String generateRowOutput(String delim)
    {
        String row = "";
        
        for(Allele a : data)
        {
            if(a != null)
            {
                row += a.output(delim);
            }
        }
        
        return row;
    }
    
    public String generateHeaderRow(String delim)
    {
        String row = "";
        
        for(int col = 0; col < headers.length; col++)
        {
            String header = headers[col];
            
            if(header.charAt(0) != '-')
            {
                if(data[col].isBitmask == false) // then there are two columns
                {
                    row += header + " Center" + delim + header + " Range" + delim;
                }
                else
                {
                    row += header + delim;
                }
            }
        }
        
        return row;
    }
    
    
    public String getColor()
    {
        return data[color].text;
    }
    
    public String getFlavor()
    {
        return data[flavor].text;
    }
    
    public String getSignatureMoves()
    {
        return data[signatureMoves].text;
    }
    
    public String getFoodFinickiness()
    {
        return data[foodFinickiness].text;
    }
    
}