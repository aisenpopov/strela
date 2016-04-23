package ru.strela.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.Hibernate;

@MappedSuperclass
public abstract class BaseEntity implements Comparable<BaseEntity> {

	protected int id;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;

        BaseEntity baseEntity = (BaseEntity) o;
        int baseEntityId = baseEntity.getId();
        if(id != baseEntityId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
    
    @Override
	public int compareTo(BaseEntity o) {
		if(id < o.getId()) {
            return -1;
        } else if(id > o.getId()) {
            return 1;
        } else {
            return 0;
        }
	}
}
