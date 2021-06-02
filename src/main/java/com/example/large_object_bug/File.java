package com.example.large_object_bug;

import lombok.*;
import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor

public class File {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private Date created = new Date();
	private String summary;

	@ContentId private String contentId;
	@ContentLength private long contentLength;
	private String mimeType = "text/plain";

	public File(String name, Date created, String summary) {
		this.name = name;
		this.created = created;
		this.summary = summary;
	}
}