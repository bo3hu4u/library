package models.base;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseEntity<V> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected V id;

    public V getId() {
        return id;
    }

    public void setId(V id) {
        this.id = id;
    }
}
