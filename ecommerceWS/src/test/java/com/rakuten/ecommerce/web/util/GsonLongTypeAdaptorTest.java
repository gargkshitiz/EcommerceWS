package com.rakuten.ecommerce.web.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.rakuten.ecommerce.web.util.GsonLongTypeAdapter;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
/**
 * @author Kshitiz Garg
 */
public class GsonLongTypeAdaptorTest {

	private static final String SAMPLE_STRING = "test";

	private static final long NUMBER = 47;

	private GsonLongTypeAdapter gsonLongTypeAdapter = new GsonLongTypeAdapter();

	@Mock 
	private JsonReader jsonReader;

	@Mock
	private JsonWriter jsonWriter;

	@Mock
	private Number inputNum;
	
	@Before
	public void setup() throws Exception{
		MockitoAnnotations.initMocks(this);
		Mockito.when(jsonReader.nextString()).thenReturn(String.valueOf(NUMBER));
	}
	
	@Test
	public void read() throws Exception{
		Number number = gsonLongTypeAdapter.read(jsonReader);
		Assert.assertEquals(NUMBER, number.longValue());
	}
	
	@Test
	public void readWhenJsonTokenIsNull() throws Exception{
		whenJsonTokenIsNull();
		Number number = gsonLongTypeAdapter.read(jsonReader);
		Mockito.verify(jsonReader, Mockito.times(1)).nextNull();
		Assert.assertNull(number);
	}

	@Test
	public void readWhenStringIsEmpty() throws Exception{
		whenStringIsEmpty();
		Number number = gsonLongTypeAdapter.read(jsonReader);
		Assert.assertNull(number);
	}

	@Test(expected=JsonSyntaxException.class)
	public void readWhenNFEOccurs() throws Exception{
		whenNFEOccurs();
		Number number = gsonLongTypeAdapter.read(jsonReader);
		Assert.assertNull(number);
	}
	
	@Test
	public void write() throws Exception{
		gsonLongTypeAdapter.write(jsonWriter, inputNum);
		Mockito.verify(jsonWriter, Mockito.only()).value(inputNum);
	}
	
	private void whenNFEOccurs() throws Exception {
		Mockito.when(jsonReader.nextString()).thenReturn(SAMPLE_STRING);
	}

	private void whenStringIsEmpty() throws Exception {
		Mockito.when(jsonReader.nextString()).thenReturn("");
	}
	
	private void whenJsonTokenIsNull() throws Exception {
		Mockito.when(jsonReader.peek()).thenReturn(JsonToken.NULL);
	}

}