package com.example.large_object_bug.controller;

import com.example.large_object_bug.model.File;
import com.example.large_object_bug.repository.FileFSRepository;
import com.example.large_object_bug.stores.FileContentStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@Slf4j
public class FileContentController {

	@Autowired private FileFSRepository filesRepo;
	@Autowired private FileContentStore contentStore;
	
	@RequestMapping(value="/files/{fileId}", method = RequestMethod.PUT)
	public ResponseEntity<?> setContent(@PathVariable("fileId") Long id, @RequestPart MultipartFile file)
			throws IOException {

		Optional<File> f = filesRepo.findById(id);
		if (f.isPresent()) {
			f.get().setMimeType(file.getContentType());

			contentStore.setContent(f.get(), file.getInputStream());

			// save updated content-related info
			filesRepo.save(f.get());

			return new ResponseEntity<Object>(HttpStatus.OK);
		}
		return null;
	}

	@RequestMapping(value="/files/{fileId}", method = RequestMethod.GET)
	public ResponseEntity<?> getContent(@PathVariable("fileId") Long id) {

		filesRepo.save(new File());

		Optional<File> f = filesRepo.findById(id);
		if (f.isPresent()) {
			InputStreamResource inputStreamResource = new InputStreamResource(contentStore.getContent(f.get()));
			HttpHeaders headers = new HttpHeaders();
			headers.setContentLength(f.get().getContentLength());
			headers.set("Content-Type", f.get().getMimeType());
			return new ResponseEntity<Object>(inputStreamResource, headers, HttpStatus.OK);
		}
		return null;
	}

	@RequestMapping(value = "/files/delete/{fileId}", method = RequestMethod.GET)
	public ResponseEntity<?> deleteContent(@PathVariable("fileId") Long id) {
		Optional<File> f = filesRepo.findById(id);
		if (f.isPresent()) {
			contentStore.unsetContent(f.get());
			return new ResponseEntity<Object>("File deleted", HttpStatus.OK);
		}
		return null;
	}
}