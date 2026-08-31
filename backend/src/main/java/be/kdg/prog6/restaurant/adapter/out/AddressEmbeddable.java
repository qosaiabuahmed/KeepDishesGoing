package be.kdg.prog6.restaurant.adapter.out;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class AddressEmbeddable {

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "country", nullable = false)
    private String country;

    protected AddressEmbeddable() {}

    public AddressEmbeddable(String street, String number, String postalCode, String city, String country) {
        this.street = street;
        this.number = number;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
    }

    public String getStreet() { return street; }
    public String getNumber() { return number; }
    public String getPostalCode() { return postalCode; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
}