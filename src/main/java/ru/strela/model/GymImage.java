package ru.strela.model;

import javax.persistence.*;

@Entity
@Table(name = "gym_image", indexes = {
        @Index(name = "gym_image_gym",  columnList="gym_id")
})
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class GymImage extends BaseEntity {

    private Gym gym;

    public GymImage() {}

    public GymImage(int id) {
        this.id = id;
    }

    @ManyToOne(targetEntity=Gym.class, fetch= FetchType.LAZY)
    @JoinColumn(name="gym_id", nullable=false)
    public Gym getGym() {
        return gym;
    }

    public void setGym(Gym gym) {
        this.gym = gym;
    }

}
