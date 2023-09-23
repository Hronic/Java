import java.util.HashMap; // import the HashMap class
import java.util.Random;

public class Test
{
    public static void main(String[] args)
    {
        String str = "+25";
        String str1 = "25.06";
        System.out.println(isNumeric(str));
        System.out.println(isNumeric(str1));



//        HashMap<Integer, ClassObject.Estate> EstateData = new HashMap<Integer, ClassObject.Estate>();
//        HashMap<Integer, ClassObject.Block> BlockData = new HashMap<Integer, ClassObject.Block>();
//        HashMap<Integer, ClassObject.Mieszkanie> MieszkanieData = new HashMap<Integer, ClassObject.Mieszkanie>();
//        HashMap<String, ClassObject.Person> PersonsData = new HashMap<String, ClassObject.Person>();
//        HashMap<Integer, Object> ItemsData = new HashMap<Integer, Object>();
//
//
//        ClassObject class2 = new ClassObject();
//
//        ClassObject.Estate obj = new ClassObject.Estate(EstateData);
//        System.out.println(EstateData.size());
//        ClassObject.Estate obj2 = new ClassObject.Estate(EstateData);
//        System.out.println(EstateData.size());
//
//        ClassObject.Block Blok1 = new ClassObject.Block(BlockData);
//        System.out.println(BlockData.size());
//        ClassObject.Block Blok2 = new ClassObject.Block(BlockData);
//        System.out.println(BlockData.size());
//
//        ClassObject.Mieszkanie Mieszkanie1 = new ClassObject.Mieszkanie(30,MieszkanieData);
//        System.out.println(MieszkanieData.size());
//        System.out.println(Mieszkanie1.ReadSpace());
//        System.out.println(Mieszkanie1.ReadId());
//        ClassObject.Mieszkanie Mieszkanie2 = new ClassObject.Mieszkanie(7,9,3,MieszkanieData);
//        System.out.println(MieszkanieData.size());
//
//
//
//        System.out.println(EstateData);
//        System.out.println(BlockData);
//        System.out.println(MieszkanieData);


    }
    private static boolean isNumeric(String strTest)
    {
        return strTest != null && strTest.matches("[0-9]+");
    }
}
