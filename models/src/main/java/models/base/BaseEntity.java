<<<<<<< HEAD
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
=======
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
>>>>>>> 81f6070450e2b4cd442ec82d43bd5516a9882ea1
