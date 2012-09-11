package org.ced.domain.attachment;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Attachment {

	@Id
	public String id;

	public Attachment() {
		this.id = UUID.randomUUID().toString();
	}
}
