package heg.manager;

import heg.entity.Address;
import heg.entity.AddressType;
import heg.entity.Person;
import org.apache.log4j.Logger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;

import static heg.entity.Address.ADDRESS_ENTITY_FIND_CITY;
import static heg.entity.Address.ADDRESS_ENTITY_FIND_STREET;

public class AddressManager {

    private static final Logger LOG = Logger.getLogger(AddressManager.class);

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public void addAddress(final String personMail, final String streetNo, final String streetName, final String city, final int postCode, final String country, final AddressType addressType) {
        Address address = new Address(streetNo, streetName, city, postCode, country, addressType);
        Person person = entityManager.find(Person.class, personMail);
        person.getAddresses().add(address);
        address.setPerson(person);
        entityManager.merge(person);
    }

    @Transactional
    public void addAddress(final String personMail, final Address address) {
        Person person = entityManager.find(Person.class, personMail);
        person.getAddresses().add(address);
        address.setPerson(person);
        entityManager.merge(person);
    }

    @Transactional
    public void removeAddress(final Long id) {
        Address address = entityManager.find(Address.class, id);
        address.setPerson(null);
        entityManager.remove(address);
    }

    @Transactional
    public Address updateAddress(final Address address) {
        return entityManager.merge(address);
    }

    public Address find(final Long id) {
        return entityManager.find(Address.class, id);
    }

    public List<Address> findByCity(final String city) {
        TypedQuery<Address> namedQuery = entityManager.createNamedQuery(ADDRESS_ENTITY_FIND_CITY, Address.class);
        namedQuery.setParameter("city", city);
        return namedQuery.getResultList();
    }

    public List<Address> findByStreetName(final String street) {
        TypedQuery<Address> namedQuery = entityManager.createNamedQuery(ADDRESS_ENTITY_FIND_STREET, Address.class);
        namedQuery.setParameter("street", street);
        return namedQuery.getResultList();
    }
}
