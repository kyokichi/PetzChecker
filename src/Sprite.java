/*
 * A generic piece of petz data
 */

public class Sprite
{
    
    // each chunk is 23 so take 23 to multiple it by X and you'll move X chunks forwards
    
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
    
    private final String[] allFlavors = {"chicken", "beef", "fish", "wetfood/leftovers", "milk", "candy/sweets", "catnip", "cheese", // 7
                                    "shiny/colorful", "chewtoy", "soft/mouse", "bone", "wood", "metal", "waterbowl", "rocks", // 15
                                    "???", "hairball/fleaspray", "???", "???", "???", "???", "???", "chemicals", // 23
                                    "garbage", "???", "fleabottle", "plants", "cardboard" }; //28
    
    private final String[] animations = {"01", "02", "03", "04", "05", "06", "07", "08",
                                    "09", "10", "11", "12", "13", "14", "15", "16",
                                    "17", "18", "19", "20", "21", "22", "23", "24",
                                    "25", "26", "27", "28", "29", "30", "31", "32" };
    
    
    
    // this will be the output file values if needed
    String[] output;
    
    static String tableHeaders = "";
    private static boolean generatedTableHeaders = false;
    
    String table = "";
    
    String notes = "";
    
    // set by constructor
    private byte[] petData;
    private int start;
    
    
    // stats that people care about
    String flavor;
    String color;
    
    
    public Sprite(byte[] petData, int start)
    {
        this.petData = petData;
        this.start = start;
    }
    
