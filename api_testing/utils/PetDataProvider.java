package petstore.utils;

import org.junit.jupiter.params.provider.Arguments;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class PetDataProvider {

    public static Stream<Arguments> provideCreatePet() {
        return Stream.of(
                arguments(0, "doggie", true, "available", 0, "string", 200, "Successfully"),
                arguments(0, "doggie", true, "available", 0, "string", 200, "Successfully"),
                arguments(1, "kitty", true, "sold", 1, "feline", 200, "Successfully"),
                arguments(-1, "null", false, "unknown", -1, "invalidCategory", 405, "Fail, not photoUrl*")
        );
    }

    public static Stream<Arguments> providerGetPet(){
        return Stream.of(
                arguments(444444444, "doggie", "available", 0, "string", 200, "Successfully"),
                arguments(999999999, "doggie", "available", 0, "string", 404, "Pet not found")
        );
    }

    public static Stream<Arguments> provideUpdatedPetData(){
        return Stream.of(
                arguments(444, "doggie_new", "available", 0, "string_new", 200, "Successfully"),
                arguments(999, "doggie_999", "available", 0, "string", 404, "Pet not found")
        );
    }

    public static Stream<Arguments> provideGetPettByStatus(){
        return Stream.of(
                arguments("available", 3, 200,"3 results"),
                arguments("available_fail", 0, 400,"0 results")
        );
    }

    public static Stream<Arguments> provideGetPetByTags(){
        return Stream.of(
                arguments(Collections.singletonList("string"), 3, "available",200,"3 results"),
                arguments(Arrays.asList("fails", "noexistenttag"), 0, "available",200,"0 results")
        );
    }

}
