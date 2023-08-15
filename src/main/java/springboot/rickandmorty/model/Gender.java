package springboot.rickandmorty.model;

public enum Gender {
    FEMALE("Female"),
    MALE("Male"),
    GENDERLESS("Genderless"),
    UNKNOWN("unknown");

    private String gender;

    Gender(String gender) {
        this.gender = gender;
    }
}
