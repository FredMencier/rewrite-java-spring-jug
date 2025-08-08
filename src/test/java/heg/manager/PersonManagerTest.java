package heg.manager;

import heg.entity.Address;
import heg.entity.AddressType;
import heg.entity.Genre;
import heg.entity.Person;
import heg.exception.PersonAlreadyExistException;
import heg.exception.UnknownPersonException;
import jakarta.persistence.PersistenceException;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonManagerTest {

    private static final Logger LOG = Logger.getLogger(PersonManagerTest.class);

    @Autowired
    PersonManager pm;

    @Before
    public void setUp() throws PersonAlreadyExistException {
        pm.findAllPerson().forEach(person -> {
            pm.remove(person);
        });
    }

    @Test
    public void shouldInsertPersonWithAddress() throws PersonAlreadyExistException, UnknownPersonException {
        Person p2 = new Person("michel@hesge.ch", "michel", 22, Genre.MASCULIN, LocalDate.now());
        Address a1 = new Address("12", "rue beauregard", "Geneve", 1210, "Suisse", AddressType.PRINCIPALE);
        a1.setPerson(p2);
        Address a2 = new Address("22", "rue beauregard", "Lausanne", 1210, "Suisse", AddressType.SECONDAIRE);
        a2.setPerson(p2);
        p2.setAddresses(List.of(a1, a2));
        pm.insert(p2);

        //verification
        Person personInserted = pm.findPersonByMail("michel@hesge.ch");
        assertNotNull(personInserted);
        assertEquals(p2.getName(), personInserted.getName());
        assertEquals(p2.getAge(), personInserted.getAge());
        assertEquals(p2.getEmail(), personInserted.getEmail());
        assertEquals(p2.getGenre(), personInserted.getGenre());
        assertEquals(p2.getBirthday(), personInserted.getBirthday());

        List<Address> addresses = personInserted.getAddresses();
        assertNotNull(addresses);
        assertEquals(2, addresses.size());

        for (Address ad : addresses) {
            Person person = ad.getPerson();
            assertNotNull(person);
            assertEquals(p2.getEmail(), person.getEmail());
            assertEquals(p2.getName(), person.getName());
        }

        //clean
        pm.remove(personInserted);
    }

    @Test(expected = UnknownPersonException.class)
    public void shouldDeletePersonAndGetException() throws UnknownPersonException {
        //initialisation
        Person personTest = new Person("test@hesge.ch", "test", 50, Genre.AUTRE, LocalDate.now());
        pm.insert(personTest);

        //Test
        pm.remove(personTest);

        //verification
        pm.findPersonByMail("test@hesge.ch");
    }

    @Test(expected = UnknownPersonException.class)
    public void shouldDeletePersonWithIdAndGetException() throws UnknownPersonException {
        //initialisation
        Person personTest = new Person("test@hesge.ch", "test", 50, Genre.AUTRE, LocalDate.now());
        pm.insert(personTest);

        //Test
        pm.remove("test@hesge.ch");

        //verification
        pm.findPersonByMail("test@hesge.ch");
    }

    @Test
    public void shouldUpdatePerson() throws UnknownPersonException {
        //initialisation
        Person p1 = new Person("jacques@hesge.ch", "jacques", 22, Genre.MASCULIN, LocalDate.of(1990, 4, 20));
        pm.insert(p1);

        //test
        p1.setAge(50);
        p1.setGenre(Genre.FEMININ);
        Person personUpdated = pm.update(p1);

        //verification
        assertEquals(50, personUpdated.getAge());
        assertEquals(Genre.FEMININ, personUpdated.getGenre());
    }

    @Test
    public void shouldUpdateGenrePerson() throws UnknownPersonException {
        //initialisation
        Person p1 = new Person("jacques@hesge.ch", "jacques", 22, Genre.MASCULIN, LocalDate.of(1990, 4, 20));
        pm.insert(p1);

        //test
        pm.updateGenre(p1.getEmail(), Genre.AUTRE);

        //verification
        Person byEmail = pm.findPersonByMail(p1.getEmail());
        assertEquals(Genre.AUTRE, byEmail.getGenre());
    }

    @Test
    public void shouldInsertPersonAndAddress() throws UnknownPersonException {
        //initialisation
        Person p1 = new Person("jean@hesge.ch", "jean", 22, Genre.MASCULIN, LocalDate.of(1990, 4, 20));
        Address a1 = new Address("12", "rue beauregard", "Geneve", 1210, "Suisse", AddressType.PRINCIPALE);
        p1.getAddresses().add(a1);
        a1.setPerson(p1);
        pm.insert(p1);

        //test
        Person personByMail = pm.findPersonByMail("jean@hesge.ch");

        //verification
        assertEquals(p1.getEmail(), personByMail.getEmail());
        assertNotNull(personByMail.getAddresses());
        assertEquals(1, personByMail.getAddresses().size());
        assertEquals(p1.getAddresses().get(0).getCountry(), personByMail.getAddresses().get(0).getCountry());
        Address address = personByMail.getAddresses().get(0);
        assertNotNull(address.getPerson());
    }

    @Test
    public void shouldFindAllPerson() {
        //initialisation
        Person p1 = new Person("jacques@hesge.ch", "jacques", 22, Genre.MASCULIN, LocalDate.of(1990, 4, 20));
        Address a1 = new Address("6", "rue des morgines", "Petit-Lancy", 1210, "Suisse", AddressType.PRINCIPALE);
        a1.setPerson(p1);
        p1.getAddresses().add(a1);
        pm.insert(p1);

        //test
        List<Person> personList = pm.findAllPerson();

        //verification
        assertNotNull(personList);
        assertEquals(1, personList.size());
        Person person = personList.get(0);
        assertEquals(p1.getEmail(), person.getEmail());
        assertNotNull(person.getAddresses());
        assertEquals(1, person.getAddresses().size());
        assertEquals(p1.getAddresses().get(0).getCountry(), person.getAddresses().get(0).getCountry());
    }

    @Ignore
    @Test
    public void shouldFindPersonByMail() throws UnknownPersonException {
        //initialisation
        Person p1 = new Person("jacques@hesge.ch", "jacques", 22, Genre.MASCULIN, LocalDate.of(1990, 4, 20));
        Address a1 = new Address("6", "rue des morgines", "Petit-Lancy", 1210, "Suisse", AddressType.PRINCIPALE);
        a1.setPerson(p1);
        p1.getAddresses().add(a1);
        pm.insert(p1);

        //test
        Person person = pm.findPersonByMail(p1.getEmail());

        //verification
        assertNotNull(person);
        LOG.info(person.toString());
        assertEquals(p1.getName(), person.getName());
        assertEquals(p1.getEmail(), person.getEmail());
        assertEquals(p1.getAge(), person.getAge());
        assertEquals(p1.getGenre(), person.getGenre());
        assertEquals(p1.getBirthday(), person.getBirthday());
        assertNotNull(person.getAddresses());
        assertEquals(1, person.getAddresses().size());
        assertEquals(p1.getAddresses().get(0).getCountry(), person.getAddresses().get(0).getCountry());
    }

    @Test
    public void shouldFindPersonByName() {
        //initialisation
        Person p1 = new Person("jacques@hesge.ch", "jacques", 22, Genre.MASCULIN, LocalDate.of(1990, 4, 20));
        pm.insert(p1);

        //test
        List<Person> persons = pm.findByName("jacques");

        //verification
        assertNotNull(persons);
        assertEquals(1, persons.size());
    }

    @Test
    public void shouldNotFindPersonByName() {
        //test
        List<Person> persons = pm.findByName("thierry");

        //verification
        assertNotNull(persons);
        assertEquals(0, persons.size());
    }

    @Test
    public void shouldFindPersonByAge() {
        //initialisation
        Person p1 = new Person("jacques@hesge.ch", "jacques", 22, Genre.MASCULIN, LocalDate.of(1990, 4, 20));
        pm.insert(p1);

        //test
        List<Person> persons = pm.findByAge(p1.getAge());

        //verification
        assertNotNull(persons);
        persons.forEach(person -> {
            assertEquals(p1.getAge(), person.getAge());
        });
    }
}
