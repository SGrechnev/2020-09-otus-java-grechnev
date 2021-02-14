package ru.otus.core.model;

import javax.persistence.*;

@Entity
@Table(name = "phone_data_set")
public class PhoneDataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne()
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "number")
    private String number;

    public PhoneDataSet() {
    }

    public PhoneDataSet(String number, Client client) {
        this.number = number;
        this.client = client;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "PhoneDataSet{" +
                "number='" + number + '\'' +
                "client_id='" + client.getId() + '\'' +
                '}';
    }
}
