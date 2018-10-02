package com.mongodb.fakerFR;

import com.github.javafaker.Faker;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * Runs a sequential insert of fake People information inside a MongoDB database
 * using Plain Old Java Object mapping (POJO) <p>
 *
 * no params
 * output : collection people of database world3 is filled with batchSize documents
 * output : displays every bulkSize documents count
 */

/* sample JSON result
{
  "_id": ObjectId("5bb372cd573406ed626df3d1"),
  "address": {
    "city": "Beauvais",
    "street": "4710 Rue d'Argenteuil",
    "zip": "42566"
  },
  "job": "Construction Officer",
  "name": "Sacha Gauthier",
  "phone": [
    {
      "number": "+33 7 03 09 64 59",
      "type": "Cell"
    },
    {
      "number": "+33 552012108",
      "type": "home"
    }
  ],
  "ssn": "350306-6213"
}

 */


public class FakerFR {

    public static void main(String[] args) {
        // Db Connect

        CodecRegistry defaultCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().register("com.mongodb.fakerFR").build();
        CodecRegistry pojoCodecRegistry = fromRegistries(defaultCodecRegistry, fromProviders(pojoCodecProvider));
        MongoClient mongoClient = new MongoClient("localhost", MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
        MongoDatabase database = mongoClient.getDatabase("world3");
        MongoCollection<People> collection = database.getCollection("people", People.class);

        // constants
        int batchSize = 300000;
        int bulkSize = 1000;

        // variables
        Faker faker = new Faker(new Locale("fr"));
        int i;
        boolean debug = false;

        // generate bulk op
        //BulkWriteOperation builder = collection.initializeUnorderedBulkOperation();


        // loop
        for (i = 0; i < batchSize; i++) {
            // fill in Doc array
            // set main fields
            String name = faker.name().fullName(); // Miss Samanta Schmidt
            String job = faker.job().title(); // Emory
            String lastName = faker.name().lastName(); // Barton
            String ssn = faker.idNumber().validSvSeSsn();

            // Set Address
            String street = faker.address().streetAddress(); // 60018 Sawayn Brooks Suite 449
            String zip = faker.address().zipCode().toString();
            String city = faker.address().cityName();

            Address address = new Address(street, city, zip);

            // Set Phone numbers array
            String cellPhone = faker.phoneNumber().cellPhone().toString();
            String landPhone = faker.phoneNumber().phoneNumber().toString();
            Phone cell = new Phone("Cell", cellPhone);
            Phone home = new Phone("home", landPhone);
            List<Phone> phone = new ArrayList<Phone>();
            phone.add(cell);
            phone.add(home);

            // define new people
            People people = new People(name, ssn, job, phone, address);

            if (debug) {
                for (Phone thePhone : people.getPhone()) {
                    System.out.println("phone type: " + thePhone.getType() + " - Phone number: " + thePhone.getNumber());
                }
                System.out.println(String.format("name: %s", people.getName()));
                System.out.println(String.format("job: %s", people.getJob()));
                System.out.println(String.format("ssn: %s", people.getSsn()));

                System.out.println(String.format("street: %s", people.getAddress().getStreet()));
                System.out.println(String.format("zip: %s", people.getAddress().getZip()));
                System.out.println(String.format("city: %s", people.getAddress().getCity()));
            }

            if (i % bulkSize == 0) {
                System.out.println("i: " + Integer.toString(i));
            }
            // bulk write array
            collection.insertOne(people);
            // close connection
        }

    }
}
