package br.inatel.karateRunner;

import com.intuit.karate.junit5.Karate;

public class Runner {
    @Karate.Test
    Karate testPost() {
        return Karate.run("classpath:karateTests/features/PostGame.feature").relativeTo(getClass());
    }

    @Karate.Test
    Karate testGet() {
        return Karate.run("classpath:karateTests/features/GetGame.feature").relativeTo(getClass());
    }
}