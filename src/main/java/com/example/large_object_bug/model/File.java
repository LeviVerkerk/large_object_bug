package com.example.large_object_bug.model;

import lombok.*;
import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;
import org.springframework.content.commons.annotations.MimeType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class File {

	@Id
	private long id;

	private String name;
	private Date created = new Date();
	private String summary;

	@ContentId
	private String contentId;

	@ContentLength
	private long contentLength;

	@MimeType
	private String mimeType = "text/plain";

	public File(long id, String name, Date created, String summary) {
		this.id = id;
		this.name = name;
		this.created = created;
		this.summary = summary;
	}
}