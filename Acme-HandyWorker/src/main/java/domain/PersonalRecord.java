
package domain;

import javax.persistence.Entity;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
public class PersonalRecord extends DomainEntity {

	private String	fullName;
	private String	photoURL;
	private String	email;
	private String	phoneNumber;
	private String	linkedInProfileURL;


	@NotBlank
	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(final String fullName) {
		this.fullName = fullName;
	}

	@URL
	public String getPhotoURL() {
		return this.photoURL;
	}

	public void setPhotoURL(final String photoURL) {
		this.photoURL = photoURL;
	}

	@Email
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@NotBlank
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@URL
	public String getLinkedInProfileURL() {
		return this.linkedInProfileURL;
	}

	public void setLinkedInProfileURL(final String linkedInProfileURL) {
		this.linkedInProfileURL = linkedInProfileURL;
	}

}
