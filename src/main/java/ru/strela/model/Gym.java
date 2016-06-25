package ru.strela.model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aisen on 28.04.2016.
 */
@Entity
@Table(name = "gym", indexes = {
        @Index(name = "gym_team", columnList="team_id"),
        @Index(name = "gym_city", columnList="city_id"),
        @Index(name = "gym_article", columnList="article_id")
})
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Gym extends BaseEntityNamed {

    private String address;
    private Team team;
    private City city;
    private List<Athlete> instructors;
    private Double longitude;
    private Double latitude;

    private Article article;

    public Gym() {}

    public Gym(int id) {
        this.id = id;
    }

    @ManyToOne(targetEntity=Team.class, fetch=FetchType.LAZY)
    @JoinColumn(name="team_id", nullable = false)
    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @ManyToOne(targetEntity=City.class, fetch=FetchType.LAZY)
    @JoinColumn(name="city_id", nullable = false)
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @ManyToMany(targetEntity = Athlete.class)
    @LazyCollection(value = LazyCollectionOption.TRUE)
    @JoinTable(name = "gym_athlete",
            joinColumns = @JoinColumn(name = "gym_id"),
            inverseJoinColumns = @JoinColumn(name = "athlete_id"))
    public List<Athlete> getInstructors() {
        if (instructors == null) {
            this.instructors = new ArrayList<Athlete>();
        }
        return instructors;
    }

    public void setInstructors(List<Athlete> instructors) {
        this.instructors = instructors;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @OneToOne(targetEntity = Article.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
