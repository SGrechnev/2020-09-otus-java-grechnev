package ru.otus.core.model;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @OneToOne(targetEntity = AddressDataSet.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private AddressDataSet address;

    @OneToMany(mappedBy = "client", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<PhoneDataSet> phoneSet = new HashSet<>();

    public Client() {
    }

    public Client(String name, AddressDataSet address) {
        this.name = name;
        this.address = new AddressDataSet(address);
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AddressDataSet getAddress() {
        return new AddressDataSet(address);
    }

    public Set<PhoneDataSet> getPhoneSet() {
        return Set.copyOf(phoneSet);
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(AddressDataSet address) {
        this.address = new AddressDataSet(address);
    }

    public void setPhoneSet(Set<PhoneDataSet> phoneSet) {
        this.phoneSet = Set.copyOf(phoneSet);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", phones=" + phoneSet +
                '}';
    }
}
