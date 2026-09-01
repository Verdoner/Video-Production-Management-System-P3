package integrationtest;

public @interface WithMockUser {

    String username();

    String password();

    String role();

    String email();

    String id();

    String phone();

    String ip();

    String name();

}
