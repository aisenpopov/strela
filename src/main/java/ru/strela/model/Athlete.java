package ru.strela.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import ru.strela.model.auth.Person;

@Entity
@Table(name = "athlete", indexes = {
		@Index(name = "athlete_person", columnList="person_id"),
		@Index(name = "athlete_registration_region", columnList="registration_region_id"),
		@Index(name = "athlete_team", columnList="team_id")
})
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Athlete extends BaseEntity implements HasImage {
	
	public enum Sex {
		male("мужской"),
		female("женский");
		
		private String title;
		
		private Sex(String title) {
			this.title = title;
		}
		
		public String getTitle() {
			return title;
		}
	}
	
	private Person person;
	
	private RegistrationRegion registrationRegion;
	private Team team;
	private String firstName;
	private String lastName;
	private String middleName;
	private String nickName;
	private String giSize;
	private String rashguardSize;
	private Date birthday;
	private Date startDate;
	private Double weight;
	private Double height;
	private boolean instructor;
	private boolean retired;
	private String passportNumber;
	private Sex sex;
	
	private String phoneNumber;
	private String mobileNumber;
	private String email;
	private String vk;
	private String facebook;
	private String instagram;
	private String skype;
	
	private Integer image;
	private Integer certificate;
	private String comment;
	
	public Athlete() {}
	
	public Athlete(int id) {
		this.id = id;
	}

	@OneToOne(targetEntity=Person.class, fetch=FetchType.LAZY)
	@JoinColumn(name="person_id")
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	@ManyToOne(targetEntity=RegistrationRegion.class, fetch=FetchType.LAZY)
	@JoinColumn(name="registration_region_id")
	public RegistrationRegion getRegistrationRegion() {
		return registrationRegion;
	}

	public void setRegistrationRegion(RegistrationRegion registrationRegion) {
		this.registrationRegion = registrationRegion;
	}

	@ManyToOne(targetEntity=Team.class, fetch=FetchType.LAZY)
	@JoinColumn(name="team_id")
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	@Column(name="first_name", nullable=false)
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Column(name="last_name")
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Column(name="middle_name")
	public String getMiddleName() {
		return middleName;
	}

	@Column(name="nick_name")
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Column(name="gi_size")
	public String getGiSize() {
		return giSize;
	}

	public void setGiSize(String giSize) {
		this.giSize = giSize;
	}

	@Column(name="rashguard_size")
	public String getRashguardSize() {
		return rashguardSize;
	}

	public void setRashguardSize(String rashguardSize) {
		this.rashguardSize = rashguardSize;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Column(name="start_date")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	@Column(columnDefinition="BOOLEAN DEFAULT FALSE")
	public boolean isInstructor() {
		return instructor;
	}

	public void setInstructor(boolean instructor) {
		this.instructor = instructor;
	}

	@Column(columnDefinition="BOOLEAN DEFAULT FALSE")
	public boolean isRetired() {
		return retired;
	}

	public void setRetired(boolean retired) {
		this.retired = retired;
	}

	@Column(name="passport_number")
	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	@Column(name="phone_number")
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Column(name="mobile_number")
	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getVk() {
		return vk;
	}

	public void setVk(String vk) {
		this.vk = vk;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getInstagram() {
		return instagram;
	}

	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}

	public String getSkype() {
		return skype;
	}

	public void setSkype(String skype) {
		this.skype = skype;
	}

	public Integer getImage() {
		return image;
	}

	@Override
	public void setImage(Integer image) {
		this.image = image;
	}

	public Integer getCertificate() {
		return certificate;
	}

	public void setCertificate(Integer certificate) {
		this.certificate = certificate;
	}

	@Column(columnDefinition="TEXT")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Transient
	public String getDisplayName() {
		StringBuilder builder = new StringBuilder();
		if(StringUtils.isNotBlank(lastName)) {
			builder.append(lastName);
		}
		if(StringUtils.isNotBlank(firstName)) {
			if(builder.length() > 0) builder.append(" ");
			builder.append(firstName);
		}
		if(StringUtils.isNotBlank(middleName)) {
			if(builder.length() > 0) builder.append(" ");
			builder.append(middleName);
		}
		
		return builder.toString();
	}

}