    // @param toTable will just decide how the headers will get changed or not
    // @param notes will include the notes section or not
    public void read(boolean toTable, String delim, boolean hasNotes)
    {
        int pos = start;

        int n = Helper.convertByteArrayToInt32(petData, pos);
        pos += 4;
        
        //System.out.println(n);
        // check to see if N is not equal to 41 then I should abort mission
        
        if(!toTable)
        {
            output = new String[headers.length];
        }

        for(int col = 0; col < n; col++)
        {
            // first 4 bytes is the CENTER
            // which for Sprite is the pet's preferred value for this adjective
            // if its 0xFFFFFFFFFF, then this is ignored and not used in any calculations

            byte[] centerB = {petData[pos], petData[pos+1], petData[pos+2], petData[pos+3]};
            int center = Helper.convertByteArrayToInt32(petData, pos);
            
            // could move this to the end
            if(!toTable)
            {
                output[col] = String.format("CENTER: 0x%x 0x%x 0x%x 0x%x -- as int: %d",
                        petData[pos], petData[pos+1], petData[pos+2], petData[pos+3], center);
            }
            
            pos += 4;



            // next 4 bytes is the RANGE
            // the max possible deviation from the Center
            // the Center is, naturally the midpoint of this range
            // Therefore the max devation is range/2 on either side of the center value
            //wr.printf("RANGE: 0x%x 0x%x 0x%x 0x%x ", 
            //      petData[pos], petData[pos+1], petData[pos+2], petData[pos+3]);

            int range = Helper.convertByteArrayToInt32(petData, pos);
            pos += 4;
            //wr.println(" -- as int: "+range);



            // next byte is the BITMASK FLAG
            // 0 then Center is an integer
            // 1 then Center is a bitmask
            //wr.printf("BITMASK: 0x%x %n", petData[pos]);
            byte bitmask = petData[pos];
            pos++;


            // next 4 bytes is the weight
            // a percent value of some kind? always appears to be 100?
            // effectively dictates how important this trait is when calculating a petz affinity for something
            //wr.printf("WEIGHT: 0x%x 0x%x 0x%x 0x%x ", 
            //      petData[pos], petData[pos+1], petData[pos+2], petData[pos+3]);

            int weight = Helper.convertByteArrayToInt32(petData, pos);
            pos += 4;
            //wr.println(" -- as int: "+weight);


            // next byte is WEIGHT TYPE
            // always appears to be 1 in the sprite except in Discipline? or 0?
            //wr.printf("WEIGHT TYPE: 0x%x %n", petData[pos]);
            byte weight_type = petData[pos];
            pos++;


            // next byte is COMBINE TYPE which is unknown or 0?
            //wr.printf("COMBINE TYPE: 0x%x %n", petData[pos]);
            byte combine_type = petData[pos];
            pos++;


            // next 4 bytes is Offset
            // appears to always be 0? Uknown effect
            //wr.printf("OFFSET: 0x%x 0x%x 0x%x 0x%x ", 
            //      petData[pos], petData[pos+1], petData[pos+2], petData[pos+3]);

            int offset = Helper.convertByteArrayToInt32(petData, pos);
            pos += 4;
            //wr.println(" -- as int: "+offset);




            // next 4 bytes is the centering rate
            // Always 60? Unknown effect
            //wr.printf("CENTERING RATE: 0x%x 0x%x 0x%x 0x%x ", 
            //      petData[pos], petData[pos+1], petData[pos+2], petData[pos+3]);

            int rate = Helper.convertByteArrayToInt32(petData, pos);
            pos += 4;
            //wr.println(" -- as int: "+rate);

            
            

            // OUTPUT TIME
            if(headers[col].charAt(0) != '-') // skip it
            {
                if(hasNotes)
                {
                    // USED TO FIND WEIRD STUFF AND STORE IN NOTES
                    if(weight != 100)
                    {
                        notes+= headers[col]+" weight is not 100; ";
                    }
                    if(weight_type != 1)
                    {
                        notes += headers[col]+" weight type is not 1; ";
                    }
                    if(combine_type != 0)
                    {
                        notes += headers[col]+" combine type is not 0; ";
                    }
                    if(offset != 0)
                    {
                        notes += headers[col]+" offset is not 0; ";
                    }
                    if(rate != 60)
                    {
                        notes += headers[col]+" center rate not 60; ";
                    }
                }
                
                
                if(bitmask == 1)
                {
                    if(col == 6) // COLOR
                    {
                        color = bytesToNames(centerB, allColors);
                        table += color + delim;
                    }
                    else if(col == 7) // FLAVOR
                    {
                        flavor = bytesToNames(centerB, allFlavors);
                        table += flavor + delim;
                    }
                    else if(col == 37) // SIGNATURE MOVES
                    {
                        String anim = bytesToNames(centerB, animations);
                        table += anim + delim;
                        
                        /*
                        if(center == 1)
                        {
                            table += "None" + delim;
                        }
                        else
                        {
                            table += center + String.format(" ( 0x%x 0x%x 0x%x 0x%x )%s", centerB[0],
                                centerB[1], centerB[2], centerB[3], delim);
                        }
                        */
                    }
                    else if(col == 39) // FOOD FINICKINESS https://gyiyg.petz.quest/foodfinickiness
                    {
                        if(center == 1)
                        {
                            table += "No food finickiness";
                        }
                        else if(center == 2)
                        {
                            table += "Only likes 1 flavor";
                        }
                        else if(center == 3)
                        {
                            table += "Dislikes only cheese";
                        }
                        else
                        {
                            table += center;
                        }
                        table += delim;
                    }
                    else
                    {
                        table += String.format("0x%x 0x%x 0x%x 0x%x%s", centerB[0],
                                centerB[1], centerB[2], centerB[3], delim);
                    }
                    
                    // CONSTRUCTS THE TABLE HEADERS
                    if(!generatedTableHeaders)
                    {
                        tableHeaders += headers[col] + delim;
                    }
                    
                    if(range != 0)
                    {
                        notes+= headers[col]+" bitmask has a range value !?; ";
                    }
                }
                else
                {
                    table += center + delim + range + delim;
                    if(!generatedTableHeaders)
                    {
                        tableHeaders += headers[col] + " Center" + delim + headers[col] + " Range" + delim;
                    }
                }
            }

        }
        
        if(!generatedTableHeaders)
        {
            generatedTableHeaders = true;
        }
    }
    
    
    
    /**
     * Creates a string of the preferences based on the 4 bytes passed in
     * @param petPref the 4 bytes read from a pet file
     * @param adjNames all the possible adjectives
     * @return a string formatted how I'd like it to appear
     */
    private static String bytesToNames(byte[] petPref, String[] adjNames)
    {
        String result = "";
        for(int outterByte = 0; outterByte < petPref.length; outterByte++)
        {
            for(int b = 0; b < 8; b++)
            {
                int index = b + (outterByte * 8);
                if( ((petPref[outterByte] & (1 << b)) != 0) && (index < adjNames.length) )
                {
                    result += adjNames[index] + " ";
                    // result += "("+index+") "; REMOVED IN RELEASE
                }
            }
        }
        
        return result;
    }
}
