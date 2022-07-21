package br.com.jdo2.poc.envixo.util;

import org.springframework.http.MediaType;

public class MediaTypeUtil {
	
	public static String getMimetype(String fileName) {
		if (fileName.endsWith(".png")) {
			return MediaType.IMAGE_PNG_VALUE;
		}else if (fileName.endsWith(".pdf")) {
			return MediaType.APPLICATION_PDF_VALUE;
		}
		return MediaType.APPLICATION_PDF_VALUE;
	}


}
