package br.com.gvt.eng.vod.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class Utils {

	public static String toUTF8(String text) {

		try {

			byte[] b;
			b = text.getBytes("UTF-8");
			text = new String(b, "UTF-8");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			text = "ERRO: " + e.getMessage();
		}

		return text;
	}

	public static Gson getGson() {
		GsonBuilder builder = new GsonBuilder();

		// Register an adapter to manage the date types as long values
		builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
			@Override
			public Date deserialize(JsonElement json, Type typeOfT,
					JsonDeserializationContext context)
					throws JsonParseException {

				try {
					Pattern pattern = Pattern.compile("\"\\d+-\\d+-\\d+\"");
					if (pattern.matcher(json.getAsJsonPrimitive().toString())
							.matches()) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd");
						return sdf.parse(json.getAsJsonPrimitive().toString()
								.replaceAll("\"", ""));
					}
				} catch (Exception e) {
				}

				return new java.sql.Date(json.getAsJsonPrimitive().getAsLong());
			}
		});
		// builder.setDateFormat("yyyy-MM-dd").create();
		return builder.create();
	}

	/**
	 * Verifica se o campo e nulo ou vazio
	 * 
	 * @param s
	 * @return boolean
	 */
	public static boolean isEmptyOrNull(String s) {
		return s == null || s.equals("");
	}

}
