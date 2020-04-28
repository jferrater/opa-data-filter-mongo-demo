package com.github.jferrater.userservice.service;

import com.github.jferrater.userservice.repository.UserRepository;
import com.github.jferrater.userservice.repository.document.User;
import com.github.jferrater.userservice.repository.document.UserType;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TestData {

    private UserRepository userRepository;

    public TestData(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createTestData() {
        //Veterinarians from SOMA Clinic
        User alex = new User(UUID.randomUUID().toString(), "SOMA", null, "alex", "password123", UserType.MANAGER,
                new String[]{"vet", "admin"}, null, "Alice Bane", "Stockholm, Sweden", "1234567", new Date(), new Date());
        User alice = new User(UUID.randomUUID().toString(), "SOMA", "alex", "alice", "password123", UserType.VETERINARIAN,
                new String[]{"vet", "admin"}, null, "Alice Bane", "Stockholm, Sweden", "1234567", new Date(), new Date());
        User john = new User(UUID.randomUUID().toString(), "SOMA", "alex", "john", "password123", UserType.VETERINARIAN,
                new String[]{"vet"}, null, "John Doe", "Stockholm, Sweden", "1234567", new Date(), new Date());
        User joffry = new User(UUID.randomUUID().toString(), "SOMA", "alex", "joffry", "password123", UserType.VETERINARIAN,
                new String[]{"vet"}, null, "Joffry Ferrater", "Stockholm, Sweden", "1234567", new Date(), new Date());
        User jolly = new User(UUID.randomUUID().toString(), "SOMA", "alex", "jolly", "password123", UserType.VETERINARIAN,
                new String[]{"vet"}, null, "Jolly Jae Ompod", "Stockholm, Sweden", "1234567", new Date(), new Date());

        //Veterinarians from VETE Clinic
        User lena = new User(UUID.randomUUID().toString(), "VETE", null, "lena", "password123", UserType.MANAGER,
                new String[]{"vet", "admin"}, null, "Wella Ferrater", "Cebu, Philippines", "1234567", new Date(), new Date());
        User wella = new User(UUID.randomUUID().toString(), "VETE", "lena", "wella", "password123", UserType.VETERINARIAN,
                new String[]{"vet", "admin"}, null, "Wella Ferrater", "Cebu, Philippines", "1234567", new Date(), new Date());
        User janice = new User(UUID.randomUUID().toString(), "VETE", "lena", "janice", "password123", UserType.VETERINARIAN,
                new String[]{"vet"}, null, "Janice Dinglasa", "Cebu, Philippines", "1234567", new Date(), new Date());
        User ken = new User(UUID.randomUUID().toString(), "VETE", "lena", "ken", "password123", UserType.VETERINARIAN,
                new String[]{"vet"}, null, "Ken Toi", "Cebu, Philippines", "1234567", new Date(), new Date());

        //Pet owners
        User bob = new User(UUID.randomUUID().toString(), null, null, "bob", "password123", UserType.PET_OWNER,
                new String[]{"user"}, null, "Bob Steward", "Paris, France", "1234567", new Date(), new Date());

        List<User> users = List.of(alice, john, joffry, jolly, wella, janice, ken, bob, alex, lena);
        userRepository.saveAll(users);
    }
}
