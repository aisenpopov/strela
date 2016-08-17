package ru.strela.web.controller.dto;

import org.apache.commons.lang.StringUtils;
import ru.strela.model.Athlete;
import ru.strela.util.DateUtils;

public class AthleteDTO {

	private int id;
	private PersonDTO person;

	private String firstName;
	private String lastName;
	private String middleName;
	private String nickName;
	private String giSize;
	private String rashguardSize;
	private String birthday;
	private Double weight;
	private Double height;
	private String sex;

	private String mobileNumber;
	private String email;
	private String vk;
	private String facebook;
	private String instagram;
	private String skype;

	public AthleteDTO() {}

	public AthleteDTO(Athlete athlete) {
		if (athlete != null) {
			id = athlete.getId();
			person = new PersonDTO(athlete.getPerson());

			firstName = athlete.getFirstName();
			lastName = athlete.getLastName();
			middleName = athlete.getMiddleName();
			nickName = athlete.getNickName();
			giSize = athlete.getGiSize();
			rashguardSize = athlete.getRashguardSize();
			birthday = DateUtils.formatDDMMYYYY(athlete.getBirthday());
			weight = athlete.getWeight();
			height = athlete.getHeight();
			sex = athlete.getSex() != null ? athlete.getSex().name() : null;

			mobileNumber = athlete.getMobileNumber();
			email = athlete.getEmail();
			vk = athlete.getVk();
			facebook = athlete.getFacebook();
			instagram = athlete.getInstagram();
			skype = athlete.getSkype();
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PersonDTO getPerson() {
		return person;
	}
	
	public void setPerson(PersonDTO person) {
		this.person = person;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getGiSize() {
		return giSize;
	}

	public void setGiSize(String giSize) {
		this.giSize = giSize;
	}

	public String getRashguardSize() {
		return rashguardSize;
	}

	public void setRashguardSize(String rashguardSize) {
		this.rashguardSize = rashguardSize;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

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
