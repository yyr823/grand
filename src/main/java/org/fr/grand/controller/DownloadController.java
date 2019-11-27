/**
 * 
 */
package org.fr.grand.controller;

import java.io.IOException;

import org.fr.grand.conf.SystemLog;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({ "/download" })
public class DownloadController {
	@SystemLog(module = "File management", methods = "downloadFile")
	@RequestMapping({ "/file" })
	public ResponseEntity<InputStreamResource> downloadFile(String fileName) throws IOException {
		String filePath = "C:/authen/" + fileName;
		FileSystemResource file = new FileSystemResource(filePath);
		if (file.exists()) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Content-Disposition",
					"attachment; filename=" + new String(file.getFilename().getBytes("utf-8"), "ISO-8859-1"));
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");
			return ((ResponseEntity.BodyBuilder) ResponseEntity.ok().headers(headers))
					.contentLength(file.contentLength())
					.contentType(MediaType.parseMediaType("application/octet-stream"))
					.body(new InputStreamResource(file.getInputStream()));
		}
		return null;
	}

}
