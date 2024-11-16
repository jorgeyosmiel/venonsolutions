package petstore.tests;

import io.restassured.response.Response;
import petstore.models.Category;
import petstore.models.Tag;
import petstore.endpoints.PetEndpoint;
import petstore.models.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class PetTests {


    @BeforeEach
    public void setup() {
    }


    @ParameterizedTest(name = "CreatePet: {7}")
    @MethodSource("petstore.utils.PetDataProvider#provideCreatePet")
    public void testCreatePet(int id, String name, Boolean addPhoto, String status, int categoryId, String categoryName, int expectedStatus, String expectedMessage) {
        Pet pet = new Pet();
        pet.setId((id));
        pet.setName(name);
        pet.setStatus(status);

        Category category = new Category();
        category.setId((categoryId));
        category.setName(categoryName);
        pet.setCategory(category);

        Tag tag = new Tag();
        tag.setId(0);
        tag.setName("exampleTag");
        pet.setTags(Collections.singletonList(tag));

        if (addPhoto) {
            List<String> photoUrls = Arrays.asList("http://example.com/photo1", "http://example.com/photo2");
            pet.setPhotoUrls(photoUrls);
        }

        // Crear la mascota usando el endpoint
        Response createdPet = PetEndpoint.createPet(pet);

        assertEquals((expectedStatus), createdPet.statusCode());

        if (expectedStatus == 200) {
            // Validar que la respuesta contenga los datos correctos del Pet
            assertEquals(id, createdPet.getBody().jsonPath().getInt("id"));
            assertEquals(name, createdPet.getBody().jsonPath().getString("name"));
            assertEquals(status, createdPet.getBody().jsonPath().getString("status"));
        }

    }

    @ParameterizedTest(name = "GetPet: {6}")
    @MethodSource("petstore.utils.PetDataProvider#provideUpdatedPetData")
    public void testUpdatePet(int id, String name, String status, int categoryId, String categoryName, int expectedStatusCode, String expectedMessage) {
        Pet pet = new Pet();
        pet.setId((id));
        pet.setName(name);
        pet.setStatus(status);

        Category category = new Category();
        category.setId((categoryId));
        category.setName(categoryName);
        pet.setCategory(category);

        Tag tag = new Tag();
        tag.setId(0);
        tag.setName("exampleTag");
        pet.setTags(Collections.singletonList(tag));

        List<String> photoUrls = Arrays.asList("http://photo1.com", "http://photo2.com");
        pet.setPhotoUrls(photoUrls);

        if (expectedStatusCode == 200) {
            Response createdPet = PetEndpoint.createPet(pet);
            assertEquals(200, createdPet.statusCode(), "Validate pet creation");
        }

        Response getPet = PetEndpoint.updatePet(pet);

        assertEquals(expectedStatusCode, getPet.statusCode());

        // Response Validation
        if (expectedStatusCode == 200) {
            assertEquals(id, getPet.getBody().jsonPath().getInt("id"));
            assertEquals(name, getPet.getBody().jsonPath().getString("name"));
            assertEquals(status, getPet.getBody().jsonPath().getString("status"));
        }
    }

    @ParameterizedTest(name = "UpdatePet: {6}")
    @MethodSource("petstore.utils.PetDataProvider#providerGetPet")
    public void testGetPetById(int id, String name, String status, int categoryId, String categoryName, int expectedStatusCode, String expectedMessage) {

        if (expectedStatusCode == 200) {
            Pet pet = new Pet();
            pet.setId((id));
            pet.setName(name);
            pet.setStatus(status);

            Category category = new Category();
            category.setId((categoryId));
            category.setName(categoryName);
            pet.setCategory(category);

            Tag tag = new Tag();
            tag.setId(0);
            tag.setName("exampleTag");
            pet.setTags(Collections.singletonList(tag));

            List<String> photoUrls = Arrays.asList("http://photo1.com", "http://photo2.com");
            pet.setPhotoUrls(photoUrls);

            Response createdPet = PetEndpoint.createPet(pet);

            assertEquals(200, createdPet.statusCode(), "Validate pet creation");
        }

        Response getPet = PetEndpoint.getPet(id);

        assertEquals(expectedStatusCode, getPet.statusCode());

        // Response Validation
        if (expectedStatusCode == 200) {
//            assertEquals(id, getPet.getBody().jsonPath().getInt("id"));
            assertEquals(name, getPet.getBody().jsonPath().getString("name"));
            assertEquals(status, getPet.getBody().jsonPath().getString("status"));
        }
    }

    @ParameterizedTest(name = "GetPetByStatus: {3}")
    @MethodSource("petstore.utils.PetDataProvider#provideGetPettByStatus")
    public void testGetPetByStatus(String status, int results, int expectedStatusCode, String caseMessage) {

        List<Pet> petList = new ArrayList<>();

        System.out.println("Results: "+results);
        for (int i = 0; i < results; i++) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddHHmmss");
            String id = LocalDateTime.now().format(formatter);
            String name = generateRandomName();

            Pet pet = new Pet();
            pet.setId(Integer.parseInt(id));
            pet.setName(name);
            pet.setStatus(status);

            Category category = new Category();
            category.setId((0));
            category.setName("CategoryName");
            pet.setCategory(category);

            Tag tag = new Tag();
            tag.setId(0);
            tag.setName("exampleTag");
            pet.setTags(Collections.singletonList(tag));

            List<String> photoUrls = Arrays.asList("http://photo1.com", "http://photo2.com");
            pet.setPhotoUrls(photoUrls);

            Response createdPet = PetEndpoint.createPet(pet);
            assertEquals(200, createdPet.statusCode(), "Validate pet creation");

            //  Save Pets on List
            petList.add(pet);
        }

        Response petsResponse = PetEndpoint.getPetsByStatus(status);
        assertEquals(expectedStatusCode, petsResponse.statusCode(), "Validate getPets");

        if(expectedStatusCode==200){

            List<Pet> pets = petsResponse.jsonPath().getList("", Pet.class);

            System.out.println("PetList: "+ petList.size());
            System.out.println("Pets: "+ pets.size());

            assertTrue(pets.size()>=petList.size(), "Evaluate petList size.");

            for (Pet createdPet : petList) {
                boolean petExists = false;
                for (Pet petResult : pets){
                    if (petResult.getId()== createdPet.getId()){
                        petExists = true;
                    }
                }
                assertTrue(petExists);
            }
        }
    }

    @ParameterizedTest(name = "GetPetByTag: {4}")
    @MethodSource("petstore.utils.PetDataProvider#provideGetPetByTags")
    public void testGetPetByTags(List<String> tags, int results, String status, int statusCode, String expectedMessage) {

        List<Pet> petList = new ArrayList<>();

        for (int i = 0; i < results; i++) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddHHmmss");
            String id = LocalDateTime.now().format(formatter);
            String name = generateRandomName();

            Pet pet = new Pet();
            pet.setId(Integer.parseInt(id));
            pet.setName(name);
            pet.setStatus(status);

            Category category = new Category();
            category.setId((0));
            category.setName("CategoryName");
            pet.setCategory(category);

            List<Tag> petTags = new ArrayList<>();
            for (int j = 0; j < tags.size(); j++) {
                Tag tag = new Tag();
                tag.setId(j);
                tag.setName(tags.get(j));
                petTags.add(tag);
            }
            pet.setTags(petTags);

            List<String> photoUrls = Arrays.asList("http://photo1.com", "http://photo2.com");
            pet.setPhotoUrls(photoUrls);

            Response createdPet = PetEndpoint.createPet(pet);
            assertEquals(200, createdPet.statusCode(), "Validate pet creation");

            //  Save Pet on List
            petList.add(pet);
        }

        Response petsResponse = PetEndpoint.getPetsByTags(tags);
        assertEquals(petsResponse.statusCode(), statusCode);

        if (statusCode==200){
            List<Pet> pets = petsResponse.jsonPath().getList("", Pet.class);
            assertTrue(pets.size()>=petList.size());

            for (Pet createdPet : petList) {
                boolean petExists = false;
                for (Pet petResult : pets){
                    if (petResult.getId()== createdPet.getId()){
                        petExists = true;
                    }
                }
                assertTrue(petExists);
            }
        }

    }

    public static String generateRandomName() {
        String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder name = new StringBuilder(10);
        Random RANDOM = new Random();
        for (int i = 0; i < 10; i++) {
            int index = RANDOM.nextInt(ALPHABET.length());
            name.append(ALPHABET.charAt(index));
        }
        return name.toString();
    }








    }


