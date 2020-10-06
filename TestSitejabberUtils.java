import static org.junit.Assert.assertEquals;
import org.junit.Test;
import sitejabber.Utils;

public class TestSitejabberUtils
{
	@Test
	public void testEncryptDecrypts()
	{
		String string = "test string";
		String key = "XlVzl5iwuIakc8FMlf2Ws2XCkMs6WVHh";
		Utils sitejabber = new Utils(); 

		// assert statements
		try
		{
			assertEquals(string, sitejabber.decrypt(sitejabber.encrypt(string, key), key));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void testDecrypts()
	{
		String string = "9UKN/xS8ZyJJGp7bEGhW38sxHX350H4A6byUMWti/46wuGwndeDOEHuszNS8uHPcr3HETIwQt9akos51iCyfPahBDNhLyWW6lgU2A1IaxMk/bjlL13EpRm5lhuuUngpmgs1VAat3VZkue4CLxhsi5pT4j2DdhMxzBas01X54JND0tfpbQjPtPC4tLDRHayr5Jgyj1OmAf3KQo3LiwZcygA==";
		String key = "XlVzl5iwuIakc8FMlf2Ws2XCkMs6WVHh";
		Utils sitejabber = new Utils(); 

		try
		{
			assertEquals("{\"email\":\"janedoe@gmail.com\",\"order_date\":\"06-13-2013\",\"order_id\":\"1234\",\"first_name\":\"Jane\",\"last_name\":\"Doe\"}", sitejabber.decrypt(string, key));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}